<template>
  <span>
    <a-input disabled :placeholder="placeholder" v-model="value">
      <span v-if="!disabled" slot="addonAfter" @click="showFileSelectorModal" class="j-input-btn">
        <a-icon type="folder-open"></a-icon>
        <span class="j-input-btn-title">浏览</span>
      </span>
    </a-input>

    <file-select-modal ref="selectorModal" @change="handleSelectFile"></file-select-modal>
  </span>
</template>

<script>
import FileSelectModal from './FileSelectModal'

export default {
  name: 'FileSelector',
  components:{
    FileSelectModal
  },
  props: {
    value: String,
    disabled: {
      type: Boolean,
      default: false
    },
    placeholder: String
  },
  methods: {
    showFileSelectorModal(){
      this.$refs.selectorModal.visible = true
    },
    handleSelectFile(selectedKeys){
      this.$emit('input', selectedKeys[0])
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