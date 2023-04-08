<template>
  <div>
    <div style="margin: 10px 0">
      <el-date-picker type="date" placeholder="依据日期搜索" v-model="arriveTime" style="width: 200px"
                      format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd hh:mm:ss"></el-date-picker>
      <el-date-picker type="date" placeholder="依据日期搜索" v-model="leaveTime" style="width: 200px"
                      format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd hh:mm:ss"></el-date-picker>

      <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
      <el-button style="margin-left: 5px" type="warning" @click="reset">重置</el-button>
    </div>

    <div>
      <b>总计: {{ this.finance}}</b>
    </div>
<!--    <div style="margin: 10px 0">-->
<!--      <el-button type="primary" @click="handleAdd">新增<i class="el-icon-circle-plus-outline"></i></el-button>-->

<!--      <el-button type="danger" style="margin-left: 5px">批量删除<i class="el-icon-remove-outline"></i></el-button>-->

<!--    </div>-->

    <el-table :data="tableData" border stripe :header-cell-class-name="headerBg" row-key="id" default-expand-all max-height="500">
      <el-table-column fixed="left" label="客房名称">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" type="text" size="small">{{ scope.row.orderRoomNum }}-{{ scope.row.orderRoomTitle }}</el-button>
        </template>
      </el-table-column>
      <el-table-column prop="consumer" label="顾客" ></el-table-column>
      <el-table-column prop="phone" label="电话" ></el-table-column>
      <el-table-column prop="arriveTime" label="入住日期" ></el-table-column>
      <el-table-column prop="leaveTime" label="退房日期" ></el-table-column>
      <el-table-column label="天数">
        <template slot-scope="scope">
          <div>{{ divTime(scope.row) / 24 }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="price" label="金额￥/天" ></el-table-column>
      <el-table-column fixed="right" label="操作" width="120">
        <template slot-scope="scope">
          <el-button @click="handleEdit(scope.row)" type="text" size="small" class="el-icon-edit">编辑</el-button>
          <el-popconfirm
              confirm-button-text='确定'
              cancel-button-text='取消'
              title="您确定删除吗"
              @confirm = "handleDel(scope.row.financeId)"
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

<!--    <el-dialog title="入住登记信息" :visible.sync="dialogFormVisible" width="400px">-->
<!--      <el-form label-width="80px" size="small">-->
<!--        <el-form-item label="消费者" >-->
<!--          <el-input v-model="form.consumerName" style="width: 240px"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="电话" >-->
<!--          <el-input v-model="form.phone" style="width: 240px"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="身份证" >-->
<!--          <el-input v-model="form.identity" style="width: 240px"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="押金" >-->
<!--          <el-input v-model="form.deposit" style="width: 240px"></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="登记时间" >-->
<!--          <el-date-picker type="date" placeholder="选择时间" v-model="recordTime" style="width: 240px"-->
<!--                          format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd hh:mm:ss"></el-date-picker>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="登记人" >-->
<!--          <el-input v-model="form.username" style="width: 240px"></el-input>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button @click="dialogFormVisible = false">取 消</el-button>-->
<!--        <el-button type="primary" @click="save">确 定</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->

  </div>
</template>

<script>
export default {
  name: "Finance",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      arriveTime: "",
      leaveTime: "",
      form: {},
      finance: 0,
      dialogFormVisible: false,
      headerBg: 'headerBg'
    }
  },
  created() {
    //请求分页查询数据
    this.load()
  },
  methods: {
    load(){
      console.log(this.arriveTime)
      this.request.get("/finance/page",{
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          arriveTime: this.arriveTime,
          leaveTime: this.leaveTime
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
        this.finance = 0
        for ( let item of this.tableData){
          let time1 = Date.parse(new Date(item.arriveTime))
          let time2 = Date.parse(new Date(item.leaveTime))
          this.finance = this.finance + item.price * Math.abs(parseInt((time2 - time1) / 1000 / 3600)) / 24
        }
      })
    },
    reset(){
      this.arriveTime = ""
      this.leaveTime = ""
      this.load()
    },
    save(){
      this.request.post("/finance/save", this.form).then(res => {
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
      this.request.delete("/finance/" + id).then(res => {
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
