<template>
  <Component :is="computedComponent" v-bind="props" v-model="selfValue"  v-on="eventsConvert"/>
</template>

<script>
export default {
  name: `DynamicComponent`,
  props: {
    component: {
      required: true,
      type: String
    },
    // 动态属性
    props: {
      default: () => ({}),
      type: Object
    },
    // 动态事件
    events: {
      type: Object,
      default: () => ({})
    },
    value: {
      type: [String, Number, Object]
    }
  },
  data() {
    return {
      computedComponent: null,
      selfValue: null
    };
  },
  computed: {
    //重新构建新的动态事件
    eventsConvert(){
      const events = {}
      const that = this
      for (const event in this.events) {
        events[event] = function() {
          that.$emit(event, arguments)
        }
      }
      return events
    }
  },
  watch: {
    component: {
      immediate: true,
      handler(newComponent) {
        if(newComponent){
          this.computedComponent = () => ({
            component: import(`@/${newComponent}.vue`)
          })
        }
      }
    },
    value(val){
      if(this.selfValue != val){
        this.selfValue = val
      }
    },
    selfValue(val){
      this.$emit('input', val)
    }
  }
};

</script>
