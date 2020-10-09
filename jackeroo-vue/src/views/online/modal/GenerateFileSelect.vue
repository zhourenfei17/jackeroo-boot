<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :fullscreen.sync="fullscreen"
    :switchFullscreen="showFullscreenBtn"
    :confirmLoading="loading"
    okText="生成代码"
    @ok="handleSubmit"
    @cancel="cancel"
  >
    <j-spin :spinning="loading">
      <a-form-model ref="formModel" :model="form" :rules="rules" v-bind="layout">
        <a-row :gutter="formGutter">
          <a-col :span="rowSpan">
            <a-form-model-item label="代码生成路径" prop="outputDir">
              <file-selector v-model="form.outputDir" placeholder="请选择代码生成路径" :disabled="flag.view"></file-selector>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>
  </j-modal>
</template>

<script>
import { getAction } from '@/api/manage'
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'
import {FileSelector} from '@/components'

export default {
  mixins: [JackerooFromMixins],
  components:{
    FileSelector
  },
  data(){
    return {
      title: '生成代码',
      width: '40vw',
      form: {
        outputDir: ''
      },
      id: null,
      rules: {
        outputDir: [
          {required: true, message: '请填写代码生成路径'},
          {max: 30, message: '长度需要在0到30之间'}
        ]
      },
      url: {
        generateCode: '/online/generate/generateCode'
      }
    }
  },
  methods: {
    show(id){
      this.id = id
      this.visible = true
      this.loading = false
    },
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        if(success){
          const formData = this.form

          this.$loading.show()
          getAction(this.url.generateCode, {id: this.id, outputDir: formData.outputDir}).then(result => {
            if(!result.code){
              this.$message.success('生成代码成功')
              this.cancel()
            }else{
              this.$message.error("生成代码失败")
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
