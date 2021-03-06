import {Spin} from 'ant-design-vue'
import './style.less'

//已弃用，转移至./Spin.vue
//全局建议使用JSpin替代Spin，这样可以统一的设置Loading的样式
export default{
  name: 'JSpin',
  props: Object.assign({}, Spin.props, {
    wrapperClassName: {
      type: String,
      default: 'jackeroo-spin'
    },
    // 新增属性，加载动画消失时的延迟时间
    /* delayHidden: {
      type: Number,
      default: 200
    }, */
    delay: {
      type: Number,
      default: 200
    }
  }),
  computed: {
    loadingTransitionStatus(){
      return this.$data.sSpinning
    },
  },
  watch: {
    loadingTransitionStatus(newVal){
      if(!newVal){
        console.log('hidden1', new Date().getTime())
        setTimeout(() => {
          console.log('hidden2', new Date().getTime())
          this.spinClassName = this.wrapperClassName
        }, 400);
      }else{
        console.log('show')
        this.spinClassName = (this.wrapperClassName || '') + ' jackeroo-spin-loading'
      }
    }
  },
  /* watch: {
    loadingTransitionStatus(newStatus){
      if(!newStatus){
        if(this.delayHidden > 0){
          setTimeout(() => {
            this.loadingStatus = newStatus
          }, this.delayHidden);
          return
        }
      }
      this.loadingStatus = newStatus
    }
  }, */
  data(){
    return {
      loadingStatus: true,
      spinClassName: ''
    }
  },
  mixins: [Spin],
  render(h) {
    const style = {
      width: '150px',
      height: '150px',
      transform: 'translate(-50%, 0)'
    }
    let src = require('../../../assets/loading_car.gif')

    const items = this.$slots.default
    return (
      <a-spin spinning={this.loadingTransitionStatus} wrapperClassName={this.spinClassName} delay={this.delay}>
        <img slot="indicator" style={style} src={src}></img>
        {items}
      </a-spin>
    )
  },
}