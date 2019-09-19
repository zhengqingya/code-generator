import request from '@/utils/request'

export function loginByUsername(username, password, openId, accessToken) {
  const data = {
    username,
    password,
    openId,
    accessToken
  }
  return request({
    url: '/api/auth/login',
    method: 'post',
    data
  })
}

/* QQ第三方登录 */
export function loginByQQ(data) {
  return request({
    url: '/api/auth/loginByQQ',
    method: 'get',
    data: data
  })
}

export function logout(token) {
  const data = {
    token
  }
  return request({
    url: '/api/auth/logout',
    method: 'post',
    params: data
  })
}

export function getUserInfo(token) {
  return request({
    url: '/api/system/user/getCurrentUserInfo',
    method: 'post',
    params: { 'token' : token }
  })
}

