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
      <!-- <a-col :span="rowSpan">
        <a-form-model-item label="生成功能名" prop="funName">
          <a-input v-model="formScheme.funName" placeholder="请输入生成功能名"></a-input>
        </a-form-model-item>
      </a-col> -->
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
      <a-col :span="rowSpan">
        <a-form-model-item label="是否启用权限" prop="enableSecurity">
          <j-dict-select v-model="formScheme.enableSecurity" dictCode="YES_NO" type="radio"></j-dict-select>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan" v-if="formScheme.enableSecurity == 1">
        <a-form-model-item label="功能名称" prop="securitySign">
          <a-input v-model="formScheme.securitySign" placeholder="请输入功能名称" style="width: 90%"></a-input>
          <a-tooltip placement="topLeft" overlayClassName="tooltip-content"> 
            <template slot="title">
              <span>用来拼接权限标识，</span><span class="tooltip-red">规则【功能模块代码:功能名称:权限代码】</span>
              <br>
              <span>示例：</span>
              <br>
              <span class="tooltip-red">功能模块: 即当前页面的所属模块，例如系统管理对应的代码为【system】</span>
              <br>
              <span class="tooltip-red">功能名称: 例如用户管理页面填写【user】</span>
              <br>
              <span class="tooltip-red">权限代码: 基本的操作权限，由所选模板决定，例如添加的权限代码对应为【add】</span>
              <br>
              <span class="tooltip-red">拼接后权限标识: system:menu:add</span>
            </template>
            <a-icon type="exclamation-circle" style="margin-left:20px;"></a-icon>
          </a-tooltip>
        </a-form-model-item>
      </a-col>
    </a-row>
  </a-form-model>
</template>

<script>
import { JSelect, JDictSelect} from '@/components'
import {ValidatorMixins} from '@/mixins/ValidatorMixins';

export default {
  components: {
    JSelect,
    JDictSelect
  },
  mixins: [ValidatorMixins],
  props: ['formScheme', 'layout', 'formGutter', 'rowSpan'],
  data() {
    return {
      schemeRules: {
        template: [
          {required: true, message: '请选择生成模板'}
        ],
        author: [
          {max: 50, message: '长度需要在0到50之间'}
        ],
        packageName: [
          {required: true, message: '请填写生成包名'},
          {max: 100, message: '长度需要在0到100之间'}
        ],
        moduleId: [
          {required: true, message: '请选择所属模块'}
        ],
        formStyle: [
          {required: true, message: '请选择表单风格'}
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
        ],
        enableSecurity: [
          {required: true, message: '请选择是否启用权限'}
        ],
        securitySign: [
          {required: true, message: '请填写权限标识'},
          {max: 30, message: '长度需要在0到30之间'},
          {validator: this.validLetterAndUnderline}
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

<style lang="less" scoped>
.tooltip-red{
  color: #ff4545;
}
</style>