<template>
  <a-form-model ref="onlineScheme" :model="formScheme" :rules="schemeRules" v-bind="layout">
    <a-row :gutter="formGutter">
      <a-col :span="rowSpan">
        <a-form-model-item label="生成模板" prop="template">
          <a-select v-model="formScheme.template" placeholder="请选择生成模板">
            <a-select-option value="standard">标准模板</a-select-option>
          </a-select>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan">
        <a-form-model-item label="生成作者" prop="author">
          <a-input v-model="formScheme.author" placeholder="请输入生成作者"></a-input>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan">
        <a-form-model-item label="生成包名" prop="packageName">
          <a-input v-model="formScheme.packageName" placeholder="请输入生成包路径"></a-input>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan">
        <a-form-model-item label="所属模块" prop="moduleId">
          <j-select v-model="formScheme.moduleId" placeholder="请选择所属模块" :url="url.findModuleList"></j-select>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan">
        <a-form-model-item label="生成功能名" prop="funName">
          <a-input v-model="formScheme.funName" placeholder="请输入生成功能名"></a-input>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan">
        <a-form-model-item label="表单风格" prop="formStyle">
          <j-dict-select v-model="formScheme.formStyle" placeholder="请选择表单风格" dictCode="GEN_FORM_STYLE"></j-dict-select>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan">
        <a-form-model-item label="是否显示复选框" prop="showCheckbox">
          <j-dict-select v-model="formScheme.showCheckbox" dictCode="YES_NO" type="radio"></j-dict-select>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan">
        <a-form-model-item label="是否分页" prop="enablePagination">
          <j-dict-select v-model="formScheme.enablePagination" dictCode="YES_NO" type="radio"></j-dict-select>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan">
        <a-form-model-item label="是否生成API文档" prop="enableSwagger">
          <j-dict-select v-model="formScheme.enableSwagger" dictCode="YES_NO" type="radio"></j-dict-select>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan">
        <a-form-model-item label="服务器端校验" prop="enableServerValid">
          <j-dict-select v-model="formScheme.enableServerValid" dictCode="YES_NO" type="radio"></j-dict-select>
        </a-form-model-item>
      </a-col>
    </a-row>
  </a-form-model>
</template>

<script>
import { JSelect, JDictSelect} from '@/components'

export default {
  components: {
    JSelect,
    JDictSelect
  },
  props: ['formScheme', 'layout', 'formGutter', 'rowSpan'],
  data() {
    return {
      schemeRules: {
        template: [
          {required: true, message: '请选择生成模板'}
        ],
        author: [
          {required: true, message: '请填写生成作者'},
          {max: 50, message: '长度需要在0到50之间'}
        ],
        packageName: [
          {required: true, message: '请填写生成包名'},
          {max: 100, message: '长度需要在0到100之间'}
        ],
        moduleId: [
          {required: true, message: '请选择所属模块'}
        ],
        outputDir: [
          {required: true, message: '请填写代码生成路径'},
          {max: 30, message: '长度需要在0到30之间'}
        ],
        showCheckbox: [
          {required: true, message: '请选择是否显示复选框'}
        ],
        enablePagination: [
          {required: true, message: '请选择是否分页'}
        ],
        enableSwagger: [
          {required: true, message: '请选择是否生成API文档'}
        ],
        enableServerValid: [
          {required: true, message: '请选择是否开启服务器端校验'}
        ]
      },
      url: {
        findModuleList: '/system/module/allList'
      }
    }
  },
  methods: {
    validate(callback){
      if(callback && typeof callback == 'function'){
        this.$refs.onlineScheme.validate(callback)
      }else{
        return this.$refs.onlineScheme.validate()
      }
    },
    resetFields(){
      this.$refs.onlineScheme.resetFields()
    },
    clearValidate(){
      this.$refs.onlineScheme.clearValidate()
    }
  }
}
</script>