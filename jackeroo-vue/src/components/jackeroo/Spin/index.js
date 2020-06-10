import {Spin} from 'ant-design-vue'
import './style.less'

Spin.setDefaultIndicator({
  indicator: {
    render: function(h) {
      return h('img', {
        style :{
          width: '150px',
          height: '150px',
          transform: 'translate(-50%, 0)'
        },
        attrs: {
          src: require('../../../assets/loading_car.gif')
        }
      })
    },
  },
});

//全局建议使用JSpin替代Spin，这样可以统一的设置Loading的样式
export default{
  name: 'JSpin',
  props: Object.assign({}, Spin.props, {
    wrapperClassName: {
      type: String,
      default: 'jackeroo-spin'
    }
  }),
  mixins: [Spin],
  methods: {
    /* indicator: {
      render() {
        return `<img src="loadImg" />`
      },
    }, */
  },
}