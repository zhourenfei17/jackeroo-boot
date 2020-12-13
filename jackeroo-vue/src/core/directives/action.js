import Vue from 'vue'
import { hasPermissions } from '@/utils/util';

/**
 * Action 权限指令
 * 指令用法：
 *  - 在需要控制 action 级别权限的组件上使用 v-action:[logic]="permission" , 
 * |     名称       |              说明              |     类型       |   默认值  |
 * | ------------- | ------------------------------ | ------------- | -------- |
 * |  logic        | 多权限指令策略，可选and、or       |      -         |   and    |
 * |  permission   | 权限指令                        |  String|Array  |    -    |
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
    if(!hasPermissions(binding.value, binding.arg)){
      el.parentNode && el.parentNode.removeChild(el) || (el.style.display = 'none')
    }
  }
})

export default action
