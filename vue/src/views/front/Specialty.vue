<template>
  <div class="school-wrap">
    <div class="form-box">
      <el-form label-width="80px" class="ms-content" hide-required-asterisk>
        <div style="display: flex">
          <div class="search-box">
            <el-form-item label="专业查询" style="display: inline-block; margin-left: 25px">
              <el-input style="width: 200px; margin-left: 40px" suffix-icon="el-icon-search" placeholder="请输入专业名称"
                        v-model="name"></el-input>
              <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
              <el-button class="ml-5" type="goon" @click="reset">重置</el-button>
            </el-form-item>
          </div>
        </div>

        <div />
        <el-form-item label="专业类别" style="display: inline-block; margin-left: 37px;">
          <el-radio-group v-model="form.category" @change="category">
            <div style="max-width: 1400px; margin-left: 33px">
              <el-radio class="province-radio" v-for="(category, index) in categoryList" width="90px"
                        :key="index"
                        :label="category.value"
                        :value="category.value"
                        border>
                {{category.value}}
              </el-radio>
            </div>
          </el-radio-group>
        </el-form-item>
      </el-form>
    </div>

    <div>
      <el-card class="el-card-define">
        <el-card v-for="index in total" width="90px"
                  v-if="index >= 1"
                  style="height: 100%; overflow: auto">
          <div>
            <span class="title">
              {{tableData[index - 1].category}}
            </span>
            <el-divider class="divider"/>
            <div>
              <div class="subtitle">{{tableData[index - 1].specialty}}</div>
              <el-divider />
              <el-descriptions :title="name"
                               v-for="name in cutName(tableData[index - 1].name)"
                               :key="name"
                               :column="4">
                <el-descriptions-item label="专业代码">
                  <el-tag size="small" type="danger">{{tableData[index - 1].code}}</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="修业年限">
                  <el-tag size="small" type="info">四年</el-tag>
                </el-descriptions-item>
                <el-descriptions-item label="授予学位">
                  <el-tag size="small" type="success">学士学位</el-tag>
                </el-descriptions-item>
                <el-descriptions-item>
                  <el-button type="warning" @click="check(name)" size="small" style="font-weight: bold">
                    查看所设专业院校
                  </el-button>
                </el-descriptions-item>
              </el-descriptions>
            </div>
            <div  v-for="i in all"
                  :key="i"
                  v-if="i > total">
              <div v-if="tableData[i - 1].category === tableData[index - 1].category">
                <el-divider class="divider"/>
                <div class="subtitle">{{tableData[i - 1].specialty}}</div>
                <el-divider />
                <el-descriptions :title="name"
                                 v-for="name in cutName(tableData[i - 1].name)"
                                 :key="name"
                                 :column="4">
                  <el-descriptions-item label="专业代码">
                    <el-tag size="small" type="danger">{{tableData[i - 1].code}}</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="修业年限">
                    <el-tag size="small" type="info">四年</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item label="授予学位">
                    <el-tag size="small" type="success">学士学位</el-tag>
                  </el-descriptions-item>
                  <el-descriptions-item>
                    <el-button type="warning" @click="check(name)" size="small" style="font-weight: bold">
                      查看所设专业院校
                    </el-button>
                  </el-descriptions-item>
                </el-descriptions>
              </div>
            </div>
              </div>
          </el-card>
        </el-card>

      <div style="padding: 20px 0">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pageNum"
            :page-sizes="[2, 5, 10, 20]"
            :page-size="pageSize"
            layout="total"
            :total="all">
        </el-pagination>
      </div>
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
      all: 0,
      tableData: [],
      pageSchoolList: [],
      pageNum: 1,
      pageSize: 100,
      name: "",
      button: 0,
      form: {
        schoolClass: '全部',
        category: '全部',
      },
      categoryList: [
        {value: '全部'}, {value: '哲学'}, {value: '经济学'}, {value: '法学'}, {value: '教育学'},
        {value: '文学'}, {value: '历史学'}, {value: '理学'}, {value: '工学'}, {value: '农学'},
        {value: '医学'}, {value: '管理学'}, {value: '艺术学'}
      ],
    }
  },
  components: {
    'v-goTop': GoTop
  },
  created() {
    // 刷新页面
    this.load()
  },
  methods: {
    load() {
      this.request.get("/specialty/pageName", {
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
        this.tableData = res.data.records
        this.total = res.data.total
        // console.log(this.total)
        this.form.category = '全部'
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
      this.form.category = '全部'
      this.load()
    },
    category(val) {
      this.request.get("/specialty/pageSpecialty", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          category: this.form.category
        }
      }).then(res => {
        // Token 不合法
        // if (res.code === '402') {
        //   this.$router.push("/login")
        //   return false
        // }
        console.log(res.data)
        this.tableData = res.data.records
        if (val !== '全部') {
          this.total = 1
        }
        else {
          this.total = 12
        }
        this.all = res.data.total
        console.log(this.all)
      })
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
    cutName(name) {
      return name.split("、")
    },
    check(row) {
      // alert(JSON.stringify(row))
      console.log(row)
      this.$router.push({
        path: "/front/school",
        query: {
          specialtyName: row
        }
      })
    },
  }
}
</script>

<style scoped>
.divider {
  background-color: #b6d7fb;
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

.title{
  display: flex;
  font-size: 30px;
  font-weight: bold;
  color: #FF8800;
}

.subtitle {
  width: 1440px;
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  font-size: large;
  font-weight: bold;
  color: #4C83FF;
}
</style>
