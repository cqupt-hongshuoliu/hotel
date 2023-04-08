<template>
  <div class="wrapper">
    <div style="margin: 80px auto; background-color: #fff; width: 350px; height: 520px; padding: 10px; border-radius: 10px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>注册</b></div>
      <el-form :model="form" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input placeholder="请输入用户名" size="medium" style="margin: 10px 0" prefix-icon="el-icon-user" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="请确认密码" size="medium" style="margin: 10px 0" prefix-icon="el-icon-lock" show-password v-model="form.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input placeholder="请输入昵称" size="medium" style="margin: 10px 0" prefix-icon="el-icon-s-custom" v-model="form.nickname"></el-input>
        </el-form-item>

        <el-form-item prop="phone">
          <el-input placeholder="请输入手机号" size="medium" style="margin: 10px 0" prefix-icon="el-icon-phone" v-model="form.phone"></el-input>
        </el-form-item>
        <div style="margin: 10px 0; text-align: right">
          <el-button type="primary" size="small" autocomplete="off" @click="userRegister">确认</el-button>
          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/login')">返回登录</el-button>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import md5 from "js-md5";
import {Base64} from "js-base64";

export default {
  name: "Register",
  data() {
    return {
      form: {},
      registerForm: {},
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur'},
          { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur'}
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur'},
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur'},
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods: {
    userRegister() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          if (this.form.password !== this.form.confirmPassword) {
            this.$message.error("两次密码不一致")
            return false;
          }
          this.registerForm = this.form
          this.registerForm.password = this.base64encode(this.form.password)
          this.request.post("user/register", this.registerForm).then(res => {
            if(res.code === '200'){
              this.$message.success(res.msg)
              this.$router.push("/login")
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