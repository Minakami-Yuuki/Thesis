<template>
  <el-card style="width: 500px; padding: 20px;" class="mid">
    <el-form label-width="80px" size="small" :rules="rules" :model="form" ref="userForm">
      <el-form-item class="avatar-uploader-changePassword">
        <img v-if="form.avatar" :src="form.avatar" class="avatar">
      </el-form-item>
      <el-form-item label="修改密码" prop="password">
        <el-input v-model="form.password" auto-complete="false" show-password></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" auto-complete="false" show-password></el-input>
      </el-form-item>
      <el-form-item style="margin: 10px 0; text-align: left">
        <el-button type="primary" size="small" autocomplete="off" @click="confirmPassword">确定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import {Header} from "element-ui";

export default {
  name: "PersonInfo",
  data() {
    return{
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},

      rules: {
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请再次输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      },
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
    confirmPassword() {
      // 登录请求校验
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          if(this.form.password !== this.form.confirmPassword) {
            this.$message.error("两次输入的密码不一致!")
            // 中断 Axios
            return false
          }
          // 表单验证合法
          // 获取后端数据判断跳转
          this.request.post("/user", this.form).then(res => {
            if(res.code === '200') {
              // 修改成功: 延时跳转
              setTimeout(() => {
                this.$router.push("/login")
                this.$message.success("修改成功, 请重新登录")
              }, 100)
            }
          })
        }
        // 表单验证不合法 (未写数据)
        else {
          this.$message.error("密码不能为空!")
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