import {ValidatorMixins} from './ValidatorMixins'
import { httpAction } from '@/api/manage'

export const JackerooFormMixins = {
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
      // 表单栅格间距
      formGutter: 24,
      // 表单列数，取值1-4
      formCol: 1,
    }
  },
  computed: {
    requestUrl(){
      if(this.flag.add){
        return this.url.save
      }else if(this.flag.edit){
        return this.url.update
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
    // 非全屏占列数
    realFormCol(){
      if(typeof this.formCol != 'number'){
        return 1
      }else if(this.formCol < 1){
        return 1
      }else if(this.formCol > 4){
        return 4
      }else{
        return this.formCol
      }
    },
    // 全屏占列数
    fullscreenCol(){
      if(this.realFormCol < 3){
        return this.realFormCol * 2
      }else{
        return 4
      }
    },
    rowSpan(){
      if(!this.fullscreen){
        return 24 / this.realFormCol
      }else{
        return 24 / this.fullscreenCol
      }
    }
  },
  methods: {
    copyProperties(source, target){
      for(var prop in target){
        if(source.hasOwnProperty(prop)){
          if(typeof target[prop] == 'object' && typeof source[prop] != 'object' && target[prop] != null){
            
          }else{
            target[prop] = source[prop]
          }
        }
      }
    },
    cancel(){
      this.visible = false
      this.flag.add = false
      this.flag.edit = false
      this.flag.view = false

      if(this.$refs.formModel){
        this.$refs.formModel.resetFields()
        this.$refs.formModel.clearValidate()
      }
      this.loading = true
    },
    submit(formData){
      return httpAction(this.requestUrl, formData, this.requestMethod)
    }
  },
}