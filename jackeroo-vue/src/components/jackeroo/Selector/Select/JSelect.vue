<template>
  <a-select v-if="type == 'select'" :value="realValue" v-bind="$attrs" :mode="multi ? 'multiple' : 'default'"
    @change="handleSelectChange" :getPopupContainer = "(target) => target.parentNode">
    <a-select-option :value="undefined" class="unselect" style="color:#ccc;">
      <span style="display: inline-block;width: 100%;color: rgb(197, 197, 197);">
        请选择
      </span>
    </a-select-option>
    <a-select-option v-for="(item,index) in data" :key="index" :value="item[valueField]">
      <span style="display: inline-block;width: 100%" :title="item.text">
        {{ item[textField]}}
      </span>
    </a-select-option>
  </a-select>
  <a-radio-group :value="realValue" v-else-if="type == 'radio'" v-bind="$attrs" @change="handleRadioChange">
    <a-radio v-for="(item, index) in data" :key="index" :value="item[valueField]">{{item[textField]}}</a-radio>
  </a-radio-group>
  <a-radio-group :value="realValue" v-else-if="type == 'radioBtn'" v-bind="$attrs" @change="handleRadioChange">
    <a-radio-button v-for="(item, index) in data" :key="index" :value="item[valueField]">{{item[textField]}}</a-radio-button>
  </a-radio-group>
  <a-checkbox-group :value="realValue" v-else-if="type == 'checkbox'" v-bind="$attrs" @change="handleCheckboxChange">
    <a-checkbox v-for="(item, index) in data" :key="index" :value="item[valueField]">{{item[textField]}}</a-checkbox>
  </a-checkbox-group>
</template>

<script>
import {getAction} from '@/api/manage'

export default {
  name: 'JSelect',
  props: {
    // 请求地址，与list二选一
    url: {
      type: String,
      required: false,
      default: ''
    },
    // 数据列表，与url二选一
    list: {
      type: Array,
      required: false,
      default: () => {
        return []
      }
    },
    // 组件类型，支持['select', 'radio', 'checkbox', 'radioBtn']
    type: {
      type: String,
      default: 'select'
    },
    // 是否多选，仅type=='select'下有效
    multi: {
      type: Boolean,
      default: false
    },
    // 对于多选的情况下，在未知value的类型的情况下，默认期望是否返回Array数据类型
    multiArray: {
      type: Boolean,
      default: false
    },
    valueField: {
      type: String,
      required: false,
      default: 'id'
    },
    textField: {
      type: String,
      required: false,
      default: 'name'
    },
    // 请求url所携带的参数，仅在传递url的时候生效
    param: {
      type: Object
    },
    value:{
      type: [String, Number, Array],
      required: false
    },
  },
  data(){
    return {
      data: [],
      valueType: 'string'
    }
  },
  computed: {
    // 参数的数据类型
    paramType(){
      return Array.isArray(this.value) ? 'array' : typeof this.value
    },
    realValue() {
      if(this.value == null || this.value == undefined){
        // 对于多选的情况，如果未传值，则默认为空数组
        if(this.type == 'checkbox' || (this.type == 'select' && this.multi)){
          return []
        }
      }else if(this.paramType == 'string' && this.valueType == 'number'){
        return Number(this.value)
      }else if(this.paramType == 'number' && this.valueType == 'string'){
        return String(this.value)
      }else if(this.type == 'checkbox' || (this.type == 'select' && this.multi)){
        if(this.paramType == 'string'){
          return this.value.split(',')
        }
      }else if(this.paramType == 'array'){
        return [...this.value]
      }
      return this.value
    }
  },
  watch: {
    /* value(newVal){
      if(this.paramType == 'array'){
        if(this.type == 'checkbox'){
          this.arraryValue = [...this.value]
        }else if(this.type == 'select' && this.multi){
          this.arraryValue = [...this.value]
        }
      }else{
        if(this.type == 'checkbox'){
          this.arraryValue = this.value.split(',')
        }else if(this.type == 'select' && this.multi){
          this.arraryValue = this.value.split(',')
        }
      }
    }, */
    list(newVal){
      if(newVal && newVal.length > 0){
        this.data = this.list
        if(this.data.length > 0){
          this.valueType = typeof this.data[0][this.valueField]
        }
      }
    },
    url(newVal){
      if(newVal){
        this.loadUrl()
      }
    }
  },
  beforeMount(){
    // 如果url存在，则通过url读取数据；如果list存在，则直接使用
    if(this.url){
      this.loadUrl()
    }else if(this.list && this.list.length > 0){
      this.data = this.list
      if(this.data.length > 0){
        this.valueType = typeof this.data[0][this.valueField]
      }
    }
  },
  methods: {
    loadUrl(){
      getAction(this.url, this.param || {}).then(res => {
        if(res.code == 0){
          this.data = res.data

          if(res.data.length > 0){
            this.valueType = typeof res.data[0][this.valueField]
          }
        }
      })
    },
    // select
    handleSelectChange(value){
      if(this.multi){
        // 如果是多选，根据value的数据类型返回对应的数据类型格式，string类型的多个值用,隔开
        // 如果传入的值是null或者undefined，无法判断其类型，则根据multiArray属性来返回对应类型的结果值
        if(!this.value){
          if(this.multiArray){
            this.$emit('input', value)
          }else{
            this.$emit('input', value.join(','))
          }
        }else if(this.paramType == 'array'){
          this.$emit('input', value)
        }else{
          if(value.length > 0){
            this.$emit('input', value.join(','))
          }else{
            this.$emit('input', null)
          }
        }
      }else{
        this.$emit('input', this.transformResultValue(value))
      }
    },
    handleRadioChange(e){
      this.$emit('input', this.transformResultValue(e.target.value))
    },
    // checkbox，根绝value的类型返回对应的数据类型，string类型的多个值用,隔开
    handleCheckboxChange(checkedValue){
      if(!this.value){
        // 如果传入的值是null或者undefined，无法判断其类型，则根据multiArray属性来返回对应类型的结果值
        if(this.multiArray){
          this.$emit('input', checkedValue)
        }else{
          this.$emit('input', checkedValue.join(','))
        }
      }else if(this.paramType == 'string'){
        if(checkedValue.length > 0){
          this.$emit('input', checkedValue.join(','))
        }else{
          this.$emit('input', null)
        }
      }else{
        this.$emit('input', checkedValue)
      }
    },
    transformResultValue(value){
      if(value != null && value != undefined){
        if(typeof this.value == 'string'){
          return String(value)
        }else if(typeof this.value == 'number'){
          return Number(value)
        }
      }
      return value
    }
  }
}
</script>

<style lang="less" scoped>
  .unselect{
    color:#ccc;
  }
</style>