<template>
  <a-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="loading"
    @ok="ok"
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
        <a-form-model-item ref="c" label="员工号" prop="code">
          <a-input v-model="form.code" placeholder="请输入员工号"></a-input>
        </a-form-model-item>
      </a-form-model>
    </a-spin>
  </a-modal>
</template>

<script>
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
      }
    }
  },
  methods: {
    ok(){
      this.visible = false
    },
    cancel(){
      this.visible = false
    }
  }
}
</script>