<template>
  <a-select v-if="type == 'select'" :value="multiple ? arraryValue : realValue" v-bind="$attrs" :mode="multiple ? 'multiple' : 'default'"
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
  <a-checkbox-group :value="arraryValue" v-else-if="type == 'checkbox'" v-bind="$attrs" @change="handleCheckboxChange">
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
    multiple: {
      type: Boolean,
      default: false
    },
    // 多选的情况下，返回的结果是否为一个数组，默认返回String，多个结果之间用,隔开
    multipleArray: {
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
      arraryValue: [],
      valueType: 'string'
    }
  },
  computed: {
    realValue() {
      if(typeof this.value == 'string' && this.valueType == 'number'){
        return Number(this.value)
      }else if(typeof this.value == 'number' && this.valueType == 'string'){
        return String(this.value)
      }
      return this.value
    }
  },
  watch: {
    value(newVal){
      if(this.multipleArray){
        if(this.type == 'checkbox'){
          this.arraryValue = [...this.value]
        }else if(this.type == 'select' && this.multiple){
          this.arraryValue = [...this.value]
        }
      }else{
        if(this.type == 'checkbox'){
          this.arraryValue = this.value.split(',')
        }else if(this.type == 'select' && this.multiple){
          this.arraryValue = this.value.split(',')
        }
      }
    },
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
    handleSelectChange(value){
      if(this.multiple){
        if(this.multipleArray){
          this.$emit('input', value)
        }else{
          this.$emit('input', value.join(','))
        }
      }else{
        this.$emit('input', this.transformResultValue(value))
      }
    },
    handleRadioChange(e){
      this.$emit('input', this.transformResultValue(e.target.value))
    },
    handleCheckboxChange(checkedValue){
      if(this.multipleArray){
        this.$emit('input', checkedValue)
      }else{
        this.$emit('input', checkedValue.join(','))
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