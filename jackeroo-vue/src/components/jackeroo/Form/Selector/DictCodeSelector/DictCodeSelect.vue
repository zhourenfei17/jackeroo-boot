<template>
  <span>
    <a-input v-bind="$attrs" readOnly :value="text" @mouseenter="showClose" @mouseleave="hideClose">
      <span v-if="!disabled" slot="addonAfter" @click="showDictCodeSelectorModal" class="j-input-btn">
        <a-icon type="select"></a-icon>
      </span>

      <a-icon type="close-circle" v-if="allowClear" slot="suffix" :class="closeClass" @click="clearIcon" @mouseenter="showClose" @mouseleave="hideClose"></a-icon>
    </a-input>

    <dict-code-select-modal ref="dictCodeSelectModal" @ok="handleDictCodeSelectOk"></dict-code-select-modal>
  </span>
</template>

<script>
import DictCodeSelectModal from './DictCodeSelectModal'
import {getAction} from '@/api/manage';

export default {
  name: 'JDictCodeSelect',
  props:{
    disabled: {
      type: Boolean,
      default: false
    },
    value: String,
    allowClear: {
      type: Boolean,
      default: true
    }
  },
  data(){
    return {
      text: '',
      dictCode: null,
      closeClass: 'icon-hide',
    }
  },
  watch: {
    value(val){
      if(!val){
        this.text = ''
      }
      this.handleChange()
    }
  },
  mounted(){
    if(this.value){
      getAction('/system/dict/getByDictCode', {dictCode: this.value}).then(result => {
        if(!result.code){
          this.dictCode = result.data.dictCode
          this.text = result.data.dictName
        }
      })
    }
  },
  components: {
    DictCodeSelectModal
  },
  methods: {
    showDictCodeSelectorModal(){
      this.$refs.dictCodeSelectModal.show()
    },
    handleDictCodeSelectOk(row){
      if(row){
        this.text = row.dictName
        this.dictCode = row.dictCode
        this.$emit('input', row.dictCode)
      }else{
        this.text = ''
        this.dictCode = null
        this.$emit('input', null)
      }
    },
    handleChange(){
      this.$emit('change', {dictCode: this.value, dictName: this.text})
    },
    clearIcon(){
      this.$emit('input', null)
    },
    showClose(){
      if(this.value){
        this.closeClass = 'icon-close'
      }
    },
    hideClose(){
      this.closeClass = 'icon-hide'
    }
  }
}
</script>

<style lang="less" scoped>
.j-input-btn{
  cursor: pointer;
  
  .j-input-btn-title{
    padding-left: 6px;
  }
}

.icon-hide{
  display: none;
}
.icon-close{
  color: #D3D3D3;
}
.icon-close:hover{
  color: #696969;
}
</style>