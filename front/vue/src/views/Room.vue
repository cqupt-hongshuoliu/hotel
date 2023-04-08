<template>
  <div>
        <div style="margin: 10px 0">
          <el-input v-model="roomType" suffix-icon="el-icon-search" placeholder="依据房间类型模糊检索" style="width: 200px"></el-input>
          <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
          <el-button style="margin-left: 5px" type="warning" @click="reset">重置</el-button>
        </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>

<!--      <el-button type="danger" style="margin-left: 5px">批量删除<i class="el-icon-remove-outline"></i></el-button>-->

    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" row-key="id" default-expand-all max-height="500">
      <el-table-column prop="roomType" label="类型" ></el-table-column>
      <el-table-column prop="roomTitle" label="客房标题" ></el-table-column>
      <el-table-column prop="roomNumber" sortable label="房间号" ></el-table-column>
      <el-table-column prop="roomPrice" label="房间价格￥/天" ></el-table-column>
      <el-table-column prop="roomState" label="状态" ></el-table-column>
      <el-table-column fixed="right" label="操作" width="120">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" type="text" size="small" class="el-icon-edit">编辑</el-button>
          <el-popconfirm
              confirm-button-text='确定'
              cancel-button-text='取消'
              title="您确定删除吗"
              @confirm = "handleDel(scope.row.roomId)"
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

    <el-dialog title="房间信息" :visible.sync="dialogFormVisible" width="400px">
      <el-form label-width="100px" size="small">
        <el-form-item label="类型" >
          <el-select v-model="form.roomType" placeholder="请选择" style="width: 240px">
            <el-option v-for="item in roomTypes" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value"/>{{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="客房标题" >
          <el-input v-model="form.roomTitle" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item label="房间号" >
          <el-input v-model="form.roomNumber" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item label="房间价格" >
          <el-input v-model="form.roomPrice" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item label="状态" >
          <el-select v-model="form.roomState" placeholder="请选择" style="width: 240px">
            <el-option v-for="item in roomStates" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value"/>{{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="房间描述" >
          <el-input v-model="form.roomDescription" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item label="房间首页图" >
          <el-input v-model="form.roomPicture" style="width: 240px" disabled></el-input>
          <el-upload action="http://localhost:9091/file/upload/"
                     :show-file-list="false"
                     :data={purpose:this.purpose}
                     style="display: inline-block"
                     :headers="{ token: this.u.token }"
                     :on-success="handleFileSuccess">
            <el-button type="primary" style="margin-left: 5px">上传图片<i class="el-icon-top"></i></el-button>
          </el-upload>
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
  name: "Room",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      roomType: "",
      form: {},
      dialogFormVisible: false,
      headerBg: 'headerBg',
      roomTypes: [],
      roomStates: [],
      u: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null,
      purpose: "房间首页图"
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    load(){
      this.request.get("/room/page",{
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          roomType: this.roomType
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/room/state").then(res => {
        this.roomStates = res.data
      })

      this.request.get("/room/type").then(res => {
        this.roomTypes = res.data
      })

    },
    reset(){
      this.roomType = ""
      this.load()
    },
    save(){
      this.request.post("/room/save", this.form).then(res => {
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
      this.request.delete("/room/" + id).then(res => {
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
      this.form.roomPicture = res
    }
  }
}
</script>

<style>
.headerBg {
  background: lightblue!important;
}
</style>
