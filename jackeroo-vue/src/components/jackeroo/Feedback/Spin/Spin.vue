<template>
    <a-spin :spinning="loading" :wrapperClassName="spinClassName" v-bind="$attrs">
      <img slot="indicator" class="jackeroo-loading-img" :src="loadingImg" />

      <slot></slot>
    </a-spin>
</template>

<script>
import {Spin} from 'ant-design-vue'

//全局建议使用JSpin替代Spin，这样可以统一的设置Loading的样式
export default {
  name: 'JSpin',
  components: {
    Spin
  },
  props: {
    spinning: {
      tpye: Boolean,
      required: true
    },
    wrapperClassName: {
      type: String,
      default: 'jackeroo-spin'
    },
    /**
     * 原delay属性的意图是防止spinning变化间隔时间太短而引起loading图标的闪烁，
     * 即如果 spinning从true => false的变化间隔时间小于 dealy的时间，则不显示loading图标；
     * ====================================================================
     * 现覆盖原有属性的意图，更改为spinning每次变化都会延迟 delay 的时间后生效，用于延长loading图标的显示，避免闪烁太快的现象，
     * 即如果spinning从true => false至少要 delay ms 后才生效；
     * ====================================================================
     * 由于目前JSpin主要用于JModal等组件，这样每次打开modal会有个loading的动画过程
     * 当 spinning从 true => false，延长loading时间，避免loadding时间过短而出现闪烁的情况
     * 当 spinning从 false => true，延后loading界面出现的时间，目前在关闭modal时，会将loadding立即设为true，如果loading立即生效，会在modal未完全关闭前出现loading界面，而出现闪烁的情况
     */
    delay: {
      type: Number,
      default: 200
    }
  },
  created(){
    this.spinClassName = (this.wrapperClassName || '') + ' jackeroo-spin-loading'
    
    setTimeout(() => {
      this.loading = this.spinning
    }, 300)
  },
  data() {
    return {
      firstLoad: true,
      loadingImg: require('../../../../assets/loading_car.gif'),
      spinClassName: '',
      loading: true
    }
  },
  watch: {
    spinning(val){
      if(!val){
        // 隐藏
        setTimeout(() => {
          this.loading = val
        }, this.delay);
      }else{
        // 显示
        setTimeout(() => {
          this.loading = val
        }, this.delay);
      }
    },
    loading(val){
      if(!val){
        // 隐藏
        this.spinClassName = this.wrapperClassName
      }else{
        // 显示
        this.spinClassName = (this.wrapperClassName || '') + ' jackeroo-spin-loading'
      }
    }
  }
}
</script>

<style lang="less" scoped>
  .jackeroo-loading-img{
    width: 150px;
    height: 150px;
    transform: translate(-50%, -50%);
  }

  .jackeroo-spin{
    opacity: 1.0;

    .ant-spin-blur{
      opacity: 0;
    }
  }

  .jackeroo-spin-loading{
    background-color: #fff;
  }
</style>

<style lang="less">
  .jackeroo-spin{
    .ant-spin-blur{
      opacity: 0;
    }
  }
  .jackeroo-spin-loading{
    .ant-spin{
      background-color: #fff;
    }
  }
</style>