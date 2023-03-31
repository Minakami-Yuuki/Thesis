<template>
  <el-card style="width: 500px; padding: 20px;" class="mid">
    <el-form label-width="80px" size="small" :rules="rules" :model="form">

      <el-form-item class="avatar-uploader-personInfo">
      <el-upload
          action="http://localhost:8089/file/upload"
          :show-file-list="false"
          :on-success="handleAvatarSuccess">
        <img v-if="form.avatar" :src="form.avatar" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>
      </el-form-item>

      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username" auto-complete="false"></el-input>
      </el-form-item>
      <el-form-item label="昵称" prop="nickname">
        <el-input v-model="form.nickname" auto-complete="false"></el-input>
      </el-form-item>
      <el-form-item style="margin: 10px 0; text-align: left">
        <el-button type="primary" size="small" autocomplete="off" @click="save">确定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>

export default {
  name: "PersonInfo",
  data() {
    return{
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},

      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'}
        ],
        nickname: [
          {required: true, message: '请输入昵称', trigger: 'blur'},
          {min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    this.getUser().then(res => {
      // 登录成功
      this.form = res
      if(!res) {
        this.$router.push("/login")
        return false
      }
    })
  },
  methods: {
    // 同步个人信息数据
    async getUser() {
      return (await this.request.get("/user/" + this.user.id)).data
    },
    save() {
      this.request.post("/user", this.form).then(res => {
        if(res.code === '200') {
          // 触发父级更新User方法
          this.$emit("refreshUser")

          this.$router.push("/user")
          this.$message.success("保存成功")
        }
        else {
          this.$message.error("保存失败")
        }
      })
    },
    // 显示头像
    handleAvatarSuccess(res) {
      this.form.avatar = res
    }
  },
}
</script>
<style scoped>
</style>