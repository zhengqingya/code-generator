import request from '@/utils/request'

export function generateCode(tableInfo, packageConfig) {
  return request({
    url: '/api/code/generate',
    method: 'post',
    data: {
      'table_info': tableInfo,
      'package_config': packageConfig
    }
  });
}
