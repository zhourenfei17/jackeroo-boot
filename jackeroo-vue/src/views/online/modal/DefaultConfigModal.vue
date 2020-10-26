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
    >
    <j-spin :spinning="loading">
      <a-form-model ref="formModel" :model="form" :rules="rules" v-bind="layout">
        <a-row :gutter="formGutter">
          <a-col :span="rowSpan">
            <a-form-model-item label="排序字段" prop="sortColumn">
              <a-input v-model="form.sortColumn" placeholder="请输入排序字段"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="排序方式" prop="sortType">
              <j-dict-select v-model="form.sortType" dictCode="GEN_SORT_TYPE" type="radio"></j-dict-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="主键策略" prop="idStrategy">
              <j-dict-select v-model="form.idStrategy" dictCode="GEN_ID_STRATEGY" placeholder="请选择主键策略"></j-dict-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="逻辑删字段" prop="logicColumn">
              <a-input v-model="form.logicColumn" placeholder="请输入逻辑删字段"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="生成包名" prop="packageName">
              <a-input v-model="form.packageName" placeholder="请输入生成包名"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="表单风格" prop="formStyle">
              <j-dict-select v-model="form.formStyle" dictCode="GEN_FORM_STYLE" placeholder="请选择表单风格"></j-dict-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="显示复选框" prop="showCheckbox">
              <j-dict-select v-model="form.showCheckbox" dictCode="YES_NO" type="radio"></j-dict-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="是否分页" prop="enablePagination">
              <j-dict-select v-model="form.enablePagination" dictCode="YES_NO" type="radio"></j-dict-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="生成swagger文档" prop="enableSwagger">
              <j-dict-select v-model="form.enableSwagger" dictCode="YES_NO" type="radio"></j-dict-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="服务器端校验" prop="enableServerValid">
              <j-dict-select v-model="form.enableServerValid" dictCode="YES_NO" type="radio"></j-dict-select>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>

      <data-card :icon="false">
        <template slot="toolbar">
          <a-button type="primary" icon="plus" @click="handleAddColumn">添加列</a-button>
        </template>

        <edit-table
          ref="editTable"
          size="small"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :pagination="false">

        </edit-table>
      </data-card>
    </j-spin>
  </j-modal>
</template>

<script>
import { getAction, postAction, httpAction } from '@/api/manage'
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'
import { JDictSelect, EditTable, DataCard } from '@/components'

export default {
  mixins: [JackerooFromMixins],
  components: {
    JDictSelect,
    EditTable,
    DataCard
  },
  data(){
    return {
      title: '默认配置项',
      tableName: 'online_default_config',
      width: '60vw',
      // 表单列数
      formCol: 2,
      form: {
        id: null,
        sortColumn: null,
        sortType: null,
        idStrategy: undefined,
        logicColumn: null,
        packageName: null,
        formStyle: undefined,
        showCheckbox: null,
        enablePagination: null,
        enableSwagger: null,
        enableServerValid: null,
        columnConfig: null
      },
      rules: {
        id: [
          {required: true, message: '请输入id'},
          {max: 19, message: '长度需要在0到19之间'},
        ],
        sortColumn: [
          {max: 30, message: '长度需要在0到30之间'},
        ],
        sortType: [
          {max: 5, message: '长度需要在0到5之间'},
        ],
        idStrategy: [
          {max: 20, message: '长度需要在0到20之间'},
        ],
        logicColumn: [
          {max: 20, message: '长度需要在0到20之间'},
        ],
        packageName: [
          {max: 50, message: '长度需要在0到50之间'},
        ],
        formStyle: [
        ],
        showCheckbox: [
        ],
        enablePagination: [
        ],
        enableSwagger: [
        ],
        enableServerValid: [
        ],
        columnConfig: [
        ]
      },
      columns:[
        {
          title: '#',
          customRender: (text, record, index) => {
            return index + 1
          }
        },
        {
          title: '列名',
          dataIndex: 'dbFieldName',
          width: 160,
          type: 'input'
        },
        {
          dataIndex: 'enableList',
          title: '列表',
          width: 60,
          type: 'checkbox'
        },
        {
          dataIndex: 'enableForm',
          title: '表单',
          width: 60,
          type: 'checkbox'
        },
        {
          dataIndex: 'enableQuery',
          title: '查询',
          width: 60,
          type: 'checkbox'
        },
        {
          dataIndex: 'enableSort',
          title: '排序',
          width: 60,
          type: 'checkbox'
        },
        {
          dataIndex: 'queryType',
          title: '查询方式',
          type: 'select',
          dictCode: 'GEN_QUERY_TYPE',
          rule: [{required: true, message: '请选择查询方式'}]
        },
        {
          dataIndex: 'formType',
          title: '控件类型',
          type: 'select',
          dictCode: 'GEN_COMPONENT_TYPE',
          rule: [{required: true, message: '请选择控件类型'}]
        },
      ],
      dataSource: [],
      url: {
        getById: '/online/onlinedefaultconfig/',
        add: '/online/onlinedefaultconfig/add',
        update: '/online/onlinedefaultconfig/update'
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
    handleAddColumn(){
      this.dataSource.push({id: new Date().getTime()})
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
