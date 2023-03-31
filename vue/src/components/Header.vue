<template>
  <div style="font-size: 12px; line-height: 60px; display: flex; min-width:1200px;">
    <div style="flex: 1; font-size: 18px">
      <span :class="collapseBtnClass" style="cursor: pointer" @click="collapse"></span>
    </div>

    <el-dropdown style="width: 100px; cursor: pointer">
      <div style="display: inline-block" class="item-codes">
        <img :src="user.avatar" alt="" referrerpolicy="no-referrer"
             style="max-width: 30px; border-radius: 50%; position: relative; top: 10px; right: 5px">
        <span>{{user.nickname}}</span> <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
      </div>
      <el-dropdown-menu slot="dropdown" style="width: 80px" >
        <el-dropdown-item style="font-size: 10px; padding: 5px 0; text-align: center"
                          @click.native="toPersonInfo">个人信息</el-dropdown-item>
        <el-dropdown-item style="font-size: 10px; padding: 5px 0; text-align: center"
                          @click.native="toChangPassword">修改密码</el-dropdown-item>
        <el-dropdown-item style="font-size: 10px; padding: 5px 0; text-align: center"
                          @click.native="logout">退出</el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  props: {
    collapseBtnClass: String,
    collapse: Function,
    user: Object
  },
  data() {
    return {
      path: [],
    }
  },
  methods: {
    logout() {
      this.$router.push("/login")
      localStorage.clear()
      this.$message.success("退出成功")
    },
    toPersonInfo() {
      this.$router.push("/personinfo")
    },
    toChangPassword() {
      this.$router.push("/changePSW")
    }
  }
}
</script>

<style scoped>

</style>