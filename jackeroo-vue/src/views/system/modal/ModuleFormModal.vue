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
            <a-form-model-item label="模块名称" prop="name">
              <a-input v-model="form.name" placeholder="请输入模块名称" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="模块代码" prop="code">
              <a-input v-model="form.code" placeholder="请输入模块代码" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="排序号" prop="sort">
              <a-input-number v-model="form.sort" placeholder="请输入排序号" :disabled="flag.view" style="width: 100%;"></a-input-number>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="描述" prop="remark">
              <a-input v-model="form.remark" placeholder="请输入描述" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>
  </j-modal>
</template>

<script>
import { getAction, postAction, httpAction } from '@/api/manage'
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'

export default {
  mixins: [JackerooFromMixins],
  data(){
    return {
      title: '模块信息',
      tableName: 'sys_module',
      width: '40vw',
      form: {
        id: null,
        name: '',
        code: '',
        sort: null,
        remark: ''
      },
      rules: {
        name: [
          {required: true, message: '请输入模块名称'}, 
          {max: 20, message: '长度需要在0到20之间'}
        ],
        code: [
          {required: true, message: '请输入模块代码'}, 
          {validator: this.validUnique, message: '模块代码不能重复', trigger: 'blur'},
          {max: 50, message: '长度需要在0到50之间'}
        ],
        sort: [],
        remark: [{max: 100, message: '长度需要在0到100之间'}],
      },
      url: {
        getById: '/system/module/',
        save: '/system/module/save',
        update: '/system/module/update'
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
          console.log('formData', formData)

          this.$loading.show()
          httpAction(this.requestUrl, formData, this.requestMethod).then(result => {
            if(result.code === 0){
              this.$message.success('保存成功！')
              this.cancel()
              this.$emit('ok')
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
