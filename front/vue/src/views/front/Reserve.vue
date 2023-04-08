<template>
  <div class="box">
    <div title="左" style="height: 650px; border:1px solid #ccc; margin-right: 10px" >
      <el-carousel >
        <el-carousel-item v-for="item in imgs" :key="item.url">
          <img :src="item.url" width="100%" height="100%">
        </el-carousel-item>
      </el-carousel>
      <div style="margin-top: 50px; margin-left: 5px">
        <h1>{{this.room.roomNumber}}-{{this.room.roomTitle}}</h1>
      </div>
      <div style="margin-top: 50px; margin-left: 5px; margin-right: 5px">
        <span>{{this.room.roomDescription}}</span>
      </div>
    </div>
    <div title="右" style="height: 650px; border:1px solid #ccc">

      <img src="../../assets/logo1.jpg" alt="" style="width: 100px; height: 100px; position: relative; margin-top: 30px;
           margin-bottom: 10px; margin-left: 200px">
      <div style="margin-left: 200px">
        <h1>{{this.room.roomPrice}} /天</h1>
      </div>
      <div style="margin-left: 100px;margin-top: 20px">
        <span >入住日期</span>
        <div style="margin-top: 10px; margin-bottom: 10px">
          <el-date-picker type="date" v-model="arriveTime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd hh:mm:ss"
                          ></el-date-picker>
        </div>
        <span >退房日期</span>
        <div style="margin-top: 10px">
          <el-date-picker type="date"  v-model="leaveTime" format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd hh:mm:ss"></el-date-picker>
        </div>
        <div style="margin-top: 10px">
          <el-button type="primary" @click="reserve">快速预定</el-button>
        </div>
      </div>

      <div style="margin-top: 10px; border:1px solid #000000; margin-left: 50px; width: 500px; height: 200px" >
        <h1 style="margin-left: 30px; margin-top: 30px; margin-bottom: 20px">客服电话</h1>
        <span style="margin-left: 30px; margin-bottom: 20px">24小时客服电话，快速咨询和预定</span>
        <i class="el-icon-phone" style="margin-left: 30px"></i>
        <span style="margin-left: 5px">15703004934</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Reserve",
  data(){
    return {
      room: localStorage.getItem("room")? JSON.parse(localStorage.getItem("room")) : null,
      user: localStorage.getItem("user")? JSON.parse(localStorage.getItem("user")) : null,
      imgs: [],
      arriveTime:  "",
      leaveTime:  ""
    }
  },
  created() {
    this.load()
  },
  methods: {
    load(){
      this.request.get("/file/getZoumadeng").then( res =>{
        this.imgs = res.data
      })
    },
    reserve(){
      if (this.user === null){
        this.$message.error("请登陆账户")
        return
      }
      if ( this.arriveTime === "" || this.leaveTime === ""){
        this.$message.error("时间不可为空")
        return
      }
      localStorage.setItem("arriveTime", JSON.stringify(this.arriveTime))
      localStorage.setItem("leaveTime", JSON.stringify(this.leaveTime))
      this.$router.push("/front/pay")
    }
  }
}
</script>

<style >
.box {
  width: 100%;
  height: 800px;
  display: flex;
  flex-direction: row;
  margin-bottom: 30px;

}
.box > div {
  height: 22px;
  flex: 1;
}
</style>