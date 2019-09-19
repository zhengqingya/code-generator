import request from '@/utils/request'

export function getProjectPage(query) {
  return request({
    url: '/api/code/project/listPage',
    method: 'post',
    data: query
  });
}

export function getProjectList(query) {
  return request({
    url: '/api/code/project/list',
    method: 'post',
    data: query
  });
}

export function getPackageList(id) {
  return request({
    url: '/api/code/project/listPackage',
    method: 'post',
    data: {id: id}
  });
}

export function saveOrUpdateProject(form) {
  return request({
    url: '/api/code/project/saveOrUpdateProject',
    method: 'post',
    data: form
  });
}

export function deleteProject(id) {
  return request({
    url: '/api/code/project/deleteProject',
    method: 'post',
    data: {
      'id': id
    }
  });
}

export function getProjectPackageTree(id) {
  return request({
    url: '/api/code/project/tree',
    method: 'post',
    data: {id: id}
  });
}

export function saveOrUpdatePackage(form) {
  return request({
    url: '/api/code/project/saveOrUpdatePackage',
    method: 'post',
    data: form
  });
}

export function deletePackage(id) {
  return request({
    url: '/api/code/project/deletePackage',
    method: 'post',
    data: {
      'id': id
    }
  });
}

export function generateCode(tableInfo, packageConfig) {
  return request({
    url: '/api/code/project/generate',
    method: 'post',
    data: {
      'tableInfo': tableInfo,
      'packageConfig': packageConfig
    }
  });
}

