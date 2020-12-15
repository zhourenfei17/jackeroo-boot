<script>
import { filterEmpty } from '@/components/_util/util'
import { hasPermissions, hasRoles } from '@/utils/util'

//ant-design-vue的Dropdown和Menu已经注册为全局组件，因此此处不需要再单独引用，否则需要手动import
export default {
  name: 'ActionMenuList',
  props:{
    title: {
      type: String,
      default: '更多'
    },
  },
  methods: {
    filterItem(items){
      const list = []
      for (const item of items) {
        // 若果当前Node的v-show=false，或者v-action、v-role无权限时，此处也会获取到，因此需要过滤掉；而v-if=false则不会出现该情况
        if(item.data.directives){
          let isHide = item.data.directives.some((action) => {
            let showFlag = action.name == 'show' && action.value == false
            if(showFlag){
              return true
            }else if(action.name === 'action'){
              return !hasPermissions(action.value, action.arg)
            }else if(action.name === 'role'){
              return !hasRoles(action.name, action.arg)
            }
          })
          if(!isHide){
            list.push(item)
          }
        }else{
          list.push(item)
        }
      }
      return list
    }
  },
  render(){
    const items = this.filterItem(filterEmpty(this.$slots.default))
    const itemList = []
    for(var item of items){
      itemList.push((<a-menu-item>{item}</a-menu-item>))
    }

    return (
      <a-dropdown>
        <j-link class="ant-dropdown-link">{this.title}<a-icon type="down" style="margin-left:5px;"/></j-link>
        <a-menu slot="overlay">
          {itemList}
        </a-menu>
      </a-dropdown>
    )
  }
}
</script>