<template>
  <div class="wrapper">
    <div style="margin: 200px auto; background-color: #fff; width: 350px; height: 300px; padding: 20px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登录</b></div>
      <el-form :model="form" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="form.password"></el-input>
        </el-form-item>
        <div style="margin: 10px 0; text-align: right">
          <el-button type="primary" size="small" autocomplete="off" @click="login">登录</el-button>
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import {resetRouter, setRouters} from "@/router";
import md5 from 'js-md5';
import {Base64} from 'js-base64'

export default {
  name: "Login",
  data() {
    return {
      form: {},
      loginForm: {
        username: "",
        password: ""
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur'},
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur'},
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          this.loginForm.username = this.form.username
          this.loginForm.password = this.base64encode(this.form.password)
          this.request.post("user/login", this.loginForm).then(res => {
            if(res.code === '200'){
              localStorage.setItem("user", JSON.stringify(res.data))
              resetRouter() //  重置路由
              setRouters()  //  动态设置当前用户路由
              this.$message.success(res.msg)
              if (res.data.role === 'ROLE_USER'){
                this.$router.push("/front/home")
              }else {
                this.$router.push("/")
              }
            }else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    },
    base64encode(password){
      let salt = md5(this.form.username)
      password = salt[0] + salt[2] + password + salt[5] + salt[4]
      return Base64.encode(password)
    }
  }
}
</script>

<style scoped>
  .wrapper{
    height: 100vh;
    background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);
    overflow: hidden;
  }
</style>