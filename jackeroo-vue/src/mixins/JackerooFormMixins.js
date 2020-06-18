import {ValidatorMixins} from './ValidatorMixins'

export const JackerooFromMixins = {
  mixins: [ValidatorMixins],
  data(){
    return {
      flag: {
        add: false,
        edit: false,
        view: false
      },
      visible: false,
      // 是否全屏
      fullscreen: false,
      // 是否显示全屏按钮
      showFullscreenBtn: true,
      loading: true,
      layout: {
        labelCol: {span: 6},
        wrapperCol: {span: 16}
      },
      // 表单展示列数
      column: {
        // 普通情况
        normal: 1,
        // 全屏情况
        fullscreen: 2
      },
    }
  },
  computed: {
    requestUrl(){
      if(this.flag.add){
        return this.url.add
      }else if(this.flag.edit){
        return this.url.edit
      }else{
        return ''
      }
    },
    requestMethod(){
      if(this.flag.add){
        return 'POST'
      }else if(this.flag.edit){
        return 'PUT'
      }else{
        return ''
      }
    },
    rowSpan(){
      if(!this.fullscreen){
        return 24 / this.column.normal
      }else{
        return 24 / this.column.fullscreen
      }
    }
  },
  methods: {
    copyProperties(source, target){
      for(var prop in target){
        if(source.hasOwnProperty(prop)){
          target[prop] = source[prop]
        }
      }
    },
    cancel(){
      this.visible = false
      this.flag.add = false
      this.flag.edit = false
      this.flag.view = false

      this.$refs.formModel.resetFields()
      this.$refs.formModel.clearValidate()
      this.loading = true
    }
  },
}