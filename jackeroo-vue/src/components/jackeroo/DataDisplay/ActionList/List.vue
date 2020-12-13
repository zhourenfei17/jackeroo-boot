<script>
import { filterEmpty } from '@/components/_util/util'
import MenuItemList from './ActionMenuList'
import { hasPermissions } from '@/utils/util'

//ant-design-vue的Divider已经注册为全局组件，因此此处不需要再单独引用，否则需要手动import
const ActionList = {
  MenuItemList,
  name: 'ActionList',
  methods: {
    filterItem(items){
      const list = []
      for (const item of items) {
        if(item.componentOptions.tag === 'action-menu-list'){
          list.push(item)
        }else if(item.data.directives){
          // 若果当前Node的v-show=false，或者v-action无权限时，此处也会获取到，因此需要过滤掉；而v-if=false则不会出现该情况
          let isHide = item.data.directives.some((action) => {
            let showFlag = action.name == 'show' && action.value == false
            if(showFlag){
              return true
            }else{
              let actionFlag = action.name == 'action' && hasPermissions(action.value, action.arg)
              return !actionFlag
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
    const itemList = this.filterItem(filterEmpty(this.$slots.default))
    const actionList = []
    for (let i = 0; i < itemList.length; i++) {
      if(itemList[i].componentOptions.tag === 'action-menu-list'){
        const dropdownItems = this.filterItem(filterEmpty(itemList[i].componentOptions.children))
        if(dropdownItems.length > 0){
          actionList.push((<a-divider type="vertical" />))
          actionList.push(itemList[i])
        }
      }else{
        if(i > 0){
          actionList.push((<a-divider type="vertical" />))
        }
        actionList.push(itemList[i])
      }
    }
    // console.log('actionList', actionList)
    /* for(var i = 0; i < items.length; i++){
      // 若果当前Node的v-show=false，或者v-action无权限时，此处也会获取到，因此需要过滤掉；而v-if=false则不会出现该情况
      let nodeShow = false
      if(items[i].data.directives){
        nodeShow = items[i].data.directives.some((item) => {
          let showFlag = item.name == 'show' && item.value == false
          if(showFlag){
            return true
          }else{
            let actionFlag = item.name == 'action' && hasPermissions(item.value, item.arg)
            return !actionFlag
          }
        })
      }else{
        if(items[i].componentOptions.tag === 'action-menu-list'){
          const dropdownItems = filterEmpty(items[i].componentInstance.$slots.default)

          for (const dropdownItem of dropdownItems) {
            if(dropdownItem.data.directives){

            }
          }
        }
      }
      if(!nodeShow){
        itemsList.push(items[i])
      }
      if(!nodeShow && i != items.length - 1){
        itemsList.push((<a-divider type="vertical" />))
      }
    } */
    return (
      <div>
        {actionList}
      </div>
    )
  }
}

ActionList.install = function (Vue) {
  Vue.component(ActionList.name, ActionList);
  Vue.component(ActionList.MenuItemList.name, ActionList.MenuItemList);
};
export default ActionList;
</script>