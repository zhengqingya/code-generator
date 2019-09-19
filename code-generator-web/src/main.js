import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import '@/styles/index.scss' // global css

import App from './App'
import router from './router'
import store from './store'

/* ================================= zq: start ==================================== */
// 抽取公用的实例 - 操作成功与失败消息提醒内容等
import myMix from './utils/mixin'
Vue.mixin(myMix)

// 全局组件 删除按钮等
import './globalComponents'

// 引入页面全局权限 start ------------------------\
import permission from '@/directive/permission/index.js' // 权限判断指令 : v-has
import checkBtnPermission from '@/utils/permission' // 权限判断函数 ： this.global_checkBtnPermission()
import waves from '@/directive/waves' // Waves directive
// import Pagination from '@/components/Pagination' // TODO globalComponents 已经引入 后期删除 微信项目 后移出此
Vue.use(permission);
Vue.use(checkBtnPermission);
Vue.use(waves);
// Vue.use(Pagination); // TODO 待移出
// 引入页面全局权限 end --------------------------

//导入代码高亮文件
import hljs from 'highlight.js'
//导入代码高亮样式
import 'highlight.js/styles/monokai-sublime.css'
//自定义一个代码高亮指令
Vue.directive('highlight',function (el) {
  let highlight = el.querySelectorAll('pre code');
  highlight.forEach((block)=>{
    hljs.highlightBlock(block)
  })
})

// 引入全局函数方法1 -> 通过this.httpTest() 调用
Vue.prototype.httpTest = function (){
  alert('执行成功 - 全局函数');
}

// 引入全局函数方法2 -> 抽取
import zqTestMethod from '@/utils/globalFunction';
Vue.use(zqTestMethod);

/* ================================== zq: end =================================== */

import i18n from './lang' // Internationalization
import './icons' // icon
import './errorLog' // error log
import './permission' // permission control
// import './mock' // simulation data

// button permission
import '@/directive/permission/button/index'

import * as filters from './filters' // global filters

Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  i18n: (key, value) => i18n.t(key, value)
})

// register global utility filters.
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

import _ from 'lodash'
Vue.prototype._ = _

new Vue({
  el: '#app',
  router,
  store,
  i18n,
  render: h => h(App)
})
