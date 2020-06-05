<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="loading"
    @ok="handleSubmit"
    @cancel="cancel"
  >
    <a-spin :spinning="loading">
      <a-form-model ref="formModel" :model="form" :rules="rules" v-bind="layout">
        <a-form-model-item ref="name" label="姓名" prop="name">
          <a-input v-model="form.name" placeholder="请输入姓名"></a-input>
        </a-form-model-item>
        <a-form-model-item ref="account" label="账号" prop="account">
          <a-input v-model="form.account" placeholder="请输入账号"></a-input>
        </a-form-model-item>
        <a-form-model-item ref="code" label="员工号" prop="code">
          <a-input v-model="form.code" placeholder="请输入员工号"></a-input>
        </a-form-model-item>
        <a-form-model-item ref="password" label="密码" prop="password">
          <a-input v-model="form.password" placeholder="请输入密码" type="password"></a-input>
        </a-form-model-item>
        <a-form-model-item ref="gender" label="性别" prop="gender">
          <a-select v-model="form.gender" placeholder="请选择性别">
            <a-select-option value="">请选择</a-select-option>
            <a-select-option value="1">男</a-select-option>
            <a-select-option value="2">女</a-select-option>
          </a-select>
        </a-form-model-item>
        <a-form-model-item ref="phone" label="手机" prop="phone">
          <a-input v-model="form.phone" placeholder="请输入手机"></a-input>
        </a-form-model-item>
        <a-form-model-item ref="telephone" label="座机" prop="telephone">
          <a-input v-model="form.telephone" placeholder="请输入座机"></a-input>
        </a-form-model-item>
        <a-form-model-item ref="birthday" label="生日" prop="birthday">
          <a-date-picker v-model="form.birthday" placeholder="请选择生日"></a-date-picker>
        </a-form-model-item>
      </a-form-model>
    </a-spin>
  </a-modal>
</template>

<script>
import { getAction, postAction } from '@/api/manage'

export default {
  data(){
    return {
      title: '用户信息',
      width: '50vw',
      visible: false,
      loading: false,
      form: {
        name: '',
        account: '',
        code: ''
      },
      rules: {
        name: [{required: true, message: '请输入姓名'}],
        account: [{required: true, message: '请输入账号'}],
        code: []
      },
      layout: {
        labelCol: {span: 4},
        wrapperCol: {span: 14}
      },
      url: {
        getById: '/user/',
        add: '/user/add'
      }
    }
  },
  methods: {
    add(){
      this.loading = false
      this.$nextTick(() => {
        this.$refs.formModel.resetFields()
      })
      
    },
    edit(id){
      this.loading = true
      this.$nextTick(() => {
        this.$refs.formModel.resetFields()
      })
      getAction(this.url.getById + id).then(result => {
        this.form = result.data
      }).finally(() => {
        this.loading = false
      })
    },
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        if(success){
          postAction(this.url.add, this.form).then(result => {
            if(result.code === 0){
              this.$message.success('保存成功！')
            }else{
              this.$message.error(result.msg)
            }
          })
        }
      })
    },
    cancel(){
      this.visible = false
    }
  }
}
</script>