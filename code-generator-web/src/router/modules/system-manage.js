/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/views/layout/Layout'

const systemManage = {
  path: '/systemManage',
  component: Layout,
  alwaysShow: true,
  name: '系统管理',
  redirect: 'noredirect',
  meta: {
    title: '系统管理',
    icon: 'component',
    resources: 'systemManage'
  },
  children: [
    {
      path: 'user',
      component: () => import('@/views/system-manage/user'),
      name: '用户管理',
      meta: { title: '用户管理', icon: 'my-user', noCache: true, resources: 'user' }
    },
    {
      path: 'role',
      component: () => import('@/views/system-manage/role'),
      name: '角色管理',
      meta: { title: '角色管理', icon: 'my-role', noCache: true, resources: 'role' }
    },
    {
      path: 'roleSetting/:id(\\d+)',
      component: () => import('@/views/system-manage/roleSetting'),
      name: '权限设置',
      meta: { title: '权限设置', icon: 'my-role', noCache: true, resources: 'roleSetting' },
      hidden: true
    },
    {
      path: 'menu',
      component: () => import('@/views/system-manage/menu'),
      name: '菜单管理',
      meta: { title: '菜单管理', icon: 'my-sysmenu', noCache: true, resources: 'menu' }
    },
    {
      path: 'log',
      component: () => import('@/views/system-manage/log'),
      name: '系统日志',
      meta: { title: '系统日志', icon: 'my-sysmenu', noCache: true, resources: 'log' }
    }
  ]
};

export default systemManage
