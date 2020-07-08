<template>
  <a-select :value="getValueSting" v-bind="_attrs" @change="onChange" :getPopupContainer = "(target) => target.parentNode">
    <a-select-option :value="undefined" class="unselect" style="color:#ccc;">
      <span style="display: inline-block;width: 100%;color: rgb(197, 197, 197);">
        请选择
      </span>
    </a-select-option>
    <a-select-option v-for="(item,index) in list" :key="index" :value="item.value">
      <span style="display: inline-block;width: 100%" :title="item.text">
        {{ item.text}}
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
      list: []
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
    if(this.url){
      this.loadUrl()
    }
  },
  methods: {
    loadUrl(){
      getAction(this.url, {}).then(res => {
        if(res.code == 0){
          const list = []
          for(var row of res.data){
            list.push({
              value: row[this.valueField],
              text: row[this.textField]
            })
          }
          this.list = list
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