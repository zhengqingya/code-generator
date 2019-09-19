<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button v-waves class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-refresh" @click="getList()">刷新</el-button>
      <el-button v-waves class="filter-item" style="margin-left: 10px;" type="primary" :icon="expandAll ? 'el-icon-caret-bottom' : 'el-icon-caret-right'" @click="handleExpand()">
        {{ expandAllTitle }}
      </el-button>
      <el-button v-has="'sys:menu:add'" v-waves class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-plus" @click="handleCreate()">
        {{ $t('table.add') }}
      </el-button>
    </div>

    <tree-table v-loading="listLoading" :data="menuTree" :default-expand-all="expandAll" :columns="columns" border default-children="children">

      <template slot="type" slot-scope="{scope}">
        <span>{{ formatMenuType(scope.row) }}</span>
      </template>

      <template slot="operation" slot-scope="{scope}">
        <el-button v-show="scope.row.type == 'menu'" v-has="'sys:menu:addsub'" type="primary" size="mini" icon="el-icon-plus" @click="handleCreate(scope.row)" plain>
          添加下级
        </el-button>
        <el-button v-has="'sys:menu:edit'" type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(scope.row)" plain>
          {{ $t('table.edit') }}
        </el-button>
        <cus-del-btn v-has="'sys:menu:delete'" @ok="handleDelete(scope.row)"></cus-del-btn>
      </template>
    </tree-table>

    <el-dialog :title="titleMap[dialogStatus]" :visible.sync="dialogFormVisible" @close="handleDialogClose">
      <el-form ref="dataForm" :rules="rules" :model="form" label-position="right" label-width="120px" style="width: 400px; margin-left:50px;">
        <el-form-item v-show="parseInt(form.level) != 1" :label="tempDesc.parentName" prop="parentName">
          <el-input v-model="form.parentName" :placeholder="'请输入' + tempDesc.parentName" disabled/>
        </el-form-item>
        <el-form-item :label="tempDesc.url" prop="url">
          <!--          <el-input v-model="form.url" :placeholder="'请输入' + tempDesc.url" :disabled="dialogStatus !='create'"/>-->
          <el-input v-model="form.url" :placeholder="'请输入' + tempDesc.url"/>
        </el-form-item>
        <el-form-item :label="tempDesc.resources" prop="resources">
          <!--          <el-input v-model="form.resources" :placeholder="'请输入' + tempDesc.resources" :disabled="dialogStatus !='create'"/>-->
          <el-input v-model="form.resources" :placeholder="'请输入' + tempDesc.resources"/>
        </el-form-item>
        <el-form-item :label="tempDesc.title" prop="title">
          <el-input v-model="form.title" :placeholder="'请输入' + tempDesc.title"/>
        </el-form-item>
        <el-form-item :label="tempDesc.type" prop="type">
          <el-select v-model="form.type" class="filter-item" :placeholder="'请选择' + tempDesc.type" style="width: 280px;"
                     clearable>
            <el-option v-for="item in menuTypeOptions" :key="item.key" :label="item.display_name" :value="item.key"/>
          </el-select>
        </el-form-item>
        <el-form-item :label="tempDesc.icon" prop="icon">
          <el-input v-model="form.icon" :placeholder="'请输入' + tempDesc.icon"/>
        </el-form-item>
        <el-form-item :label="tempDesc.sortNo" prop="sortNo">
          <el-input v-model="form.sortNo" type="number" :placeholder="'请输入' + tempDesc.sortNo"/>
        </el-form-item>
        <el-form-item :label="tempDesc.remarks" prop="remarks">
          <el-input v-model="form.remarks" type="textarea" :rows="7" :placeholder="'请输入' + tempDesc.remarks"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer" style="text-align: center">
        <el-button @click="dialogFormVisible = false">
          {{ $t('table.cancel') }}
        </el-button>
        <el-button type="primary" @click="submitForm()">
          {{ $t('table.confirm') }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { saveMenu, deleteMenu, treeMenu } from '@/api/system/menu'

  import treeTable from '@/components/TreeTable'
  import { validateCh } from '@/utils/validate'

  // 常量title
  const tempDesc = {
    'id': '',
    'parentId': '',
    'url': 'url',
    'resources': '编码',
    'title': '名称',
    'level': '级别',
    'sortNo': '排序',
    'icon': '图标',
    'type': '类型',
    'remarks': '备注',
    'parentName': '上级菜单'
  }
  const menuTypeOptions = [
    { key: 'menu', display_name: '菜单' },
    { key: 'button', display_name: '按钮' }
  ]

  export default {
    components: { treeTable },
    data() {
      const _validateCh = (rule, value, callback) => {
        if (!!value && validateCh(value)) {
          callback(new Error('不能包含中文'))
        }
        callback()
      }
      return {
        expandAll: false,
        // expandAll: true, // 是否全部展开
        expandAllTitle: '展开全部',
        tempDesc: tempDesc,
        listLoading: true,
        columns: [
          {
            label: tempDesc.title,
            key: 'title',
            width: 220,
            expand: true,
            align: 'left',
            headerAlign: 'center'
          },
          {
            label: tempDesc.url,
            key: 'url'
          },
          {
            label: tempDesc.resources,
            key: 'resources'
          },
          {
            label: tempDesc.type,
            key: 'type'
          },
          {
            label: tempDesc.icon,
            key: 'icon'
          },
          {
            label: tempDesc.sortNo,
            key: 'sortNo'
          },
          {
            label: tempDesc.remarks,
            key: 'remarks'
          }
          //          {
          //            label: '操作',
          //            key: 'operation',
          //            width:300
          //          }
        ],
        menuList: [],
        menuTree: [],// 树形菜单
        dialogFormVisible: false,
        dialogStatus: '',
        titleMap: {
          update: '编辑',
          create: '增加'
        },
        form: {
          'id': undefined,
          'parentId': undefined,
          'url': undefined,
          'resources': undefined,
          'title': undefined,
          'level': undefined,
          'sortNo': undefined,
          'icon': undefined,
          'type': undefined,
          'remarks': undefined,
          'parentName': undefined
        },
        menuTypeOptions,
        rules: {
          resources: [
            { required: true, message: '请输入' + tempDesc.resources, trigger: 'blur' },
            { min: 1, max: 100, message: '长度在 1 到 100 个字符', trigger: 'blur' },
            { validator: _validateCh, trigger: 'blur' }
          ],
          title: [
            { required: true, message: '请输入' + tempDesc.title, trigger: 'blur' },
            { min: 1, max: 25, message: '长度在 1 到 50 个字符', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '请选择' + tempDesc.type, trigger: 'blur' }
          ],
          sortNo: [
            { required: true, message: '请输入' + tempDesc.sortNo, trigger: 'blur' }
          ],
          remarks: [
            { max: 200, message: '长度最多200个字符', trigger: 'blur' }
          ]
        }
      }
    },
    created() {
      if (this.global_checkBtnPermission(['sys:menu:add', 'sys:menu:addsub', 'sys:menu:edit', 'sys:menu:delete'])) {
        this.columns.push({ label: '操作', key: 'operation', width: 320 })
      }
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = true
        treeMenu().then(response => {
          this.menuList = response.data.menuList
          this.menuTree = response.data.menuTree
          // Just to simulate the time of the request
          // setTimeout(() => {
          //     this.listLoading = false
          // }, 1.5 * 1000)
          this.listLoading = false
        })
      },
      handleCreate(row) {
        this.resetForm()
        this.dialogStatus = 'create'
        this.dialogFormVisible = true

        if (!row) { // 一级菜单
          this.form.level = '1'// 默认一级菜单
          this.form.parentId = '0'
        }
        if (row) { // 子菜单
          this.form.level = parseInt(row.level) + 1
          this.form.parentId = row.id
          this.form.parentName = row.title;
        }
      },
      handleUpdate(row) {
        this.form = []
        // this.form = Object.assign({}, row) // copy obj  此方法在这里暂时不能使用，会出错 -> 有多余参数
        this.form.id = row.id
        this.form.parentId = row.parentId
        this.form.title = row.title
        this.form.url = row.url
        this.form.resources = row.resources
        this.form.type = row.type
        this.form.icon = row.icon
        this.form.sortNo = row.sortNo
        this.form.remarks = row.remarks

        this.dialogStatus = 'update'
        this.dialogFormVisible = true

        var parentNode = this.findTreeNode(row.parentId)
        if (parentNode) {
          this.form.parentName = parentNode.title
        }

        this.form = Object.assign({}, this.form)
      },
      handleDelete(row) {
        deleteMenu(row.id).then(response => {
          if (response.code == 200) {
            this.getList()
            this.submitOk(response.message)
          } else {
            this.submitFail(response.message)
          }
        })
      },
      submitForm() {
        this.form.parentName = undefined;
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            saveMenu(this.form).then(response => {
              if (response.code == 200) {
                this.getList()
                this.submitOk(response.message)
                this.dialogFormVisible = false
              } else {
                this.submitFail(response.message)
              }
            })
          }
        })
      },
      formatMenuType(row, column) {
        switch (row.type) {
          case 'menu':
            return '菜单'
            break
          case 'button':
            return '按钮'
            break
        }
      },
      findTreeNode(id) {
        var node = this._.find(this.menuList, { id: parseInt(id) })
        return node
      },
      handleExpand() {
        this.expandAll = !this.expandAll
        this.expandAllTitle = this.expandAll ? '折叠全部' : '展开全部'
      },
      resetForm() {
        this.form = {
          'id': undefined,
          'parentId': undefined,
          'url': undefined,
          'resources': undefined,
          'title': undefined,
          'level': undefined,
          'sortNo': undefined,
          'icon': undefined,
          'type': undefined,
          'remarks': undefined,
          'parentName': undefined
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
