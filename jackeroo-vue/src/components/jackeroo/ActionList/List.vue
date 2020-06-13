<script>
import { filterEmpty } from '@/components/_util/util'
import MenuItemList from './ActionMenuList'

//ant-design-vue的Divider已经注册为全局组件，因此此处不需要再单独引用，否则需要手动import
const ActionList = {
  MenuItemList,
  name: 'ActionList',
  render(){
    const items = filterEmpty(this.$slots.default)
    const itemsList = []
    for(var i = 0; i < items.length; i++){
      itemsList.push(items[i])

      // 若果当前Node的v-show=false，此处也会获取到，因此需要过滤掉；而v-if=false则不会出现该情况
      let nodeShow = false
      if(items[i].data.directives){
        nodeShow = items[i].data.directives.some((item) => {
          return item.name == 'show' && item.value == false
        })
      }

      if(!nodeShow && i != items.length - 1){
        itemsList.push((<a-divider type="vertical" />))
      }
    }
    return (
      <div>
        {itemsList}
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