<template>
  <j-select v-bind="$attrs" :value="value" :url="url.getDictItemByDictCode" :param="{dictCode: dictCode}" valueField="value" textField="label" @input="handleChange">

  </j-select>
</template>

<script>
import {JSelect} from '@/components'

export default {
  // 数据字典选择器
  name: 'JDictSelector',
  props: {
    // 类型，支持['select', 'check']
    type: {
      type: String,
      default: 'select'
    },
    // 字典code
    dictCode: {
      type: String,
      required: true
    },
    value:{
      type: [String, Number],
      required: false
    },
  },
  components: {
    JSelect
  },
  data() {
    return {
      url: {
        getDictItemByDictCode: '/system/dict/getDictItemList'
      }
    }
  },
  methods: {
    handleChange(value){
      this.$emit('input', value)
    }
  }
}
</script>