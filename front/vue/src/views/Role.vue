<template xmlns="http://www.w3.org/1999/html">
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

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg">
      <el-table-column prop="name" label="名称" ></el-table-column>
      <el-table-column prop="flag" label="唯一标识" ></el-table-column>
      <el-table-column prop="description" label="描述" ></el-table-column>
      <el-table-column fixed="right" label="操作" width="180">
        <template slot-scope="scope">
          <el-button @click="selectMenu(scope.row.id)" type="text" size="small" class="el-icon-menu">分配菜单</el-button>
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

    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="名称" >
          <el-input v-model="form.name" ></el-input>
        </el-form-item>
        <el-form-item label="唯一标识" >
          <el-select v-model="form.flag" placeholder="请选择角色">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i>{{ item.name }}</i>
            </el-option>
          </el-select>
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

    <el-dialog title="菜单分配" :visible.sync="dialogMenuVisible" width="30%">
      <el-tree
          :props="props"
          :data="menuData"
          node-key="id"
          ref="tree"
          :default-expanded-keys="expends"
          show-checkbox>
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span><i :class="data.icon"></i> {{ data.name }}</span>
        </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogMenuVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {resetRouter} from "@/router";

export default {
  name: "Role",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      dialogMenuVisible: false,
      headerBg: 'headerBg',
      menuData: [],
      props: {
        label: 'name'
      },
      expends:[],
      roleId: 0,
      options: []
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    load(){
      this.request.get("role/page",{
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/role/flag").then(res => {
        this.options = res.data
      })
    },
    reset(){
      this.name = ""
      this.load()
    },
    save(){
      this.request.post("role/save", this.form).then(res => {
        if (res.code === '200'){
          this.$message.success(res.msg)
          this.dialogFormVisible = false
          this.load()
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    saveRoleMenu(){
      this.request.post("role/setRoleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        if (res.code === '200'){
          this.dialogMenuVisible = false
          this.$message.success(res.msg)
          this.$router.push("/login")
          resetRouter()
          localStorage.removeItem("user")
          resetRouter()
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
      this.request.delete("role/" + id).then(res => {
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
    selectMenu(roleId){
      this.roleId = roleId
      //  请求菜单数据
      this.request.get("/menu/list").then(res => {
        if (res.code === '200'){
          this.menuData = res.data
          //将后端返回的菜单数据处理成id数组
          this.expends = this.menuData.map(v => v.id)
        }
      })

      this.request.get("role/getRoleMenu/" + roleId).then(res =>{
        if (res.code === '200'){
          this.$nextTick(() => {
            this.$refs.tree.setCheckedKeys(res.data)
          })
        }
      })

      this.dialogMenuVisible = true
    }
  }
}
</script>

<style>
.headerBg {
  background: lightblue!important;
}
</style>
