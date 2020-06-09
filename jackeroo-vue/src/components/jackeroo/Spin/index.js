import {Spin} from 'ant-design-vue'
import './style.less'

export default{
  name: 'JSpin',
  props: Object.assign({}, Spin.props, {
    wrapperClassName: {
      type: String,
      default: 'jackeroo-spin'
    },
    tip: {
      type: String,
      default: '正在努力加载中...'
    }
  }),
  mixins: [Spin]
}