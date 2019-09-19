<template>
  <div class="app-container">
    <cus-wraper>
      <cus-filter-wraper>
        <el-button type="primary" @click="getList" icon="el-icon-search" v-waves>查询</el-button>
        <el-button v-has="'code:project:add'" type="primary" @click="handleCreateProject" icon="el-icon-plus" v-waves>新建一个项目</el-button>
      </cus-filter-wraper>
      <div class="table-container">
        <el-table v-loading="listLoading" :data="list" size="mini" element-loading-text="Loading" fit border highlight-current-row>
          <el-table-column label="项目ID" prop="id" align="center"></el-table-column>
          <el-table-column label="项目名称" prop="name" align="center"></el-table-column>
          <el-table-column label="所属用户" prop="username" align="center"></el-table-column>
          <el-table-column label="状态" prop="status" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status == 1 ? 'success' : 'danger'" hit>
                {{ scope.row.status == 1 ? '启用' : '停用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" v-if="this.global_checkBtnPermission(['code:project:edit','code:project:delete'])" :label="$t('table.actions')">
            <template slot-scope="scope">
              <el-button v-has="'code:project:edit'" size="mini" type="primary" @click="handleUpdateProject(scope.row)" icon="el-icon-edit" plain v-waves> {{ $t('table.edit') }} </el-button>
              <el-button v-has="'code:projectPackage:showTree'" size="mini" type="primary" @click="showPackageTree(scope.row)" icon="el-icon-edit" plain v-waves>编辑项目包</el-button>
              <cus-del-btn v-has="'code:project:delete'" @ok="handleDeleteProject(scope.row)"></cus-del-btn>
            </template>
          </el-table-column>
        </el-table>

        <!-- 分页 -->
        <cus-pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList"/>
      </div>

      <!-- 添加、修改项目 -->
      <el-dialog :title="titleMap[dialogStatus]" :visible.sync="dialogProjectVisible" width="40%" @close="handleDialogClose">
        <el-form ref="dataForm" :model="projectForm" :rules="rules" label-width="100px" class="demo-ruleForm">
          <el-form-item label="项目名称:" prop="name">
            <el-input v-model="projectForm.name"></el-input>
          </el-form-item>
          <el-form-item label="状态:" prop="flag">
            <el-select v-model="projectForm.status" class="filter-item" placeholder='请选择' style="width: 280px;">
              <el-option v-for="item in statusList" :key="item.key" :label="item.display_name" :value="item.key"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogProjectVisible = false" v-waves> {{ $t('table.cancel') }} </el-button>
          <el-button type="primary" @click="submitProjectForm" v-waves> {{ $t('table.confirm') }} </el-button>
        </span>
      </el-dialog>

      <!-- 树形包 -->
      <el-dialog :title="titleMap[dialogStatus]" :visible.sync="dialogPackageTreeVisible" width="40%" @close="handleDialogClose">
        <cus-wraper>
          <div style="margin-left:20px;margin-top:-30px;" class="filter-container">
            <el-button type="primary" @click="getTree(projectForm.id)" icon="el-icon-refresh">刷新</el-button>
<!--            <el-button v-has="'code:projectPackage:add'" v-if="checkShow" type="warning" @click="handleCreatePackage" icon="el-icon-plus">添加</el-button>-->
<!--            <el-button v-has="'code:projectPackage:edit'" v-if="checkShow" type="warning" @click="handleUpdatePackage" icon="el-icon-edit">修改</el-button>-->
<!--            <el-button v-has="'code:projectPackage:delete'" v-if="checkShow" type="danger" @click="handleCreatePackage" icon="el-icon-delete">删除</el-button>-->
          </div>
          <el-row style="margin-top:10px;" :gutter="20">
            <el-col :span="16">
              <div class="grid-content bg-purple-dark">
                <el-card class="box-card" shadow="never">
                  <div slot="header" class="clearfix">
                    <span style="line-height: 36px;">
                      <label> {{ projectForm.name }} </label>
                    </span>
                  </div>
                  <div class="text item">
<!--                    <el-tree :data="treeData" node-key="id" default-expand-all :expand-on-click-node="true" @node-click="handleNodeClick">-->
<!--                      <span slot-scope="{ node }" class="custom-tree-node">-->
<!--                        <span>{{ node.label }}</span>-->
<!--                      </span>-->
<!--                    </el-tree>-->
                    <el-tree
                      :data="treeData"
                      node-key="id"
                      default-expand-all
                      :expand-on-click-node="false">
                        <span class="custom-tree-node" slot-scope="{ node, data }">
                          <span>{{ node.label }}</span>
                          <span style="margin-left: 50px;">
                            <el-button v-has="'code:projectPackage:add'" type="text" size="mini" @click="() => handleCreatePackage(data,projectForm.name)" icon="el-icon-plus"></el-button>
                            <el-button v-has="'code:projectPackage:edit'" type="text" size="mini" @click="() => handleUpdatePackage(data)" icon="el-icon-edit"></el-button>
                            <el-button v-has="'code:projectPackage:delete'" type="text" size="mini" @click="() => handleDeletePackage(node, data)" icon="el-icon-delete"></el-button>
                          </span>
                        </span>
                    </el-tree>
                  </div>
                </el-card>
              </div>
            </el-col>
          </el-row>
        </cus-wraper>
      </el-dialog>

      <!-- 添加、修改项目包 -->
      <el-dialog :title="titleMap[dialogStatus]" :visible.sync="dialogPackageVisible" width="40%" @close="handleDialogClose">
        <el-form ref="dataForm" :model="packageForm" :rules="rulesPackage" label-width="100px" class="demo-ruleForm">
          <el-form-item label="包名:" prop="name">
            <el-input v-model="packageForm.name"></el-input>
          </el-form-item>
          <el-form-item label="父包:" prop="parentId" v-show="showPackageParent">
<!--            <el-input v-model="packageForm.parentId"></el-input>-->
            <el-select v-model="packageForm.parentId" placeholder="请选择父包" style="width:260px">
              <el-option v-for="item in packageList" :key="item.id" :value="item.id" :label="item.name"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogPackageVisible = false" v-waves>取 消</el-button>
          <el-button type="primary" @click="submitPackageForm" v-waves>确 定</el-button>
        </span>
      </el-dialog>


    </cus-wraper>
  </div>
</template>

<script>
    import {
        deleteProject,
        getProjectPackageTree,
        getProjectPage,
        saveOrUpdatePackage,
        saveOrUpdateProject,
        deletePackage,
        getPackageList
    } from '@/api/code-generator/project'

    export default {
    name: 'Project',
    data() {
      return {
        dialogPackageTreeVisible: false, // 树形包弹出框
        dialogProjectVisible: false, // 项目form表单弹出框
        dialogPackageVisible: false, // 包弹出框
        // checkShow: false, // 树形包: 添加、修改、删除 按钮默认不显示
        showPackageParent: false,// 是否显示包上级选择栏
        list: [],
        treeData: [],// 树形包数据
        packageList: [], // 包列表
        statusList: [{ key: 1, display_name: '启用' }, { key: 0, display_name: '停用' }],
        projectId: undefined, // 树包关联项目ID
        listLoading: true,
        total: 0,
        listQuery: {
          page: 1,
          limit: 10
        },
        projectForm: {
          id: undefined, //项目ID
          name: undefined, //项目名称
          userId: undefined, //所属用户ID
          username: undefined, //所属用户名
          status: undefined //状态：是否启用  0:停用  1:启用
        },
        packageForm: {
          id: undefined, //包ID
          name: undefined, //包名
          parentId: undefined, //父包ID
          projectId: undefined, //包关联项目ID
        },
        dialogStatus: '',
        titleMap: {
          updateProject: '编辑项目',
          createProject: '新建项目',
          treePackage: '项目树包',
          updatePackage: '编辑包',
          createPackage: '新建包'
        },
        rules: {
          name: [
            { required: true, message: '请输入项目名称', trigger: 'blur' }
          ]
        },
        rulesPackage: {
          name: [
            { required: true, message: '请输入包名', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = true
        getProjectPage(this.listQuery).then(response => {
          this.list = response.data.records
          this.total = response.data.total
          this.listLoading = false
        })
      },
      // 项目树形包管理
      getTree(id) {
        getProjectPackageTree(id).then(response => {
          var records = response.data
          // 替换后端返回对象数据中的属性名为前端所要的属性名
          // records = JSON.parse(JSON.stringify(records).replace(/id/g, 'id'))
          records = JSON.parse(JSON.stringify(records).replace(/name/g, 'label'))
          this.treeData = records
        })
      },
      showPackageTree(row) {
        this.getTree(row.id);
        this.projectId = row.id;
        this.projectForm = Object.assign({}, row);
        this.dialogStatus = 'treePackage';
        this.dialogPackageTreeVisible = true;
      },
      getPackageList(id){
        getPackageList(id).then(res => {
          this.packageList = res.data
        });
      },
      handleCreatePackage(data,projectName) {
        this.resetForm();
        this.projectForm.id = data.projectId;
        this.projectForm.name = projectName;
        this.packageForm.parentId = data.id;
        this.packageForm.projectId = data.projectId;
        this.dialogStatus = 'createPackage';
        this.dialogPackageVisible = true;
          // const newChild = { id: id++, label: 'test', children: [] };
          // if (!data.children) {
          //     this.$set(data, 'children', []);
          // }
          // data.children.push(newChild);
      },
      handleUpdatePackage(data) {
        this.showPackageParent = true;
        // 如果是顶级包则不显示
        if (data.parentId === 0){
          this.showPackageParent = false;
        }
        this.getPackageList(data.projectId);
        this.packageForm.id = data.id;
        this.packageForm.name = data.label;
        this.packageForm.parentId = data.parentId;
        this.packageForm.projectId = data.projectId;
        this.dialogStatus = 'updatePackage';
        this.dialogPackageVisible = true;
      },
      handleDeletePackage(node, data) {
          this.$confirm('此操作将永久删除该包, 是否继续?', '提示', {
              confirmButtonText: '确定',
              type: 'warning'
          }).then(() => {
              let id = data.id;
              deletePackage(id).then(response => {
                  if (response.code == 200) {
                      this.getTree(this.projectId);
                      // const parent = node.parent;
                      // const children = parent.data.children || parent.data;
                      // const index = children.findIndex(d => d.id === data.id);
                      // children.splice(index, 1);
                      // this.submitOk(response.message)
                  } else {
                      this.submitFail(response.message)
                  }
              })
          });
      },
      submitPackageForm() {
          this.$refs.dataForm.validate(valid => {
              if (valid) {
                  saveOrUpdatePackage(this.packageForm).then(response => {
                      if (response.code == 200) {
                          this.getTree(response.data)
                          this.submitOk(response.message)
                          this.dialogPackageVisible = false
                      } else {
                          this.submitFail(response.message)
                      }
                  }).catch(err => {
                      console.log(err)
                  })
              }
          })
      },

      // ======================================================================

      // 项目管理
      handleCreateProject() {
        this.resetForm()
        this.dialogStatus = 'createProject'
        this.dialogProjectVisible = true
      },
      handleUpdateProject(row) {
        this.projectForm = Object.assign({}, row);
        this.dialogStatus = 'updateProject'
        this.dialogProjectVisible = true
      },
      handleDeleteProject(row) {
        let id = row.id
        deleteProject(id).then(response => {
          if (response.code == 200) {
            this.getList()
            this.submitOk(response.message)
          } else {
            this.submitFail(response.message)
          }
        })
      },
      submitProjectForm() {
        this.$refs.dataForm.validate(valid => {
          if (valid) {
            saveOrUpdateProject(this.projectForm).then(response => {
              if (response.code == 200) {
                this.getList()
                this.submitOk(response.message)
                this.dialogProjectVisible = false
              } else {
                this.submitFail(response.message)
              }
            }).catch(err => {
              console.log(err)
            })
          }
        })
      },
      resetForm() {
        this.projectForm = {
          id: undefined, //项目ID
          name: undefined, //项目名称
          userId: undefined, //所属用户ID
          username: undefined, //所属用户名
          status: undefined //状态：是否启用  0:停用  1:启用
        };
        this.packageForm = {
          id: undefined, //包ID
          name: undefined, //包名
          parentId: undefined, //父包ID
          projectId: undefined, //包关联项目ID
        };
      },
      // 监听dialog关闭时的处理事件
      handleDialogClose() {
        if (this.$refs['dataForm']) {
          this.$refs['dataForm'].clearValidate() // 清除整个表单的校验
        }
        // 清空树包数据
        // this.treeData = [];
        // 隐藏上级包选择栏
        this.showPackageParent = false;
      }
    }
  }
</script>
