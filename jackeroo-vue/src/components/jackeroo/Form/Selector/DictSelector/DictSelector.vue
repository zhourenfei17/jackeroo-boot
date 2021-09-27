<template>
  <j-select v-bind="$attrs" :value="value" :url="url.getDictItemByDictCode" :param="{dictCode: dictCode}" 
    valueField="value" textField="label" @input="handleChange" :type="type" :allowClear="allowClear" v-on="$listeners">

  </j-select>
</template>

<script>
import {JSelect} from '@/components'

export default {
  // 数据字典选择器
  name: 'JDictSelector',
  props: {
    // 类型，支持['select', 'radio', 'radioBtn', 'checkbox']
    type: {
      type: String,
      default: 'select'
    },
    // 字典code
    dictCode: {
      type: String,
      required: true
    },
    // 是否多选，仅type=='select'下有效
    multi: {
      type: Boolean,
      default: false
    },
    value:{
      type: [String, Number, Array],
      required: false
    },
    // select的allowClear属性，改变默认值
    allowClear: {
      type: Boolean,
      default: true
    }
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