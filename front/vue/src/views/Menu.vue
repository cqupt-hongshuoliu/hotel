<template>
  <div>
    <div style="margin: 10px 0">
      <el-input v-model="name" suffix-icon="el-icon-search" placeholder="依据名称模糊检索" style="width: 200px"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>

<!--      <el-button type="danger" style="margin-left: 5px">批量删除<i class="el-icon-remove-outline"></i></el-button>-->

    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" row-key="id" default-expand-all max-height="600">
      <el-table-column prop="name" label="名称" ></el-table-column>
      <el-table-column prop="path" label="路径" ></el-table-column>
      <el-table-column label="图标" align="center">
        <template slot-scope="scope">
          <i :class="scope.row.icon" style="font-size: 20px"/>
        </template>
      </el-table-column>
      <el-table-column prop="pagePath" label="页面路径" ></el-table-column>
      <el-table-column prop="description" label="描述" ></el-table-column>
      <el-table-column fixed="right" label="操作" width="200">
        <template slot-scope="scope">
          <el-button @click="handleChildren(scope.row.id)" type="text" v-if="!scope.row.pid && !scope.row.path" size="small" class="el-icon-plus">新增子菜单</el-button>
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

    <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="名称" >
          <el-input v-model="form.name" ></el-input>
        </el-form-item>
        <el-form-item label="路径" >
          <el-input v-model="form.path" ></el-input>
        </el-form-item>
        <el-form-item label="图标" >
            <el-select v-model="form.icon" placeholder="请选择">
              <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
                <i :class="item.value"/>{{ item.name }}
              </el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="页面路径" >
          <el-input v-model="form.pagePath" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述" >
          <el-input v-model="form.description" autocomplete="off"></el-input>
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
  name: "Menu",
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
      options:[]
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    load(){
      this.request.get("/menu/list", {
        params: {
          name: this.name
        }
      }).then(res => {
        this.tableData = res.data
      })

      this.request.get("/menu/icons").then(res => {
        this.options = res.data
      })
    },
    reset(){
      this.name = ""
      this.load()
    },
    save(){
      this.request.post("/menu/save", this.form).then(res => {
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
    handleChildren(pid){
      this.dialogFormVisible = true
      this.form = {}
      if (pid){
        this.form.pid = pid
      }
    },
    handleEdit(row){
      this.form = row
      this.dialogFormVisible = true
    },
    handleDel(id){
      this.request.delete("/menu/" + id).then(res => {
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
    }
  }
}
</script>

<style>
.headerBg {
  background: lightblue!important;
}
</style>
