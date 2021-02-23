<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :fullscreen.sync="fullscreen"
    :switchFullscreen="showFullscreenBtn"
    :confirmLoading="loading"
    :disabled="flag.view"
    @ok="handleSubmit"
    @cancel="cancel"
  >
    <j-spin :spinning="loading">
      <a-form-model ref="formModel" :model="form" :rules="rules" v-bind="layout">
        <a-row :gutter="formGutter">
          <a-col :span="rowSpan">
            <a-form-model-item label="权限名称" prop="label">
              <a-input v-model="form.label" placeholder="请输入权限名称" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="权限代码" prop="value">
              <a-input v-model="form.value" placeholder="请输入权限代码" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>
  </j-modal>
</template>

<script>
import {JackerooFormMixins} from '@/mixins/JackerooFormMixins'

export default {
  mixins: [JackerooFormMixins],
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
          {required: true, message: '请输入权限代码'}, 
          {max: 30, message: '长度需要在0到30之间'},
          {validator: this.uniqueValue, trigger: 'blur', message: '该权权限代码已存在'},
          {validator: this.validLetterAndUndlerline}
        ]
      },
      permissionList: [],
      oldValue: '',
      url: {
        
      }
    }
  },
  methods: {
    add(permissionList){
      this.loading = false
      this.permissionList = permissionList
    },
    edit(record, permissionList){
      this.form = JSON.parse(JSON.stringify(record))
      this.oldValue = record.value
      this.permissionList = permissionList
      this.loading = false
    },
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        if(success){
          const formData = JSON.parse(JSON.stringify(this.form))
          
          if(this.flag.add){
            this.$emit('add', formData)
          }else if(this.flag.edit){
            this.$emit('edit', formData, this.oldValue)
          }
          
          this.cancel()
        }
      })
    },
    uniqueValue(rule, value, callback){
      let unique = this.permissionList.some(item => {
        if(this.flag.add){
          return item.value == value
        }else if(this.flag.edit){
          return item.value == value && value != this.oldValue
        }
      })
      if(unique){
        callback(rule.message)
      }else{
        callback()
      }
    }
  }
}
</script>
