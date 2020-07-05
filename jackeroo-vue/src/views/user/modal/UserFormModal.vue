<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :fullscreen.sync="fullscreen"
    :switchFullscreen="showFullscreenBtn"
    :confirmLoading="loading"
    @ok="handleSubmit"
    @cancel="cancel"
  >
    <j-spin :spinning="loading">
      <a-form-model ref="formModel" :model="form" :rules="rules" v-bind="layout">
        <a-row :gutter="24">
          <a-col :span="rowSpan">
            <a-form-model-item label="姓名" prop="name">
              <a-input v-model="form.name" placeholder="请输入姓名" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="账号" prop="account">
              <a-input v-model="form.account" placeholder="请输入账号" :disabled="flag.view || flag.edit"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="员工号" prop="code">
              <a-input v-model="form.code" placeholder="请输入员工号" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan" v-if="flag.add">
            <a-form-model-item label="密码" prop="password">
              <a-input v-model="form.password" placeholder="请输入密码" type="password"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan" v-if="flag.add">
            <a-form-model-item label="确认密码" prop="passwordAgain">
              <a-input v-model="form.passwordAgain" placeholder="请再次输入密码" type="password"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="性别" prop="gender">
              <a-select v-model="form.gender" placeholder="请选择性别" :disabled="flag.view">
                <a-select-option value="">请选择</a-select-option>
                <a-select-option :value="1">男</a-select-option>
                <a-select-option :value="2">女</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="手机" prop="phone">
              <a-input v-model="form.phone" placeholder="请输入手机" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="座机" prop="telephone">
              <a-input v-model="form.telephone" placeholder="请输入座机" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="生日" prop="birthday">
              <a-date-picker v-model="form.birthday" placeholder="请选择生日" valueFormat="YYYY-MM-DD" style="width:100%" :disabled="flag.view"></a-date-picker>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>
  </j-modal>
</template>

<script>
import { getAction, postAction, httpAction } from '@/api/manage'
import md5 from 'md5'
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'

export default {
  mixins: [JackerooFromMixins],
  data(){
    return {
      title: '用户信息',
      tableName: 'sys_user',
      width: '40vw',
      md5Flag: false,
      form: {
        id: null,
        name: '',
        account: '',
        code: '',
        password: '',
        passwordAgain: '',
        gender: undefined,
        phone: '',
        telephone: '',
        birthday: undefined
      },
      rules: {
        name: [{required: true, message: '请输入姓名'}],
        account: [
          {required: true, message: '请输入账号'},
          {validator: this.validUnique, message: '该账号已存在', trigger: 'blur'}
        ],
        code: [],
        password: [{required: true, message: '请输入密码'}],
        passwordAgain: [
          {required: true, message: '请再次输入密码'}, 
          {validator:(rule, value, callback)=>{
            return value != this.form.password ? callback('两次输入的密码不一致') : callback()
          }, trigger: 'blur'}],
        gender: [],
        phone: [
          {required: true, message: '请输入手机'},
          {validator: this.validPhone, trigger: 'blur'},
          {validator: this.validUnique, message: '该手机号已存在', trigger: 'blur'}
        ],
        telephone: [],
        birthday: []
      },
      url: {
        getById: '/system/user/',
        add: '/system/user/add',
        update: '/system/user/update'
      }
    }
  },
  methods: {
    add(){
      this.form.id = null
      this.loading = false
    },
    edit(id){
      getAction(this.url.getById + id).then(result => {
        this.copyProperties(result.data, this.form)
      }).finally(() => {
        this.loading = false
      })
    },
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        if(success){
          const formData = this.form
          if(!this.md5Flag && this.flag.add){
            formData.password = md5(formData.password)
            formData.passwordAgain = md5(formData.passwordAgain)

            this.md5Flag = true
          }
          
          console.log('formData', formData)

          this.$loading.show()
          httpAction(this.requestUrl, formData, this.requestMethod).then(result => {
            if(result.code === 0){
              this.$message.success('保存成功！')
              this.md5Flag = false
              this.cancel()
              this.$emit('ok')
            }else{
              this.$message.error(result.msg)
            }
          }).finally(() => {
            this.$loading.hide()
          })
        }
      })
    },
  }
}
</script>
