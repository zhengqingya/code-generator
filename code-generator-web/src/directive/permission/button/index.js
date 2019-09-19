import Vue from 'vue'
import store from '@/store'

/** 自定义权限指令 : v-has **/

Vue.directive('has', {
  bind: function(el, binding) {
    // 获取按钮权限
    if (!Vue.prototype.$_has(binding.value)) {
      el.parentNode.removeChild(el)
    }
  }
})

// 权限检查方法
Vue.prototype.$_has = function(value) {
  // example
  // array :  v-has="['userList:add','userList:edit']"
  // single : v-has="'userList:add'"
  if (!value) {
    return false
  }
  // 获取用户按钮权限 - 从后台拿数据
  const dynamicButtons = store.getters.buttons
  if (dynamicButtons === undefined || dynamicButtons === null || dynamicButtons.length < 1) {
    return false
  }
  if (value instanceof Array && value.length > 0) {
    const hasPermission = dynamicButtons.some(button => {
      return value.includes(button.resources)
    })
    return hasPermission
  } else {
    const hasPermission = dynamicButtons.some(button => {
      return button.resources === value
    })
    return hasPermission
  }
}

// 权限检查方法
// Vue.prototype.$_has = function(value) {
//   // 获取用户按钮权限
//   let isExist = false
//   const dynamicButtons = store.getters.buttons
//   if (dynamicButtons === undefined || dynamicButtons === null || dynamicButtons.length < 1) {
//     return isExist
//   }
//   dynamicButtons.forEach(button => {
//     if (button.resources === value) {
//       isExist = true
//       return isExist
//     }
//   })
//   return isExist
// }
