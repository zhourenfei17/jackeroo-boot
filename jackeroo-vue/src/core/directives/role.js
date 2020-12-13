import Vue from 'vue'
import { hasRoles } from '@/utils/util';

/**
 * Role 角色权限指令
 * 指令用法：
 *  - 在需要控制 role 级别权限的组件上使用 v-role:[logic]="role" , 
 * |     名称       |              说明              |     类型       |   默认值  |
 * | ------------- | ------------------------------ | ------------- | -------- |
 * |  logic        | 多角色令策略，可选and、or         |      -         |   and    |
 * |  role         | 角色指令                        |  String|Array  |    -    |
 * 
 * 如下：
 *    <a-button v-role="'admin'" >添加用户</a-button>
 *    <a-button v-role="['admin', 'super_admin']">删除用户</a-button>
 *    <a v-role:or="['admin','super_admin']" @click="edit(record)">修改</a>
 *
 *  - 当前用户没有该角色权限时，组件上使用了该指令则会被隐藏
 *  - 当后台权限跟 pro 提供的模式不同时，只需要针对这里的权限过滤进行修改即可
 *
 */
const role = Vue.directive('role', {
  inserted: function (el, binding, vnode) {
    if(!hasRoles(binding.value, binding.arg)){
      el.parentNode && el.parentNode.removeChild(el) || (el.style.display = 'none')
    }
  }
})

export default role
