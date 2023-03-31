<template>
 <div class="wrapper-back">
   <div style="margin: 150px auto; width: 450px; height: 400px; padding: 20px; border-radius: 10px " class="login_form">
     <div style="margin: 20px 0; text-align: center; font-size: 24px; font-weight: bold; color: #EE82EE"> Register </div>

       <el-form  :model="user" :rules="rules" ref="userForm">

         <el-form-item prop="username">
           <el-input size="medium"
                     placeholder="请输入用户名"
                     style="margin: 10px 0"
                     prefix-icon="el-icon-star-on"
                     v-model="user.username">
           </el-input>
         </el-form-item>

         <el-form-item prop="nickname">
           <el-input size="medium"
                     placeholder="请输入昵称"
                     style="margin: 10px 0"
                     prefix-icon="el-icon-help"
                     v-model="user.nickname">
           </el-input>
         </el-form-item>

         <el-form-item prop="password">
           <el-input size="medium"
                     placeholder="请输入密码"
                     style="margin: 10px 0"
                     prefix-icon="el-icon-star-off"
                     show-password
                     v-model="user.password">
           </el-input>
         </el-form-item>

         <el-form-item prop="confirmPassword">
           <el-input size="medium"
                     placeholder="请再次输入密码"
                     style="margin: 10px 0"
                     prefix-icon="el-icon-star-off"
                     show-password
                     v-model="user.confirmPassword">
           </el-input>
         </el-form-item>

         <el-form-item style="margin: 10px 0; text-align: right">
           <el-button type="danger" size="small" autocomplete="off" @click="register">注册</el-button>
           <el-button type="primary" size="small" autocomplete="off" @click="BackToLogin">返回登录</el-button>
         </el-form-item>

       </el-form>
     </div>
   </div>
</template>

<script>

export default {
  name: "Register",
  data() {
    return {
      user: {},

      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'}
        ],
        nickname: [
          {required: true, message: '请输入昵称', trigger: 'blur'},
          {min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请再次输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    register() {
      // 登录请求校验
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          if(this.user.password !== this.user.confirmPassword) {
            this.$message.error("两次输入的密码不一致!")
            // 中断 Axios
            return false
          }
          // 表单验证合法
          // 获取后端数据判断跳转
          this.request.post("/user/register", this.user).then(res => {
            if(res.code === '200') {
              // 登录成功: 延时跳转
              setTimeout(() => {
                this.$router.push("/login")
                this.$message({
                  duration: 800,
                  message: "注册成功!",
                  type: "success"
                })
              }, 100)
            }
            // 登录失败:
            else {
              this.$message.error(res.msg)
            }
          })
        }
        // 表单验证不合法 (未写数据)
        else {
          this.$message({
            duration: 1200,
            message: "用户名、昵称、密码不能为空!",
            type: "error"
          })
        }
      })
    },
    BackToLogin() {
      setTimeout(() => {
        this.$router.push("/login")
      }, 100)
    },
  }
}
</script>

<style scoped>
.login_form{
  width: 100%;
  border:1px solid  transparent;
  border-radius: 15px;
  padding-right: 30px;
  /* 为其整体设置接近透明的效果*/
  background-color: rgba(255,255,255,0.3);
  /* 设置box-shadow使其有立体感 */
  box-shadow: 5px 5px 0 0  rgba(0,0,0,0.2);
  position: relative;
  left: -15px;
}
</style>