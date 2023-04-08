<template>
  <el-card style="width: 500px;">
    <el-form label-width="80px" size="small">
      <el-upload
          class="avatar-uploader"
          action="http://localhost:9091/file/upload"
          :data={purpose:this.purpose}
          :show-file-list="false"
          :on-success="handleAvatarSuccess">
        <img v-if="form.avatarUrl" :src="form.avatarUrl" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>

      <el-form-item label="用户名" >
        <el-input v-model="form.username" disabled autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="昵称" >
        <el-input v-model="form.nickname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" >
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话" >
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="地址" >
        <el-input v-model="form.address" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>

</template>

<script>
export default {
  name: "Person",
  data(){
    return{
      form:{},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      purpose: ""
    }
  },
  created() {
    this.getUser().then(res=>{
      this.form = res
    })
  },
  methods:{
    async getUser() {
      return  (await this.request.get("user/username/" + this.user.username)).data
    },
    save(){
      this.request.post("user/save",this.form).then(res => {
        if (res.code === '200'){
          this.getUser().then(res =>{
            this.user.avatarUrl = res.avatarUrl
            localStorage.setItem("user", JSON.stringify(this.user))
            window.location.reload()
            this.$message.success(res.msg)
          })
        }else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAvatarSuccess(res) {
      this.form.avatarUrl = res
    }
  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>