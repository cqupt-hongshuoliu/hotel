<template>
  <div>

    <div style="margin: 10px 0">
      <el-input v-model="orderConsumerName" suffix-icon="el-icon-search" placeholder="依据顾客名模糊检索" style="width: 200px"></el-input>
      <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset">重置</el-button>
    </div>

    <div style="margin: 10px 0">
      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>

<!--      <el-button type="danger" style="margin-left: 5px">批量删除<i class="el-icon-remove-outline"></i></el-button>-->

    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" row-key="id" default-expand-all max-height="500">
      <el-table-column fixed="left" label="订单号" width="138px">
        <template slot-scope="scope">
          <el-button @click="to" type="text" size="small">{{ scope.row.orderNum }}</el-button>
        </template>
      </el-table-column>
      <el-table-column fixed="left" label="客房名称">
        <template slot-scope="scope">
          <span >{{ scope.row.orderRoomNum }}-{{ scope.row.orderRoomTitle }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed="left" label="联系人信息" width="190px">
        <template slot-scope="scope">
          <div>姓名: {{ scope.row.orderConsumerName }}</div>
          <div>电话: {{ scope.row.orderPhone }}</div>
          <div>身份证: {{ scope.row.orderIdentity }}</div>
        </template>
      </el-table-column>
      <el-table-column fixed="left" label="住宿时间" width="200px">
          <template slot-scope="scope">
            <div>入住时间: {{ scope.row.arriveTime }}</div>
            <div>到期时间: {{ scope.row.leaveTime }}</div>
            <div>总计: {{divTime(scope.row) / 24}}天</div>
          </template>
      </el-table-column>
      <el-table-column fixed="left" label="总价￥">
        <template slot-scope="scope">
          <div>{{ scope.row.orderPrice * divTime(scope.row) / 24 }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="orderState" label="状态" ></el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="140px"></el-table-column>
      <el-table-column fixed="right" label="操作" width="470px">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" type="warning" class="el-icon-edit" style="margin-left: 5px"
              :disabled="(scope.row.orderState=='已取消') || (scope.row.orderState=='已退房')">编辑</el-button>

          <el-popconfirm confirm-button-text='确定' cancel-button-text='取消' title="您确定取消吗" @confirm = "handleCancel(scope.row.orderId)">
            <el-button slot="reference" type="warning" class="el-icon-edit" style="margin-left: 5px"
                :disabled="(scope.row.orderState=='已取消') || (scope.row.orderState=='已退房') || (scope.row.orderState=='已入住')">取消订单</el-button>
          </el-popconfirm>

          <el-popconfirm confirm-button-text='确定' cancel-button-text='取消' title="您确定入住吗" @confirm = "handleArrive(scope.row.orderId)">
            <el-button slot="reference" type="primary" class="el-icon-edit" style="margin-left: 5px"
                :disabled="(scope.row.orderState=='已取消') || (scope.row.orderState=='已退房') || (scope.row.orderState=='已入住')">入住登记</el-button>
          </el-popconfirm>

          <el-popconfirm confirm-button-text='确定' cancel-button-text='取消' title="您确定退房吗" @confirm = "handleLeave(scope.row.orderId)">
            <el-button slot="reference" type="primary" class="el-icon-edit" style="margin-left: 5px"
                       :disabled="(scope.row.orderState=='已取消') || (scope.row.orderState=='已退房')">退房登记</el-button>
          </el-popconfirm>

          <el-popconfirm confirm-button-text='确定' cancel-button-text='取消' title="您确定删除吗" @confirm = "handleDel(scope.row.orderId)">
            <el-button slot="reference" type="danger" class="el-icon-remove-outline" style="margin-left: 5px">删除</el-button>
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

    <el-dialog title="订单信息" :visible.sync="dialogFormVisible" width="400px">
      <el-form label-width="100px" size="small">
        <el-form-item label="房间号" >
          <el-input v-model="form.orderRoomNum" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item label="客房名" >
          <el-input v-model="form.orderRoomTitle" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item label="预约者姓名" >
          <el-input v-model="form.orderConsumerName" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item label="预约者电话" >
          <el-input v-model="form.orderPhone" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item label="预约者身份证" >
          <el-input v-model="form.orderIdentity" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item label="入住时间" >
          <el-col :span="11">
            <el-date-picker type="date" placeholder="选择日期" v-model="form.arriveTime" style="width: 240px"
                            format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd hh:mm:ss"></el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="到期时间" >
          <el-col :span="11">
            <el-date-picker type="date" placeholder="选择日期" v-model="form.leaveTime" style="width: 240px"
                            format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd hh:mm:ss"></el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="单价￥" >
          <el-input v-model="form.orderPrice" style="width: 240px"></el-input>
        </el-form-item>
        <el-form-item label="状态" >
          <el-select v-model="form.orderState" placeholder="请选择" style="width: 240px">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value"/>{{ item.name }}
            </el-option>
          </el-select>
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
  name: "Order",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      orderConsumerName: "",
      form: {},
      dialogFormVisible: false,
      headerBg: 'headerBg',
      disabled: false,
      options:[],
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null,
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    load(){
      this.request.get("/order/page",{
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          orderConsumerName: this.orderConsumerName
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/order/state").then(res => {
        this.options = res.data
      })
    },
    reset(){
      this.orderConsumerName = ""
      this.load()
    },
    save(){
      this.request.post("/order/save", this.form).then(res => {
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
      this.request.delete("/order/" + id).then(res => {
        if (res.code === '200'){
          this.$message.success(res.msg)
          this.load()
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    handleCancel(id){
      this.request.put("/order/cancel/" + id).then(res => {
        if (res.code === '200'){
          this.$message.success(res.msg)
          this.load()
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    handleArrive(id){
      this.request.post("/order/arrive/" + id, this.user).then(res => {
        if (res.code === '200'){
          this.$message.success(res.msg)
          this.load()
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    handleLeave(id){
      this.request.post("/order/leave/" + id, this.user).then(res => {
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
    divTime(row) {
      let time1 = Date.parse(new Date(row.leaveTime))
      let time2 = Date.parse(new Date(row.arriveTime))
      return Math.abs(parseInt((time2 - time1) / 1000 / 3600))
    }
  }
}
</script>

<style>
.headerBg {
  background: lightblue!important;
}
</style>
