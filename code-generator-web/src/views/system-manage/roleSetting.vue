<template>
  <div class="app-container">
    <div style="display: block;height: 10px"></div>
    <span style="font-size: 25px;font-weight: bold;">角色 ： {{ roleData.name }}</span>
    <div style="display: block;height: 20px"></div>
    <el-tabs v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane label="分配用户" name="allotUser">
        <el-button v-show="userList.length > 0" type="primary" style="margin-bottom: 20px;margin-left: 20px;"
                   @click="saveUserRole">保存</el-button>
        <el-tree
          ref="userTree"
          :data="userTree"
          show-checkbox
          :props="defaultPropsUser"
          :default-checked-keys="defaultCheckedKeysUser"
          node-key="id"></el-tree>
      </el-tab-pane>
      <el-tab-pane label="分配菜单" name="allotMenu">
        <el-button v-show="menuTree.length > 0" type="primary" style="margin-bottom: 20px;margin-left: 20px;"
                   @click="saveRoleMenu">保存</el-button>
        <!-- check-strictly：在显示复选框的情况下，是否严格的遵循父子不互相关联的做法，默认为 false -->
        <el-tree
          :check-strictly="true"
          :data="menuTree"
          show-checkbox
          node-key="id"
          ref="menuTree"
          highlight-current
          :default-checked-keys="defaultCheckedKeysMenu"
          :props="defaultPropsMenu">
        </el-tree>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
    import {
        getRoleById,
        getRoleUserByRoleId,
        treeUser,
        getRoleMenuByRoleId,
        saveUserRole,
        saveRoleMenu
    } from '@/api/system/role'
    import {treeMenu} from '@/api/system/menu'

    export default {
        name: 'roleSetting',
        data() {
            return {
                roleId: undefined,
                roleData: {
                    id: undefined, //主键ID
                    code: undefined, //角色编码
                    name: undefined, //角色名称
                    remarks: undefined, //角色描述
                    // createTime: undefined, //创建时间
                    // updateTime: undefined, //更新时间
                },
                activeName: 'allotUser',// 选项卡

                defaultPropsUser: {
                    children: 'children',
                    label: 'username'
                },
                defaultCheckedKeysUser: [],
                userList: [],
                userTree: [],

                // 格式化后端传过来的数据为vue树所需参数
                defaultPropsMenu: {
                    children: 'children',
                    label: 'title'
                },
                defaultCheckedKeysMenu: [],
                menuList: [],
                menuTree: [],

                defaultPropsWxAccount: {
                    children: 'children',
                    label: 'name'
                }
            }
        },
        created() {
            this.roleId = this.$route.params.id;
            this.getRoleData();// 获取角色信息
            this.getUserTabData();
            this.getMenuTabData();
        },
        methods: {
            getRoleData() {
                getRoleById(this.roleId).then(res => {
                    this.roleData = res.data
                })
            },
            // 角色与用户关联
            getUserTabData() {
                // 获取树节点数据
                treeUser().then(response => {
                    this.userList = response.data.userList;
                    this.userTree = response.data.userTree;
                });
                // 获取树节点选中数据
                getRoleUserByRoleId({'roleId': this.roleId}).then(response => {
                    if (response.data && response.data.length > 0) {
                        this.defaultCheckedKeysUser = this._.map(response.data, 'userId')
                    }
                })
            },
            // 角色与菜单关联
            getMenuTabData() {
                treeMenu().then(response => {
                    this.menuList = response.data.menuList
                    this.menuTree = response.data.menuTree
                });
                getRoleMenuByRoleId({'roleId': this.roleId}).then(response => {
                    if (response.data && response.data.length > 0) {
                        this.defaultCheckedKeysMenu = this._.map(response.data, 'menuId')
                    }
                })
            },
            handleTabClick(tab, event) {
                //        if(this.activeName === 'allotUser'){
                //          this.getUserTabData();
                //        }
                //        if(this.activeName === 'allotMenu'){
                //          this.getMenuTabData();
                //        }
                //        if(this.activeName === 'allotWxAccount'){
                //          this.getWxAccountTabData();
                //        }
            },
            saveUserRole() {
                var selectedIds = this.$refs.userTree.getCheckedKeys();
                this.roleId = parseInt(this.roleId);// 转换成数字类型
                saveUserRole({'roleId': this.roleId, 'userIds': selectedIds.join(',')}).then( response => {
                    if (response.code == 200) {
                        this.submitOk(response.message);
                    } else {
                        this.submitFail(response.message);
                    }
                });
            },
            saveRoleMenu() {
                var selectedIds = this.$refs.menuTree.getCheckedKeys();
                this.roleId = parseInt(this.roleId);// 转换成数字类型
                saveRoleMenu({'roleId': this.roleId, 'menuIds': selectedIds.join(',')}).then( response => {
                    if (response.code === 200) {
                        this.submitOk(response.message);
                    } else {
                        this.submitFail(response.message);
                    }
                })
            }
        }
    }
</script>
