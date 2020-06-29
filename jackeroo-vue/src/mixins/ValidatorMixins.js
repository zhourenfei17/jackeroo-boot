import {validtedUnique} from '@/api/manage'

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
    },
    // 唯一性校验
    validUnique(rule, value, callback){
      var params = {
        tableName: rule.tableName ? rule.tableName : this.tableName,
        columnName: rule.columnName ? rule.columnName : rule.field, //接口会进行去驼峰处理
        dataValue: value,
        dataId : rule.dataId ? rule.dataId : this.form.id
      };
      validtedUnique(params).then((res) => {
        if (res.data) {
          callback(rule.message)
        } else {
          callback()
        }
      })
    },
  },
}