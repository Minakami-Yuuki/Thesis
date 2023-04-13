<template>
  <div class="detail-wrap" :datasrc="tableData">
    <div id="tag-card" class="common-card">
      <div class="logo-wrap">
        <img v-if="detail.avatar" :src="detail.avatar" class="logo">
      </div>
      <div class="right-content">
        <div class="name-pos">
          {{ detail.name }}
          <div class="tag" style="font-size: 10px">
            {{ detail.detailProvince + detail.detailArea }}
          </div>
        </div>
        <div class="tag-line">
          <div class="tag">公办</div>
          <div v-if="detail.detailClassFlag >= 0" class="tag">普通本科</div>
          <div v-if="detail.detailClassFlag >= 3" class="tag">985工程</div>
          <div v-if="detail.detailClassFlag >= 2" class="tag">211工程</div>
          <div v-if="detail.detailClassFlag >= 1" class="tag">双一流</div>
        </div>
      </div>
    </div>

    <div id="pic-intro">
      <div class="pic-wrap">
        <img class="vid-img"
             :src= "backgroundUrl"  alt=""/>
      </div>
      <div id="intro" class="common-card">
        <div class="round-wrap">
          <div>
            <div id="round-study" ref="round-study"></div>学习指数
          </div>
          <div>
            <div id="round-life"></div>生活指数
          </div>
          <div>
            <div id="round-job"></div>就业指数
          </div>
          <div>
            <div id="round-compare"></div>综合评分
          </div>
        </div>
        <div class="content-wrap" :title="detail.detailDescription">
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          {{ detail.detailDescription | ellipsis}}
        </div>
      </div>
    </div>

    <div class="tables">
      <div class="table-left">
        <div class="score-table common-card">
          <div style="margin-bottom: 10px">
            <el-select v-model="form.type" placeholder="Select">
              <el-option value="国家特色专业" />
            </el-select>
          </div>
          <el-card v-for="(name, index) in this.detail.detailSpecialty">
            <el-descriptions :title="name" :key="index">
              <el-descriptions-item label="最低分数" size="small">
                <el-tag style="font-weight: bold">
                  {{(Math.random() * (detail.detailScore - (detail.detailScore - 20)) + (detail.detailScore - 20)).toFixed(0)}}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item label="最低排名" size="small">
                <el-tag style="font-weight: bold">
                  {{(Math.random() * (detail.detailRank - (detail.detailRank - 20)) + (detail.detailRank - 20)).toFixed(0)}}
                </el-tag>
              </el-descriptions-item>
              <el-descriptions-item>
                <el-button type="danger"
                           @click="
                           collection(tableData, name)"
                           size="mini" style="font-weight: bold">
                  收藏
                </el-button>
              </el-descriptions-item>
            </el-descriptions>
          </el-card>
        </div>
      </div>
      <div class="table-right">
        <!-- <div class="table-wrap"> -->
        <div id="level-chart" class="common-card"></div>
        <!-- </div> -->
        <!-- <div class="bar-wrap"> -->
        <div id="rate-bar" class="common-card"></div>
        <div v-show="detail.men_rate" class="boy-girl common-card">
          <div class="el-icon-male" style="font-size: 35px; color: #409EFF; margin-left: 30px" />
          <div style="margin-right: 10px;">
            {{detail.men_rate}}%
          </div>
          <div v-show="detail.men_rate" id="scale-bar"></div>
          <div style="margin-left: 10px;">
            {{detail.female_rate}}%
          </div>
          <div class="el-icon-female" style="font-size: 35px; color: 	#FF44AA; margin-left: 10px;" />
        </div>
        <!-- </div> -->
      </div>
    </div>
    <v-goBack></v-goBack>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import GoBack from "../../components/GoBack";

export default {
  name: "Details",
  created() {
    this.load()
  },
  data() {
    return {
      pageNum: 1,
      pageSize: 10,
      tableData: [],
      tableDataList: [],
      detail: {},
      collectionList: JSON.parse(localStorage.getItem("collection")) ? JSON.parse(localStorage.getItem("collection")) : [],
      form: {
        type: "国家特色专业",
      },
      backgroundUrl: require('../../assets/background.png')
    }
  },
  // 省略字数
  filters: {
    ellipsis(value) {
      if (!value) return '';
      if (value.length > 200) {
        return value.slice(0, 200) + '...'
      }
      return value
    }
  },
  components: {
    'v-goBack': GoBack
  },
  methods: {
    load() {
      // 跨页面获取信息
      let detailFrom = this.$route.query
      console.log(detailFrom.detailName)
      this.detail.avatar = detailFrom.detailAvatar
      this.detail.name = detailFrom.detailName
      this.request.get("/school/pageName", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: detailFrom.detailName
        }
      }).then(res => {
        // Token 不合法
        // if (res.code === '402') {
        //   this.$router.push("/login")
        //   return false
        // }
        console.log(res)
        this.tableData = res.data.records[0]
        this.switchClassFlag()
        this.detail.detailClassFlag = this.tableData.classFlag
        this.detail.detailProvince = this.tableData.province
        this.detail.detailArea = this.tableData.area
        this.detail.detailDescription = this.tableData.description
        this.detail.detailSpecialty = this.tableData.specialty.split('、').slice(0, 10)
        this.detail.detailScore = this.tableData.minScore
        this.detail.detailRank = this.tableData.minRank
      })
      this.detail.men_rate = (Math.random() * (80 - 60) + 60).toFixed(2)
      this.detail.female_rate = (100 - this.detail.men_rate).toFixed(2)
      console.log("detailAvatar", this.detail)
    },
    // 收藏操作
    collection(tableData, name) {
      if (localStorage.getItem("stdUser") || localStorage.getItem("user")) {
        this.collectionList.push(tableData)
        this.$set(this.collectionList, this.collectionList.length - 1, {
          area: tableData.area,
          classFlag: tableData.classFlag,
          id: tableData.id,
          name: tableData.name,
          province: tableData.province,
          minRank: tableData.minRank,
          minScore: tableData.minScore,
          specialty: name,
          avatar: tableData.avatar,
        });
        // this.$set(this.collectionList[this.collectionList.length - 1], 'specialty', name)
        localStorage.setItem("collection", JSON.stringify(this.collectionList))

        let i = 0, j = 0
        for (i; i < this.collectionList.length - 1 && j === 0; i++) {
          if ((this.collectionList[i].name === tableData.name) && (this.collectionList[i].specialty === name)) {
            this.collectionList.splice(i, 1)
            localStorage.setItem("collection", JSON.stringify(this.collectionList))
            j = 1
          }
        }
        if (j === 1) {
          this.$message({
            duration: 1200,
            message: "当前院校已添加至收藏!",
            type: "error"
          })
        }
        else {
          this.$message({
            duration: 800,
            message: "收藏成功!",
            type: "success"
          })
        }
      }
      else {
        this.$message({
          duration: 1200,
          message: "请进行用户登录!",
          type: "error"
        })
      }
    },
    switchClassFlag() {
      if (this.tableData.classFlag === 3 || this.tableData.classFlag === 985) {
        this.tableData.classFlag = 985
      } else if (this.tableData.classFlag === 2 || this.tableData.classFlag === 211) {
        this.tableData.classFlag = 211
      } else if (this.tableData.classFlag === 1 || this.tableData.classFlag === '双一流') {
        this.tableData.classFlag = '双一流'
      }
      else {
        this.tableData.classFlag = '普通本科'
      }
    }
  },
  mounted() {
    // 初始渲染图表
    let roundStudy = echarts.init(document.getElementById('round-study'));
    let roundLife = echarts.init(document.getElementById('round-life'));
    let roundJob = echarts.init(document.getElementById('round-job'));
    let roundCompare = echarts.init(document.getElementById('round-compare'));
    let levelChart = echarts.init(document.getElementById('level-chart'));
    let rateBar = echarts.init(document.getElementById('rate-bar'));
    let scaleBar = echarts.init(document.getElementById('scale-bar'));
    roundLife.setOption({
      title: {
        text: (Math.random() * (4.9 - 4.0) + 4.0).toFixed(1),
        left: 'center',
        top: 'center',
        textStyle: {
          fontWeight: 'bold',
          fontSize: 30
        },
      },
      series: [
        {
          type: 'pie',
          itemStyle: {
            color: function (colors) {
              const colorList = [
                '#91cd77',
                '#ffffff',
                '#ef6567',
                '#f9c956',
                '#75bedc'
              ];
              return colorList[colors.dataIndex];
            },
          },
          label: {
            show: false,
            position: 'center',
          },
          data: [
            {
              value: 4.5,
              name: 'A'
            },
            {
              value: 0.5,
              name: 'B'
            }
          ],
          radius: ['60%', '70%'],
        }
      ]
    });
    roundStudy.setOption({
      title: {
        text: (Math.random() * (4.9 - 4.0) + 4.0).toFixed(1),
        left: 'center',
        top: 'center',
        textStyle: {
          fontWeight: 'bold',
          fontSize: 30
        },
      },
      series: [
        {
          type: 'pie',
          itemStyle: {
            color: function (colors) {
              const colorList = [
                '#fc8251',
                '#ffffff',
                '#91cd77',
                '#ef6567',
                '#f9c956',
                '#75bedc'
              ];
              return colorList[colors.dataIndex];
            },
          },
          label: {
            show: false,
            position: 'center',
          },
          data: [
            {
              value: 4.5,
              name: 'A'
            },
            {
              value: 0.5,
              name: 'B'
            }
          ],
          radius: ['60%', '70%'],
        }
      ]
    });
    roundJob.setOption({
      title: {
        text: (Math.random() * (4.9 - 4.0) + 4.0).toFixed(1),
        left: 'center',
        top: 'center',
        textStyle: {
          fontWeight: 'bold',
          fontSize: 30
        },
      },
      series: [
        {
          type: 'pie',
          itemStyle: {
            color: function (colors) {
              var colorList = [
                '#ef6567',
                '#ffffff',
                '#91cd77',
                '#f9c956',
                '#75bedc'
              ];
              return colorList[colors.dataIndex];
            },
          },
          label: {
            show: false,
            position: 'center',
          },
          data: [
            {
              value: 4.7,
              name: 'A'
            },
            {
              value: 0.3,
              name: 'B'
            }
          ],
          radius: ['60%', '70%'],

        }
      ]
    });
    roundCompare.setOption({
      title: {
        text: (Math.random() * (4.9 - 4.5) + 4.5).toFixed(1),
        left: 'center',
        top: 'center',
        textStyle: {
          fontWeight: 'bold',
          fontSize: 30
        },
      },
      series: [
        {
          type: 'pie',
          itemStyle: {

            color: function (colors) {
              var colorList = [
                '#f9c956',
                '#ffffff',
                '#91cd77',
                '#ef6567',
                '#75bedc'
              ];
              return colorList[colors.dataIndex];
            },
          },
          label: {
            show: false,
            position: 'center',
          },
          data: [
            {
              value: 4.9,
              name: 'A'
            },
            {
              value: 0.1,
              name: 'B'
            }
          ],
          radius: ['60%', '70%'],

        }
      ]
    });
    levelChart.setOption({
      title: {
        text: "学术水平",
        textStyle: {
          color: 'black',
        },
        top: '4%',
        left: 'center'
      },
      grid: {
        top: '18%',
        left: '20%',
        right: '2%',
        bottom: '3%'
      },
      color: ['#3398DB'],
      tooltip: {
        trigger: 'axis',
        axisPointer: {            // 坐标轴指示器，坐标轴触发有效
          type: 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        }
      },
      xAxis: {
        type: 'value',
        axisLabel: {
          show: true,
          color: 'white',
          fontSize: 12,
          formatter: function (value) {
            if (value >= 10000) {
              value = value / 10000 + 'w';
            }
            return value;
          }
        },
        //y轴线设置显示
        axisLine: {
          show: true
        },
      },
      yAxis: {
        type: 'category',
        inverse: true,
        data: ['博士点', '硕士点', '重点学科'],

      },
      series: [{
        label: {
          show: true,
          // rotate: 90,
          formatter: '{c}',
          fontSize: 16,
          color: 'white',
        },
        emphasis: {
          focus: 'series'
        },
        data: [Math.floor(Math.random() * (30 - 10) + 10),
               Math.floor(Math.random() * (60 - 20) + 20),
               Math.floor(Math.random() * (30 - 10) + 10),],
        type: 'bar',
        barMaxWidth: "60%",
      }]
    });
    rateBar.setOption({
      grid: {   // 直角坐标系内绘图网格
        left: '20',  //grid 组件离容器侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '50',
        right: '20',
        bottom: '0',
        containLabel: true   //gid区域是否包含坐标轴的刻度标签。为true的时候，
        // left/right/top/bottom/width/height决定的是包括了坐标轴标签在内的
        //所有内容所形成的矩形的位置.常用于【防止标签溢出】的场景
      },
      xAxis: {  //直角坐标系grid中的x轴,
        //一般情况下单个grid组件最多只能放上下两个x轴,
        //多于两个x轴需要通过配置offset属性防止同个位置多个x轴的重叠。
        type: 'value',//坐标轴类型,分别有：
        //'value'-数值轴；'category'-类目轴;
        //'time'-时间轴;'log'-对数轴
        splitLine: { show: false },//坐标轴在 grid 区域中的分隔线
        axisLabel: { show: false },//坐标轴刻度标签
        axisTick: { show: false },//坐标轴刻度
        axisLine: { show: false },//坐标轴轴线
      },
      yAxis: {
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
          color: 'black',
          fontSize: 15
        },
        data: ['毕业落实率', '国内升学率', '毕业出国率']//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
      },
      color: ['#c23531', '#2f4554', '#61a0a8', '#d48265', '#91c7ae', '#749f83'],
      series: [//系列列表。每个系列通过 type 决定自己的图表类型
        {
          name: '%',//系列名称
          type: 'bar',//柱状、条形图
          barWidth: 15,//柱条的宽度,默认自适应
          data: [(Math.random() * (100 - 95) + 95).toFixed(2),
                 (Math.random() * (50 - 45) + 45).toFixed(2),
                 (Math.random() * (40 - 30) + 30).toFixed(2)],//系列中数据内容数组
          label: { //图形上的文本标签
            show: true,
            position: 'right',//标签的位置
            offset: [-40, -40],  //标签文字的偏移，此处表示向上偏移40
            formatter: '{c}{a}',//标签内容格式器 {a}-系列名,{b}-数据名,{c}-数据值
            color: 'black',//标签字体颜色
            fontSize: 15  //标签字号
          },
          itemStyle: {//图形样式
            //emphasis图形在高亮状态下的样式
            borderRadius: 7,//柱条圆角半径,单位px.
            //此处统一设置4个角的圆角大小;
            //也可以分开设置[10,10,10,10]顺时针左上、右上、右下、左下
            color: function (params) {
              const colorList = [
                new echarts.graphic.LinearGradient(
                    0, 0, 1, 0,
                    [{
                      offset: 0,
                      color: '#52E5E7'//柱图渐变色起点颜色
                    },
                      {
                        offset: 1,
                        color: '#130CB7'//柱图渐变色终点颜色
                      }
                    ]
                ),
                new echarts.graphic.LinearGradient(
                    0, 0, 1, 0,
                    [{
                      offset: 0,
                      color: '#FFE985'//柱图渐变色起点颜色
                    },
                      {
                        offset: 1,
                        color: '#FA742B'//柱图渐变色终点颜色
                      }
                    ]
                ),
                new echarts.graphic.LinearGradient(
                    0, 0, 1, 0,
                    [{
                      offset: 0,
                      color: '#81FBB8'//柱图渐变色起点颜色
                    },
                      {
                        offset: 1,
                        color: '#28C76F'//柱图渐变色终点颜色
                      }
                    ]
                )
              ];

              return colorList[params.dataIndex]
            },
          },
          zlevel: 1//柱状图所有图形的 zlevel 值,
          //zlevel 大的 Canvas 会放在 zlevel 小的 Canvas 的上面
        },
        {
          name: '进度条背景',
          type: 'bar',
          barGap: '-100%',//不同系列的柱间距离，为百分比。
          // 在同一坐标系上，此属性会被多个 'bar' 系列共享。
          // 此属性应设置于此坐标系中最后一个 'bar' 系列上才会生效，
          //并且是对此坐标系中所有 'bar' 系列生效。
          barWidth: 15,
          data: [100, 100, 100],
          color: '#e6e6e6',//柱条颜色
          itemStyle: {
            borderRadius: 7,
          }
        }
      ]
    });
    scaleBar.setOption({
      title: {
        text: "男女比例",
        textStyle: {
          color: 'black',
        },
        top: '4%',
        left: 'center',
      },
      grid: {   // 直角坐标系内绘图网格
        left: '0',  //grid 组件离容器左侧的距离,
        //left的值可以是80这样具体像素值，
        //也可以是'80%'这样相对于容器高度的百分比
        top: '0',
        right: '0',
        bottom: '0',
        containLabel: true   //gid区域是否包含坐标轴的刻度标签。为true的时候，
        // left/right/top/bottom/width/height决定的是包括了坐标轴标签在内的
        //所有内容所形成的矩形的位置.常用于【防止标签溢出】的场景
      },
      xAxis: {  //直角坐标系grid中的x轴,
        //一般情况下单个grid组件最多只能放上下两个x轴,
        //多于两个x轴需要通过配置offset属性防止同个位置多个x轴的重叠。
        type: 'value',//坐标轴类型,分别有：
        //'value'-数值轴；'category'-类目轴;
        //'time'-时间轴;'log'-对数轴
        splitLine: { show: false },//坐标轴在 grid 区域中的分隔线
        axisLabel: { show: false },//坐标轴刻度标签
        axisTick: { show: false },//坐标轴刻度
        axisLine: { show: false },//坐标轴轴线
      },
      yAxis: {
        type: 'category',
        axisTick: { show: false },
        axisLine: { show: false },
        axisLabel: {
          color: 'black',
          fontSize: 25
        },
        data: []//类目数据，在类目轴（type: 'category'）中有效。
        //如果没有设置 type，但是设置了axis.data,则认为type 是 'category'。
      },
      series: [//系列列表。每个系列通过 type 决定自己的图表类型
        {
          name: '%',//系列名称
          type: 'bar',//柱状、条形图
          barWidth: 10,//柱条的宽度,默认自适应
          data: [this.detail.men_rate],//系列中数据内容数组
          // label: { //图形上的文本标签
          //     show: true,
          //     position: 'right',//标签的位置
          //     offset: [0, -40],  //标签文字的偏移，此处表示向上偏移40
          //     formatter: '{c}{a}',//标签内容格式器 {a}-系列名,{b}-数据名,{c}-数据值
          //     color: 'black',//标签字体颜色
          //     fontSize: 36  //标签字号
          // },
          itemStyle: {//图形样式
            //emphasis图形在高亮状态下的样式
            borderRadius: [10, 0, 0, 10],//柱条圆角半径,单位px.
            //此处统一设置4个角的圆角大小;
            //也可以分开设置[10,10,10,10]顺时针左上、右上、右下、左下
            color: '#22B6ED',
          },
          zlevel: 1//柱状图所有图形的 zlevel 值,
          //zlevel 大的 Canvas 会放在 zlevel 小的 Canvas 的上面
        },
        {
          name: '进度条背景',
          type: 'bar',
          barGap: '-100%',//不同系列的柱间距离，为百分比。
          // 在同一坐标系上，此属性会被多个 'bar' 系列共享。
          // 此属性应设置于此坐标系中最后一个 'bar' 系列上才会生效，
          //并且是对此坐标系中所有 'bar' 系列生效。
          barWidth: 10,
          data: [100],
          color: '#FFB6C1',//柱条颜色
          itemStyle: {
            borderRadius: 10,
          }
        }
      ]
    });
  }
}
</script>

<style scoped>
#draw-border {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
}

/*button {*/
/*  border: 0;*/
/*  background: unset;*/
/*  text-transform: uppercase;*/
/*  color: #4361ee;*/
/*  font-weight: bold;*/
/*  position: relative;*/
/*  outline: none;*/
/*  padding: 10px 20px;*/
/*  box-sizing: border-box;*/
/*}*/

/*button::before, button::after {*/
/*  box-sizing: inherit;*/
/*  position: absolute;*/
/*  content: '';*/
/*  border: 2px solid transparent;*/
/*  width: 0;*/
/*  height: 0;*/
/*}*/

/*button::after {*/
/*  bottom: 0;*/
/*  right: 0;*/
/*}*/

/*button::before {*/
/*  top: 0;*/
/*  left: 0;*/
/*}*/

/*button:hover::before, button:hover::after {*/
/*  width: 100%;*/
/*  height: 100%;*/
/*}*/

/*button:hover::before {*/
/*  border-top-color: #4361ee;*/
/*  border-right-color: #4361ee;*/
/*  transition: width 0.3s ease-out, height 0.3s ease-out 0.3s;*/
/*}*/

/*button:hover::after {*/
/*  border-bottom-color: #4361ee;*/
/*  border-left-color: #4361ee;*/
/*  transition: border-color 0s ease-out 0.6s, width 0.3s ease-out 0.6s, height 0.3s ease-out 1s;*/
/*}*/

.info-title {
  color: #19d3ea;
  font-size: 18px;
  width: 100%;   /*一定要设置宽度，或者元素内含的百分比*/
  overflow:hidden; /*溢出的部分隐藏*/
  white-space: nowrap; /*文本不换行*/
  text-overflow:ellipsis;/*ellipsis:文本溢出显示省略号（...）；clip：不显示省略标记（...），而是简单的裁切*/
}

.common-card {
  border-radius: 10px;
  box-shadow: 0 0 13px #e6e6e6;
  margin: 10px;
  padding: 30px;
}

.detail-wrap {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: auto;
  width: 80%;
  height: 100%;
  background-color: #FFFFFF;
}

#tag-card {
  width: 100%;
  height: 200px;
  /* background-color: aquamarine; */
  background-image: linear-gradient(135deg, #4C83FF 10%, #2AFADF 100%);
  display: flex;
  flex-direction: row;
}

.logo-wrap {
  float: left;
  background: #FFFFFF;
  padding: 20px;
  margin: 20px;
  border-radius: 40px;
}

.logo {
  width: 100%;
  height: 100%;
}

.right-content {
  padding: 10px
}

.name-pos {
  font-size: xx-large;
  color: #FFFFFF;
  margin: 20px;
}

.tag-line {
  display: flex;
  margin-top: 50px;
  margin-left: 10px;
}

.tag {
  /* background-color: #4CAF50; */
  /* border-color: #3faff3; */
  border-radius: 40vh;
  color: #3faff3;
  padding: 10px 15px;
  margin: 5px;
  text-align: center;
  text-decoration: none;
  border: 2px solid #3faff3;
  display: inline-block;
  cursor: pointer;
  font-weight: bold;
  background-color: #FFFFFF;
  /* background-image: linear-gradient( 135deg, #3C8CE7 10%, #00EAFF 100%); */
}

#pic-intro {
  display: flex;
}

.pic-wrap {
  background: #FFFFFF;
  /* padding: 20px; */
  margin: 20px;
  border-radius: 40px;
}

.vid-img {
  width: 460px;
  height: 259px;
  border-radius: 40px;
}

#intro {
  display: flex;
  flex-direction: column;
  padding: 10px;
}

.round-wrap {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
}

.round-wrap>* {
  /* width: 100px;
  height: 100px; */
  display: flex;
  flex-direction: column;
  align-items: center;
  font-weight: bold;
}

.round-wrap>*>* {
  width: 100px;
  height: 100px;
}

.content-wrap {
  padding: 20px;
}

/* .table-wrap {
    display: flex;
    flex-direction: row;
    justify-content: center;
} */

#score-chart {
  width: 400px;
  height: 300px;
}

#level-chart {
  width: 400px;
  height: 300px;
}

/* .bar-wrap {
    display: flex;
    flex-direction: row;
    justify-content: center;
} */

#rate-bar {
  width: 400px;
  height: 250px;
  margin-right: 50px;
}

.boy-girl {
  display: flex;
  flex-direction: row;
  align-items: center;
  width: 400px;
}

#scale-bar {
  width: 200px;
  height: 150px;
}

.score-table {
  width: 90%;
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
</style>