<template>
  <div style="height: 1000px">
    <div>
      <h2 style="margin-bottom: 30px">联系方式</h2>
      <span>姓名</span>
      <div style="width: 400px; margin-top: 10px; margin-bottom: 10px">
        <el-input autosize v-model="form.consumerName"></el-input>
      </div>
      <span>身份证</span>
      <div style="width: 400px; margin-top: 10px; margin-bottom: 10px">
        <el-input autosize v-model="form.identity"></el-input>
      </div>
      <span>电话</span>
      <div style="width: 400px; margin-top: 10px; margin-bottom: 10px">
        <el-input autosize v-model="form.phone"></el-input>
      </div>
    </div>
    <div style="margin-top: 50px">
      <h2 style="margin-bottom: 30px">结算</h2>
      <div>
        <td style="height: 50px; width: 595px; line-height: 50px ; border: 1px solid #eee;">客房类型</td>
        <td style="height: 50px; width: 595px; line-height: 50px ; border: 1px solid #eee;">{{this.form.room.roomType}}</td>
      </div>
      <div>
        <td style="height: 50px; width: 595px; line-height: 50px ; border: 1px solid #eee;">房间号</td>
        <td style="height: 50px; width: 595px; line-height: 50px ; border: 1px solid #eee;">{{this.form.room.roomNumber}}</td>
      </div>
      <div>
        <td style="height: 50px; width: 595px; line-height: 50px ; border: 1px solid #eee;">单价</td>
        <td style="height: 50px; width: 595px; line-height: 50px ; border: 1px solid #eee; color: red">￥{{this.form.room.roomPrice}}/天</td>
      </div>
      <div>
        <td style="height: 50px; width: 595px; line-height: 50px ; border: 1px solid #eee;">入住日期</td>
        <td style="height: 50px; width: 595px; line-height: 50px ; border: 1px solid #eee;">{{this.form.arriveTime}}</td>
      </div>
      <div>
        <td style="height: 50px; width: 595px; line-height: 50px ; border: 1px solid #eee;">退房日期</td>
        <td style="height: 50px; width: 595px; line-height: 50px ; border: 1px solid #eee;">{{this.form.leaveTime}}</td>
      </div>
      <div>
        <td style="height: 50px; width: 595px; line-height: 50px ; border: 1px solid #eee;">天数</td>
        <td style="height: 50px; width: 595px; line-height: 50px ; border: 1px solid #eee;">{{divTime() / 24}}</td>
      </div>
      <div>
        <th style="height: 50px; width: 595px; line-height: 50px; text-align: left; border: 1px solid #eee; color: black">总价</th>
        <th style="height: 50px; width: 595px; line-height: 50px; text-align: left; border: 1px solid #eee; color: red">￥{{divTime() / 24 * this.form.room.roomPrice}}</th>
      </div>
    </div>
    <div style="margin-top: 30px">
      <el-button type="primary" @click="pay">结算</el-button>
    </div>
  </div>
</template>

<script>
export default {
  name: "Pay",
  data(){
    return{
      form: {
        arriveTime: localStorage.getItem("arriveTime")? JSON.parse(localStorage.getItem("arriveTime")) : null,
        leaveTime: localStorage.getItem("leaveTime")? JSON.parse(localStorage.getItem("leaveTime")) : null,
        room: localStorage.getItem("room")? JSON.parse(localStorage.getItem("room")) : null,
      }
    }
  },
  methods:{
    divTime() {
      let time1 = Date.parse(new Date(this.form.leaveTime))
      let time2 = Date.parse(new Date(this.form.arriveTime))
      return Math.abs(parseInt((time2 - time1) / 1000 / 3600))
    },
    pay(){
      this.request.post("/order/addOrder", this.form).then(res=>{
        if (res.code === '200'){
          console.log(res.data)
          this.$message.success(res.msg)
          this.$router.push("/front/home")
        }else {
          this.$message.error(res.msg)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>