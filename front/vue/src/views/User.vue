<template>
  <div>
    <div style="margin: 10px 0">
      <el-input v-model="username" suffix-icon="el-icon-search" placeholder="依据用户名模糊检索" style="width: 200px"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>

<!--      <el-button type="danger" style="margin-left: 5px">批量删除<i class="el-icon-remove-outline"></i></el-button>-->

      <el-upload action="http://localhost:9091/user/import/"
                 :show-file-list="false"
                 accept="xlsx"
                 style="display: inline-block"
                 :headers="{ token: this.u.token }"
                 :on-success="handleExcelSuccess">
        <el-button type="primary" style="margin-left: 5px">导入<i class="el-icon-bottom"></i></el-button>
      </el-upload>

      <el-button type="primary" @click="exp" style="margin-left: 5px">导出<i class="el-icon-top"></i></el-button>
    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" max-height="500">
      <el-table-column prop="username" label="用户名" ></el-table-column>
      <el-table-column prop="role" label="角色" ></el-table-column>
      <el-table-column prop="nickname" label="昵称" ></el-table-column>
      <el-table-column prop="phone" label="电话"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column fixed="right" label="操作" width="120">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" type="text" size="small" class="el-icon-edit">编辑</el-button>
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
        <el-form-item label="用户名" >
          <el-input v-model="form.username" ></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select v-model="form.role" placeholder="请选择角色">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i>{{ item.name }}</i>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="昵称" >
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" >
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话" >
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址" >
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "User",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      form: {},
      dialogFormVisible: false,
      headerBg: 'headerBg',
      u: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null,
      options:[]
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    load(){
      this.request.get("/user/page",{
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          username: this.username
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/role/flag").then( res => {
        this.options = res.data
      })

    },
    reset(){
      this.username = ""
      this.load()
    },
    save(){
      this.request.post("user/save", this.form).then(res => {
        if (res.code === '200'){
          this.$message.success(res.msg)
          this.dialogFormVisible = false
          this.load()
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd(){
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row){
      this.form = row
      this.dialogFormVisible = true
    },
    handleDel(id){
      this.request.delete("/user/" + id).then(res => {
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
    exp(){
      window.open("api/user/export")
      this.$message.success("导出成功")
    },
    handleExcelSuccess() {
      this.$message.success("导入成功")
      this.load()
    }
  }
}
</script>

<style>
.headerBg {
  background: lightblue!important;
}
</style>
