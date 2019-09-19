import request from '@/utils/request'

export function fetchDatabasePage(query) {
  return request({
    url: '/api/code/database/listPage',
    method: 'post',
    data: query
  });
}

export function saveDatabase(form) {
  return request({
    url: '/api/code/database/save',
    method: 'post',
    data: form
  });
}

export function deleteDatabase(id) {
  return request({
    url: '/api/code/database/delete',
    method: 'post',
    data: {
      'id': id
    }
  });
}

export function fetchTableList(query) {
  return request({
    url: '/api/code/database/tableList',
    method: 'post',
    data: query
  });
}

export function fetchColumnList(query) {
  return request({
    url: '/api/code/database/columnList',
    method: 'post',
    data: query
  });
}

export function saveTable(tableInfo) {
  return request({
    url: '/api/code/database/saveTable',
    method: 'post',
    data: tableInfo
  });
}
