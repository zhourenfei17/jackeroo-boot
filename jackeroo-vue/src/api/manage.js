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
export function postAction(url,parameter) {
  return axios({
    url: url,
    method: method.POST ,
    data: parameter
  })
}

//post method= {post | put}
export function httpAction(url,parameter,method) {
  return axios({
    url: url,
    method:method ,
    data: parameter
  })
}

//put
export function putAction(url,parameter) {
  return axios({
    url: url,
    method: method.PUT,
    data: parameter
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
    data: parameter
  })
}
//获取服务器返回的文件流
export function getFile(url, parameter) {
  return axios({
    url: url,
    method: method.GET,
    params: parameter,
    responseType: 'blob'
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
