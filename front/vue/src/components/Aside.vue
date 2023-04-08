<template>
  <el-menu :default-openeds="['1', '3']" style="min-height: 100%; overflow-x: hidden"
           background-color = "rgb(48, 65, 86)"
           text-color="#fff"
           active-text-color="#ffd04b"
           :collapse-transition="false"
           :collapse="isCollapse"
           router
  >
    <div style="height: 60px; line-height: 60px; width: auto; text-align: center">
      <img src="../assets/logo1.jpg" alt="" style="width: 40px; position: relative; top: 5px; margin-right: 5px; margin-left: 5px">
      <b style="color: #cccccc; padding-right: 30px;padding-left: 24px" v-show="logoShow">后台管理系统</b>
    </div>

    <div v-for="item in menus" :key="item.id">
      <div v-if="item.path">
        <el-menu-item :index="item.path">
          <i :class="item.icon"></i>
          <span slot="title" style="margin-left: 20px">{{ item.name}}</span>
        </el-menu-item>
      </div>
      <div v-else>
        <el-submenu :index="item.id + ''">
          <template slot="title"><i :class="item.icon"></i>
            <span style="margin-left: 20px">{{ item.name}}</span>
          </template>
          <div v-for="subItem in item.children" :key="subItem.id">
            <el-menu-item :index="subItem.path">
              <i :class="subItem.icon"></i>
              <span slot="title" style="margin-left: 20px">{{ subItem.name}}</span>
            </el-menu-item>
          </div>
        </el-submenu>
      </div>

    </div>

  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean,
    logoShow: Boolean,
    menus: Array
  }
}
</script>

<style scoped>

</style>