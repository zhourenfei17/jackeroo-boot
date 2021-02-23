<template>
  <a :class="linkClass" :disabled="disabled" :href="href" v-bind="$attrs" @click="handleClick">
    <a-icon v-if="icon" :type="icon"></a-icon>
    <span v-if="$slots.default" :class="icon ? 'jackeroo-link-icon-span' : undefined"><slot /></span>
  </a>
</template>

<script>
export default {
  name: 'JLink',
  props: {
    // 类型，可选值[primary, success, warning, error, info]
    type: {
      type: String,
      default: 'primary'
    },
    // 是否禁用
    disabled: {
      type: Boolean,
      default: false
    },
    // 是否显示下划线
    underline: {
      type: Boolean,
      default: false
    },
    // 原生href属性
    href: {
      type: String
    },
    // 图标类名
    icon: {
      type: String
    },
    // 大小，可选值[default, small, large]
    size: {
      type: String,
      default: 'default'
    }
  },
  data(){
    return {
      typeArray: ['primary', 'success', 'warning', 'error', 'info'],
      sizeArray: ['default', 'small', 'large']
    }
  },
  computed: {
    selfType(){
      if(this.typeArray.indexOf(this.type) == -1){
        return 'primary'
      }else{
        return this.type
      }
    },
    selfSize(){
      if(this.sizeArray.indexOf(this.size) == -1){
        return 'default'
      }else{
        return this.size
      }
    },
    linkClass(){
      let linkClass = `jackeroo-link-${this.selfType} jackeroo-link-size-${this.selfSize}`
      if(this.underline){
        linkClass += ' jackeroo-link-underline'
      }

      return linkClass
    }
  },
  methods: {
    handleClick(){
      this.$emit('click')
    }
  }
}
</script>

<style lang="less" scoped>
@import '~ant-design-vue/dist/antd.less';

@success-5: color(~`colorPalette('@{success-color}', 5) `);
@warning-5: color(~`colorPalette('@{warning-color}', 5) `);
@error-5: color(~`colorPalette('@{error-color}', 5) `);
@info-color: #909399;
@info-5: color(~`colorPalette('@{info-color}', 5) `);

.jackeroo-link-primary {
  color: @primary-color;
}
.jackeroo-link-primary:hover {
  color: @primary-5;
}

.jackeroo-link-success {
  color: @success-color;
}
.jackeroo-link-success:hover {
  color: @success-5;
}

.jackeroo-link-warning {
  color: @warning-color;
}
.jackeroo-link-warning:hover {
  color: @warning-5;
}

.jackeroo-link-error {
  color: @error-color;
}
.jackeroo-link-error:hover {
  color: @error-5;
}

.jackeroo-link-info {
  color: @info-color;
}
.jackeroo-link-info:hover {
  color: @info-5;
}

.jackeroo-link-size-default{
  font-size: @font-size-base;
}
.jackeroo-link-size-small{
  font-size: @font-size-sm;
}
.jackeroo-link-size-large{
  font-size: @font-size-lg;
}

.jackeroo-link-underline:hover{
  text-decoration: underline;
}

.jackeroo-link-icon-span{
  margin-left: 5px;
}
</style>