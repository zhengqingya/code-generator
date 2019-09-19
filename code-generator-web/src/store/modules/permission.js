import { asyncRoutes, constantRoutes } from '@/router'

/**
 *
 * @param  {Array} userRouter 后台返回的用户权限json
 * @param  {Array} allRouter  前端配置好的所有动态路由的集合
 * @return {Array} realRoutes 过滤后的路由
 */

export function filterAsyncRoutes(allRouter = [], userRouter = []) {
  var realRoutes = []
  for (var i = 0; i < allRouter.length; i++) {
    var v = allRouter[i]
    // 处理hidden类型路由
    if (v.hidden && v.hidden == true) {
      realRoutes.push(v)
      continue
    }
    for (var j = 0; j < userRouter.length; j++) {
      var item = userRouter[j]
      if (v.meta && v.meta.resources) {
        if (item.resources === v.meta.resources) {
          if (item.children && item.children.length > 0) {
            v.children = filterAsyncRoutes(v.children, item.children)
          }
          v.meta.title = item.title
          // 处理图标，优先显示配置的图标
          if (item.icon) {
            v.meta.icon = item.icon
          }
          realRoutes.push(v)
        }
      }
    }
  }
  // allRouter.forEach((v, i) => {
  //   userRouter.forEach((item, index) => {
  //     if (item.resources === v.meta.resources) {
  //       if (item.children && item.children.length > 0) {
  //         v.children = filterAsyncRoutes(item.children, v.children)
  //       }
  //       v.meta.title = item.title
  //       v.meta.icon = item.icon
  //       realRoutes.push(v)
  //     }
  //   })
  // })
  // console.log(realRoutes);
  return realRoutes
}

const permission = {
  state: {
    routes: [],
    addRoutes: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    }
  },
  actions: {
    GenerateRoutes({ commit }, data) {
      return new Promise(resolve => {
        // 使用权限
        const accessedRoutes = filterAsyncRoutes(asyncRoutes, data)
        // console.log(accessedRoutes);
        commit('SET_ROUTES', accessedRoutes)
        resolve(accessedRoutes)

        // 不使用权限
        // commit('SET_ROUTES', asyncRoutes)
        // resolve(asyncRoutes)
      })
    }
  }
}

export default permission
