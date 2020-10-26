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
    class="generate-modal"
  >
    

    <j-spin :spinning="loading">
      <a-steps :current="current" @change="handleStepChange" size="small">
        <a-step title="基本信息"></a-step>
        <a-step title="字段信息"></a-step>
        <a-step title="生成信息"></a-step>
      </a-steps>
      
      <div class="generate-content">
        <a-form-model ref="onlineTable" :model="formTable" :rules="tableRules" v-bind="layout" v-show="current == 0">
          <a-row :gutter="formGutter">
            <a-col :span="rowSpan">
              <a-form-model-item label="表名" prop="tableName">
                <a-input v-model="formTable.tableName" placeholder="请输入表名" disabled></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="rowSpan">
              <a-form-model-item label="表说明" prop="comment">
                <a-input v-model="formTable.comment" placeholder="请输入表说明" :disabled="flag.view"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="rowSpan">
              <a-form-model-item label="实体类名称" prop="className">
                <a-input v-model="formTable.className" placeholder="请输入实体类名称" :disabled="flag.view"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="rowSpan">
              <a-form-model-item label="主键策略" prop="idStrategy">
                <j-dict-select v-model="formTable.idStrategy" placeholder="请选择主键策略" dictCode="GEN_ID_STRATEGY"></j-dict-select>
              </a-form-model-item>
            </a-col>
            <a-col :span="rowSpan">
              <a-form-model-item label="删除策略" prop="delStrategy">
                <j-dict-select v-model="formTable.delStrategy" dictCode="GEN_DEL_STRATEGY" type="radio"></j-dict-select>
              </a-form-model-item>
            </a-col>
            <a-col :span="rowSpan" v-if="formTable.delStrategy">
              <a-form-model-item label="逻辑删字段" prop="logicField">
                <j-select v-model="formTable.logicField" :list="dataSource" valueField="entityFieldName" textField="entityFieldName" placeholder="请选择逻辑删字段">

                </j-select>
              </a-form-model-item>
            </a-col>
          </a-row>
        </a-form-model>

        <edit-table
          ref="onlineTableField"
          size="small"
          rowKey="dbFieldName"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="false"
          :scroll="{x: 1800}"
          v-show="current == 1">

        </edit-table>

        <a-form-model ref="onlineScheme" :model="formScheme" :rules="schemeRules" v-bind="layout" v-show="current == 2">
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
                <a-input v-model="formScheme.author" placeholder="请输入生成作者" :disabled="flag.view"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="rowSpan">
              <a-form-model-item label="生成包名" prop="packageName">
                <a-input v-model="formScheme.packageName" placeholder="请输入生成包路径" :disabled="flag.view"></a-input>
              </a-form-model-item>
            </a-col>
            <a-col :span="rowSpan">
              <a-form-model-item label="所属模块" prop="moduleId">
                <j-select v-model="formScheme.moduleId" placeholder="请选择所属模块" :url="url.findModuleList"></j-select>
              </a-form-model-item>
            </a-col>
            <a-col :span="rowSpan">
              <a-form-model-item label="生成功能名" prop="funName">
                <a-input v-model="formScheme.funName" placeholder="请输入生成功能名" :disabled="flag.view"></a-input>
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
      </div>
    </j-spin>

    <template slot="footer">
      <span style="padding-right:50px;">
        <a-button @click="handlePrev" :disabled="current == 0" icon="double-left">上一步</a-button>
        <a-button @click="handleNext" :disabled="current == 2" icon="double-right">下一步</a-button>
      </span>
      
      <a-button @click="cancel">取消</a-button>
      <a-button type="primary" @click="handleSubmit(false)">保存</a-button>
      <a-button type="primary" @click="handleSubmit(true)">保存并生成代码</a-button>
    </template>
  </j-modal>
</template>

<script>
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'
import { getAction, postAction } from '@/api/manage'
import {EditTable, FileSelector, JSelect, JDictSelect, JSpin} from '@/components'

export default {
  components:{
    EditTable,
    FileSelector,
    JSelect,
    JDictSelect,
    JSpin
  },
  mixins: [JackerooFromMixins],
  data(){
    return {
      title: '生成代码',
      width: '80vw',
      fullscreen: true,
      current: 0,
      formTable: {
        id: null,
        tableName: '',
        comment: '',
        className: '',
        idStrategy: null,
        delStrategy: null,
        logicField: ''
      },
      formScheme: {
        id: null,
        packageName: '',
        moduleId: null,
        showCheckbox: null,
        formStyle: null,
        author: '',
        outputDir: '',
        template: null,
        enablePagination: null,
        enableSwagger: null,
        enableServerValid: null
      },
      tableRules:{
        tableName: [
          {required: true, message: '请输入表名'},
        ],
        comment: [
          {required: true, message: '请输入表说明'}, 
        ],
        className: [
          {required: true, message: '请输入实体类名称'}, 
        ],
        idStrategy: [
          {required: true, message: '请选择主键策略'}, 
        ],
        delStrategy: [
          {required: true, message: '请删除策略'}, 
        ],
        logicField: [
          {required: true, message: '请选择逻辑删字段'}
        ]
      },
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
      dataSource: [],
      columns: [
        {
          title: '#',
          fixed: 'left',
          width: 60,
          customRender: (text, record, index) => {
            return index + 1
          }
        },
        {
          dataIndex: 'dbFieldName',
          title: '列名',
          width: 110,
          fixed: 'left',
          type: 'text'
        },
        {
          dataIndex: 'dbFieldDesc',
          title: '列描述',
          width: 120,
          fixed: 'left',
          type: 'input',
          rule: [{required: true, message: '请填写列描述'},{max: 50, message: '长度需要在0到50之间'}]
        },
        {
          dataIndex: 'dbFieldType',
          title: '字段类型',
          width: 100,
          type: 'text'
        },
        {
          dataIndex: 'entityFieldName',
          title: '属性名称',
          width: 120,
          type: 'input',
          rule: [{required: true, message: '请填写属性名称'},{max: 50, message: '长度需要在0到50之间'}]
        },
        {
          dataIndex: 'entityFieldType',
          title: '属性类型',
          width: 120,
          type: 'select',
          dictCode: 'GEN_JAVA_TYPE',
          rule: [{required: true, message: '请选择属性类型'}]
        },
        {
          dataIndex: 'dbFieldLength',
          width: 80,
          title: '字段长度',
          type: 'text'
        },
        {
          dataIndex: 'primaryKey',
          title: '主键',
          width: 40,
          disabled: true,
          type: 'checkbox'
        },
        {
          dataIndex: 'nullable',
          title: '非空',
          width: 40,
          disabled: true,
          type: 'checkbox'
        },
        {
          dataIndex: 'enableList',
          title: '列表',
          width: 40,
          type: 'checkbox'
        },
        {
          dataIndex: 'enableForm',
          title: '表单',
          width: 40,
          type: 'checkbox'
        },
        {
          dataIndex: 'enableQuery',
          title: '查询',
          width: 40,
          type: 'checkbox'
        },
        {
          dataIndex: 'enableSort',
          title: '排序',
          width: 40,
          type: 'checkbox'
        },
        {
          dataIndex: 'queryType',
          title: '查询方式',
          type: 'select',
          width: 100,
          dictCode: 'GEN_QUERY_TYPE',
          rule: [{required: true, message: '请选择查询方式'}]
        },
        {
          dataIndex: 'formType',
          title: '控件类型',
          type: 'select',
          width: 150,
          dictCode: 'GEN_COMPONENT_TYPE',
          rule: [{required: true, message: '请选择控件类型'}]
        },
        {
          dataIndex: 'formRequired',
          title: '必填',
          width: 40,
          type: 'checkbox'
        },
        {
          dataIndex: 'formValidator',
          title: '校验',
          type: 'multiple',
          width: 200,
          dictCode: 'GEN_VALIDATOR'
        },
        {
          dataIndex: 'formDictCode',
          title: '字典code',
          width: 180,
          type: 'dictCodeSelector',
          rule: [{max: 50, message: '长度需要在0到50之间'}]
        },
        {
          dataIndex: 'singleLine',
          title: '单独行',
          type: 'checkbox'
        }
      ],
      url: {
        detail: '/online/generate/findTableDetailInfo',
        findModuleList: '/system/module/allList',
        get: '/online/table/',
        save: '/online/generate/save',
      },
      tableName: ''
    }
  },
  methods: {
    add(tableName){
      this.tableName = tableName
      this.visible = true

      getAction(this.url.detail, {tableName: tableName}).then(result => {
        if(!result.code){
          this.copyProperties(result.data.table, this.formTable)
          this.copyProperties(result.data.scheme, this.formScheme)
          this.dataSource = result.data.columns
        }
      }).finally(() => {
        this.loading = false
      })
    },
    edit(tableId){
      getAction(this.url.get + tableId).then(result => {
        if(!result.code){
          this.copyProperties(result.data.table, this.formTable)
          this.copyProperties(result.data.scheme, this.formScheme)
          this.dataSource = result.data.columns
        }
      }).finally(() => {
        this.loading = false
      })
    },
    handleStepChange(current){
      this.current = current
    },
    handlePrev(){
      if(this.current > 0){
        this.current--
      }
    },
    handleNext(){
      if(this.current < 2){
        this.current++
      }
    },
    handleSubmit(generate = false){
      this.$refs.onlineTable.validate((success) => {
        if(success){
          this.$refs.onlineScheme.validate(success2 => {
            if(success2){
              const data = this.$refs.onlineTableField.getValues((data) => {
                if(data){
                  const formData = {}
                  formData.onlineTable = this.formTable
                  formData.onlineScheme = this.formScheme
                  formData.onlineTableField = data

                  console.log('formData', formData)

                  postAction('/online/generate/save', formData).then(result => {
                    if(!result.code){
                      this.$message.success('保存成功')
                      if(generate){
                        this.$emit('generate', result.data)
                      }
                      this.$emit('ok')
                      this.cancel()
                    }else{
                      this.$message.error('保存失败')
                    }
                  })
                }else{
                  this.current = 1
                }
              })
            }else{
              this.current = 2
            }
          })
        }else{
          this.current = 0
        }
      })
    },
    cancel(){
      this.visible = false
      this.current = 0
      
      this.$refs.onlineTable.resetFields()
      this.$refs.onlineTable.clearValidate()

      this.$refs.onlineScheme.resetFields()
      this.$refs.onlineScheme.clearValidate()

      this.dataSource = []

      this.loading = true
    },
  }
}
</script>

<style lang="less" scoped>
.generate-content{
  background-color: #ffffff;
  padding: 20px;
  margin: 40px 0;
}
</style>

<style lang="less">
.generate-modal{
  .ant-modal-body{
    background-color: #f0f2f5;
  }
}
</style>