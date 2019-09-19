import store from '@/store'

/**
 * @param {Array} value
 * @returns {Boolean}
 * @example see @/views/permission/directive.vue
 */
export function checkPermission(value) {
  if (value && value instanceof Array && value.length > 0) {
    const roles = store.getters && store.getters.roles
    const permissionRoles = value

    const hasPermission = roles.some(role => {
      return permissionRoles.includes(role)
    })

    if (!hasPermission) {
      return false
    }
    return true
  } else {
    console.error(`need roles! Like v-permission="['admin','editor']"`)
    return false
  }
}

// 检查该用户是否有按钮权限
export function checkBtnPermission(value) {
  // example
  // array :  v-if="['userList:add','userList:edit']"
  // single : v-if="'userList:add'"
  if (!value) {
    return false
  }
  // 获取用户按钮权限
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

export default {
  // Vue.js的插件应当有一个公开方法 install。这个方法的第一个参数是 Vue 构造器，第二个参数是一个可选的选项对象。
  install: function (Vue) {
    Vue.prototype.global_checkBtnPermission = (param) => checkBtnPermission(param)
    // Vue.prototype.global_func = (param) => alert(param)
  }
}
