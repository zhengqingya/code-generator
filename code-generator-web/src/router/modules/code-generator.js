/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/views/layout/Layout'

const codeGenerator = {
  path: '/codeGenerator',
  component: Layout,
  alwaysShow: true,
  name: '代码生成器',
  redirect: 'noredirect',
  meta: {
    title: '代码生成器',
    icon: 'tree',
    // icon: 'database',
    resources: 'codeGenerator'
  },
  // hidden: false,
  children: [
    {
      path: 'project',
      component: () => import('@/views/code-generator/project'),
      name: '项目管理',
      meta: { title: '项目管理', icon: 'documentation', noCache: true, resources: 'project' }
    },
    {
      path: 'bs_template',
      component: () => import('@/views/code-generator/bs_template'),
      name: '初始模板',
      meta: { title: '初始模板', icon: 'documentation', noCache: true, resources: 'bs_template' }
    },
    {
      path: 'project_template',
      component: () => import('@/views/code-generator/project_template'),
      name: '项目模板管理',
      meta: { title: '项目模板管理', icon: 'documentation', noCache: true, resources: 'project_template' }
    },
    {
      path: 'database',
      component: () => import('@/views/code-generator/database/database'),
      name: '数据库管理',
      meta: { title: '数据库管理', icon: 'icon-news', noCache: true, resources: 'database' }
    }, {
      path: 'table',
      component: () => import('@/views/code-generator/database/table'),
      name: '数据库表',
      hidden: true,
      meta: { title: '数据库表', icon: 'database', noCache: true, resources: 'table' }
    }, {
      path: 'column',
      component: () => import('@/views/code-generator/database/column'),
      name: '数据表字段',
      hidden: true,
      meta: { title: '数据表字段', icon: 'database', noCache: true, resources: 'column' }
    },
  ]
};

export default codeGenerator
