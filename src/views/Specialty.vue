<template>
  <div>
    <div style="margin: 10px 0">
    <el-input style="width: 200px; margin-right: 5px" suffix-icon="el-icon-search" placeholder="请输入专业名称" v-model="name" ></el-input>
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button class="ml-5" type="danger" @click="reset">重置</el-button>
    <el-button style="float: right" type="info" @click="exp">导出<i class="el-icon-top"></i></el-button>
      <el-upload action="http://localhost:8089/user/import"
                 accept=".xlsx"
                 :show-file-list="false"
                 :on-success="handleExcelImportSuccess"
                 style="display: inline-block; float: right">
    <el-button type="warning" class="ml-10">导入<i class="el-icon-bottom"></i></el-button>
      </el-upload>

    <el-popconfirm
        class="ml-5"
        confirm-button-text='确定'
        cancel-button-text='再想想'
        icon="el-icon-info"
        icon-color="red"
        title="您确定需要删除所选项吗？"
        @confirm="delBatch">
      <el-button style="float: right" type="danger" slot="reference" class="ml-10">批量删除 <i class="el-icon-remove-outline"></i></el-button>
    </el-popconfirm>
    <el-button style="float: right" type="primary" @click="handleAdd">新增 <i class="el-icon-circle-plus-outline"></i></el-button>
  </div>

    <el-table :data="tableData" @selection-change="handleSelectionChange">
    <el-table-column type="selection" width="55"></el-table-column>
    <el-table-column prop="id" label="ID" min-width="40px"></el-table-column>
    <el-table-column prop="category" label="专业学科"></el-table-column>
      <el-table-column prop="specialty" label="专业大类"></el-table-column>
      <el-table-column prop="name" label="专业名称" :formatter="stateFormat"></el-table-column>
      <el-table-column prop="code" label="专业代码"></el-table-column>
    <el-table-column label="操作" align="center">
      <template slot-scope="scope">

        <el-button type="success" @click="handleEdit(scope.row)" style="margin: 5px">编辑 <i class="el-icon-edit"></i></el-button>
        <el-popconfirm
            class="ml-5"
            confirm-button-text='确定'
            cancel-button-text='再想想'
            icon="el-icon-info"
            icon-color="red"
            title="您确定需要删除当前项吗？"
            @confirm="handleDelete(scope.row.id)"
        >
          <el-button type="danger" slot="reference" style="margin-right: 5px;">删除 <i class="el-icon-remove-outline"></i></el-button>
        </el-popconfirm>
      </template>
    </el-table-column>
  </el-table>

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

    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%">
      <el-form label-width="80px" size="small">
        <el-form-item label="专业学科">
          <el-input v-model="form.category" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="专业大类">
          <el-input v-model="form.specialty" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="专业名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="专业代码">
          <el-input v-model="form.code" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "Specialty",
  data() {
    return {
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: '',
      input: '',
      // 表单
      dialogFormVisible: false,
      form: {
        category: "",
        specialty: "",
        name: "",
        code: ""
      },
      multipleSelection: [],
    }
  },
  created() {
    // 刷新页面
    this.load()
  },
  methods: {
    load() {
      // 请求分页查询
      this.request.get("/specialty/pageName", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        // Token 不合法
        if(res.code === '402') {
          this.$router.push("/login")
          return false
        }
        console.log(res.data)
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    // 重置搜索
    reset() {
      this.name = ""
      this.load()
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
    // 表单绑定
    // 按钮弹窗
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    // 批量删除按钮
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    // 提取 mul 中需要用的数据 (id) 并按照id进行删除
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)
      this.request.delete("/specialty/del/batch", {data: ids}).then(res => {
        if(res.code === '200') {
          this.$message.success("批量删除成功!")
          this.load()
        }
        else {
          this.$message.error("批量删除失败!")
        }
      })
    },
    // 删除按钮
    handleDelete(id) {
      this.request.delete("/specialty/" + id).then(res => {
        if(res.data) {
          this.$message.success("删除成功!")
          this.load()
        }
        else {
          this.$message.error("删除失败!")
        }
      })
    },
    // 编辑按钮
    handleEdit(row) {
      // 将值注入表单
      this.form = row
      this.dialogFormVisible = true
    },
    // 新增按钮
    save() {
      // 将表单值传入后端
      this.request.post("/specialty", this.form).then(res => {
        if(res.code === '200') {
          this.$message.success("添加成功!")
          // 刷新表单
          this.load()
          this.dialogFormVisible = false
        }
        else {
          this.$message.error("添加失败!")
        }
      })
    },
    // 导出
    exp() {
      window.open("http://localhost:8089/user/export")
    },
    // 导入成功提示
    handleExcelImportSuccess() {
      this.$message.success("导入成功!")
      this.load()
    },
    // 显示图标
    handleAvatarSuccess(res) {
      this.form.avatar = res
    },
    // 省略字数
    stateFormat(row, column, cellValue) {
      // console.log(row , column , cellValue)
      if (!cellValue) return "";
      if (cellValue.length > 10) {
        //最长固定显示4个字符
        return cellValue.slice(0, 10) + "...";
      }
      return cellValue;

    }
  }
}
</script>

<style scoped>

</style>