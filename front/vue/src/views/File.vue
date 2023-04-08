<template>
  <div>
    <div style="margin: 10px 0">
      <el-input v-model="name" suffix-icon="el-icon-search" placeholder="依据文件名模糊检索" style="width: 200px"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="add">新增<i class="el-icon-circle-plus-outline"></i></el-button>
      <!--      <el-button type="danger" style="margin-left: 5px">批量删除<i class="el-icon-remove-outline"></i></el-button>-->

    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" max-height="500">
      <el-table-column prop="name" label="文件名" ></el-table-column>
      <el-table-column prop="type" label="文件类型" ></el-table-column>
      <el-table-column prop="size" label="文件大小(kb)" ></el-table-column>
      <el-table-column prop="purpose" label="使用途径" ></el-table-column>
      <el-table-column label="下载">
        <template slot-scope="scope">
          <el-button type="primary" @click="download(scope.row.url)">下载</el-button>
        </template>
      </el-table-column>
      <el-table-column label="启用">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enable" active-color="#13ce66" inactive-color="#ccc" @change="changeEnable(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" width="120">
        <template slot-scope="scope">
          <el-popconfirm
              confirm-button-text='确定'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              title="您确定删除吗"
              @confirm = "handleDel(scope.row.id)"
          >
            <el-button slot="reference" type="text" size="small" class="el-icon-remove-outline">删除</el-button>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>

    <div class="block" style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="使用途径" >
          <el-input v-model="purpose" ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-upload action="http://localhost:9091/file/upload/"
                   :show-file-list="false"
                   :data={purpose:this.purpose}
                   style="display: inline-block"
                   :headers="{ token: this.u.token }"
                   :on-success="handleFileSuccess">
          <el-button type="primary" style="margin-left: 5px">上传文件<i class="el-icon-top"></i></el-button>
        </el-upload>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "File",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      headerBg: 'headerBg',
      u: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null,
      options:[],
      purpose: ""
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    load(){
      this.request.get("/file/page",{
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    reset(){
      this.name = "";
      this.load()
    },
    add(){
      this.purpose = ""
      this.dialogFormVisible = true
    },
    save(){
      this.request.post("file/save", this.form).then(res => {
        if (res.code === '200'){
          this.$message.success(res.msg)
          this.dialogFormVisible = false
          this.load()
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    handleEdit(row){
      this.form = row
      this.dialogFormVisible = true
    },
    handleDel(id){
      this.request.delete("/file/" + id).then(res => {
        if (res.code === '200'){
          this.$message.success(res.msg)
          this.load()
        }else {
          this.$message.error(res.msg)
        }
      })

    },
    handleSizeChange(pageSize){
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum){
      this.pageNum = pageNum
      this.load()
    },
    handleFileSuccess(res){
      this.$message.success("上传成功")
      this.dialogFormVisible = false
      this.load()
    },
    download(url){
      window.open(url)
    },
    changeEnable(row){
      this.request.post("file/save", row).then(res => {
        if (res.code === '200'){
          this.$message.success(res.msg)
          this.load()
        }else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style>
.headerBg {
  background: lightblue!important;
}
</style>
