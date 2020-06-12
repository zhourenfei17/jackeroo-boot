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
      loading: false,
      layout: {
        labelCol: {span: 4},
        wrapperCol: {span: 14}
      },
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
    }
  },
}