import request from '@/utils/request';

export function getRolePage(query) {
  return request({
    url: '/api/system/role/listPage',
    method: 'post',
    data: query
  });
}

export function saveOrUpdateRole(form) {
  return request({
    url: '/api/system/role/saveOrUpdate',
    method: 'post',
    data: form
  });
}

export function deleteRole(id) {
  return request({
    url: '/api/system/role/delete',
    method: 'post',
    data: { 'id': id }
  });
}

export function getRoleById(id) {
  return request({
    url: '/api/system/role/detail',
    method: 'post',
    data: { 'id': id }
  })
}

export function treeUser() {
  return request({
    url: '/api/system/user/treeUser',
    method: 'post'
  })
}

export function getRoleUserByRoleId(data) {
  return request({
    url: '/api/system/userRole/list',
    method: 'post',
    data: data
  })
}

export function getRoleMenuByRoleId(data) {
  return request({
    url: '/api/system/roleMenu/list',
    method: 'post',
    data: data
  })
}

export function saveUserRole(data) {
  return request({
    url: '/api/system/userRole/saveUserRole',
    method: 'post',
    data: data
  })
}

export function saveRoleMenu(data) {
  return request({
    url: '/api/system/roleMenu/saveRoleMenu',
    method: 'post',
    data: data
  })
}
