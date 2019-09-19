<template>
  <div class="app-container">
    <cus-wraper>
      <cus-filter-wraper>
        <el-input v-model="listQuery.name" placeholder="请输入角色名称" style="width:200px" clearable></el-input>
        <el-button type="primary" @click="getList" icon="el-icon-search" v-waves>{{ $t('table.search') }}</el-button>
        <el-button v-has="'sys:role:add'" type="primary" @click="handleCreate" icon="el-icon-plus" v-waves>{{ $t('table.add') }}</el-button>
      </cus-filter-wraper>

      <div class="table-container">
        <el-table v-loading="listLoading" :data="list" size="mini" element-loading-text="Loading" fit border highlight-current-row>
          <el-table-column label="ID" prop="id" align="center"></el-table-column>
          <el-table-column label="角色编码" prop="code" align="center"></el-table-column>
          <el-table-column label="角色名称" prop="name" align="center"></el-table-column>
          <el-table-column label="备注" prop="remarks" align="center"></el-table-column>
          <el-table-column label="是否已关联系统用户" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.isRelatedSysUser == 1 ? 'success' : 'danger'" hit>
                {{ scope.row.isRelatedSysUser == 1 ? '&emsp;是&emsp;' : '&emsp;否&emsp;' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="是否已关联系统资源" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.isRelatedSysMenu == 1 ? 'success' : 'danger'" hit>
                {{ scope.row.isRelatedSysMenu == 1 ? '&emsp;是&emsp;' : '&emsp;否&emsp;' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label='是否已关联微信账号' align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.isRelatedWxAccount == 1 ? 'success' : 'danger'" hit>
                {{ scope.row.isRelatedWxAccount == 1 ? '&emsp;是&emsp;' : '&emsp;否&emsp;' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column v-if="this.global_checkBtnPermission(['sys:role:edit','roleSetting','sys:role:delete'])" :label="$t('table.actions')" align="center">
            <template slot-scope="scope">
              <el-button v-has="'sys:role:edit'" size="mini" type="primary" @click="handleUpdate(scope.row)" icon="el-icon-edit" plain v-waves> {{ $t('table.edit') }}</el-button>
              <router-link :to="'/systemManage/roleSetting/'+scope.row.id">
                <el-button v-has="'roleSetting'" type="primary" size="mini" style="width: 100px;margin-left: 10px;margin-right: 10px;" icon="el-icon-setting" plain>
                  权限设置
                </el-button>
              </router-link>
              <cus-del-btn v-has="'sys:role:delete'" @ok="handleDelete(scope.row)"></cus-del-btn>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页 -->
        <cus-pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList"/>
      </div>

      <el-dialog :title="titleMap[dialogStatus]" :visible.sync="dialogVisible" width="40%" @close="handleDialogClose">
        <el-form ref="dataForm" :model="form" :rules="rules" label-width="100px" class="demo-ruleForm">
          <el-form-item label="角色编码:" prop="code">
<!--            <el-input v-model="form.code" :disabled="dialogStatus != 'create'"></el-input>-->
            <el-input v-model="form.code"></el-input>
          </el-form-item>
          <el-form-item label="角色名称:" prop="name">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="角色描述:" prop="remarks">
            <el-input v-model="form.remarks" type="textarea" :rows="7"></el-input>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false" v-waves> {{ $t('table.cancel') }}</el-button>
          <el-button type="primary" @click="submitForm" v-waves>{{ $t('table.confirm') }}</el-button>
        </span>
      </el-dialog>
    </cus-wraper>
  </div>
</template>

<script>
    import {getRolePage, saveOrUpdateRole, deleteRole} from '@/api/system/role'

    export default {
        data() {
            return {
                tableKey: 0,
                list: null,
                total: 0,
                listLoading: true,
                listQuery: {
                    page: 1,
                    limit: 10,
                    name: undefined
                },
                showReviewer: false,
                form: {
                    id: undefined, //主键ID
                    code: undefined, //角色编码
                    name: undefined, //角色名称
                    remarks: undefined, //角色描述
                    isRelatedSysUser: undefined, //是否已关联系统用户
                    isRelatedSysMenu: undefined, //是否已关联系统资源
                    isRelatedWxAccount: undefined //是否已关联微信账号
                },
                dialogVisible: false,
                dialogStatus: '',
                titleMap: {
                    update: '编辑',
                    create: '创建'
                },
                rules: {
                    // code: [
                    //     {required: true, message: '请输入角色编码', trigger: 'blur'},
                    //     {min: 1, max: 25, message: '长度在 1 到 20 个字符', trigger: 'blur'}
                    // ],
                    name: [
                        {required: true, message: '请输入角色名', trigger: 'blur'},
                        {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
                    ],
                    remarks: [
                        {max: 200, message: '长度最多200个字符', trigger: 'blur'}
                    ]
                }
            }
        },
        created() {
            this.getList()
        },
        methods: {
            getList() {
                this.listLoading = true;
                getRolePage(this.listQuery).then(response => {
                    this.list = response.data.records;
                    this.total = response.data.total;
                    this.listLoading = false;
                })
            },
            handleCreate() {
                this.resetForm();
                this.dialogStatus = 'create';
                this.dialogVisible = true;
            },
            handleUpdate(row) {
                this.form = Object.assign({}, row);
                this.form.isRelatedSysMenu = undefined;
                this.form.isRelatedSysUser = undefined;
                this.form.isRelatedWxAccount = undefined;
                this.dialogStatus = "update";
                this.dialogVisible = true;
                console.log(this.dialogStatus);
            },
            handleDelete(row) {
                if (row.isRelatedSysUser == '1' || row.isRelatedSysMenu == '1' || row.isRelatedWxAccount == '1') {
                    this.$message({message: '请先解除关联', type: 'warning'});
                    return false
                }
                deleteRole(row.id).then(response => {
                    if (response.code === 200) {
                        this.getList();
                        this.submitOk(response.message);
                    } else {
                        this.submitFail(response.message);
                    }
                });
            },
            submitForm() {
                this.$refs['dataForm'].validate(valid => {
                    if (valid) {
                        saveOrUpdateRole(this.form).then(response => {
                            if (response.code == 200) {
                                this.getList();
                                this.submitOk(response.message);
                                this.dialogVisible = false;
                            } else {
                                this.submitFail(response.message);
                            }
                        }).catch(err => { console.log(err);  });
                    }
                });
            },
            resetForm() {
                this.form = {
                    id: undefined, //主键ID
                    code: undefined, //角色编码
                    name: undefined, //角色名称
                    remarks: undefined //角色描述
                }
            },
            // 监听dialog关闭时的处理事件
            handleDialogClose() {
                if (this.$refs['dataForm']) {
                    this.$refs['dataForm'].clearValidate(); // 清除整个表单的校验
                }
            }
        }
    }
</script>
