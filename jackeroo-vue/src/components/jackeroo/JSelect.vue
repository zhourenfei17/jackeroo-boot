<template>
  <a-select :value="getValueSting" v-bind="_attrs" @change="onChange" :getPopupContainer = "(target) => target.parentNode">
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
</template>

<script>
import {getAction} from '@/api/manage'

export default {
  name: 'JSelect',
  props: {
    url: {
      type: String,
      required: false,
      default: ''
    },
    list: {
      type: Array,
      required: false,
      default: () => {
        return []
      }
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
    value:{
      type: [String, Number],
      required: false
    },
  },
  data(){
    return {
      data: []
    }
  },
  computed: {
    _attrs() {
      let attrs = { ...this.$attrs }
      return attrs
    },
    getValueSting(){
      return this.value != null ? this.value.toString() : undefined;
    },
  },
  created(){
    // 如果url存在，则通过url读取数据；如果list存在，则直接使用
    if(this.url){
      this.loadUrl()
    }else if(this.list && this.list.length > 0){
      this.data = this.list
    }
  },
  methods: {
    loadUrl(){
      getAction(this.url, {}).then(res => {
        if(res.code == 0){
          this.data = res.data
        }
      })
    },
    onChange(value){
      this.$emit('input', value)
    }
  }
}
</script>

<style lang="less" scoped>
  .unselect{
    color:#ccc;
  }
</style>