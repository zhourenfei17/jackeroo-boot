<template>
  <span>
    <a-input disabled v-bind="$attrs" :value="text">
      <span v-if="!disabled" slot="addonAfter" @click="showDictCodeSelectorModal" class="j-input-btn">
        <a-icon type="select"></a-icon>
      </span>
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
  },
  data(){
    return {
      text: '',
      dictCode: null
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
</style>