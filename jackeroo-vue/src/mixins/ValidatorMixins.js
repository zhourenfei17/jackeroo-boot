export const ValidatorMixins = {
  methods: {
    // 验证手机号
    validPhone(rule, value, callback){
      if(/^1[3456789]\d{9}$/.test(value)){ 
        callback()
      }else{
        callback('请输入正确的手机号')
      }
    },
    // 验证身份证号
    validIdNumber(rule, value, callback){
      if(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(value)){
        callback()
      }else{
        callback('请输入正确的身份证号')
      }
    },
    // 验证邮政编码
    validPostCode(rule, value, callback){
      if(/[1-9]{1}(\d+){5}/.test(value)){
        callback()
      }else{
        callback('请输入正确的邮政编码')
      }
    }
  },
}