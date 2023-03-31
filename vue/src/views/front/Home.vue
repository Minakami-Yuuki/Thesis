<template>
  <div>
    <div>
      <el-carousel :interval="5000" arrow="always" :height="banH + 'px'">
        <el-carousel-item v-for="item in imgList" :key="item.id">
          <img :src="item.idView" class="header-background" alt="">
        </el-carousel-item>
      </el-carousel>
    </div>

    <el-divider class="divider-red"></el-divider>
    <el-divider class="divider-yellow"></el-divider>
    <el-divider class="divider-blue"></el-divider>

    <div>
      <el-card class="common-card">
        <div style="display: inline-block;">
          <span style="font-weight: bold; font-size: 20px">推荐院校（面向北京）</span>
          <el-button style="margin-left: 1280px; margin-bottom: 15px; font-size: 10px" type="primary" @click.native="load">换一换
            <i class="el-icon-refresh-right"/></el-button>
        </div>
        <el-row :gutter="20"  class="el-row" type="flex" >
          <el-col :span="8" v-for = "item in this.detail.slice(0, 6)" class="el-col" >
            <el-card class="el-card">
              <div slot="header" class="clearfix">
                <span style="font-size: 20px; font-weight: bold">{{item.name}}</span>
              </div>
              <div >
                <div class="text item item_desr">
                  <span >
                    <img v-if="item.avatar" :src="item.avatar" class="avatar">
                  </span>
                </div>
                <div class="text item">
                  <div class="item_tag">
                    <span>地区：</span>
                  </div>
                  <div class="item_desr">
                    {{item.province + item.area}}
                  </div>
                </div>
                <div class="text item">
                  <div class="item_tag">
                    <span>层级：</span>
                  </div>
                  <div class="item_desr">
                    <div v-if="item.classFlag >= 3" class="tag">985 工程</div>
                    <div v-if="item.classFlag >= 2 && item.classFlag < 3" class="tag">211 工程</div>
                    <div v-if="item.classFlag >= 1 && item.classFlag < 2" class="tag">双一流</div>
                    <div v-if="item.classFlag >= 0 && item.classFlag < 1" class="tag">普通本科</div>
                  </div>
                </div>
                <div class="text item">
                  <div class="item_tag">
                    <span>最低分数线：</span>
                  </div>
                  <div class="item_desr">
                    {{item.minScore}}
                  </div>
                </div>
                <div class="text item">
                  <div class="item_tag">
                    <span>最低排名：</span>
                  </div>
                  <div class="item_desr">
                    {{item.minRank}}
                  </div>
                </div>
                <div class="text item">
                  <div class="item_tag">
                    <span>招生网址：</span>
                  </div>
                  <div class="item_desr">
                    <router-link @click.native="jumpTo(item.link)" to>
                      <span>{{item.link}}</span>
                    </router-link>
                  </div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <span style="margin-left: 620px; font-size: 20px; font-weight: bolder"> 欢迎填报心仪院校 </span>
      </el-card>
    </div>

    <v-goTop></v-goTop>

    <el-divider class="divider-red"></el-divider>
    <el-divider class="divider-yellow"></el-divider>
    <el-divider class="divider-blue"></el-divider>
  </div>
</template>

<script>
import GoTop from "../../components/GoTop";

export default {
  name: "Home",
  created() {
    this.load()
  },
  data() {
    return {
      banH: 300,
      detail: [],
      imgList: [
        {id: 0, idView: require("@/assets/Recommend1.png")},
        {id: 1, idView: require("@/assets/Recommend2.png")},
        {id: 2, idView: require("@/assets/Recommend3.png")},
        {id: 3, idView: require("@/assets/Recommend4.png")},
        {id: 4, idView: require("@/assets/Recommend5.png")},
      ]
    }
  },
  methods: {
    setBanH () {
      this.banH = 660 // 轮播图高度
    },
    load() {
      this.request.get("/school").then(res => {
        this.detail = res.data
        this.randomSortArray(this.detail)
        console.log(this.detail)
      })
    },
    randomSortArray(arr) {
      const len = arr.length;
      for (let i = 0; i < len - 1; i++) {
        const index = parseInt(Math.random() * (len - i));
        const temp = arr[index];
        arr[index] = arr[len - i - 1];
        arr[len - i - 1] = temp;
      }
      return arr;
    },
    jumpTo(url) {
      window.open(url, "_blank")
    },
  },
  mounted() {
    this.setBanH()
  },
  components: {
    'v-goTop': GoTop
  },
}
</script>

<style scoped>
.el-button--primary {
  background-color: rgb(247, 146, 146) !important;
}
/*鼠标经过*/
.el-button--primary:hover {
  background-color: rgb(178, 253, 144) !important;
}
/*鼠标按下*/
.el-button--primary:focus {
  background-color: rgb(159, 230, 240) !important;
}

.common-card {
  border-radius: 10px;
  box-shadow: 0 0 13px #e6e6e6;
  margin: 10px;
  padding: 30px;
}

.all{
  margin-top: -30px;
  word-break: break-all;
  height: 100%;
}

.mid{
  margin-top: 25%;
  height: 50%;
}

.mid_item{
  justify-content: center;
  align-items: center;
}

.item {
  margin-bottom: 10px;
}

.item_tag{
  float:left;
  text-align:left;
}

.item_desr{
  margin-left: 30%;
  min-height: 30px;
  text-align: left;
}

.text {
  width: 100%;
  font-size: 12px;
  font-weight: bold;
  color: #909399;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both
}

.el-card {
  min-width: 50%;
  height: 100%;
  margin-right: 20px;
  /*transition: all .5s;*/
}

.el-card:hover{
  margin-top: -5px;
}

.el-row {
  margin-bottom: 20px;
  display: flex;
  flex-wrap: wrap
}
.el-col {
  border-radius: 4px;
  align-items: stretch;
  margin-bottom: 40px;
}

.el-card-define {
  min-height: 100%;
  height: 100%;
}
.el-card-define >>> .el-card__body {
  height: 100%;
}

.home-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: auto;
  width: 80%;
  height: 100%;
  background-color: #FFFFFF;
}

.tables {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  width: 100%;
}

.table-left {
  width: 100%;
}

.table-right {
  display: flex;
  flex-direction: column;
  align-content: stretch;
}

.el-carousel__item h3 {
  color: #475669;
  font-size: 18px;
  opacity: 0.75;
  line-height: 300px;
  margin: 0;
}

.el-carousel__item:nth-child(2n) {
  background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
  background-color: #d3dce6;
}

.carousel-wrap {
  width: 100%;
  height: 484px;
}

.header-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
}
</style>