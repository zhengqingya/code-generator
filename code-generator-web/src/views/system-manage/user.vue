<template>
  <div class="app-container">
    <cus-wraper>
      <cus-filter-wraper>
        <el-input v-model="listQuery.username" placeholder="请输入账号" style="width:200px" clearable></el-input>
        <el-button type="primary" @click="getList" icon="el-icon-search">查询</el-button>
        <el-button v-has="'sys:user:add'" type="primary" @click="handleCreate" icon="el-icon-plus">{{ $t('table.add') }}</el-button>
      </cus-filter-wraper>
      <div class="table-container">
        <el-table v-loading="listLoading" :data="list" size="mini" fit element-loading-text="Loading" border
                  highlight-current-row align="center">
          <el-table-column label="账号" prop="username" align="center"></el-table-column>
          <el-table-column label="登录密码" prop="pwd" align="center"></el-table-column>
          <el-table-column label="昵称" prop="nickName" align="center"></el-table-column>
          <el-table-column label="性别" prop="sex" align="center">
            <template slot-scope="scope">
              <span>{{ scope.row.sex==0 ? '男':'女' }}</span>
            </template>
          </el-table-column>
          <el-table-column label="手机号码" prop="phone" align="center"></el-table-column>
          <el-table-column label="邮箱" prop="email" align="center"></el-table-column>
          <el-table-column label="头像" prop="avatar" align="center">
            <template slot-scope="scope">
              <span>
                <img :src="scope.row.avatar" alt="" style="width: 50px;height: 50px">
              </span>
            </template>
          </el-table-column>
          <el-table-column label="状态" prop="flag" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.flag == 1 ? 'success' : 'danger'" hit>
                {{ scope.row.flag == 1 ? '启用' : '停用' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column align="center" v-if="this.global_checkBtnPermission(['sys:user:edit','sys:user:delete'])" :label="$t('table.actions')">
            <template slot-scope="scope">
              <el-button v-has="'sys:user:edit'" size="mini" type="primary" @click="handleUpdate(scope.row)" icon="el-icon-edit" plain>
                {{ $t('table.edit') }}
              </el-button>
              <cus-del-btn v-has="'sys:user:delete'" @ok="handleDelete(scope.row)"/>
            </template>
          </el-table-column>
        </el-table>
        <!-- 分页 -->
        <cus-pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList"/>
      </div>

      <el-dialog :title="titleMap[dialogStatus]" :visible.sync="dialogVisible" width="40%" @close="handleDialogClose">
        <el-form ref="dataForm" :model="form" :rules="rules" label-width="80px" class="demo-ruleForm">
          <el-form-item label="账号:" prop="username">
<!--            <el-input v-model="form.username" :disabled="dialogStatus!='create'"></el-input>-->
            <el-input v-model="form.username"></el-input>
          </el-form-item>
          <el-form-item label="登录密码:" prop="pwd">
            <el-input v-model="form.pwd"></el-input>
          </el-form-item>
          <el-form-item label="昵称:" prop="nickName">
            <el-input v-model="form.nickName"></el-input>
          </el-form-item>
          <el-form-item label="性别:" prop="sex">
            <el-select v-model="form.sex" class="filter-item" placeholder='请选择' style="width: 280px;">
              <el-option v-for="item in sexList" :key="item.key" :label="item.display_name"
                         :value="item.key"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="手机号码:" prop="phone">
            <el-input v-model="form.phone"></el-input>
          </el-form-item>
          <el-form-item label="邮箱:" prop="email">
            <el-input v-model="form.email"></el-input>
          </el-form-item>
          <el-form-item label="头像:" prop="avatar">
            <span>
              <img :src="form.avatar" alt="" style="width: 50px;height: 50px">
            </span>
          </el-form-item>
          <el-form-item label="状态:" prop="flag">
            <el-select v-model="form.flag" class="filter-item" placeholder='请选择' style="width: 280px;">
              <el-option v-for="item in flagList" :key="item.key" :label="item.display_name"
                         :value="item.key"></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false"> {{ $t('table.cancel') }} </el-button>
          <el-button type="primary" @click="submitForm"> {{ $t('table.confirm') }} </el-button>
        </span>
      </el-dialog>
    </cus-wraper>
  </div>
</template>

<script>
  import { getSysUserPage, saveSysUser, deleteSysUser } from '@/api/system/user'

  export default {
    data() {
      return {
        dialogVisible: false,
        list: [],
        sexList: [{ key: '0', display_name: '男' }, { key: '1', display_name: '女' }],
        flagList: [{ key: '1', display_name: '启用' }, { key: '0', display_name: '停用' }],
        listLoading: true,
        total: 0,
        listQuery: {
          page: 1,
          limit: 10,
          username: undefined
        },
        input: '',
        form: {
          id: undefined, //主键ID
          username: undefined, //账号
          pwd: undefined, //登录密码
          nickName: undefined, //昵称
          sex: undefined, //性别 0:男 1:女
          phone: undefined, //手机号码
          email: undefined, //邮箱
          avatar: undefined, //头像
          flag: undefined, //状态
          // gmtCreate: undefined, //创建时间
          // gmtModified: undefined //更新时间
        },
        dialogStatus: '',
        titleMap: {
          update: '编辑',
          create: '创建'
        },
          rules: {
              username: [
                  {required: true, message: '请输入账号', trigger: 'blur'},
              ],
              pwd: [
                  { pattern: /^(\w){6,16}$/, message: '请设置6-16位字母、数字组合'}
              ],
              nickName: [
                  {required: true, message: '请输入你昵称', trigger: 'blur'},
              ],
              flag: [
                  {required: true, message: '请选择状态', trigger: 'blur'},
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
        getSysUserPage(this.listQuery).then(response => {
          this.list = response.data.records
          this.total = response.data.total
          this.listLoading = false
        })
      },
      handleCreate() {
        this.resetForm()
        this.dialogStatus = 'create'
        this.dialogVisible = true
      },
      handleUpdate(row) {
        this.form = Object.assign({}, row)
        this.dialogStatus = 'update'
        this.dialogVisible = true
      },
      handleDelete(row) {
        let id = row.id
        deleteSysUser(id).then(response => {
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
            saveSysUser(this.form).then(response => {
              if (response.code == 200) {
                this.getList()
                this.submitOk(response.message)
                this.dialogVisible = false
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
        this.form = {
          id: undefined, //主键ID
          username: undefined, //账号
          pwd: undefined, //登录密码
          nickName: undefined, //昵称
          sex: undefined, //性别 0:男 1:女
          phone: undefined, //手机号码
          email: undefined, //邮箱
          avatar: undefined, //头像
          flag: undefined, //状态
          // gmtCreate: undefined, //创建时间
          // gmtModified: undefined //更新时间
        }
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
