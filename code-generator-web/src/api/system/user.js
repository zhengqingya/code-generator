import request from '@/utils/request';

export function getSysUserPage(query) {
  return request({
    url: '/api/system/user/listPage',
    method: 'post',
    data: query
  });
}

export function updatePersonalInfo(form) {
  return request({
    url: '/api/system/user/updatePersonalInfo',
    method: 'post',
    data: form
  });
}

export function saveSysUser(form) {
  return request({
    url: '/api/system/user/save',
    method: 'post',
    data: form
  });
}

export function deleteSysUser(id) {
  return request({
    url: '/api/system/user/delete',
    method: 'post',
    data: {'id': id}
  });
}

export function getSysUserById(id) {
  return request({
    url: '/api/system/user/getById',
    method: 'post',
    data: {'id': id}
  });
}
