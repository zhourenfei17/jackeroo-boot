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
      if(/^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X|x)$/.test(value)){
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
        if(res.code == 0){
          if (res.data) {
            callback()
          } else {
            callback(rule.message)
          }
        }
      })
    },
  },
}