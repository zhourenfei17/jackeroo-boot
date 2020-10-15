import {validtedUnique} from '@/api/manage'

export const ValidatorMixins = {
  methods: {
    // 验证手机号
    validMobile(rule, value, callback){
      if(!value || /^1[3456789]\d{9}$/.test(value)){ 
        callback()
      }else{
        callback('请输入正确的手机号')
      }
    },
    // 验证电话号码
    validPhone(rule, value, callback){
      if(!value || /^((0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?)$/.test(value)){
        callback()
      }else{
        callback('请输入正确的电话号')
      }
    },
    // 验证身份证号
    validIdNumber(rule, value, callback){
      if(!value || /^[1-9]\d{7}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}$|^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{3}([0-9]|X|x)$/.test(value)){
        callback()
      }else{
        callback('请输入正确的身份证号')
      }
    },
    // 验证邮政编码
    validPostCode(rule, value, callback){
      if(!value || /[1-9]{1}(\d+){5}/.test(value)){
        callback()
      }else{
        callback('请输入正确的邮政编码')
      }
    },
    // 唯一性校验
    /**
     * rule 参数详解 
     *    tableName： (String)          指定数据库表名，默认读取form页面的tableName属性
     *    columnName: (String)          指定数据库表中的字段列/实体类中的字段，默认为rules中的propertyName
     *    dataId:     (String|Number)   当编辑状态时，需要排除当前数据的id，默认为form页面的form.id
     *    condition:  (String)          指定额外的唯一性条件，需要符合sql语法，例如： type=1 and del_flag=0
     *    param:      (Object)          当condition中包含非固定参数时使用，例如：condition='type=1 and status = #{status}',param={status: 1}
     * @param {*} rule 
     * @param {*} value 
     * @param {*} callback 
     */
    validUnique(rule, value, callback){
      if(!value){
        callback()
        return
      }
      var params = {
        tableName: rule.tableName ? rule.tableName : this.tableName,
        columnName: rule.columnName ? rule.columnName : rule.field, //接口会进行去驼峰处理
        dataValue: value,
        dataId : rule.dataId ? rule.dataId : this.form.id
      }
      if(rule.condition){
        params.condition = rule.condition
        if(rule.param){
          params.param = JSON.stringify(rule.param)
        }
      }
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