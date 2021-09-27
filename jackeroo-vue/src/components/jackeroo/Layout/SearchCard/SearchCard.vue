<template>
  <a-card :bordered="false" :bodyStyle="bodyStyle">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="enter">
          <a-row :gutter="gutter">
            <slot></slot>

            <slot name="more" v-if="$slots.more && advanced"></slot>

            <a-col v-bind="advanced ? slave : master" v-if="$slots.operate">
              <div class="table-page-search-submitButtons">
                <slot name="operate"></slot>

                <a @click="toggleAdvanced" v-if="$slots.more">
                  {{ advanced ? '收起' : '展开' }}
                  <a-icon :type="advanced ? 'up' : 'down'"/>
                </a>
              </div>
            </a-col>
          </a-row>
        </a-form>
        
      </div>
  </a-card>
</template>

<script>
export default {
  name: 'SearchCard',
  props: {
    // 栅格间距
    gutter:{
      type: [Number, Object, Array],
      default: 48
    },
    enter: {
      type: Function,
      default: () => {

      }
    }
  },
  data(){
    return {
      bodyStyle: {
        paddingBottom: '0px'
      },
      // 展开/关闭
      advanced: false
    }
  },
  computed:{
    master(){
      const slotDefault = this.$slots.default
      return this.lookAndComputeLayout(slotDefault)
    },
    slave(){
      const more = [...this.$slots.default, ...this.$slots.more]
      if(more){
        return this.lookAndComputeLayout(more)
      }else{
        return null
      }
    }
  },
  methods: {
    // 查询 展开/收起
    toggleAdvanced(){
      this.advanced = !this.advanced
    },
    lookAndComputeLayout(slotList){
      const props = {}
      for(const slot of slotList){
        for(let prop in slot.componentOptions.propsData){
          props[prop] = (props[prop] || 0) + slot.componentOptions.propsData[prop]
        }
      }
      for(let prop in props){
        let span = 24 - (props[prop] % 24)
        props[prop] = span < 4 ? 24 : span
      }
      return props
    }
  }
}
</script>
