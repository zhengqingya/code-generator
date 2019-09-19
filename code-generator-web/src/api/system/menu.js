import request from '@/utils/request';

export function treeMenu() {
  return request({
    url: '/api/system/menu/treeMenu',
    method: 'post'
  })
}

export function saveMenu(form) {
  return request({
    url: '/api/system/menu/save',
    method: 'post',
    data: form
  })
}

export function deleteMenu(id) {
  return request({
    url: '/api/system/menu/delete',
    method: 'post',
    data: { 'id': id }
  });
}

// ========================== 上面是用到的

export function getSysPermissionPage(query) {
    return request({
        url: '/api/system/menu/listPage',
        method: 'post',
        data: query
    });
}

export function getSysPermissionById(id) {
    return request({
        url: '/api/system/menu/getById',
        method: 'post',
        data: { 'id': id }
    });
}
