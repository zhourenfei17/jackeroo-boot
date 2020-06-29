import {axios} from '@/utils/request'
import qs from 'qs'

const api = {
  permission: '/permission',
  permissionNoPager: '/permission/no-pager',
  orgTree: '/org/tree',
  validUniqueUrl: '/system/valid/unique'
}

export default api

const method = {
  GET: 'GET',
  POST: 'POST',
  PUT: 'PUT',
  DELETE: 'DELETE'
}

//post
//使用application / x-www-form-urlencoded格式发送数据
export function postAction(url,parameter) {
  return axios({
    url: url,
    method: method.POST ,
    headers: { 'content-type': 'application/x-www-form-urlencoded' },
    data: qs.stringify(parameter)
  })
}

//post method= {post | put}
export function httpAction(url,parameter,method) {
  return axios({
    url: url,
    method:method ,
    headers: { 'content-type': 'application/x-www-form-urlencoded' },
    data: qs.stringify(parameter)
  })
}

//put
export function putAction(url,parameter) {
  return axios({
    url: url,
    method: method.PUT,
    headers: { 'content-type': 'application/x-www-form-urlencoded' },
    data: qs.stringify(parameter)
  })
}

//get
export function getAction(url,parameter) {
  return axios({
    url: url,
    method: method.GET,
    params: parameter
  })
}

//deleteAction
export function deleteAction(url,parameter) {
  return axios({
    url: url,
    method: method.DELETE,
    params: parameter
  })
}

export function getPermissions (parameter) {
  return axios({
    url: api.permissionNoPager,
    method: 'get',
    params: parameter
  })
}

export function getOrgTree (parameter) {
  return axios({
    url: api.orgTree,
    method: 'get',
    params: parameter
  })
}

export function validtedUnique(parameter){
  return axios({
    url: api.validUniqueUrl,
    method: method.GET,
    params: parameter
  })
}
