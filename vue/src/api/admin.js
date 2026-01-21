import request from '@/utils/request'

export function getUserList(params) { return request({ url: '/admin/user/list', method: 'get', params }) }
export function getUserDetail(id) { return request({ url: `/admin/user/${id}`, method: 'get' }) }
export function updateUserStatus(userId, status) { return request({ url: '/admin/user/status', method: 'put', params: { userId, status } }) }
