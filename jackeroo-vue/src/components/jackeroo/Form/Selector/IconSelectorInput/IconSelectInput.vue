<template>
  <div>
    <a-input :placeholder="placeholder" v-model="value" readOnly :disabled="disabled" @mouseenter="showClose" @mouseleave="hideClose">
      <span v-if="!value" slot="addonBefore" style="width:15px;display: inline-block;"></span>
      <span v-else slot="addonBefore">
        <icon-font v-if="value.indexOf('icon-') == 0" :type="value"></icon-font>
        <a-icon v-else :type="value"></a-icon>
      </span>

      <a-icon type="close-circle" slot="suffix" :class="closeClass" @click="clearIcon" @mouseenter="showClose" @mouseleave="hideClose"></a-icon>

      <a-icon v-if="disabled" slot="addonAfter" type="setting"/>
      <a-icon v-else slot="addonAfter" type="setting" @click="selectIcons"/>
    </a-input>

    <a-modal :visible="iconVisible" title="选择图标" width="50vw" @cancel="handleIconCancel" @ok="handleIconOk">
      <icon-selector @change="handleIconChange" :value="tempIconValue"></icon-selector>
    </a-modal>
  </div>
</template>

<script>
import {IconSelector} from '@/components'

export default {
  name: 'IconSelectInput',
  props:{
    value: {
      type: String
    },
    disabled: {
      type: Boolean,
      default: false
    },
    placeholder:{
      type: String,
      defalut: '请选择图标'
    }
  },
  components: {
    IconSelector
  },
  data() {
    return {
      iconVisible: false,
      closeClass: 'icon-hide',
      tempIconValue: ''
    }
  },
  watch:{
    value(val, oldVal){
      this.tempIconValue = this.value
    }
  },
  created(){
    this.tempIconValue = this.value
  },
  methods: {
    selectIcons(){
      this.iconVisible = true
    },
    handleIconCancel(){
      this.iconVisible = false
    },
    handleIconOk(){
      this.$emit('input', this.tempIconValue)
      this.iconVisible = false
    },
    handleIconChange(icon){
      this.tempIconValue = icon
    },
    clearIcon(){
      this.$emit('input', '')
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