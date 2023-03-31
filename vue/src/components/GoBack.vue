<template>
  <div id="goBack">
    <div class="goBack" @click="goBack">
      <i class="el-icon-caret-left goBackIcon"></i>
    </div>
  </div>
</template>
<script>
export default {
  name: "goBack",
  data() {
    return {
      goTopShow: true
    };
  },
  methods: {
    goBack() {
      if (window.history.length <= 1) {
        this.$router.push({path:'/'})
        return false
      } else {
        this.$router.go(-1)
      }
    }
  },
};
</script>

<style scoped>
.goBack {
  position: fixed;
  right: 20px;
  bottom: 60px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #fff;
  padding: 1px;
  cursor: pointer;
  box-shadow: 0 0 6px rgba(0, 0, 0, 0.12);
}
.goBack:hover .goTopIcon {
  color: rgba(51, 153, 255, 1);
}
.goBackIcon {
  font-size: 30px;
  margin-left: 5px;
  margin-top: 5px;
  color: rgba(51, 200, 45, 0.8);
}
</style>