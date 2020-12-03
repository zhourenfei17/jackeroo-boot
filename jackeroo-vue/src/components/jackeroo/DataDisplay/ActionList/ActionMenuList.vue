<script>
import { filterEmpty } from '@/components/_util/util'

//ant-design-vue的Dropdown和Menu已经注册为全局组件，因此此处不需要再单独引用，否则需要手动import
export default {
  name: 'ActionMenuList',
  props:{
    title: {
      type: String,
      default: '更多'
    },
  },
  render(){
    const items = filterEmpty(this.$slots.default)
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