<template>
  <div>
    <div style="margin: 10px 0">
      <el-carousel :interval="4000" type="card" height="400px">
        <el-carousel-item v-for="item in imgs" :key="item.url">
          <img :src="item.url" width="100%" height="100%">
        </el-carousel-item>
      </el-carousel>
    </div>

    <div style="margin: 10px 0">
      <el-card>
        <el-date-picker type="date" placeholder="依据日期搜索" v-model="arriveTime" style="width: 200px"
                        format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd hh:mm:ss"></el-date-picker>
        <el-date-picker type="date" placeholder="依据日期搜索" v-model="leaveTime" style="width: 200px; margin-left: 5px"
                        format="yyyy-MM-dd HH:mm:ss" value-format="yyyy-MM-dd hh:mm:ss"></el-date-picker>
        <el-select v-model="roomType" placeholder="请选择" style="width: 240px; margin-left: 5px">
          <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
            <i :class="item.value"/>{{ item.name }}
          </el-option>
        </el-select>
        <el-button style="margin-left: 5px" type="primary" @click="load">搜索</el-button>
        <el-button style="margin-left: 5px" type="warning" @click="reset">重置</el-button>
      </el-card>
    </div>

    <div style="margin: 10px 0">
      <el-row :gutter="10">
        <el-col :span="6" style="margin-right: 5px; width: 395px" v-for="room in rooms" :key="room.roomNumber">
          <el-card style="height: 600px; width: 390px; border:1px solid #ccc; margin-bottom: 5px">
            <img :src="room.roomPicture" alt="" width="100%" height="100%" style="margin-top: 5px; margin-bottom: 10px">
            <div style="margin-top: 10px; margin-bottom: 10px"><h2>{{room.roomTitle}}</h2></div>
            <div style="margin-top: 10px; margin-bottom: 10px; color: #FC466B">
              <i class="el-icon-message-solid"></i>
              <span style="margin-left: 10px">{{room.roomPrice}} / 天</span>
            </div>
            <div style="margin-top: 10px; margin-bottom: 10px">
              <i class="el-icon-office-building" style="margin-right: 5px"></i>
              <span style="margin-right: 210px">{{room.roomType}}</span>
              <i class="el-icon-location-information" style="margin-right: 5px"></i>
              <span>{{room.roomNumber}}</span>
            </div>
            <div class="size" style="margin-bottom: 20px"><span>{{room.roomDescription}}</span></div>
            <div>
              <el-button type="primary" @click="to(room)">查看&预定</el-button>
            </div>
          </el-card>
        </el-col>


      </el-row>
    </div>

  </div>
</template>

<script>
export default {
  name: "FrontHome",
  data(){
    return {
      imgs: [],
      arriveTime: "",
      leaveTime: "",
      roomType: "",
      rooms: [],
      options: []
    }
  },
  methods:{
    load(){
      this.request.get("/file/getZoumadeng").then( res =>{
        this.imgs = res.data
      })

      this.request.get("/room/getRoomFront",{
        params:{
          arriveTime: this.arriveTime,
          leaveTime:this.leaveTime,
          roomType: this.roomType
        }
      }).then(res => {
        this.rooms = res.data
      })
    },
    to(room){
      this.$router.push("/front/reserve")
      localStorage.setItem("room", JSON.stringify(room))
    },
    reset(){
      this.arriveTime = ""
      this.leaveTime = ""
      this.roomType = ""
      this.load()
    }
  },
  created() {
    this.roomType = this.$route.query.roomType
    this.load()
    this.request.get("/room/type").then(res =>{
      if (res.code === '200'){
        this.options = res.data
      }
    })
  }
}
</script>

<style>
.size{
  width: 350px;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
}

</style>