<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :fullscreen.sync="fullscreen"
    :switchFullscreen="showFullscreenBtn"
    :confirmLoading="loading"
    :autoHeight="false"
    maxHeight="60vh"
    :disabled="flag.view"
    @ok="handleSubmit"
    @cancel="cancel"
  >
    <j-spin :spinning="loading">
      <a-form-model ref="formModel" :model="form" :rules="rules" v-bind="layout">
        <a-row :gutter="formGutter">
          <a-col :span="rowSpan">
            <a-form-model-item label="密码" prop="password">
              <a-input v-model="form.password" placeholder="请输入密码" type="password"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="确认密码" prop="passwordAgain">
              <a-input v-model="form.passwordAgain" placeholder="请再次输入密码" type="password"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>
  </j-modal>
</template>

<script>
import { getAction, postAction, httpAction, putAction } from '@/api/manage'
import md5 from 'md5'
import {JackerooFormMixins} from '@/mixins/JackerooFormMixins'

export default {
  mixins: [JackerooFormMixins],
  data(){
    return {
      title: '修改密码',
      width: '450px',
      md5Flag: false,
      form: {
        password: undefined,
        passwordAgain: undefined
      },
      rules: {
        password: [{required: true, message: '请输入密码'}],
        passwordAgain: [
          {required: true, message: '请再次输入密码'}, 
          {validator:(rule, value, callback)=>{
            return value != this.form.password ? callback('两次输入的密码不一致') : callback()
          }, trigger: 'blur'}
        ]
      },
      url: {
        changePwd: '/system/user/changeSelfPwd'
      },
      showFullscreenBtn: false
    }
  },
  methods: {
    show(){
      this.visible = true
      this.loading = false
    },
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        if(success){
          this.$loading.show()
          putAction(this.url.changePwd, {pwd: md5(this.form.password)}).then(result => {
            if(!result.code){
              this.$message.success('密码修改成功！')
              this.cancel()
            }
          }).finally(() => {
            this.$loading.hide()
          })
        }
      })
    }
  }
}
</script>
