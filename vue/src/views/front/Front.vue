<template>
  <div>
<!--  头部  -->
    <div style="display: flex; height: 60px; line-height: 60px; border-bottom: 1px solid #eee">
      <div style="width: 300px; display: flex; padding-left: 30px;">
        <div style="width: 60px">
          <img src="../../assets/SG.jpg" alt="" style="width: 30px; position: relative; top: 5px; right: 0">
        </div>
        <div>
          <div style="flex: 1; font-weight: bold"> 高考志愿填报推荐系统 </div>
        </div>
      </div>

      <!--  导航菜单  -->
      <div style="flex: 1">
        <el-menu :default-active="this.$route.path" mode="horizontal">
          <el-menu-item index="/front/home">
            <a @click="$router.push('/front')" target="_blank">
              首页</a>
          </el-menu-item>
          <el-menu-item index="/front/school">
            <a @click="$router.push('/front/school')" target="_blank">
              高校数据查询</a>
          </el-menu-item>
          <el-menu-item index="/front/speciality">
            <a @click="$router.push('/front/speciality')" target="_blank">
              专业信息查询</a>
          </el-menu-item>
          <el-menu-item index="/front/application">
            <a @click="$router.push('/front/application')" target="_blank">
              志愿填报</a>
          </el-menu-item>
          <el-menu-item index="/front/recommend">
            <a @click="$router.push('/front/recommend')" target="_blank">
              院校推荐</a>
          </el-menu-item>
          <el-menu-item index="/front/report">
            <a @click="$router.push('/front/report')" target="_blank">
              推荐结果</a>
          </el-menu-item>
          <el-menu-item index="/front/collection">
            <a @click="$router.push('/front/collection')" target="_blank">
              院校收藏</a>
          </el-menu-item>
          <el-menu-item index="/login">
            <a @click="$router.push('/login')" target="_blank">
              后台系统</a>
          </el-menu-item>
        </el-menu>
<!--        <ul style="list-style: none; background-color: #409EFF">-->
<!--          <li class="item"><a href="../login"> 菜单1 </a></li>-->
<!--          <li class="item"><a href="../login"> 菜单2 </a></li>-->
<!--          <li class="item"><a href="../login"> 菜单3 </a></li>-->
<!--          <li class="item"><a href="../login"> 菜单4 </a></li>-->
<!--        </ul>-->
      </div>

      <!-- 名称 -->
      <div style="width: 150px">
        <div v-if="!user.username && !stdUser.username" style="display: flex; margin-top: 15px;">
          <el-button @click="$router.push('/front/login')"> 登录 </el-button>
          <el-button @click="$router.push('/front/register')"> 注册 </el-button>
        </div>
        <div v-else>
          <el-dropdown style="width: 100px; cursor: pointer">
            <div style="display: inline-block" class="item-codes">

              <img v-if="user.avatar"
                   :src="user.avatar" alt="" referrerpolicy="no-referrer"
                   style="max-width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
              <img v-else-if="stdUser.avatar"
                   :src="stdUser.avatar" alt="" referrerpolicy="no-referrer"
                   style="max-width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">

              <span v-if="user.nickname">{{user.nickname}}</span>
              <span v-else-if="stdUser.nickname">{{stdUser.nickname}}</span>
              <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
            </div>
            <el-dropdown-menu slot="dropdown" style="width: 80px">
              <el-dropdown-item style="font-size: 10px; padding: 5px 0; text-align: center"
                                @click.native="toPersonInfo">个人信息</el-dropdown-item>
              <el-dropdown-item style="font-size: 10px; padding: 5px 0; text-align: center"
                                @click.native="toChangPassword">修改密码</el-dropdown-item>
              <el-dropdown-item style="font-size: 10px; padding: 5px 0; text-align: center"
                                @click.native="logout">退出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>

      <el-dialog title="用户信息" :visible.sync="dialogInfoFormVisible" width="40%">
        <el-card style="margin-top: -40px; min-height: 100%; height: 100%; padding: 20px;" class="mid">
          <el-form label-width="80px" size="small" :rules="rules" :model="form" ref="userForm">
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
            <el-form-item style="margin-left: 55px">
              <el-button type="primary" size="small" autocomplete="off" @click="save">确定</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-dialog>

      <el-dialog title="修改密码" :visible.sync="dialogPasswordFormVisible" width="40%">
        <el-card style="margin-top: -40px; min-height: 100%; height: 100%; padding: 20px;" class="mid">
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
      </el-dialog>
    </div>

    <router-view />

  </div>


</template>

<script>
export default {
  name: "Front",
  data() {
    return {
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      stdUser: localStorage.getItem("stdUser") ? JSON.parse(localStorage.getItem("stdUser")) : {},
      form: {},
      dialogInfoFormVisible: false,
      dialogPasswordFormVisible: false,

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
    logout() {
      this.$router.push("/front")
      localStorage.clear()
      this.user.username = null
      this.stdUser.username = null
      this.$message.success("退出成功")
    },
    // 打开个人资料
    toPersonInfo() {
      this.dialogInfoFormVisible = true
      if (JSON.stringify(this.user) !== '{}') {
        this.form = this.user
      }
      else if(JSON.stringify(this.stdUser) !== '{}') {
        this.form = this.stdUser
      }
    },
    // 打开修改密码界面
    toChangPassword() {
      this.dialogPasswordFormVisible = true
      if (JSON.stringify(this.user) !== '{}') {
        this.form = this.user
      }
      else if(JSON.stringify(this.stdUser) !== '{}') {
        this.form = this.stdUser
      }
    },
    // 确认修改个人信息
    save() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          if (JSON.stringify(this.user) !== '{}') {
            // 将表单值传入后端
            this.request.post("/user", this.form).then(res => {
              if (res.code === '200') {
                this.$message.success("修改成功!")
                // 关闭弹窗
                this.dialogInfoFormVisible = false
              } else {
                this.$message.error("修改失败!")
              }
            })
          } else if (JSON.stringify(this.stdUser) !== '{}') {
            // 将表单值传入后端
            this.request.post("/stdUser", this.form).then(res => {
              if (res.code === '200') {
                this.$message({
                  duration: 800,
                  message: "修改成功!",
                  type: "success"
                })
                // 关闭弹窗
                this.dialogInfoFormVisible = false
              } else {
                this.$message({
                  duration: 1200,
                  message: "修改失败!",
                  type: "error"
                })
              }
            })
          }
        }
        else {
          this.$message({
            duration: 1200,
            message: "输入不符合要求!",
            type: "error"
          })
        }
      })
    },
    // 确认修改密码
    confirmPassword() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {
          if(this.form.password !== this.form.confirmPassword) {
            this.$message.error("两次输入的密码不一致!")
            // 中断 Axios
            return false
          }
          if (JSON.stringify(this.user) !== '{}') {
            // 将表单值传入后端
            this.request.post("/user", this.form).then(res => {
              if (res.code === '200') {
                this.$message.success("修改成功!")
                // 关闭弹窗
                this.dialogPasswordFormVisible = false
              } else {
                this.$message.error("密码不能为空!")
              }
            })
          }
          else if (JSON.stringify(this.stdUser) !== '{}') {
            // 将表单值传入后端
            this.request.post("/stdUser", this.form).then(res => {
              if (res.code === '200') {
                this.$message.success("修改成功!")
                // 关闭弹窗
                this.dialogPasswordFormVisible = false
              } else {
                this.$message.error("密码不能为空!")
              }
            })
          }
        }
        else {
          this.$message({
            duration: 1200,
            message: "输入不符合要求!",
            type: "error"
          })
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
.el-menu--collapse .el-menu .el-submenu, .el-menu--popup{
  min-width: 120px!important;
}

.item {
  display: inline-block;
  width: 100px;
  text-align: center;
  cursor: pointer;
  color: rebeccapurple;
}

.item:hover {
  background-color: yellow;
}

.school-wrap {
  margin: auto;
  text-align: center;
  height: 100%;
}
.search-header {
  display: flex;
  justify-content: space-evenly;
  align-items: stretch;
  margin: 40px auto;
  height: 40px;
}
.search-box {
  display: flex;
  justify-content: start;
  width: 50%;
}
.el-input {
  width: auto;
}
.search-button {
  margin-left: 5px;
  height: 28px;
}
.el-radio-group {
  margin: 0 40px;
}
.school-list-wrap {
  height: 100%;
}
ul {
  justify-content: center;
  padding-inline-start: 0;
}
li {
  list-style-type: none;
}
.el-card {
  margin: 20px auto;
  height: 140px;
  border-radius: 20px;
}
/* .el-card__body {
  margin: 40px auto;
} */
.school-image {
  float: left;
  /* size: auto; */
}
.school-detial {
  float: left;
}
.el-pagination {
  justify-content: center;
}
</style>