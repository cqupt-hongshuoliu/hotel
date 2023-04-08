<template>
  <div style="background-image: linear-gradient(to top, #a8edea 0%, #fed6e3 100%);">
<!--    头部-->
    <div  style="height: 800px; line-height: 60px; border-bottom: 1px solid #eee;" id="building">
      <div style="display: flex; height: 60px; line-height: 60px; border-bottom: 1px solid #eee;">

        <div style="flex: 1;height: 60px; color: white; margin-left: 180px">
          <i class="el-icon-message"></i>
          <span>1161775911@qq.com</span>
        </div>

        <div style="flex: 1;height: 60px; color: white">
          <i class="el-icon-phone"></i>
          <span>15703004934</span>
        </div>

        <div style="flex: 1;height: 60px; color: white; margin-right: 100px">
          <i class="el-icon-location-outline"></i>
          <span>中国上海市中山东一路32号  邮编 ：200002</span>
        </div>

        <div  v-if="user.username" style="margin-left: 5px;">
          <el-button @click="toBack" type="text" class="el-icon-position"  size="big" style="margin-right: 5px; color: white">后台</el-button>
        </div>

        <div style="width: 180px; height: 60px; color: white" >
          <div v-if="!user.username" style="text-align: right; padding-right: 30px; color: white">
            <el-button @click="$router.push('/login')">登录</el-button>
            <el-button @click="$router.push('/register')">注册</el-button>
          </div>

          <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
            <div style="display: inline-block">
              <img :src="user.avatarUrl" alt=""
                   style="width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px"
                   referrerpolicy="no-referrer"
              >
              <span style="color: white">{{user.nickname}}</span><i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/person" style="text-decoration: none">个人信息</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <router-link to="/modifyPassword" style="text-decoration: none">修改密码</router-link>
              </el-dropdown-item>
              <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                <span style="text-decoration: none" @click="loginOut">退出</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>

      </div>

      <div style="display: flex; height: 60px; line-height: 60px; margin-top: 20px">

        <div style="width: 300px; display: flex; padding-left: 170px">
          <div style="width: 60px">
            <img src="../../assets/logo1.jpg" alt="" style="width: 50px; position: relative; top: 5px; right: 0">
          </div>
          <div style="flex: 1; color: white" >飞翔酒店</div>
        </div>

        <div>
          <el-button style="background-color: rgba(0, 0, 0, 0); color: white; margin-left: 400px" size="big" @click="to()">首页</el-button>
          <el-button style="background-color: rgba(0, 0, 0, 0); color: white; margin-left: 30px" size="big" @click="to('单间')">单间</el-button>
          <el-button style="background-color: rgba(0, 0, 0, 0); color: white; margin-left: 30px" size="big" @click="to('标间')">标间</el-button>
          <el-button style="background-color: rgba(0, 0, 0, 0); color: white; margin-left: 30px" size="big" @click="to('大床房')">大床房</el-button>
          <el-button style="background-color: rgba(0, 0, 0, 0); color: white; margin-left: 30px" size="big" @click="to('三人间')">三人间</el-button>
          <el-button style="background-color: rgba(0, 0, 0, 0); color: white; margin-left: 30px" size="big" @click="to('套房')">套房</el-button>
        </div>

<!--        <el-menu-->
<!--            :default-active="'1'"-->
<!--            class="el-menu-demo"-->
<!--            mode="horizontal"-->
<!--            background-color="rgb(0, 0, 0, 0)"-->
<!--            text-color="#fff"-->
<!--            active-text-color="#ffd04b">-->
<!--          <el-menu-item index="1">首页</el-menu-item>-->

<!--          <el-menu-item index="3">消息中心</el-menu-item>-->
<!--          <el-menu-item index="4"><a href="https://www.ele.me" target="_blank">订单管理</a></el-menu-item>-->
<!--        </el-menu>-->
      </div>

    </div>

    <div style="width: 1190px; margin: 0 auto; margin-top: 100px; ">
      <router-view/>
    </div>

    <div style="background-color: black; width: auto; height: 100px" >
      <div style="color: white; height: 100px; margin-left: 80px; line-height: 100px;">Copyright © 2023 </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Front",
  data(){
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {}
    }
  },
  created() {

  },
  methods: {
    loginOut(){
      localStorage.removeItem("user")
      this.$message.success("退出成功")
      window.location.reload()
    },
    toBack(){
      this.$router.push("/")
    },
    to(roomType){
      this.$router.push({
        path: '/front/home',
        query: {
          roomType: roomType
        }
      })
      window.location.reload()
    }
  }
}
</script>

<style>
#building{
  background:url("../../assets/img/logo.jpg");
  width:100%;
  height:100%;
  background-size:100% 100%;
}
</style>