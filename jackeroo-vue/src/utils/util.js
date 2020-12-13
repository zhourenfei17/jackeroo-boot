import { add } from "lodash"
import store from '@/store'

export function timeFix () {
  const time = new Date()
  const hour = time.getHours()
  return hour < 9 ? '早上好' : hour <= 11 ? '上午好' : hour <= 13 ? '中午好' : hour < 20 ? '下午好' : '晚上好'
}

export function welcome () {
  const arr = ['休息一会儿吧', '准备吃什么呢?', '要不要打一把 DOTA', '我猜你可能累了']
  const index = Math.floor(Math.random() * arr.length)
  return arr[index]
}

/**
 * 触发 window.resize
 */
export function triggerWindowResizeEvent () {
  const event = document.createEvent('HTMLEvents')
  event.initEvent('resize', true, true)
  event.eventType = 'message'
  window.dispatchEvent(event)
}

export function handleScrollHeader (callback) {
  let timer = 0

  let beforeScrollTop = window.pageYOffset
  callback = callback || function () {}
  window.addEventListener(
    'scroll',
    event => {
      clearTimeout(timer)
      timer = setTimeout(() => {
        let direction = 'up'
        const afterScrollTop = window.pageYOffset
        const delta = afterScrollTop - beforeScrollTop
        if (delta === 0) {
          return false
        }
        direction = delta > 0 ? 'down' : 'up'
        callback(direction)
        beforeScrollTop = afterScrollTop
      }, 50)
    },
    false
  )
}

export function isIE () {
  const bw = window.navigator.userAgent
  const compare = (s) => bw.indexOf(s) >= 0
  const ie11 = (() => 'ActiveXObject' in window)()
  return compare('MSIE') || ie11
}

/**
 * Remove loading animate
 * @param id parent element id or class
 * @param timeout
 */
export function removeLoadingAnimate (id = '', timeout = 1500) {
  if (id === '') {
    return
  }
  setTimeout(() => {
    document.body.removeChild(document.getElementById(id))
  }, timeout)
}

/**
 * 判断是否拥有权限指令
 * @param {String|Array} permissions 权限指令，多个用数组传递
 * @param {String} logic 多权限指令时的逻辑策略，可选 and、or，默认为and
 * @returns {Boolean} true为有权限，false为无权限
 */
export function hasPermissions(permissions, logic){
  logic = logic != 'or' ? 'and' : 'or'
  // 所有的角色，以及角色拥有的权限指令
  const roles = store.getters.roles
  // 将权限指令转化为数组，统一处理
  const actionName = typeof permissions === 'string' ? [permissions] : permissions

  const authArray = []
  for (const action of actionName) {
    for (const role of roles) {
      if(role.permissionList.indexOf(action) > -1){
        authArray.push(true)
        break
      }
    }
  }
  if(logic === 'and'){
    // and，不全为true，则表示没有权限
    return authArray.length === actionName.length
  }else{
    // or，不包含true，则表示没有权限
    return authArray.length > 0
  }
}

/**
 * 判断是否拥有该角色编码
 * @param {String|Array} roles 角色编码，多个用数组传递
 * @param {String} logic 多角色编码时的逻辑策略，可选 and、or，默认为and
 * @returns {Boolean} true为有权限，false为无权限
 */
export function hasRoles(roles, logic){
  logic = logic != 'or' ? 'and' : 'or'
  // 所有的角色，以及角色拥有的权限指令
  const roleList = store.getters.roles
  // 将角色编码统一转化为数组，统一处理
  const roleNames = typeof roles === 'string' ? [roles] : roles
  const authArray = []
  for (const role of roleList) {
    if(roleNames.indexOf(role.roleCode) > -1){
      authArray.push(true)
    }
  }
  if(logic === 'and'){
    // and，不全为true，则表示没有权限
    return authArray.length === roleNames.length
  }else{
    // or，不包含true，则表示没有权限
    return authArray.length > 0
  }
}