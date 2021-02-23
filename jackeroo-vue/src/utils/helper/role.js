import { hasRoles } from '@/utils/util';


function plugin (Vue) {
  if (plugin.installed) {
    return
  }
  /**
   * $role插件
   * @param {String|Array} roles 角色编码，多个用数组传递
   * @param {String} logic 多角色编码策略，可选 and、or，默认为and
   * 示例：
   * <a-button v-if="$role('admin')">新增</a-button>
   * <a-button v-if="$role(['admin', 'super_admin'])">新增</a-button>
   * <a-button v-if="$role(['admin', 'super_admin'], 'or')">新增</a-button>
   */
  !Vue.prototype.$role && Object.defineProperties(Vue.prototype, {
    $role: {
      get() {
        return (roles, logic) => {
          return hasRoles(roles, logic)
        }
      }
    }
  })

}

export default plugin
