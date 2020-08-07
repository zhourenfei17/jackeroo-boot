<template>
  <a-drawer
    :visible="visible"
    :maskClosable="maskClosable"
    v-bind="_attrs"
    @close="close">

    <slot></slot>

    <div class="jackeroo-drawer-footer">
      <a-button style="marginRight: 8px;" @click="cancel">
        {{cancelText}}
      </a-button>
      <a-button type="primary" @click="ok">
        {{okText}}
      </a-button>
    </div>

  </a-drawer>
</template>

<script>
export default {
  name: 'JDrawer',
  props: {
    cancelText: {
      type: String,
      default: '取消'
    },
    okText: {
      type: String,
      default: '保存'
    },
    visible: Boolean,
    // 点击遮罩层是否允许关闭
    maskClosable: {
      type: Boolean,
      default: false
    }
  },
  data(){
    return {
      
    }
  },
  computed: {
     _attrs() {
      let attrs = { ...this.$attrs }
      return attrs
    },
    slotsKeys() {
      return Object.keys(this.$slots)
    },
    scopedSlotsKeys() {
      return Object.keys(this.$scopedSlots)
    },
  },
  methods: {
    cancel(){
      this.close()
      this.$emit('close')
    },
    ok(){
      this.$emit('ok')
    },
    close(){
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style lang="less" scoped>
  .jackeroo-drawer-footer{
    position: absolute;
    right: 0;
    bottom: 0;
    width: 100%;
    border-top: 1px solid #e9e9e9;
    padding: 10px 16px;
    background: #fff;
    text-align: right;
    z-index: 1;
  }
</style>