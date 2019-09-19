import request from '@/utils/request'

export function loginByPhoneOrSn(user, password) {
  const data = {
    user: user,
    password: password,
    source : 'admin'
  };
  return request({
    url: '/api/user/login',
    method: 'post',
    data
  });
}

export function logout(token) {
  return request({
    url: '/api/user/logout',
    method: 'post',
    data: {'token': token }
  });
}

export function getInfo(token) {
  return request({
    url: '/api/user/getByToken',
    method: 'post',
    data: {'token': token }
  });
}

