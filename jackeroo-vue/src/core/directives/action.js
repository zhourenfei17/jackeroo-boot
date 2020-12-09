import Vue from 'vue'
import store from '@/store'

/**
 * Action 权限指令
 * 指令用法：
 *  - 在需要控制 action 级别权限的组件上使用 v-action:[logic]="permission" , 
 *      名称       |              说明              |     类型       |   默认值
 *   logic        | 多权限指令策略，可选and、or       |      -         |   and
 *   permission   | 权限指令                        |  String|Array  |    -
 * 
 * 如下：
 *    <a-button v-action="'system:user:add'" >添加用户</a-button>
 *    <a-button v-action="['system:user:delete', 'system:user:remove']">删除用户</a-button>
 *    <a v-action:or="['system:user:edit','system:user:update']" @click="edit(record)">修改</a>
 *
 *  - 当前用户没有权限时，组件上使用了该指令则会被隐藏
 *  - 当后台权限跟 pro 提供的模式不同时，只需要针对这里的权限过滤进行修改即可
 *
 */
const action = Vue.directive('action', {
  inserted: function (el, binding, vnode) {
    // 多权限指令策略，默认为and
    let logic = binding.arg || 'and'
    if(logic != 'or'){
      logic = 'and'
    }
    // 权限指令，支持多个权限指令，多个权限指令使用数组
    const actionName = typeof binding.value == 'string' ? [binding.value] : binding.value
    // 所有的角色，以及角色拥有的权限指令
    const roles = store.getters.roles
    // 当前页面拥有的权限指令
    // const permissionList = vnode.context.$route.meta.permission
    // const permissionId = elVal instanceof String && [elVal] || elVal
    const authArray = []
    for (const action of actionName) {
      let hasAuth = false
      for (const role of roles) {
        if(role.permissionList.indexOf(action) > -1){
          hasAuth = true
          break
        }
      }
      authArray.push(hasAuth)
    }
    if(logic == 'and'){
      // and，不全为true，则表示没有权限
      if(authArray.filter(item => item).length < authArray.length){
        // 没有权限则删除该组件，或者隐藏
        el.parentNode && el.parentNode.removeChild(el) || (el.style.display = 'none')
      }
    }else{
      // or，不包含true，则表示没有权限
      if(authArray.filter(item => item).length == 0){
        el.parentNode && el.parentNode.removeChild(el) || (el.style.display = 'none')
      }
    }
  }
})

export default action
