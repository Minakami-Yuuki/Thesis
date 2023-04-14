<template>
  <el-menu :default-openeds="['1', '2', '3', '4']"
           :default-active="this.$route.path"
           style="min-height: 100%; overflow-x: hidden"
           background-color="#000"
           text-color="#fff"
           active-text-color="#409eff"
           :collaspe-transition="false"
           :collapse="isCollapse"
           router
           @select="handleSelect"
  >
    <div style="height: 60px; line-height: 60px; text-align: center">
      <img src="../assets/Rin.png" alt="" style="width: 20px; position: relative; top: 3px; margin-right: 3px">
      <b style="color: #fff" v-show="!isCollapse">后台管理系统</b>
    </div>

    <el-menu-item index="/home">
        <i class="el-icon-house"></i>
        <span slot="title">主页</span>
    </el-menu-item>

    <el-submenu index="1">
      <template slot="title">
        <i class="el-icon-location"></i>
        <span slot="title">系统管理</span>
      </template>
          <span slot="title"/>
          <el-menu-item index="/user" class="el-icon-user"> 管理员信息管理 </el-menu-item>
    </el-submenu>

    <el-submenu index="2">
      <template slot="title">
        <i class="el-icon-menu"></i>
        <span slot="title">用户管理</span>
      </template>
      <span slot="title"/>
      <el-menu-item index="/Student" class="el-icon-user-solid"> 用户信息管理 </el-menu-item>
      <el-menu-item index="/Application" class="el-icon-medal"> 用户志愿管理 </el-menu-item>
      <el-menu-item index="/Collection" class="el-icon-folder-add"> 用户收藏管理 </el-menu-item>
    </el-submenu>

    <el-submenu index="3">
      <template slot="title"><i class="el-icon-setting"></i>
        <span slot="title">院校管理</span>
      </template>
      <span slot="title"/>
      <el-menu-item index="/school" class="el-icon-house"> 院校信息管理 </el-menu-item>
      <el-menu-item index="/schoolSpecialty" class="el-icon-price-tag"> 院校专业管理 </el-menu-item>
      <el-menu-item index="/schoolScore" class="el-icon-discount"> 院校分数线管理 </el-menu-item>
      <el-menu-item index="/schoolDescription" class="el-icon-cpu"> 院校具体描述管理 </el-menu-item>
    </el-submenu>

    <el-submenu index="4">
      <template slot="title"><i class="el-icon-setting"></i>
        <span slot="title">专业管理</span>
      </template>
      <span slot="title"/>
      <el-menu-item index="/specialty" class="el-icon-connection"> 专业细分管理 </el-menu-item>
    </el-submenu>

    <el-menu-item index="/front">
      <template slot="title"><i class="el-icon-s-flag"></i>
        <span>前端首页</span>
      </template>
    </el-menu-item>

  </el-menu>
</template>

<script>
export default {
  name: "Aside",
  props: {
    isCollapse: Boolean
  },
  methods: {
    handleSelect(index) {
      console.log(index)
    }
  },
}
</script>

<style scoped>

</style>