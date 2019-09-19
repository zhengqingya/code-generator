import request from '@/utils/request'

export function getLogPage(query) {
  return request({
    url: '/api/system/log/listPage',
    method: 'post',
    data: query
  })
}

export function saveOrUpdateLog(form) {
  return request({
    url: '/api/system/log/saveOrUpdate',
    method: 'post',
    data: form
  })
}

export function deleteLog(id) {
  return request({
    url: '/api/system/log/delete',
    method: 'post',
    data: { 'id': id }
  })
}

export function getLogById(id) {
  return request({
    url: '/api/system/log/detail',
    method: 'post',
    data: { 'id': id }
  })
}
