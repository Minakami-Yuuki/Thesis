<template>
  <div class="school-wrap">
    <div class="form-box">
      <el-form label-width="80px" class="ms-content" hide-required-asterisk ref="rulesForm">
        <div style="display: flex">
          <div class="search-box">
            <el-form-item label="分数填报" style="display: inline-block; margin-left: 25px" prop="score">
              <el-input-number style="width: 200px; margin-left: 40px" suffix-icon="el-icon-search" disabled v-model="score"></el-input-number>

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
          <el-table-column prop="minScore" label="最低录取分数线"></el-table-column>
          <el-table-column prop="minRank" label="最低录取排名"></el-table-column>
          <el-table-column label="操作" align="center">
            <template slot-scope="scope">
              <el-button type="primary" slot="reference" @click="details(scope.row)"> 查看院校 <i class="el-icon-add-location"></i></el-button>
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
      tableData: [],
      pageNum: 1,
      pageSize: 10,
      application: JSON.parse(localStorage.getItem("report")) ? JSON.parse(localStorage.getItem("report")) : [],
      score: JSON.parse(localStorage.getItem("score")) ? JSON.parse(localStorage.getItem("score")) : undefined,
    }
  },
  created() {
    let detailForm = this.$route.query
    if (detailForm.recommendDetails) {
      this.score = detailForm.score
      localStorage.setItem("score", JSON.stringify(detailForm.score))
      for (let i = 0; i < detailForm.recommendDetails.length; i++) {
        this.request.get("/school/pageName", {
          params: {
            pageNum: this.pageNum,
            pageSize: this.pageSize,
            name: detailForm.recommendDetails[i],
          }
        }).then(res => {
          this.application.push(res.data.records[0])
          localStorage.setItem("report", JSON.stringify(this.application))
        })
      }
    }
  },
  methods: {
    // 重置搜索
    reset() {
      this.$router.push("/front/recommend")
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
