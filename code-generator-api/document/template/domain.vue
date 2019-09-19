<template>
    <div class="demo">
        <cus-wraper>
            <div class="mb15">
                <Row :gutter="5">

                    #foreach($vo in $vos )
<i-col :span="4">
                        <Input v-model="listQuery.${vo.filed}" placeholder="${vo.title}"/>
                    </i-col>
                    #end

                    <!--<i-col :span="4">
                        <Select v-model="listQuery.machineType" placeholder="请选择机械类型查询" clearable>
                            <Option v-for="item in machineTypeList" :value="item.value" :key="item.value">{{ item.label }}</Option>
                        </Select>
                    </i-col>-->
                    <i-col :span="4">
                        <Button type="primary" @click="getListPage">查询</Button>
                        <Button type="primary" @click="handleCreate">添加</Button>
                    </i-col>
                </Row>
            </div>
            <Table :columns="columns" size="small" style="margin-top:10px" :loading="listLoading" :data="list" disabled-hover>
                <template slot-scope="{row}" slot="action">
                    <text-Button @click="handleDetail(row)">详情</text-Button>
                    <Divider type="vertical" />
                    <text-Button @click="handleUpdate(row)">编辑</text-Button>
                    <Divider type="vertical" />
                    <del-button @on-ok="handleDelete(row)">删除</del-button>
                </template>
            </Table>
            <cus-page @pageChange="handleCurrentChange" @pageSizeChange="handleSizeChange" :pageSize="listQuery.pageSize" :total="total"></cus-page>
        </cus-wraper>

        <!--  新增、编辑、详情 - 采用抽屉  +++++++++++++++++++++++++++++++++++++++++++++++++++++     -->
        <Drawer :title="titleValue[dialogStatus]" v-model="drawer" @on-visible-change="handleDrawerClose" width="30%" :mask-closable="false">
            <!-- 详情 -->
            <template v-if="dialogStatus == 'detail'">
        #foreach($vo in $vos )
        <cus-cell label="${vo.title}">{{formData_detail.${vo.filed}}}</cus-cell>
        #end
    </template>
            <!-- 编辑和新增 -->
            <template v-else>
                <Form ref="formData" :model="formData" label-position="top" :rules="ruleFormData">
                    <FormItem label="项目名称：" prop="projectId" label-position="top" clearable>
                        <Select v-model="formData.projectId" filterable placeholder="请选择">
                            <Option
                                v-for="item in $store.state.app.allProjectList"
                                :value="item.projectId"
                                :key="item.projectId"
                            >{{ item.name }}</Option>
                        </Select>
                    </FormItem>
                    #foreach($vo in $vos )
<FormItem label="${vo.title}：" prop="${vo.filed}">
                        <Input v-model="formData.${vo.filed}" placeholder="请输入${vo.title}" />
                    </FormItem>
                    #end
</Form>
                <div class="demo-drawer-footer">
                    <Button type="primary" @click="ok">保存</Button>
                    <Button style="margin-right: 8px" @click="drawer = false">关闭</Button>
                </div>
            </template>
        </Drawer>
    </div>
</template>
<script>
    import {
        getDataListPage,
        saveOrUpdateData,
        deleteData,
    } from '@/api/crane/${domain}'

    export default {
        name: 'zq-app',
        data () {
            //校验
            #foreach($vo in $vos )
const validate${vo.filed} = (rule, value, callback) => {
                if (!value) {
                    callback(new Error('请选择${vo.title}！！！'))
                } else {
                    callback()
                }
            };
            #end

            return {
                listQuery: {
                    page: 1,
                    pageSize: 10,
#foreach($vo in $vos )
                    ${vo.filed}: undefined,
#end

                },
                list: [],
                total: 0,
                drawer: false, // form表单抽屉弹出页面
                listLoading: false,
                dialogStatus: '',//title自定义标题
                titleValue:{
                    create:"创建",
                    detail:"详情",
                    update:"编辑"
                },//title信息
                formData: {
        #foreach($vo in $vos )
            ${vo.filed}: undefined,// ${vo.title}
        #end
        },
                formData_detail: {},
                columns: [
                    #foreach($vo in $vos ){
                        title: '${vo.title}',
                        key: '${vo.filed}',
                        align: 'center'
                    },
                    #end{
                        title: '操作',
                        align: 'center',
                        slot: 'action'
                    }
                ],
                //校验
                ruleFormData: {
                    #foreach($vo in $vos )
                    ${vo.filed}: [
                        {
                            required: true,
                            validator: validate${vo.filed},
                            trigger: 'blur'
                        }
                    ],
#end
                }
            }
        },
        mounted () {
            this.getListPage();
            this.getFilterList();
        },
        methods: {
            getFilterList () {
                //如果没有获取到数据，重新加载
                const hasLabour = this.$store.getters.hasLabour;
                if (!hasLabour) this.$store.dispatch('getLabourList')
            },
            getListPage () {
                this.listLoading = true;
                getDataListPage(this.listQuery).then(res => {
                    this.list = res.data.data.records;
                    this.total = res.data.data.total;
                    this.listLoading = false
                })
            },
            handleCreate () {
                this.dialogStatus = 'create';
                this.getFilterList();
                this.resetFormCustom();
                this.drawer = true;
            },
            handleUpdate (row) {
                this.formData = Object.assign({}, row);
                // this.formData.projectId = parseInt(row.projectId);//字符串转数字
                // this.formData.dateRegistered = new Date(row.dateRegistered);//时间
                this.dialogStatus = 'update';
                this.getFilterList();
                this.drawer = true;
            },
            //自动赋值
       /*     handleUserChange(val) {
                if (val) {
                    const currentPerson = this.在此前加符号哦store.state.app.labours.find(item => item.personnelId === val);
                    this.formData.icCardId = currentPerson.cardNum;//一卡通
                    this.formData.identityCardId = currentPerson.idCard;//身份证号
                }
            },*/
            handleDetail (row) {
                this.formData_detail = Object.assign({}, row);
                this.dialogStatus = 'detail';
                this.drawer = true;
            },
            handleDelete (row) {
                deleteData({ ${domain}Id: row.${domain}Id })
                    .then(response => {
                        if (response.data.status == 1) {
                            this.submitSucc(response.data.message);
                            this.getListPage();
                        } else {
                            this.在此前加符号哦Message.error(response.data.message)
                        }
                    })
            },
            ok () {
                this.在此前加符号哦refs.formData.validate(valid => {
                    if (valid) {
                        saveOrUpdateData(this.formData)
                            .then(response => {
                                if (response.data.status == 1) {
                                    this.submitSucc(response.data.message);
                                    this.getListPage();
                                    this.drawer = false
                                } else {
                                    this.在此前加符号哦Message.error(response.data.message)
                                }
                            })
                    }
                })
            },
            handleSizeChange (val) {
                this.listQuery.pageSize = val;
                this.getListPage();
            },
            handleCurrentChange (val) {
                this.listQuery.page = val;
                this.getListPage();
            },
            handleDrawerClose(val) {
                if (!val) this.在此前加符号哦refs.formData.resetFields();
            },
            resetFormCustom () {
                this.在此前加符号哦refs.formData.resetFields();
                this.formData.${domain}Id = undefined;
            }
        }
    }
</script>
<style lang="less" scoped>
    .demo {
        .demo-title {
            height: 70px;
            line-height: 70px;
            background: #2b3b65;
            padding-left: 20px;
            font-size: 24px;
            color: #fff;
        }
        .demo-container {
            padding: 15px;
        }
        .demo-sn {
            height: 32px;
            line-height: 32px;
            font-size: 16px;
            color: #2b3b65;
        }
    }
    .demo-drawer-footer {
        width: 100%;
        position: absolute;
        bottom: 0;
        left: 0;
        border-top: 1px solid #e8e8e8;
        padding: 10px 16px;
        text-align: center;
        background: #fff;
    }
</style>
