<template>
  <div class="school-wrap">
    <div class="form-box">
      <el-form label-width="80px" class="ms-content" hide-required-asterisk :model="form" :rules="rules" ref="rulesForm">
        <div style="display: flex">
          <div class="search-box">
            <el-form-item label="分数填报" style="display: inline-block; margin-left: 25px" prop="score">
              <el-input-number style="width: 200px; margin-left: 40px" suffix-icon="el-icon-search" placeholder="请输入预估分数" v-model="form.score"></el-input-number>
              <el-button style="margin-left: 10px" type="warning" @click="saveScore">保存填报</el-button>
              <el-button style="margin-left: 10px" type="primary" @click="recommend">开始推荐</el-button>

              <el-popconfirm
                  class="ml-5"
                  confirm-button-text='确定'
                  cancel-button-text='再想想'
                  icon="el-icon-info"
                  icon-color="red"
                  title="您确定需要「重置所有推荐」吗？"
                  @confirm="reset">
                <el-button class="ml-5" slot="reference" type="goon">重置</el-button>
              </el-popconfirm>
            </el-form-item>
          </div>
        </div>
      </el-form>
    </div>

      <el-card class="el-card-define">
        <el-table :data="application" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="avatar" label="学校图标">
            <template slot-scope="scope">
              <img :src="scope.row.avatar" class="avatarSchool">
            </template>
          </el-table-column>
          <el-table-column prop="name" label="学校名称"></el-table-column>
          <el-table-column prop="province, area" label="学校地区">
            <template slot-scope="scope">
              {{scope.row.province}} {{scope.row.area}}
            </template>
          </el-table-column>
          <el-table-column prop="classFlag" label="层级"></el-table-column>
          <el-table-column prop="minScore" label="最低录取分数线"></el-table-column>
          <el-table-column prop="minRank" label="最低录取排名"></el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button type="danger" slot="reference" @click="removeApplication(scope.row)"> 取消填报 <i class="el-icon-add-location"></i></el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>

      <div style="padding: 20px 0">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-size="pageSize"
            layout="total"
            :total="application.length">
        </el-pagination>
      </div>
    </div>
</template>

<script>
import { math } from '@/utils/math.js'

export default {
  data() {
    return {
      total: 0,
      tableData: [],
      listData: [],
      pageSchoolList: [],
      pageNum: 1,
      pageSize: 10,
      list: [],
      application: localStorage.getItem("application") ? JSON.parse(localStorage.getItem("application")) : [],
      temp: [],
      form: {
        name: "",
        avatar: "",
        score: undefined,
        application1: "",
        application2: "",
        application3: "",
        application4: "",
        application5: "",
      },
      rules: {
        score: [
          {type:"number", min: 0, max: 750, message: '范围(0 - 750)', trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    this.init()
    this.switchClassFlag()
  },
  methods: {
    // 初始化
    async init() {
      this.request.get("/application/pageName", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: JSON.parse(localStorage.getItem("stdUser")) ? JSON.parse(localStorage.getItem("stdUser")).username : JSON.parse(localStorage.getItem("user")).username,
        }
      }).then(async res => {
        // console.log(res)
        this.form.score = res.data.records[0].score
        let a = []
        a[0] = res.data.records[0].application1
        a[1] = res.data.records[0].application2
        a[2] = res.data.records[0].application3
        a[3] = res.data.records[0].application4
        a[4] = res.data.records[0].application5

        const localTemp = []
        for (let i = 0; i < a.length; i++) {
          if (a[i] === null) {
            break
          }
          await this.request.get("/school/pageName", {
            params: {
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              name: a[i],
            }
          }).then(res2 => {
            // console.log(res2)
            localTemp.push(res2.data.records[0])
          })
        }
        if (localStorage.getItem("application")) {
        } else {
          localStorage.setItem("application", JSON.stringify(localTemp))
        }
      })
    },
    // 重置搜索
    reset() {
      if (!(localStorage.getItem("stdUser") || localStorage.getItem("user"))) {
        this.$message({
          duration: 1200,
          message: "请进行用户登录!",
          type: "error"
        })
      }
      else {
        this.form.score = undefined
        localStorage.removeItem("application")
        // location.reload()
        this.$message({
          duration: 200,
          message: "重置成功!",
          type: "success"
        })
      }
    },
    // 存取分页大小
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
    },
    // 批量删除按钮
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    // 取消填报
    removeApplication(row) {
      console.log(row.name)
      for (let i = 0; i < this.application.length; i++) {
        if (this.application[i].name === row.name) {
          this.application.splice(i, 1)
        }
      }
      localStorage.setItem("application", JSON.stringify(this.application))
      this.$message({
        duration: 800,
        message: "移除成功!",
        type: "success"
      })
    },
    // 保存填报
    saveScore() {
      if(!(localStorage.getItem("stdUser") || localStorage.getItem("user"))) {
        this.$message({
          duration: 1200,
          message: "请进行用户登录!",
          type: "error"
        })
      }
      else {
        if(JSON.parse(localStorage.getItem("application")) === null) {
          this.$message({
            duration: 1200,
            message: "请先进行志愿选取!",
            type: "error"
          })
          return
        }

        this.form.name = localStorage.getItem("stdUser") ? JSON.parse(localStorage.getItem("stdUser")).username : JSON.parse(localStorage.getItem("user")).username
        this.form.avatar = localStorage.getItem("stdUser") ? JSON.parse(localStorage.getItem("stdUser")).avatar : JSON.parse(localStorage.getItem("user")).avatar
        this.temp = JSON.parse(localStorage.getItem("application"))
        console.log(this.temp)
        let a = []
        for (let i = 0; i < this.temp.length; i++) {
          a[i] = this.temp[i].name
        }
        this.form.application1 = a[0]
        this.form.application2 = a[1]
        this.form.application3 = a[2]
        this.form.application4 = a[3]
        this.form.application5 = a[4]

        this.$refs['rulesForm'].validate((valid) => {
          if (valid) {
            // 表单验证合法
            // 获取后端数据判断跳转
            if(this.form.score === undefined) {
              this.$message({
                duration: 1200,
                message: "请填写分数!",
                type: "error"
              })
              return
            }
            this.request.post("/application", this.form).then(res => {
              if (res.code === '200') {
                // 将表单值传入后端
                this.$message({
                  duration: 800,
                  message: "保存成功!",
                  type: "success"
                })
              }
              else {
                this.$message({
                  duration: 1200,
                  message: "保存失败!",
                  type: "error"
                })
              }
            })
          }
          // 表单验证不合法 (未写数据)
          else {
            this.$message({
              duration: 1200,
              message: "分数填报有误!",
              type: "error"
            })
          }
        })
      }
    },
    // 开始推荐
    recommend() {
      if(!localStorage.getItem("stdUser")) {
        this.$message({
          duration: 1200,
          message: "请进行用户登录!",
          type: "error"
        })
      }
      else if (this.form.score === undefined) {
        this.$message({
          duration: 1200,
          message: "请填写分数!",
          type: "error"
        })
      }
      else {
        // 清空缓存
        localStorage.removeItem("report")

        // 推荐算法
        this.request.get("/application").then(res => {
          let listAll = []
          listAll.push(listAll[0])
          this.list = res.data
          this.request.get("/application/pageName", {
            params: {
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              name: JSON.parse(localStorage.getItem("stdUser")).username ? JSON.parse(localStorage.getItem("stdUser")).username : JSON.parse(localStorage.getItem("user")).username,
            }
          }).then(res2 => {
            if (res2.data.records[0].name === null) {
              this.$message({
                duration: 1200,
                message: "请先进行保存填报!",
                type: "error"
              })
              return
            }
            for (let i = 0; i < this.list.length; i++) {
              if(this.list[i].name === (JSON.parse(localStorage.getItem("stdUser")).username || JSON.parse(localStorage.getItem("user")).username)){
                listAll[0] = this.list[i]
              }
              else {
                if ((this.list[i].score >= math.subtract(res2.data.records[0].score, 20)) && (this.list[i].score <= math.add(res2.data.records[0].score, 10))) {
                  listAll.push(this.list[i])
                  // window.alert( JSON.stringify(this.list[i].application1) + '\n' +
                  //               JSON.stringify(this.list[i].application2) + '\n' +
                  //               JSON.stringify(this.list[i].application3) + '\n' +
                  //               JSON.stringify(this.list[i].application4) + '\n' +
                  //               JSON.stringify(this.list[i].application5))
                }
              }
            }
            this.request.post("/application/Recommend", listAll).then(res => {
              if ((listAll.length === 1) && (listAll[0].name === JSON.parse(localStorage.getItem("stdUser")).username)) {
                this.$message({
                  duration: 1200,
                  message: "暂无相似推荐院校可选!",
                  type: "error"
                })
              }
              else {
                this.$message({
                  duration: 800,
                  message: "推荐成功!",
                  type: "success"
                })
              }
              this.$router.push({
                path: "/front/report",
                query: {
                  recommendDetails: res,
                  score: res2.data.records[0].score
                }
              })
            })
            console.log(res2.data.records[0].score)
          })
          console.log(listAll)
        })
      }
    },
    switchClassFlag() {
      for (let i = 0; i < this.application.length; i++) {
        if (this.application[i].classFlag === 3 || this.application[i].classFlag === 985) {
          this.application[i].classFlag = 985
        }
        else if (this.application[i].classFlag === 2 || this.application[i].classFlag === 211) {
          this.application[i].classFlag = 211
        }
        else if (this.application[i].classFlag === 1 || this.application[i].classFlag === '双一流') {
          this.application[i].classFlag = '双一流'
        }
        else {
          this.application[i].classFlag = '普通本科'
        }
      }
    }
  }
}
</script>

<style scoped>

.school-wrap {
  margin: auto;
  text-align: center;
  height: 100%;
}

.form-box {
  margin: 40px auto;
}

.type-box {
  display: flex;
  align-items: center;
}
.type-box > *{
  margin-right: 20px
}
.el-form {
  display: flex;
  flex-direction: column;
}
.el-form > *{
  margin: 10px;
}

.el-form-item {
  display: flex;
  flex-direction: row;
  margin-bottom: 0;
}

.el-form-item .kelei{
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-bottom: 0;
}

/* .el-input {
  width: 30%;
} */

.search-button {
  margin: auto 20px;
}

.el-radio-group {
  display: flex;
  flex-direction: row;
  margin-top: 0;
}
.kelei-group {
  /* width: 70%; */
  margin: 0;
}
.province-radio {
  width: 80px;
  margin: 5px;
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
  /* display: flex; */
  align-items: center;
  margin: 20px auto;
  height: 140px;
  border-radius: 20px;
}

.school-image {
  float: left;
}

img {
  float: left;
  width: 100px;
  height: 100px;
}

.school-detial {
  float: left;
}

.add-button {
  float: right;
}

a {
  text-decoration:none;
}
a:link {
  /*默认状态*/
  color: black;
}

a:visited {
  /*浏览过的*/
  color: black;
}

a:hover {
  /*悬浮状态*/
  color: #409eff;;
}

p {
  font-size: large;
}

.el-pagination {
  justify-content: center;
}

.search-box {
  display: flex;
  justify-content: start;
}

.el-card-define {
  min-height: 100%;
  height: 100%;
}
.el-button--goon.is-active,
.el-button--goon:active {
  background: #20B2AA;
  border-color: #20B2AA;
  color: #fff;
}

.el-button--goon:focus,
.el-button--goon:hover {
  background: #48D1CC;
  border-color: #48D1CC;
  color: #fff;
}

.el-button--goon {
  color: #FFF;
  background-color: #20B2AA;
  border-color: #20B2AA;
}

</style>
