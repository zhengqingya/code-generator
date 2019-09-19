import request from '@/utils/request'

export function fetchTemplatePage(query) {
  return request({
    url: '/api/code/bsTemplate/listPage',
    method: 'post',
    data: query
  });
}

export function saveTemplate(form) {
  return request({
    url: '/api/code/bsTemplate/save',
    method: 'post',
    data: form
  });
}

export function deleteTemplate(id) {
  return request({
    url: '/api/code/bsTemplate/delete',
    method: 'post',
    data: {
      'id': id
    }
  });
}
