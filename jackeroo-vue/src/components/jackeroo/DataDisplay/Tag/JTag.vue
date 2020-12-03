<template>
  <span>
    <a-tag :class="'jackeroo-tag-' + selfType">
      {{text}}
    </a-tag>
  </span>
</template>

<script>
export default {
  name: 'JTag',
  props: {
    // 类型，可选值[primary, success, warning, error, info]
    type: {
      type: String,
      default: 'primary'
    },
    text: {
      type: String,
      required: true
    }
  },
  data(){
    return {
      typeArray: ['primary', 'success', 'warning', 'error', 'info'],
      color: {
        success: 'green',
        info: 'blue',
        warning: 'orange',
        error: 'red'
      }
    }
  },
  computed: {
    selfType(){
      if(this.typeArray.indexOf(this.type) == -1){
        return 'primary'
      }else{
        return this.type
      }
    }
  }
}
</script>

<style lang="less" scoped>
@import '~ant-design-vue/dist/antd.less';

@info-color: #909399;
@info-5: color(~`colorPalette('@{info-color}', 5) `);
@info-1: color(~`colorPalette('@{info-color}', 1) `);
@success-5: color(~`colorPalette('@{success-color}', 5) `);
@success-1: color(~`colorPalette('@{success-color}', 1) `);
@warning-5: color(~`colorPalette('@{warning-color}', 5) `);
@warning-1: color(~`colorPalette('@{warning-color}', 1) `);
@error-1: color(~`colorPalette('@{error-color}', 1) `);
@error-5: color(~`colorPalette('@{error-color}', 5) `);

.jackeroo-tag-primary{
  border-color: @primary-5;
  background-color: @primary-1;
  color: @primary-color;
}

.jackeroo-tag-info{
  border-color: @info-5;
  background-color: @info-1;
  color: @text-color;
}

.jackeroo-tag-success{
  border-color: @success-5;
  background-color: @success-1;
  color: @success-color;
}

.jackeroo-tag-warning{
  border-color: @warning-5;
  background-color: @warning-1;
  color: @warning-color;
}

.jackeroo-tag-error{
  border-color: @error-5;
  background-color: @error-1;
  color: @error-color;
}
</style>