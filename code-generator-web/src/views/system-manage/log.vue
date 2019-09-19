<template>
  <div class="app-container">
    <cus-wraper>
      <cus-filter-wraper>
        <el-input v-model="listQuery.username" placeholder="访问人" style="width:200px" clearable></el-input>
        <el-input v-model="listQuery.url" placeholder="url" style="width:200px" clearable></el-input>
        <el-date-picker
          v-model="listQuery.startTime"
          type="datetime"
          placeholder="开始时间"
          align="right"
          :picker-options="pickerOptions">
        </el-date-picker>
        <el-date-picker
          v-model="listQuery.endTime"
          type="datetime"
          placeholder="结束时间"
          align="right"
          :picker-options="pickerOptions">
        </el-date-picker>
        <el-button type="primary" @click="getList" icon="el-icon-search" v-waves>查询</el-button>
<!--        <el-button type="primary" @click="handleCreate" icon="el-icon-plus" v-waves>添加</el-button>-->
      </cus-filter-wraper>
      <div class="table-container">
        <el-table v-loading="listLoading" :data="list" size="mini" element-loading-text="Loading" fit border highlight-current-row>
          <el-table-column label="ID" prop="id" align="center"></el-table-column>
          <el-table-column label="接口名称" prop="name" align="center"></el-table-column>
          <el-table-column label="接口地址" prop="url" align="center"></el-table-column>
          <el-table-column label="访问IP" prop="ip" align="center"></el-table-column>
          <el-table-column label="访问人" prop="username" align="center"></el-table-column>
          <el-table-column label="请求状态" prop="status" align="center"></el-table-column>
          <el-table-column label="接口执行时间" prop="executeTime" align="center"></el-table-column>
          <el-table-column label="访问时间" align="center">
            <template slot-scope="scope">
              <span>{{scope.row.gmtCreate|dateTimeFilter}}</span>
            </template>
          </el-table-column>
<!--          <el-table-column align="center" label="操作">-->
<!--            <template slot-scope="scope">-->
<!--              <el-button size="mini" type="primary" @click="handleUpdate(scope.row)" icon="el-icon-edit" plain v-waves>编辑</el-button>-->
<!--              <cus-del-btn @ok="handleDelete(scope.row)"></cus-del-btn>-->
<!--            </template>-->
<!--          </el-table-column>-->
        </el-table>
        <!-- 分页 -->
        <cus-pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList"/>
      </div>

      <el-dialog :title="titleMap[dialogStatus]" :visible.sync="dialogVisible" width="40%" @close="handleDialogClose">
        <el-form ref="dataForm" :model="form" :rules="rules" label-width="100px" class="demo-ruleForm">
          <el-form-item label="主键ID:" prop="id">
            <el-input v-model="form.id"></el-input>
          </el-form-item>
          <el-form-item label="接口名称:" prop="name">
            <el-input v-model="form.name"></el-input>
          </el-form-item>
          <el-form-item label="接口地址:" prop="url">
            <el-input v-model="form.url"></el-input>
          </el-form-item>
          <el-form-item label="访问人IP:" prop="ip">
            <el-input v-model="form.ip"></el-input>
          </el-form-item>
          <el-form-item label="访问人ID:" prop="userId">
            <el-input v-model="form.userId"></el-input>
          </el-form-item>
          <el-form-item label="请求状态:" prop="status">
            <el-input v-model="form.status"></el-input>
          </el-form-item>
          <el-form-item label="接口执行时间:" prop="executeTime">
            <el-input v-model="form.executeTime"></el-input>
          </el-form-item>
          <el-form-item label="访问时间:" prop="gmtCreate">
            <el-input v-model="form.gmtCreate"></el-input>
          </el-form-item>
<!--          <el-form-item label="更新时间:" prop="gmtModified">-->
<!--            <el-input v-model="form.gmtModified"></el-input>-->
<!--          </el-form-item>-->
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false" v-waves>取 消</el-button>
          <el-button type="primary" @click="submitForm" v-waves>确 定</el-button>
        </span>
      </el-dialog>
    </cus-wraper>
  </div>
</template>

<script>
  import { getLogPage, saveOrUpdateLog, deleteLog } from '@/api/system/log'

  export default {
    name: 'Log',
    data() {
      return {
        dialogVisible: false,
        list: [],
        listLoading: true,
        total: 0,
        listQuery: {
          page: 1,
          limit: 10,
          username: undefined,
          url: undefined,
          startTime: undefined,
          endTime: undefined
        },
        pickerOptions: {
          shortcuts: [{
            text: '今天',
            onClick(picker) {
              picker.$emit('pick', new Date());
            }
          }, {
            text: '昨天',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit('pick', date);
            }
          }, {
            text: '一周前',
            onClick(picker) {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit('pick', date);
            }
          }]
        },
        input: '',
        form: {
          id: undefined, //主键ID
          name: undefined, //接口名称
          url: undefined, //接口地址
          ip: undefined, //访问人IP
          userId: undefined, //访问人ID
          status: undefined, //1 成功 0失败
          executeTime: undefined, //接口执行时间
          gmtCreate: undefined, //创建时间
          gmtModified: undefined //更新时间
        },
        dialogStatus: '',
        titleMap: {
          update: '编辑',
          create: '创建'
        },
        rules: {
          name: [
            { required: true, message: '请输入项目名称', trigger: 'blur' }
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
        getLogPage(this.listQuery).then(response => {
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
        deleteLog(id).then(response => {
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
            saveOrUpdateLog(this.form).then(response => {
              if (response.code === 200) {
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
          name: undefined, //接口名称
          url: undefined, //接口地址
          ip: undefined, //访问人IP
          userId: undefined, //访问人ID
          status: undefined, //1 成功 0失败
          executeTime: undefined, //接口执行时间
          gmtCreate: undefined, //创建时间
          gmtModified: undefined //更新时间
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
