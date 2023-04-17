<template>
  <div class="school-wrap">
    <div class="form-box">
      <el-form label-width="80px" class="ms-content" hide-required-asterisk :model="form" ref="rulesForm">
        <div style="display: flex">
          <div class="search-box">
            <el-form-item label="收藏查询" style="display: inline-block; margin-left: 25px">
              <el-input style="width: 200px; margin-left: 40px" suffix-icon="el-icon-search" placeholder="请输入学校名称" v-model="name" ></el-input>
              <el-input style="width: 200px; margin-left: 10px" suffix-icon="el-icon-search" placeholder="请输入专业名称" v-model="specialty" ></el-input>
              <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
              <el-button class="ml-5" type="goon" @click="reset">重置</el-button>
              <el-button class="ml-5" type="warning" @click="collection">保存收藏</el-button>
            </el-form-item>
          </div>
        </div>
      </el-form>
    </div>

    <el-card class="el-card-define">
      <el-table :data="collectionList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column label="学校图标">
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
        <el-table-column prop="specialty" label="专业名称"></el-table-column>
        <el-table-column prop="minScore" label="最低录取分数线"></el-table-column>
        <el-table-column prop="minRank" label="最低录取排名"></el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button type="danger" slot="reference" @click="moveOut(scope.row)">移除收藏 <i class="el-icon-add-location"></i></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <div style="padding: 20px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total"
          :total="this.collectionList.length">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      total: 0,
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      user: localStorage.getItem("stdUser") ? JSON.parse(localStorage.getItem("stdUser")) : JSON.parse(localStorage.getItem("user")),
      collectionList: localStorage.getItem("collection") ? JSON.parse(localStorage.getItem("collection")) : [],
      name: "",
      specialty: "",
      form: {
        username: "",
        avatarUser: "",
        name: "",
        province: "",
        area: "",
        classFlag: "",
        specialty: "",
        minScore: "",
        minRank: "",
        avatar: "",
      },
    }
  },
  created() {
    this.init()
  },
  methods: {
    load() {
      const temp = []
      if (JSON.parse(localStorage.getItem("collection")) === null) {
        this.$message({
          duration: 800,
          message: "未添加收藏!",
          type: "error"
        })
        return
      }
      this.collectionList = JSON.parse(localStorage.getItem("collection"))
      for (let i = 0; i < this.collectionList.length; i++) {
        if ((this.name === "") && (this.specialty === "")) {
          this.$message({
            duration: 800,
            message: "名称输入为空!",
            type: "error"
          })
          return
        }
        else if (((this.collectionList[i].name.includes(this.name)) && this.name !== "") || (this.collectionList[i].specialty.includes(this.specialty) && this.specialty !== "")) {
          temp.push(this.collectionList[i])
        }
      }
      this.collectionList.length = 0
      for (let i = 0; i < temp.length; i++) {
        this.collectionList.push(temp[i])
      }
      console.log(temp)
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
        this.collectionList = JSON.parse(localStorage.getItem("collection"))
        this.name = ""
        this.specialty = ""
      }
    },
    // 存取分页大小
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    // 批量删除按钮
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    // 移除收藏
    moveOut(row) {
      console.log(row.name)
      for (let i = 0; i < this.collectionList.length; i++) {
        if ((this.collectionList[i].name === row.name) && (this.collectionList[i].specialty === row.specialty)) {
          this.collectionList.splice(i, 1)
        }
      }
      localStorage.setItem("collection", JSON.stringify(this.collectionList))
      this.$message({
        duration: 800,
        message: "移除成功!",
        type: "success"
      })
    },
    // 初始化
    async init() {
      if (localStorage.getItem("stuUser") || localStorage.getItem("user")) {
        this.request.get("/collection/pageName", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            username: this.user.username,
          }
        }).then(async res => {
          console.log(res)
          if ((res.data.records.length !== 0) && (!localStorage.getItem("collection"))) {
            this.collectionList = res.data.records
            this.switchClassFlag()
            localStorage.setItem("collection", JSON.stringify(this.collectionList))
          }
        })
      }
    },
    // 收藏操作 (先删除后添加)
    collection() {
      if (localStorage.getItem("stdUser") || localStorage.getItem("user")) {
        let once = 1
        this.request.get("/collection/searchByName", {
          params: {
            username: this.user.username
          }
        }).then(res => {
          console.log(res.code)
          if(res.code !== '500') {
            this.request.delete("/collection/deleteByName/" + this.user.username)
          }
          this.form.username = this.user.username
          this.form.avatarUser = this.user.avatar
          this.switchBackClassFlag()
          for (let i = 0; i < this.collectionList.length; i++) {
            this.form.name = this.collectionList[i].name
            this.form.province = this.collectionList[i].province
            this.form.area = this.collectionList[i].area
            this.form.classFlag = this.collectionList[i].classFlag
            this.form.specialty = this.collectionList[i].specialty
            this.form.minScore = this.collectionList[i].minScore
            this.form.minRank = this.collectionList[i].minRank
            this.form.avatar = this.collectionList[i].avatar

            this.$refs['rulesForm'].validate((valid) => {
              if (valid) {
                // 表单验证合法
                // 获取后端数据判断跳转
                if(this.collectionList === undefined) {
                  this.$message({
                    duration: 1200,
                    message: "请填写分数!",
                    type: "error"
                  })
                  return
                }
                this.request.post("/collection", this.form).then(res2 => {
                  console.log(this.form)
                  if (once === 1) {
                    if (res2.code === '200') {
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
                    once++
                  }
                })
              }
              else {
                // 将表单值传入后端 (空表单)
                this.$message({
                  duration: 1200,
                  message: "保存失败!",
                  type: "error"
                })
              }
            })
          }
          this.switchClassFlag()
        })
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
      for (let i = 0; i < this.collectionList.length; i++) {
        if (this.collectionList[i].classFlag === 3 || this.collectionList[i].classFlag === 985) {
          this.collectionList[i].classFlag = 985
        }
        else if (this.collectionList[i].classFlag === 2 || this.collectionList[i].classFlag === 211) {
          this.collectionList[i].classFlag = 211
        }
        else if (this.collectionList[i].classFlag === 1 || this.collectionList[i].classFlag === '双一流') {
          this.collectionList[i].classFlag = '双一流'
        }
        else {
          this.collectionList[i].classFlag = '普通本科'
        }
      }
    },
    switchBackClassFlag() {
      for (let i = 0; i < this.collectionList.length; i++) {
        if (this.collectionList[i].classFlag === 985) {
          this.collectionList[i].classFlag = 3
        }
        else if (this.collectionList[i].classFlag === 211) {
          this.collectionList[i].classFlag = 2
        }
        else if (this.collectionList[i].classFlag === '双一流') {
          this.collectionList[i].classFlag = 1
        }
        else {
          this.collectionList[i].classFlag = 0
        }
      }
    },
  }
}
</script>

<style scoped>
.divider-red {
  background-color: #FF0000;
  height: 2px;
}
.divider-yellow {
  background-color: #FFFF00;
  height: 2px;
}
.divider-blue {
  background-color: #00BBFF;
  height: 2px;
}

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
