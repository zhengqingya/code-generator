import request from '@/utils/request'

export function fetchTemplatePage(query) {
  return request({
    url: '/api/code/project_template/listPage',
    method: 'post',
    data: query
  });
}

export function saveTemplate(form) {
  return request({
    url: '/api/code/project_template/save',
    method: 'post',
    data: form
  });
}

export function deleteTemplate(id) {
  return request({
    url: '/api/code/project_template/delete',
    method: 'post',
    data: {
      'id': id
    }
  });
}

export function generateTemplate(projectId) {
  return request({
    url: '/api/code/project_template/generateTemplate',
    method: 'post',
    data: {
      'projectId': projectId
    }
  });
}

export function getProjectVelocityContextPage(query) {
  return request({
    url: '/api/code/project_template/listPageCodeProjectVelocityContext',
    method: 'post',
    data: query
  });
}
