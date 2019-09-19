import request from '@/utils/request'

export function fetchDbList() {
  return request({
    url: '/api/db/list',
    method: 'post',
    data: {}
  });
}

export function saveDb(form) {
  return request({
    url: '/api/db/save',
    method: 'post',
    data: form
  });
}

export function deleteDb(dbName) {
  return request({
    url: '/api/db/delete',
    method: 'post',
    data: {
      'db_name': dbName
    }
  });
}

export function fetchTableList(query) {
  return request({
    url: '/api/db/tableList',
    method: 'post',
    data: query
  });
}

export function fetchColumnList(query) {
  return request({
    url: '/api/db/columnList',
    method: 'post',
    data: query
  });
}

export function saveTable(tableInfo) {
  return request({
    url: '/api/db/saveTable',
    method: 'post',
    data: tableInfo
  });
}
