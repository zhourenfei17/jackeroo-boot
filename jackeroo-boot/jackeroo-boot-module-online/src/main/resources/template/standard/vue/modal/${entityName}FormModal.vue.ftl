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
        <a-row :gutter="formGutter"><#list columnList as column>
            <#if column.enableForm == 1 && column.primaryKey == 0>
          <a-col :span="rowSpan">
            <a-form-model-item label="${column.dbFieldDesc}" prop="${column.entityFieldName}">
                <#if column.formType == "input">
              <a-input v-model="form.${column.entityFieldName}" placeholder="请输入${column.dbFieldDesc}" :disabled="flag.view"></a-input>
                </#if>
            </a-form-model-item>
          </a-col>
            </#if>
        </#list>
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
      title: '${table.comment}',
      tableName: '${table.tableName}',
      width: '60vw',
      form: {
            <#list columnList as column>
            <#if column.enableForm == 1 || column.primaryKey == 1>
        ${column.entityFieldName}: null<#if column_index < (columnList?size - 1)>,</#if>
            </#if>
            </#list>
      },
      rules: {
        <#list columnList as column>
            <#if column.enableForm == 1 && column.primaryKey == 0>
        ${column.entityFieldName}: [
              <#if column.formRequired == 1>
          {required: true, message: '请输入${column.dbFieldDesc}'},
              </#if>
              <#if column.formType == 'input'>
          {max: ${column.dbFieldLength}, message: '长度需要在0到${column.dbFieldLength}之间'},
              </#if>
        ]<#if column_index < (columnList?size - 1)>,</#if>
            </#if>
        </#list>
      },
      url: {
        getById: '/${module.code}/${varName}/',
        add: '/${module.code}/${varName}/add',
        update: '/${module.code}/${varName}/update'
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
