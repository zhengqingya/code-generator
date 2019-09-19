<template>
  <div class="dashboard-editor-container" style="padding: 20px;background: #f2f2f2;">
    <el-row :gutter="20">
        <el-col :span="6"yarn dev>
          <el-card class="box-card small-box">
            <div>
              <i class="el-icon-upload" style="color: #FFC107;"></i>
              <span>访问量</span>
            </div>
            <p>{{activeNum}}</p>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="box-card small-box">
            <div>
              <i class="el-icon-news" style="color: #E91E63;"></i>
              <span>粉丝</span>
            </div>
            <p>{{faceNum}}</p>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="box-card small-box">
            <div>
              <i class="el-icon-star-on" style="color: #FFC107;"></i>
              <span>star</span>
            </div>
            <p>104</p>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="box-card small-box">
            <div>
              <i class="el-icon-share" style="color: #009688;"></i>
              <span>Forked</span>
            </div>
            <p>44</p>
          </el-card>
        </el-col>
    </el-row>
    <el-row :gutter="24" >
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>项目介绍</span>
          </div>
          <div style="font-size: 20px;text-indent: 2em;padding: 0px;">CoffeeWx是一款基于VUE、Spring Boot的前后端分离的微信公众号管理系统、支持多公众号。</div>
          <h3>技术</h3>
          <ol>
            <li> 开发语言：Java</li>
            <li> 数据库：Mysql</li>
            <li> 前端框架：Vue + Axios</li>
            <li> 后端框架：Spring Boot</li>
            <li> 缓存：Redis</li>
            <li> 工具类集合：Hutool</li>
            <li> 和微信端对接工具：weixin-java-tools</li>
            <li> 内网穿透工具：Natapp</li>
          </ol>
          <div class="text-right">
            <span class="fl">QQ交流群:790782152</span>
            <el-tag @click.native="jumpUrl('home')">官网</el-tag>
            <el-tag @click.native="jumpUrl('web')" type="success">前端框架</el-tag>
            <el-tag @click.native="jumpUrl('api')" type="info">后台框架</el-tag>
            <el-tag @click.native="jumpUrl('demo')" type="warning">演示地址</el-tag>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="box-card" style="height: 300px">
          <div slot="header" class="clearfix">
            <span>登陆用户</span>
            </div>
            <div style="width: 160px;display: inline-block;float: left">
            <pan-thumb :image="avatar" style="float: left">
            Your roles:
            <span v-for="item in roles" :key="item" class="pan-info-roles">{{ item }}</span>
            </pan-thumb>
            </div>
            <div style="display: inline-block;float: left;margin-left: 20px;margin-top: 20px">
            <table>
            <tr class="userinfo-row">
            <td class="userinfo-lable">
            角色
            </td>
            <td class="userinfo-content">
            <span v-for="item in roles" :key="item" style="display: inline-block;margin-right: 5px">{{ item }}</span>
            </td>
            </tr>
            <tr class="userinfo-row">
            <td class="userinfo-lable">
            登陆名
            </td>
            <td class="userinfo-content">
            {{ name }}
            </td>
            </tr>
            <tr class="userinfo-row">
            <td class="userinfo-lable">
            昵称
            </td>
            <td class="userinfo-content">
            {{ introduction }}
            </td>
            </tr>
            </table>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="24">
      <el-col :span="12">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>更新日志</span>
            <!--<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>-->
          </div>
          <el-collapse v-model="activeName" accordion>
            <el-collapse-item title="v1.0  2019.02.26" name="3">
              <div>完善readme文档</div>
              <div>添加演示地址</div>
              <div>建立QQ交流群</div>
            </el-collapse-item>
            <el-collapse-item title="初次提交 2019.01.05" name="4">
              <div>前端vue-elementUI,git提交</div>
              <div>后端springboot,git提交</div>
            </el-collapse-item>
          </el-collapse>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="clearfix">
            <span>个人信息</span>
            <!--<el-button style="float: right; padding: 3px 0" type="text">操作按钮</el-button>-->
          </div>
          <div class="text-center">
            <el-tag>请作者喝杯咖啡</el-tag>
          </div>
          <div align="center">
            <img style="width: 300px;margin:0 auto;" src="https://timgsa.baidu.com/timg?image&quality=80&size=b10000_10000&sec=1554283598&di=1465c474061c23800a000bd53284a0ec&src=http://imgup02.iefans.net/iefans/2018-12/14/10/15447556537899_1.jpg">
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import PanThumb from '@/components/PanThumb'
export default {
  name: 'DashboardEditor',
  components: { PanThumb },
  data() {
    return {
      activeName:'更新日志',
      emptyGif: 'https://wpimg.wallstcn.com/0e03b7da-db9e-4819-ba10-9016ddfdaed3',
      activeNum:Math.round(Math.random()*10000),
      faceNum:Math.round(Math.random()*10000)
  }
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
    }
  },
  computed: {
    ...mapGetters([
      'name',
      'avatar',
      'roles',
      'introduction'
    ])
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
  .small-box{
    i{
      font-size: 18px;
    }
    span{
      font-weight: bold;
      margin-left: 10px;
    }
    p{
      font-size: 36px;
      color: #666;
      margin:1rem 0 0 1rem;
    }
  }
 .text-right{text-align: right;}
  .userinfo-row{
    height: 50px;
  }
  .userinfo-lable{
    display: inline-block;
    font-size: 20px;font-weight: bold;text-align:right;;
    margin-right: 10px;
    align:right;
    width: 80px;
  }
  .userinfo-content{
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
