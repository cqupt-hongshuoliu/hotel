<template >
  <el-container style="min-height: 100vh" >

    <el-aside width="sideWidth + 'px'" style="background-color: rgb(238, 241, 246); height: 100vh">
      <Aside :isCollapse="isCollapse" :logoShow="logoShow" :menus="this.user.menus"/>
    </el-aside>

    <el-container>

      <el-header style="border-bottom: 1px solid #ccc" >
        <Header :collapseBtnClass="collapseBtnClass" :collapse="collapse"/>
      </el-header>

      <el-main style="background-image: linear-gradient(-20deg, #e9defa 0%, #fbfcdb 100%);">
        <router-view/>
      </el-main>

    </el-container>
  </el-container>
</template>

<script>

import Aside from "@/components/Aside";
import Header from "@/components/Header";

export default {
  data(){
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth : 200,
      logoShow: true,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : null
    }
  },
  components:{
    Header,
    Aside
  },
  methods:{
    collapse(){
      this.isCollapse = ! this.isCollapse
      this.logoShow = ! this.logoShow
      if (this.isCollapse){
        this.sideWidth = 64
        this.collapseBtnClass = "el-icon-s-unfold"
      }else {
        this.sideWidth = 200
        this.collapseBtnClass = "el-icon-s-fold"
      }
    }
  }
}
</script>

