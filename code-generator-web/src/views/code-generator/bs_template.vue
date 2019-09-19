<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" @click="getList" type="primary" icon="el-icon-refresh">刷新</el-button>
      <el-button v-has="'code:bsTemplate:add'" class="filter-item" @click="handleCreate" type="primary" icon="el-icon-plus"> {{ $t('table.add') }} </el-button>
    </div>

    <el-table :data="list" v-loading.body="listLoading" border fit highlight-current-row size="small">
      <el-table-column align="center" label="ID" width="100" prop="id"></el-table-column>
      <el-table-column label="展开" align="center" type="expand">
        <template slot-scope="scope">
          <div v-highlight>
            <pre><code v-html="scope.row.content"></code></pre>
          </div>
        </template>
      </el-table-column>
      <el-table-column width="150" label="类型" align="center" prop="type">
<!--        <template slot-scope="scope">{{scope.row.type|formatTemplateType}}</template>-->
      </el-table-column>
      <el-table-column width="150" label="文件后缀名" align="center" prop="fileSuffix"></el-table-column>
      <el-table-column label="内容" align="center" prop="content" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" width="200" v-if="this.global_checkBtnPermission(['code:bsTemplate:edit','code:bsTemplate:delete'])" :label="$t('table.actions')">
        <template slot-scope="scope">
          <el-button v-has="'code:bsTemplate:edit'" size="mini" type="primary" @click="handleUpdate(scope.row)" icon="el-icon-edit" plain>
            {{ $t('table.edit') }}
          </el-button>
          <cus-del-btn v-has="'code:bsTemplate:delete'" @ok="handleDelete(scope.row)"></cus-del-btn>
        </template>
      </el-table-column>
    </el-table>

    <cus-pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList"/>

    <el-dialog :title="titleMap[dialogStatus]" :visible.sync="dialogFormVisible" width="80%" @close="handleDialogClose">
      <el-form class="small-space" :model="form" label-position="left" label-width="120px"
               ref="dataForm" :rules="rules" size="small">
        <el-form-item label="模板类型:" prop="type">
<!--          <el-select v-model="form.type" placeholder="模板类型">-->
<!--            <el-option v-for="item in templateTypeList" :key="item.value" :value="item.value" :label="item.label"></el-option>-->
<!--          </el-select>-->
          <el-input v-model="form.type" style="width:200px"></el-input>
        </el-form-item>
        <el-form-item label="生成文件后缀名:" prop="fileSuffix">
          <el-select v-model="form.fileSuffix" placeholder="ex:   .java  或  .html" style="width:300px">
            <el-option v-for="item in fileSuffixList" :key="item.id" :value="item.id" :label="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="模板内容:" prop="content">
          <el-input v-model="form.content" type="textarea" :autosize="{ minRows: 10}" :rows="10"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false" size="small"> {{ $t('table.cancel') }} </el-button>
        <el-button type="primary" @click="submitForm" size="small"> {{ $t('table.confirm') }} </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { fetchTemplatePage, saveTemplate, deleteTemplate } from '@/api/code-generator/bs_template'
  // import { tempalteType } from '@/data/basic_data'

  export default {
    name: 'BasicTemplate',
    data() {
      return {
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 10
        },
        fileSuffixList: [
          { id: '.java', value: '.java' },
          { id: '.xml', value: '.xml' },
          { id: '.py', value: '.py' },
          { id: '.vue', value: '.vue' },
          { id: '.md', value: '.md' },
          { id: '.php', value: '.php' },
          { id: '.html', value: '.html' },
          { id: '.js', value: '.js' },
          { id: '.jsp', value: '.jsp' }
        ],
        form: {
          id: undefined,
          type: undefined,
          fileSuffix: undefined,
          content: undefined
        },
        dialogFormVisible: false,
        dialogStatus: '',
        titleMap: {
          update: '编辑',
          create: '创建'
        },
        // templateTypeList: tempalteType,
        rules: {}
      }
    },
    created() {
      this.getList()
    },
    filters: {},
    methods: {
      getList() {
        this.listLoading = true
        fetchTemplatePage(this.listQuery).then(res => {
          this.list = res.data.records
          this.total = res.data.total
          this.listLoading = false
        })
      },
      handleCreate() {
        this.form = Object.assign({}, {})
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
      },
      handleUpdate(row) {
        this.form = Object.assign({}, row)
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
      },
      handleSizeChange(val) {
        this.listQuery.limit = val
        this.getList()
      },
      handleCurrentChange(val) {
        this.listQuery.page = val
        this.getList()
      },
      handleDelete(row) {
        deleteTemplate(row.id).then(response => {
          if (response.code === 200) {
            this.getList()
            this.submitOk(response.message)
          } else {
            this.submitFail(response.message)
          }
        })
      },
      submitForm() {
        this.$refs.dataForm.validate(valid => {
          if (valid) {
            saveTemplate(this.form)
              .then(response => {
                if (response.code === 200) {
                  this.getList()
                  this.submitOk(response.message)
                  this.dialogFormVisible = false
                } else {
                  this.submitFail(response.message)
                }
              })
              .catch(err => {
                console.log(err)
              })
          }
        })
      },
      // 监听dialog关闭时的处理事件
      handleDialogClose() {
        if (this.$refs['dataForm']) {
          this.$refs['dataForm'].clearValidate() // 清除整个表单的校验
        }
      }
    }
  }
</script>
<style scoped>
  .brand-container .filter-container {
    padding: 10px 0px 10px 20px;
  }

  .brand-container img {
    width: 50px;
    height: 30px;
  }
</style>
