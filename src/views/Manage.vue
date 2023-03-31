<template>
    <el-container style="min-height: 100vh;">

      <el-aside :width="sideWidth + 'px'" style="background-color: rgb(238, 241, 246); box-shadow: 2px 0 6px; rgb(0 21 41 / 35%)">
        <Aside :isCollapse="isCollapse"/>
      </el-aside>

      <el-container>

        <el-header style="border-bottom: 1px solid #ccc;">
          <Header :collapseBtnClass="collapseBtnClass"
                  :collapse="collapse"
                  :user="user"/>
        </el-header>

        <el-main>
          <!-- 当前页面子路由 -->
          <router-view @refreshUser="getUser" />
        </el-main>

      </el-container>
    </el-container>
</template>

<script>
// @ is an alias to /src

import Aside from "@/components/Aside.vue";
import Header from "@/components/Header.vue"

export default {
  name: 'Home',
  data() {
    return {
      collapseBtnClass: 'el-icon-s-fold',
      isCollapse: false,
      sideWidth: 200,
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  components: {
    Aside,
    Header
  },
  methods: {
    collapse() {  // 点击收缩按钮触发
      this.isCollapse = !this.isCollapse

      if(this.isCollapse) {
        this.sideWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
      }
      else {
        this.sideWidth = 200
        this.collapseBtnClass = 'el-icon-s-fold'
      }
    },
    getUser() {
      // 后台获取数据
      this.request.get("/user/" + this.user.id).then(res => {
        // 重新赋值 User
        this.user = res.data
      })
    },
  }
}
</script>
