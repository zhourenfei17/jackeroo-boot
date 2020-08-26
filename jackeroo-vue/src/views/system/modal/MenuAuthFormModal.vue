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
            <a-form-model-item label="权限名称" prop="label">
              <a-input v-model="form.label" placeholder="请输入权限名称" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="权限标识" prop="value">
              <a-input v-model="form.value" placeholder="请输入权限标识" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>
  </j-modal>
</template>

<script>
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'

export default {
  mixins: [JackerooFromMixins],
  data(){
    return {
      title: '权限信息',
      width: '40vw',
      form: {
        label: '',
        value: '',
      },
      rules: {
        label: [
          {required: true, message: '请输入权限名称'}, 
          {max: 20, message: '长度需要在0到20之间'}
        ],
        value: [
          {required: true, message: '请输入权限标识'}, 
          {max: 30, message: '长度需要在0到30之间'}
        ]
      },
      url: {
        
      }
    }
  },
  methods: {
    add(){
      this.loading = false
    },
    edit(id){
      this.loading = false
    },
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        if(success){
          const formData = JSON.parse(JSON.stringify(this.form))
          
          this.$emit('ok', formData)
          this.visible = false
        }
      })
    },
  }
}
</script>
