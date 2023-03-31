<template>
  <div class="school-wrap">
    <div class="form-box">
      <el-form label-width="80px" class="ms-content" hide-required-asterisk>
        <div style="display: flex">
          <div class="search-box">
            <el-form-item label="院校查询" style="display: inline-block; margin-left: 25px">
              <el-input style="width: 200px; margin-left: 40px" suffix-icon="el-icon-search" placeholder="请输入学校名称" v-model="name" ></el-input>
              <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
              <el-button class="ml-5" type="goon" @click="reset">重置</el-button>
            </el-form-item>
          </div>
          <el-radio-group v-model="form.schoolClass" size="large" @change="handleSearch" style="margin-left: 638px">
            <el-radio-button label="全部" />
            <el-radio-button label="985" />
            <el-radio-button label="211" />
            <el-radio-button label="双一流" />
            <el-radio-button label="其它" />
          </el-radio-group>
        </div>
        <el-form-item label="学校地区" style="display: inline-block; margin-left: 37px">
          <el-radio-group v-model="form.province" @change="handleSearch">
            <div style="max-width: 1400px;">
              <el-radio class="province-radio" v-for="(province, index) in areaList" width="90px"
                        :key="index"
                        :label="province.value"
                        :value="province.value"
                        border>
                {{province.value}}
              </el-radio>
            </div>
          </el-radio-group>
        </el-form-item>

      </el-form>
    </div>

      <el-card class="el-card-define">
        <el-table :data="tableData" @selection-change="handleSelectionChange">
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
              <el-button type="success" slot="reference" @click="details(scope.row)">查看 <i class="el-icon-add-location"></i></el-button>
              <el-button type="danger" slot="reference" @click="collection(scope.row)">收藏 <i class="el-icon-add-location"></i></el-button>
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
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    <v-goTop></v-goTop>
    </div>
</template>

<script>
import GoTop from "../../components/GoTop";

export default {
  data() {
    return {
      total: 0,
      tableData: [],
      pageSchoolList: [],
      pageNum: 1,
      pageSize: 10,
      name: "",
      button: 0,
      collectionList: JSON.parse(localStorage.getItem("collection")) ? JSON.parse(localStorage.getItem("collection")) : [],
      form: {
        province: '全部',
        schoolClass: '全部',
      },
      areaList: [
        {value: '全部'}, {value: '北京'}, {value: '天津'}, {value: '河北'}, {value: '山西'}, {value: '内蒙古'},
        {value: '辽宁'}, {value: '吉林'}, {value: '黑龙江'}, {value: '上海'}, {value: '江苏'}, {value: '浙江'},
        {value: '安徽'}, {value: '福建'}, {value: '江西'}, {value: '山东'}, {value: '河南'}, {value: '湖北'},
        {value: '湖南'}, {value: '广东'}, {value: '广西'}, {value: '海南'}, {value: '重庆'}, {value: '四川'},
        {value: '贵州'}, {value: '云南'}, {value: '西藏'}, {value: '陕西'}, {value: '甘肃'}, {value: '青海'},
        {value: '宁夏'}, {value: '新疆'}],
      classMap: {
        '全部': "",
        '985': 3,
        '211': 2,
        '双一流': 1,
        '其它': 0,
      }
    }
  },
  components: {
    'v-goTop': GoTop
  },
  created() {
    // 刷新页面
    let detailFrom = this.$route.query
    console.log(detailFrom)
    if (detailFrom === null) {
      this.load()
    }
    else {
      this.request.get("/school/pageSpecialty", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          specialty: detailFrom.specialtyName
        }
      }).then(res => {
        console.log(res.data)
        this.tableData = res.data.records
        for (let i = 0; i < res.data.records.length; i++) {
          if (res.data.records[i].classFlag === 3) {
            res.data.records[i].classFlag = 985
          }
          else if (res.data.records[i].classFlag === 2) {
            res.data.records[i].classFlag = 211
          }
          else if (res.data.records[i].classFlag === 1) {
            res.data.records[i].classFlag = '双一流'
          }
          else {
            res.data.records[i].classFlag = '普通本科'
          }
        }
        this.total = res.data.total
        this.button = 1
      })
    }
  },
  methods: {
    load() {
      this.request.get("/school/pageName", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        // Token 不合法
        // if (res.code === '402') {
        //   this.$router.push("/login")
        //   return false
        // }
        console.log(res.data)
        this.tableData = res.data.records
        this.total = res.data.total
        for (let i = 0; i < res.data.records.length; i++) {
          if (res.data.records[i].classFlag === 3) {
            res.data.records[i].classFlag = 985
          }
          else if (res.data.records[i].classFlag === 2) {
            res.data.records[i].classFlag = 211
          }
          else if (res.data.records[i].classFlag === 1) {
            res.data.records[i].classFlag = '双一流'
          }
          else {
            res.data.records[i].classFlag = '普通本科'
          }
        }
        if (this.name === "" && this.button === 1) {
          this.$message({
            duration: 800,
            message: "院校名称输入为空!",
            type: "error"
          })
        }
        this.button = 1
      })
    },
    // 重置搜索
    reset() {
      this.name = ""
      this.button = 0
      this.load()
    },
    // 存取分页大小
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.button = 0
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.button = 0
      this.load()
    },
    // 批量删除按钮
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    // 查询院校地区
    handleSearch(val) {
      this.request.get("/school/pageSchool", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          province: this.form.province,
          classFlag: this.classMap[this.form.schoolClass]
        }
      }).then(res => {
        // Token 不合法
        // if (res.code === '402') {
        //   this.$router.push("/login")
        //   return false
        // }
        console.log(val)
        this.tableData = res.data.records
        this.total = res.data.total
        for (let i = 0; i < res.data.records.length; i++) {
          if (res.data.records[i].classFlag === 3) {
            res.data.records[i].classFlag = 985
          }
          else if (res.data.records[i].classFlag === 2) {
            res.data.records[i].classFlag = 211
          }
          else if (res.data.records[i].classFlag === 1) {
            res.data.records[i].classFlag = '双一流'
          }
          else {
            res.data.records[i].classFlag = '普通本科'
          }
        }
      })
    },
    // 收藏操作
    collection(row) {
      if (localStorage.getItem("stdUser") || localStorage.getItem("user")) {
        console.log(row)
        this.collectionList.push(row)
        // console.log(this.collectionList)
        localStorage.setItem("collection", JSON.stringify(this.collectionList))
        let i = 0, j = 0
        for (i; i < this.collectionList.length - 1 && j === 0; i++) {
          if (this.collectionList[i].name === row.name) {
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
    // 跳转详细界面
    details(row) {
      // alert(JSON.stringify(row))
      console.log(row)
      this.$router.push({
        path: "/front/details",
        query: {
          detailName: row.name,
          detailAvatar: row.avatar,
        }
      })
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
