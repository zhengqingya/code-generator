<template>
  <div class="dashboard-editor-container" style="padding: 20px;background: #f2f2f2;">

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="box-card" style="height: 400px;">
          <div slot="header" class="clearfix">
            <span>项目介绍</span>
          </div>
          <div style="font-size: 15px;text-indent: 2em;padding: 0px;">
            基于Spring Boot+Vue+Shiro前后端分离的代码生成器
          </div>
          <h3>技术</h3>
          <ol>
            <li> 开发语言：Java</li>
            <li> 数据库：Mysql</li>
            <li> 前端框架：Vue + Axios</li>
            <li> 后端框架：Spring Boot</li>
            <li> 缓存：Redis</li>
            <li> 权限：Shiro</li>
            <li> 工具类集合：Hutool</li>
            <li> 内网穿透工具：Natapp</li>
          </ol>
          <!--<div class="text-right">
            <el-button type="primary" @click.native="jumpUrl('web')">前端项目地址</el-button>
            <el-button type="success" @click.native="jumpUrl('api')">后端项目地址</el-button>
            <el-button type="warning" @click.native="jumpUrl('demo')">演示环境</el-button>
          </div>-->
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card" style="height: 400px;">
          <div slot="header" class="clearfix">
            <span>更新日志</span>
          </div>
          <template>
            <light-timeline :items='items'></light-timeline>
          </template>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>内存信息</span>
          </div>
          <el-table
            :data="tableDataMemMerge"
            border
            style="width: 100%">
            <el-table-column
              prop="label"
              label="属性"
              width="200">
            </el-table-column>
            <el-table-column
              prop="value"
              label="值">
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>服务器信息</span>
          </div>
          <el-table
            :data="tableDataSysMerge"
            border
            style="width: 100%">
            <el-table-column
              prop="label"
              label="属性"
              width="200">
            </el-table-column>
            <el-table-column
              prop="value"
              label="值">
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

  </div>
</template>

<script>
  import Vue from 'vue';
  import LightTimeline from 'vue-light-timeline';
  Vue.use(LightTimeline);

  import {getServerInfo} from '@/api/dashboard'
  import TransactionTable from '../admin/components/TransactionTable'
  import {mapGetters} from 'vuex'
  import PanThumb from '@/components/PanThumb'
  export default {
    name: 'DashboardEditor',
    components: {PanThumb, TransactionTable},
    data() {
      return {
        activeName: '更新日志',
        emptyGif: 'https://wpimg.wallstcn.com/0e03b7da-db9e-4819-ba10-9016ddfdaed3',
        activeNum: Math.round(Math.random() * 10000),
        faceNum: Math.round(Math.random() * 10000),
        serverInfo: "",
        tableDataCpu: [],
        tableDataMem:[],
        tableDataJvm:[],
        tableDataSys:[],
        tableDataMemMerge:[],
        tableDataSysMerge:[],
        cpu:{
          "cpuNum":"核心数",
          "total":"CPU总的使用率",
          "sys":"CPU系统使用率",
          "used":"CPU用户使用率",
          "wait":"CPU当前等待率",
          "free":"CPU当前空闲率"
        },
        mem:{
          "total":"内存总量",
          "used":"已用内存",
          "free":"剩余内存",
          "usage":"使用率"
        },
        jvm:{
          "total":"当前JVM占用的内存总数(M)",
          "max":"JVM最大可用内存总数(M)",
          "free":"JVM空闲内存(M)",
          "version":"JDK版本",
          "home":"JDK路径",
          "name":"JDK名称",
          "startTime":"JDK启动时间",
          "used":"JVM已用内存(M)",
          "usage":"JVM使用率",
          "runTime":"JDK运行时间"
        },
        sys:{
          "computerName":"服务器名称",
          "computerIp":"服务器Ip",
          "userDir":"项目路径",
          "osName":"操作系统",
          "osArch":"系统架构"
        },
        activities: [{
          content: '项目2.0版本提交，完善系统，增加权限控制功能',
          timestamp: '2019/5/20'
        }, {
          content: '项目1.0版本提交，基本完成代码生成器相关功能',
          timestamp: '2019/01/05'
        }],
        items: [
          {
            type: 'star',
            tag: '2019/5/20',
            content: '项目1.0版本提交，基本完成代码生成器相关功能'
          },
          {
            type: 'star',
            tag: '2019/08/05',
            content: '项目2.0版本提交，完善系统，增加权限控制功能'
          }
        ]
      }
    },
    created(){
      this.getServerInfo();
    },
    methods: {
      jumpUrl (type) {
        switch (type) {
          case 'home':
            window.open('http://www.whjdz2012.cn')
            break;
          case 'web':
            window.open('https://gitee.com/skysong/coffeewx-web-ui')
            break;
          case 'api':
            window.open('https://gitee.com/skysong/coffeewx-admin-api')
            break;
          case 'demo':
            window.open('http://www.whjdz2012.cn')
            break;
        }
      },
      getServerInfo(){
        let _this = this;
        getServerInfo().then(res => {
          let data = res.data;
          _this.serverInfo = data;
          if (data && data.cpu) {
            _this.tableDataCpu = [];

            let _keys = [];
            _keys.push("cpuNum");
            _keys.push("used");
            _keys.push("sys");
            _keys.push("free");
            _this._.each(_keys,function (value) {
              if(value === 'used' || value === 'sys' || value === 'free'){
                _this.tableDataCpu.push({"label": _this.cpu[value], "value": data.cpu[value] + '%'});
              }else{
                _this.tableDataCpu.push({"label": _this.cpu[value], "value": data.cpu[value]});
              }
            });
            _this.tableDataSysMerge = _this.tableDataSysMerge.concat(_this.tableDataCpu);
          }
          if (data && data.mem) {
            _this.tableDataMem = [];

            let _keys = [];
            _keys.push("total");
            _keys.push("used");
            _keys.push("free");
            _keys.push("usage");
            _this._.each(_keys,function (value) {
              if(value === 'usage'){
                _this.tableDataMem.push({"label": _this.mem[value], "value": data.mem[value] + '%'});
              }else{
                _this.tableDataMem.push({"label": _this.mem[value], "value": data.mem[value]});
              }
            });
            _this.tableDataMemMerge = _this.tableDataMemMerge.concat(_this.tableDataMem);
          }
          if (data && data.jvm) {
            _this.tableDataJvm = [];

            let _keys = [];
            _keys.push("total");
            _keys.push("used");
            _keys.push("free");
            _keys.push("usage");
            _keys.push("runTime");

            _this._.each(_keys,function (value) {
              if(value === 'usage'){
                _this.tableDataJvm.push({"label": _this.jvm[value], "value": data.jvm[value] + '%'});
              }else{
                _this.tableDataJvm.push({"label": _this.jvm[value], "value": data.jvm[value]});
              }
            });
            _this.tableDataMemMerge = _this.tableDataMemMerge.concat(_this.tableDataJvm);
          }
          if (data && data.sys) {
            _this.tableDataSys = [];

            let _keys = [];
            _keys.push("computerName");
            _keys.push("computerIp");
            _keys.push("userDir");
            _keys.push("osName");
            _keys.push("osArch");
            _this._.each(_keys,function (value) {
              _this.tableDataSys.push({"label": _this.sys[value], "value": data.sys[value]});
            });
            _this.tableDataSysMerge = _this.tableDataSysMerge.concat(_this.tableDataSys);
          }

        })
      }
    },
    computed: {
      ...mapGetters([
        'name',
        'avatar',
        'roles',
        'introduction'
      ]),
      serverInfoComputed(){
        return this.serverInfo
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .el-row {
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  }

  .small-box {
    i {
      font-size: 18px;
    }
    span {
      font-weight: bold;
      margin-left: 10px;
    }
    p {
      font-size: 36px;
      color: #666;
      margin: 1rem 0 0 1rem;
    }
  }

  .text-right {
    text-align: right;
  }

  .userinfo-row {
    height: 50px;
  }

  .userinfo-lable {
    display: inline-block;
    font-size: 20px;
    font-weight: bold;
    text-align: right;;
    margin-right: 10px;
    align: right;
    width: 80px;
  }

  .userinfo-content {
    display: inline-block;
    font-size: 20px;
  }

  .emptyGif {
    display: block;
    width: 45%;
    margin: 0 auto;
  }

  .dashboard-editor-container {
    background-color: #e3e3e3;
    min-height: 100vh;
    padding: 50px 60px 0px;
    .pan-info-roles {
      font-size: 12px;
      font-weight: 700;
      color: #333;
      display: block;
    }
    .info-container {
      position: relative;
      margin-left: 190px;
      height: 150px;
      line-height: 200px;
      .display_name {
        font-size: 48px;
        line-height: 48px;
        color: #212121;
        position: absolute;
        top: 25px;
      }
    }
  }

</style>
