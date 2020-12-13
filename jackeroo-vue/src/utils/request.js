import axios from 'axios'
import router from '@/router'
import store from '@/store'
import storage from 'store'
import {notification, message} from 'ant-design-vue'
import { VueAxios } from './axios'
import { ACCESS_TOKEN } from '@/store/mutation-types'

// 创建 axios 实例
const request = axios.create({
  // API 请求的默认前缀
  baseURL: '/jackeroo/api',
  // 服务器响应的数据类型
  responseType: 'json',
  timeout: 6000 // 请求超时时间
})

// 异常拦截处理器
const errorHandler = (error) => {
  if (error.response) {
    const data = error.response.data
    // 从 localstorage 获取 token
    // const token = storage.get(ACCESS_TOKEN)
    if (error.response.status === 401) {
      notification.error({
        message: '退出登录',
        description: '登录信息失效，请重新登录'
      })
  
      store.dispatch('Logout').then(() => {
        window.location.reload()
      })
    }else if (error.response.status === 500) {
      message.error('系统服务器异常')
    }else {
      message.error(data.msg)
    }
  }
  return Promise.reject(error)
}

// request interceptor
request.interceptors.request.use(config => {
  const token = storage.get(ACCESS_TOKEN)
  // 如果 token 存在
  // 让每个请求携带自定义 token 请根据实际情况自行修改
  if (token) {
    config.headers['Access-Token'] = token
  }
  return config
}, errorHandler)

// response interceptor
request.interceptors.response.use((response) => {
  if(response.config.responseType == 'json'){
    if(response.data.code === 401){
      // token失效退出登录
      warningAndSkip()
    }else if(response.data.code === 40001){
      // 账号被挤退出登录
      warningAndSkip('您的账号已经在其他地方登录，如非本人操作，请立即修改密码！')
    }else if(response.data.code != 0){
      message.error(response.data.msg)
    }
  }
  return response.data
}, errorHandler)


function warningAndSkip(msg){
  return new Promise(function(resolve, reject){
    let token = storage.get(ACCESS_TOKEN)
    if(token){
      notification.error({
        message: '退出登录',
        description: msg || '登录信息失效，请重新登录'
      })
      
      store.commit('SET_TOKEN', '')
      store.commit('SET_ROLES', [])
      storage.remove(ACCESS_TOKEN)

      setTimeout(() => {
        let pathname = window.location.pathname
        if(router.currentRoute.fullPath == '/user/login'){
          router.replace({ path: '/user/login'})
        }else if(router.currentRoute.fullPath == '/' && pathname != '/'){
          router.replace({ path: '/user/login', query: {redirect : pathname}})
        }else{
          router.replace({ path: '/user/login', query: {redirect : router.currentRoute.fullPath}})
        }
        
        resolve('ok')
      }, 1000)
    }else{
      reject('error')
    }
  })
}

const installer = {
  vm: {},
  install (Vue) {
    Vue.use(VueAxios, request)
  }
}

export default request

export {
  installer as VueAxios,
  request as axios
}
