<template>
  <div class="brand-container">
    <div class="filter-container">
      <el-select v-model="listQuery.projectId" placeholder="项目" style="width:200px" class="filter-item" clearable>
        <el-option v-for="item in projectList" :key="item.id" :value="item.id" :label="item.name"></el-option>
      </el-select>
      <el-button class="filter-item" @click="getList" type="primary" icon="el-icon-search">查询</el-button>
      <el-button v-has="'code:database:add'" class="filter-item" @click="handleCreate" type="primary" icon="el-icon-plus"> {{ $t('table.add') }} </el-button>
    </div>

    <el-table :data="list" v-loading.body="listLoading" border fit highlight-current-row size="small">
      <el-table-column align="center" label="ID" width="80" prop="id"></el-table-column>
      <el-table-column width="140" label="项目名称" align="center" prop="project"></el-table-column>
      <el-table-column width="140" label="数据库名称" align="center" prop="name"></el-table-column>
      <el-table-column label="数据库连接url" align="center" prop="url" header-align="center"></el-table-column>
      <el-table-column width="100" label="用户名" align="center" prop="user"></el-table-column>
      <el-table-column width="140" label="密码" align="center" prop="password"></el-table-column>
      <el-table-column width="100" label="SCHEMA" align="center" prop="dbSchema"></el-table-column>
      <el-table-column width="100" label="类型" align="center" prop="dbType">
        <template slot-scope="scope">
          <span>{{scope.row.dbType|formatDbType}}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" v-if="this.global_checkBtnPermission(['code:database:edit','code:database:delete','table'])" :label="$t('table.actions')">
        <template slot-scope="scope">
          <el-button v-has="'code:database:edit'" size="mini" type="primary" @click="handleUpdate(scope.row)" icon="el-icon-edit" plain>
            {{ $t('table.edit') }}
          </el-button>
          <cus-del-btn v-has="'code:database:delete'" @ok="handleDelete(scope.row)"/>
          <router-link :to="{path:'/codeGenerator/table',query:{dataBaseId:scope.row.id}}">
            <el-button v-has="'table'" size="mini" type="primary" icon="el-icon-plus" plain>选择表</el-button>
          </router-link>
        </template>
      </el-table-column>
    </el-table>

    <cus-pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList"/>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" size="tiny" @close="handleDialogClose">
      <el-form class="small-space" :model="form" label-position="left" label-width="130px" ref="dataForm" :rules="rules" size="small">
        <el-form-item label="归属项目:" prop="projectId">
          <el-select v-model="form.projectId" placeholder="归属项目" style="width:400px">
            <el-option v-for="item in projectList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据库名称(唯一):" prop="name">
          <el-input v-model="form.name" placeholder="test"></el-input>
        </el-form-item>
        <el-form-item label="数据库类型:" prop="dbType">
          <el-select v-model="form.dbType" placeholder="数据库类型" @change="handleTypeChange">
            <el-option v-for="item in dbTypeList" :key="item.value" :value="item.value" :label="item.label"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据库连接URL:" prop="url">
          <el-input v-model="form.url" type="textarea" placeholder="jdbc:mysql://127.0.0.1:3306/test"></el-input>
        </el-form-item>
        <el-form-item label="用户名:" prop="user">
          <el-input v-model="form.user"></el-input>
        </el-form-item>
        <el-form-item label="密码:" prop="password">
          <el-input v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item label="schema:" prop="dbSchema">
          <el-input v-model="form.dbSchema" placeholder="test"></el-input>
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
  import { getProjectList } from '@/api/code-generator/project'
  import { fetchDatabasePage, saveDatabase, deleteDatabase } from '@/api/code-generator/database'
  import { dbType } from '@/data/basic_data'

  export default {
    name: 'database',
    components: {},
    data() {
      return {
        list: null,
        total: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 10,
          projectId: undefined
        },
        statusList: [{ label: '启用', value: 1 }, { label: '禁用', value: 0 }],
        form: {
          id: undefined,
          projectId: undefined,
          name: undefined,
          url: undefined,
          driver: undefined,
          user: undefined,
          password: undefined,
          dbSchema: undefined,
          dbType: undefined
        },
        dialogFormVisible: false,
        dialogStatus: '',
        textMap: {
          update: '编辑',
          create: '创建'
        },
        dbTypeList: dbType,
        projectList: [],
        rules: {}
      }
    },
    created() {
      this.getList()
      this.getProjectList()
    },
    filters: {},
    methods: {
      getList() {
        this.listLoading = true
        fetchDatabasePage(this.listQuery).then(res => {
          this.list = res.data.records
          this.total = res.data.total
          this.listLoading = false
        })
      },
      getProjectList(){
        getProjectList({}).then(res => {
          this.projectList = res.data
        })
      },
      handleTypeChange(type) {
        var dbType = this.dbTypeList.find(item => {
          return item.value === type
        })
        console.log(dbType)
        if (dbType) {
          this.form.driver = dbType.driver
          console.log(this.form.driver)
        }
      },
      handleCreate() {
        this.form = Object.assign({}, {})
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
      },
      handleUpdate(row) {
        this.form = Object.assign({}, row)
        this.form.project = undefined
        this.dialogStatus = 'update'
        this.dialogFormVisible = true
      },
      handleDelete(row) {
        deleteDatabase(row.id).then(response => {
          if (response.code == 200) {
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
            saveDatabase(this.form).then(response => {
                if (response.code == 200) {
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
