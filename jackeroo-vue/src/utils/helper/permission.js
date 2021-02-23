import { hasPermissions } from '@/utils/util';

export const PERMISSION_ENUM = {
  'add': { key: 'add', label: '新增' },
  'delete': { key: 'delete', label: '删除' },
  'edit': { key: 'edit', label: '修改' },
  'query': { key: 'query', label: '查询' },
  'get': { key: 'get', label: '详情' },
  'enable': { key: 'enable', label: '启用' },
  'disable': { key: 'disable', label: '禁用' },
  'import': { key: 'import', label: '导入' },
  'export': { key: 'export', label: '导出' }
}

function plugin (Vue) {
  if (plugin.installed) {
    return
  }
  /**
   * $auth插件
   * @param {String|Array} permissions 权限指令，多个用数组传递
   * @param {String} logic 多权限指令策略，可选 and、or，默认为and
   * 示例：
   * <a-button v-if="$auth('system:user:add')">新增</a-button>
   * <a-button v-if="$auth(['system:user:add', 'system:user:insert'])">新增</a-button>
   * <a-button v-if="$auth(['system:user:add', 'system:user:insert'], 'or')">新增</a-button>
   */
  !Vue.prototype.$auth && Object.defineProperties(Vue.prototype, {
    $auth: {
      get () {
        return (permissions, logic) => {
          return hasPermissions(permissions, logic)
        }
      }
    }
  })

  !Vue.prototype.$enum && Object.defineProperties(Vue.prototype, {
    $enum: {
      get () {
        // const _this = this;
        return (val) => {
          let result = PERMISSION_ENUM
          val && val.split('.').forEach(v => {
            result = result && result[v] || null
          })
          return result
        }
      }
    }
  })
}

export default plugin
