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
            <a-form-model-item label="字典名称" prop="dictName">
              <a-input v-model="form.dictName" placeholder="请输入字典名称" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="字典编码" prop="dictCode">
              <a-input v-model="form.dictCode" placeholder="请输入字典编码" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="备注" prop="remark">
              <a-input v-model="form.remark" placeholder="请输入备注" :disabled="flag.view"></a-input>
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
      title: '数据字典',
      tableName: 'sys_dict',
      width: '40vw',
      form: {
        id: null,
        dictCode: null,
        dictName: null,
        remark: null,
      },
      rules: {
        dictCode: [
          {required: true, message: '请输入字典编码'},
          {validator: this.validUnique, condition: 'type=0', message: '字典编码不能重复', trigger: 'blur'},
          {max: 30, message: '长度需要在0到30之间'},
        ],
        dictName: [
          {required: true, message: '请输入字典名称'},
          {max: 30, message: '长度需要在0到30之间'},
        ],
        remark: [
          {max: 100, message: '长度需要在0到100之间'},
        ],
      },
      url: {
        getById: '/system/dict/',
        save: '/system/dict/save',
        update: '/system/dict/update'
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
            if(!result.code){
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
