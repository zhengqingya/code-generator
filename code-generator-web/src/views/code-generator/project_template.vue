<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="listQuery.projectId" placeholder="项目" style="width:200px" class="filter-item" clearable>
        <el-option v-for="item in projectList" :key="item.id" :value="item.id" :label="item.name"></el-option>
      </el-select>
      <el-button class="filter-item" @click="getList" type="success" icon="el-icon-search">查询</el-button>
      <el-button v-has="'code:projectTemplate:generateTemplate'" class="filter-item" @click="handleGenerateTemplate" type="warning" icon="el-icon-loading">生成模板</el-button>
      <el-button v-has="'code:projectTemplate:add'" class="filter-item" @click="handleCreate" type="primary" icon="el-icon-plus"> {{ $t('table.add') }} </el-button>
      <el-button v-has="'code:projectTemplate:showVelocityContext'" class="filter-item" @click="getVelocityContextList" type="warning" icon="el-icon-search"> 参考模板数据源配置信息 </el-button>
    </div>

    <el-table :data="list" v-loading.body="listLoading" border fit highlight-current-row size="small">
      <el-table-column align="center" label="ID" width="100" prop="id"></el-table-column>
      <el-table-column label="展开" align="center" type="expand">
        <template slot-scope="scope">
          <div v-highlight><pre><code v-html="scope.row.content"></code></pre></div>
        </template>
      </el-table-column>
      <el-table-column width="140" label="项目名称" align="center" prop="projectName"></el-table-column>
      <el-table-column width="200" label="类型" align="center" prop="packageName"></el-table-column>
      <el-table-column width="200" label="文件后缀名" align="center" prop="fileSuffix"></el-table-column>
      <el-table-column label="内容" align="center" prop="content" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" width="250" v-if="this.global_checkBtnPermission(['code:projectTemplate:edit','code:projectTemplate:delete'])" :label="$t('table.actions')">
        <template slot-scope="scope">
          <el-button v-has="'code:projectTemplate:edit'" size="mini" type="primary" @click="handleUpdate(scope.row)" icon="el-icon-edit" plain> {{ $t('table.edit') }} </el-button>
          <cus-del-btn v-has="'code:projectTemplate:delete'" @ok="handleDelete(scope.row)"/>
        </template>
      </el-table-column>
    </el-table>

    <cus-pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList"/>

    <!-- 模板弹出框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible" size="tiny" @close="handleDialogClose">
      <el-form class="small-space" :model="form" label-position="left" label-width="120px" ref="dataForm" :rules="rules" size="small">
        <el-form-item label="归属项目:" prop="projectId">
          <el-select v-model="form.projectId" placeholder="归属项目" style="width:300px" @change="handleProjectChange(form.projectId)">
            <el-option v-for="item in projectList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="模板类型:" prop="type">
          <el-select v-model="form.type" placeholder="模板类型" style="width:260px">
            <el-option v-for="item in templateTypeList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="生成文件后缀名:" prop="fileSuffix">
<!--          <el-input v-model="form.fileSuffix"></el-input>-->
          <el-select v-model="form.fileSuffix" placeholder="ex:   .java  或  .html" style="width:300px">
            <el-option v-for="item in fileSuffixList" :key="item.id" :value="item.id" :label="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="模板内容:" prop="content">
          <el-input v-model="form.content" type="textarea" :autosize="{ minRows: 10}" :rows="10"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false" size="small">{{ $t('table.cancel') }}</el-button>
        <el-button type="primary" @click="submitForm" size="small">{{ $t('table.confirm') }}</el-button>
      </div>
    </el-dialog>

    <!-- 模板数据源弹出框 -->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogVelocityContextVisible" size="tiny" @close="handleDialogClose">
      <div>
        <el-table :data="velocityContextList" v-loading.body="listLoadingVelocityContext" border fit highlight-current-row size="small">
          <el-table-column type="index" width="50"></el-table-column>
          <el-table-column label="展开" align="center" type="expand">
            <template slot-scope="scope">
              <div v-highlight><pre><code v-html="scope.row.contextJsonObject"></code></pre></div>
            </template>
          </el-table-column>
          <el-table-column width="140" label="模板数据源" align="center" >
            <template slot-scope="scope">
              <span> ${ {{ scope.row.velocity }} } </span>
            </template>
          </el-table-column>
          <el-table-column label="内容" align="center" prop="context" show-overflow-tooltip></el-table-column>
        </el-table>
        <cus-pagination v-show="totalVelocityContext>0" :total="totalVelocityContext" :page.sync="listQueryVelocityContext.page" :limit.sync="listQueryVelocityContext.limit" @pagination="getVelocityContextList"/>
      </div>
    </el-dialog>


  </div>
</template>

<script>
  import { getPackageList, getProjectList } from '@/api/code-generator/project'
  import {
    deleteTemplate,
    fetchTemplatePage,
    generateTemplate,
    saveTemplate,
    getProjectVelocityContextPage
  } from '@/api/code-generator/project_template'

  export default {
  name: "projectTemplate",
  data() {
    return {
      list: [],
      projectList: [],
      templateTypeList: [],
      velocityContextList: undefined,
      total: 0,
      totalVelocityContext: 0,
      listLoading: true,
      listLoadingVelocityContext: true,
      listQuery: {
        page: 1,
        limit: 10,
        projectId: undefined
      },
      listQueryVelocityContext: {
        page: 1,
        limit: 10,
        projectId: 0
      },
      // statusList: [{ label: "启用", value: 1 }, { label: "禁用", value: 0 }],
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
        projectId: undefined,
        type: undefined,
        fileSuffix: undefined,
        content: undefined,
        projectName: undefined,
        packageName: undefined
      },
      dialogFormVisible: false,
      dialogVelocityContextVisible: false,
      dialogStatus: "",
      textMap: {
        update: "编辑",
        create: "创建",
        showVelocityContext: "项目模板数据源"
      },
      rules: {}
    };
  },
  created() {
    this.getList();
    this.getProjectList();
  },
  filters: {},
  methods: {
    getList() {
      this.listLoading = true;
      fetchTemplatePage(this.listQuery).then(res => {
        this.list = res.data.records;
        this.total = res.data.total;
        this.listLoading = false;
      });
    },
    getProjectList(){
      getProjectList({}).then(res => {
        this.projectList = res.data
      });
    },
    getPackageList(id){
      getPackageList(id).then(res => {
        this.templateTypeList = res.data
      });
    },
    getVelocityContextList(){
      if ( this.listQuery.projectId === undefined || this.listQuery.projectId === "" ){
        this.$message({ message: "请先选择项目!", type: "warning" });
        return;
      }
      this.velocityContextList = []
      this.dialogStatus = "showVelocityContext";
      this.dialogVelocityContextVisible = true
      this.listLoadingVelocityContext = true
      this.listQueryVelocityContext.projectId = this.listQuery.projectId
      getProjectVelocityContextPage(this.listQueryVelocityContext).then(res => {
        this.velocityContextList = res.data.records
        this.totalVelocityContext = res.data.total
      });
      this.listLoadingVelocityContext = false;
    },
    handleProjectChange(projectId){
      this.getPackageList(projectId);
    },
    handleGenerateTemplate() {
      if ( this.listQuery.projectId === undefined || this.listQuery.projectId === "" ){
        this.$message({ message: "请选择需要生成模板的项目!", type: "warning" });
        return;
      }
      this.$confirm('此操作将自动生成项目对应模板, 是否继续?', '提示', {
        confirmButtonText: '确定',
        type: 'warning'
      }).then(() => {
        generateTemplate(this.listQuery.projectId).then(response => {
          if (response.code === 200) {
            this.getList();
            this.submitOk(response.message)
          } else {
            this.submitFail(response.message);
          }
        });
      })
    },
    handleCreate() {
      this.templateTypeList = [];
      this.form = Object.assign({}, {});
      this.dialogStatus = "create";
      this.dialogFormVisible = true;
    },
    handleUpdate(row) {
      this.getPackageList(row.projectId);
      this.form = Object.assign({}, row);
      this.dialogStatus = "update";
      this.dialogFormVisible = true;
    },
    handleDelete(row) {
      deleteTemplate(row.id).then(response => {
        if (response.code == 200) {
          this.getList();
            this.submitOk(response.message)
        } else {
            this.submitFail(response.message);
        }
      });
    },
    submitForm() {
      this.form.projectName = undefined;
      this.$refs.dataForm.validate(valid => {
        if (valid) {
          saveTemplate(this.form)
            .then(response => {
              if (response.code == 200) {
                this.getList();
                  this.submitOk(response.message)
                this.dialogFormVisible = false;
              } else {
                  this.submitFail(response.message);
              }
            })
            .catch(err => {
              console.log(err);
            });
        }
      });
    },
    // 监听dialog关闭时的处理事件
    handleDialogClose() {
      if (this.$refs['dataForm']) {
        this.$refs['dataForm'].clearValidate() // 清除整个表单的校验
      }
    }
  }
};
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
