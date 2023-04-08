<template>
  <el-card style="width: 500px;">
    <el-form label-width="100px" :model="form" :rules="rules" ref="userForm">
      <el-form-item label="旧密码" prop="password">
        <el-input v-model="form.password" placeholder="请输入密码" size="medium" prefix-icon="el-icon-lock" show-password></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" placeholder="请输入密码" size="medium" prefix-icon="el-icon-lock" show-password></el-input>
      </el-form-item>
      <el-form-item label="确认新密码" prop="confirmNewPassword">
        <el-input v-model="form.confirmNewPassword" placeholder="请输入密码" size="medium" prefix-icon="el-icon-lock" show-password></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="modifyPassword">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>

</template>

<script>
import md5 from "js-md5";
import {Base64} from "js-base64";

export default {
  name: "ModifyPassword",
  data(){
    return{
      form:{},
      modifyForm: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        password: [
          { required: true, message: '请输入密码', trigger: 'blur'},
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        newPassword: [
          { required: true, message: '请输入密码', trigger: 'blur'},
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ],
        confirmNewPassword: [
          { required: true, message: '请确认密码', trigger: 'blur'},
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur'}
        ]
      }
    }
  },
  methods:{
    modifyPassword(){
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          if (this.form.newPassword !== this.form.confirmNewPassword) {
            this.$message.error("两次密码不一致")
            return false;
          }
          this.modifyForm = this.form
          this.modifyForm.password = this.base64encode(this.form.password)
          this.modifyForm.newPassword = this.base64encode(this.form.newPassword)
          this.modifyForm.confirmNewPassword = this.base64encode(this.form.confirmNewPassword)
          this.request.post("user/modifyPassword/" + this.user.username, this.modifyForm).then(res => {
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
      let salt = md5("1a2b3c4d")
      password = salt[0] + salt[2] + password + salt[5] + salt[4]
      return Base64.encode(password)
    }
  }
}
</script>

<style>

</style>