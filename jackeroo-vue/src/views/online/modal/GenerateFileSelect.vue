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
          <a-col :span="rowSpan">
            <a-form-model-item label="是否覆盖" prop="override">
              <a-switch v-model="form.override" checkedChildren="是" unCheckedChildren="否" defaultChecked></a-switch>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="需要生成的代码" prop="templateType">
              <j-dict-select v-model="form.templateType" dictCode="GEN_TEMPLATE_FILE_TYPE" type="checkbox" ></j-dict-select>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </j-spin>
  </j-modal>
</template>

<script>
import { postAction } from '@/api/manage'
import { JackerooFormMixins } from '@/mixins/JackerooFormMixins'
import { FileSelector, JDictSelect } from '@/components'

export default {
  mixins: [JackerooFormMixins],
  components:{
    FileSelector,
    JDictSelect
  },
  data(){
    return {
      title: '生成代码',
      width: '40vw',
      form: {
        outputDir: '',
        override: true,
        templateType: ['controller', 'service', 'mapper', 'entity', 'vue']
      },
      id: null,
      rules: {
        outputDir: [
          {required: true, message: '请填写代码生成路径'},
          {max: 30, message: '长度需要在0到30之间'}
        ],
        templateType: [
          {required: true, message: '请选择需要生成的代码'}
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
          const formData = {...this.form}

          this.$loading.show()
          postAction(this.url.generateCode, {
            id: this.id, 
            outputDir: formData.outputDir, 
            override: formData.override ? 1 : 0,
            templateType: formData.templateType
          }).then(result => {
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
    cancel(){
      this.visible = false
      this.flag.add = false
      this.flag.edit = false
      this.flag.view = false

      if(this.$refs.formModel){
        this.$refs.formModel.clearValidate()
      }
      this.loading = true
    },
  }
}
</script>
