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
        <a-row :gutter="formGutter"><#list columnList as column><#if column.enableForm == 1 && column.primaryKey == 0>
          <a-col :span="rowSpan">
            <a-form-model-item label="${column.dbFieldDesc}" prop="${column.entityFieldName}">
                <#if column.formType == "input">
                    <#if column.entityFieldType == 'Integer' || column.entityFieldType == 'Double' || column.entityFieldType == 'Float' || column.entityFieldType == 'BigDecimal'>
              <a-input-number v-model="form.${column.entityFieldName}" placeholder="请输入${column.dbFieldDesc}" precision="${column.dbFieldDecimal}" :disabled="flag.view"></a-input-number>
                    <#else>
              <a-input v-model="form.${column.entityFieldName}" placeholder="请输入${column.dbFieldDesc}" :disabled="flag.view"></a-input>
                    </#if>
                <#elseif column.formType == 'textarea'>
              <a-textarea v-model="form.${column.entityFieldName}" placeholder="请输入${column.dbFieldDesc}" :disabled="flag.view" :autosize="{minRows: 2, maxRows: 6}" allowClear></a-textarea>
                <#elseif column.formType == 'select || multiple_select || radio || checkbox'>
                    <#if column.formDictCode?? && column.formDictCode != ''>
              <j-dict-select
                v-model="form.${column.entityFieldName}"
                <#if column.formType != 'radio' && column.formType != 'checkbox'>placeholder="请选择${column.dbFieldDesc}"</#if>
                dictCode="${column.formDictCode}"
                <#if column.formType == 'radio' || column.formType == 'checkbox'>type="${column.formType}"</#if>
                <#if column.formType == 'multiple_select'>multi</#if>
                >
              </j-dict-select>
                    <#elseif column.formType =='select' || column.formType =='multiple_select'>
              <j-select
                v-model="form.${column.entityFieldName}"
                placeholder="请选择${column.dbFieldDesc}"
                url=""
                textField=""
                valueField=""
                <#if column.formType == 'multiple_select'>multi</#if>
                >
              </j-select>
                    </#if>
                <#elseif column.formType == 'date || datetime'>
                </#if>
            </a-form-model-item>
          </a-col></#if> </#list>
        </a-row>
      </a-form-model>
    </j-spin>
  </j-modal>
</template>

<script>
import { getAction, httpAction } from '@/api/manage'
import {JackerooFormMixins} from '@/mixins/JackerooFormMixins'

export default {
  mixins: [JackerooFormMixins],
  data(){
    return {
      title: '${table.comment}',
      <#if existUnqiue>tableName: '${table.tableName}',</#if>
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
              <#if column.formRequired == 0 && column.formType != 'input'>
        ${column.entityFieldName}: []<#if column_index < (columnList?size - 1)>,</#if>
              <#else>
        ${column.entityFieldName}: [
                <#if column.formRequired == 1>
          {required: true, message: '请输入${column.dbFieldDesc}'},
                </#if>
                <#if column.formType == 'input' || column.formType == 'textArea'>
          {max: ${column.dbFieldLength}, message: '长度需要在0到${column.dbFieldLength}之间'<#if column.entityFieldType == 'Integer' || column.entityFieldType == 'Double' || column.entityFieldType == 'Float' || column.entityFieldType == 'BigDecimal'>, type: 'number'</#if>},
                </#if>
                <#if column.formValidator?? && column.formValidator != ''>
                    <#if column.formValidator?contains('validUnique')>
          {validator: this.validUnique, message: '${column.dbFieldDesc}不能重复', trigger: 'blur'},
                    </#if>
                    <#if column.formValidator?contains('validMobile')>
          {validator: this.validMobile},
                    </#if>
                    <#if column.formValidator?contains('validPhone')>
          {validator: this.validPhone},
                    </#if>
                    <#if column.formValidator?contains('validPostCode')>
          {validator: this.validPostCode},
                    </#if>
                    <#if column.formValidator?contains('validIdNumber')>
          {validator: this.validIdNumber},
                    </#if>
                    <#if column.formValidator?contains('validUrl')>
          {validator: this.validUrl},
                    </#if>
                    <#if column.formValidator?contains('validEmail')>
          {validator: this.validEmail},
                    </#if>
                </#if>
        ]<#if column_index < (columnList?size - 1)>,</#if>
              </#if>
            </#if>
        </#list>
      },
      url: {
        getById: '/${module.code}/${pathName}/',
        save: '/${module.code}/${pathName}/save',
        update: '/${module.code}/${pathName}/update'
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
          const formData = {...this.form}
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
