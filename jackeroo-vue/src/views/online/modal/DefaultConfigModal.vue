<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :fullscreen.sync="fullscreen"
    :switchFullscreen="false"
    :confirmLoading="loading"
    :autoHeight="false"
    maxHeight="70vh"
    :bodyStyle="{backgroundColor: '#f0f2f5'}"
    @ok="handleSubmit"
    @cancel="cancel"
    >
    <j-spin :spinning="loading">
      <a-alert message="默认配置项在新建并选择数据库表后生效" type="info" style="margin-bottom:8px;"></a-alert>

      <a-form-model ref="formModel" :model="form" :rules="rules" v-bind="layout" style="backgroundColor: #fff;">
        <a-row :gutter="formGutter">
          <a-col :span="rowSpan">
            <a-form-model-item label="排序字段" prop="sortColumn">
              <a-input v-model="form.sortColumn" placeholder="请输入排序字段" style="width:90%"></a-input>

              <a-tooltip title="支持【数据库字段】或者【java实体类字段】">
                <a-icon type="question-circle" style="margin-left:10px;"></a-icon>
              </a-tooltip>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="排序方式" prop="sortType">
              <j-dict-select v-model="form.sortType" dictCode="GEN_SORT_TYPE" type="radio"></j-dict-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="主键策略" prop="idStrategy">
              <j-dict-select v-model="form.idStrategy" dictCode="GEN_ID_STRATEGY" placeholder="请选择主键策略" style="width:90%"></j-dict-select>
              <a-tooltip title="请参考MyBatis-Plus的IdType可选值的解释">
                <a-icon type="question-circle" style="margin-left:10px;"></a-icon>
              </a-tooltip>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="逻辑删字段" prop="logicColumn">
              <a-input v-model="form.logicColumn" placeholder="请输入逻辑删字段" style="width: 90%;"></a-input>

              <a-tooltip title="如果新建选择的数据库表包含该字段，则默认删除策略为逻辑删，支持【数据库字段】或者【java实体类字段】">
                <a-icon type="question-circle" style="margin-left:10px;"></a-icon>
              </a-tooltip>
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
          <a-col :span="rowSpan">
            <a-form-model-item label="是否启用权限" prop="enableSecurity">
              <j-dict-select v-model="form.enableSecurity" dictCode="YES_NO" type="radio"></j-dict-select>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>

      <a-alert message="公共字段配置，如果数据库表包含以下列字段，则该列字段默认采用以下配置" type="info" style="margin-top:8px;"></a-alert>

      <data-card :icon="false" :marginTop="8">
        <template slot="toolbar">
          <a-button icon="delete" @click="handleDelteColumn" style="margin-right: 8px;" v-show="selectedRowKeys.length > 0">删除列</a-button>
          <a-button type="primary" icon="plus" @click="handleAddColumn">添加列</a-button>
        </template>

        <edit-table
          ref="editTable"
          size="small"
          rowKey="id"
          :columns="columns"
          :dataSource="dataSource"
          :rowSelection="rowSelection"
          :pagination="false">

          <span slot="dbFieldNameSlot">
            列名 <a-tooltip title="支持【数据库字段】或者【java实体类字段】"><a-icon type="question-circle"></a-icon></a-tooltip>
          </span>

          <span slot="lockerSlot">
            乐观锁 <a-tooltip title="仅公共字段可配置该项"><a-icon type="exclamation-circle" style="color: red;"></a-icon></a-tooltip>
          </span>

          <span slot="fillStrategySlot">
            填充策略 <a-tooltip title="仅公共字段可配置该项，请参考MyBatis-Plus自动填充功能"><a-icon type="exclamation-circle" style="color: red;"></a-icon></a-tooltip>
          </span>
        </edit-table>
      </data-card>
    </j-spin>
  </j-modal>
</template>

<script>
import { getAction, postAction, httpAction } from '@/api/manage'
import {JackerooFormMixins} from '@/mixins/JackerooFormMixins'
import { JDictSelect, EditTable, DataCard } from '@/components'

export default {
  mixins: [JackerooFormMixins],
  components: {
    JDictSelect,
    EditTable,
    DataCard
  },
  data(){
    return {
      title: '默认配置项',
      tableName: 'online_default_config',
      width: '1000px',
      // 表单列数
      formCol: 2,
      layout: {
        labelCol: {span: 8},
        wrapperCol: {span: 14}
      },
      form: {
        id: undefined,
        sortColumn: undefined,
        sortType: undefined,
        idStrategy: undefined,
        logicColumn: undefined,
        packageName: undefined,
        formStyle: undefined,
        showCheckbox: undefined,
        enablePagination: undefined,
        enableSwagger: undefined,
        enableServerValid: undefined,
        enableSecurity: undefined
      },
      columns:[
        {
          title: '#',
          width: 40,
          customRender: (text, record, index) => {
            return index + 1
          }
        },
        {
          dataIndex: 'dbFieldName',
          width: 150,
          type: 'input',
          slots: {title: 'dbFieldNameSlot'},
          rule: [{required: true, message: '请输入列名'}, {max: 30, message: '长度需要在0到30之间'}]
        },
        {
          dataIndex: 'enableList',
          title: '列表',
          width: 50,
          type: 'checkbox'
        },
        {
          dataIndex: 'enableForm',
          title: '表单',
          width: 50,
          type: 'checkbox'
        },
        {
          dataIndex: 'enableQuery',
          title: '查询',
          width: 50,
          type: 'checkbox'
        },
        {
          dataIndex: 'enableSort',
          title: '排序',
          width: 50,
          type: 'checkbox'
        },
        {
          dataIndex: 'locker',
          slots: {title: 'lockerSlot'},
          width: 80,
          type: 'checkbox'
        },
        {
          dataIndex: 'fillStrategy',
          slots: {title: 'fillStrategySlot'},
          width: 140,
          type: 'select',
          dictCode: 'GEN_FILL_STRATEGY'
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
      // 选中行的key
      selectedRowKeys: [],
      // 选中行的数据
      selectedRows: [],
      url: {
        getConfig: '/online/default/config/getConfig',
        save: '/online/default/config/save'
      }
    }
  },
  computed: {
    rowSelection() {
      return {
        selectedRowKeys: this.selectedRowKeys,
        onChange: this.onSelectChange
      }
    },
    rules() {
      return {
        sortColumn: [
          {max: 30, message: '长度需要在0到30之间'},
        ],
        sortType: [
          {required: this.form.sortColumn, message: '请选择排序方式'}
        ],
        idStrategy: [
          
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
        enableSecurity:[
        ]
      }
    }
  },
  methods: {
    edit(){
      getAction(this.url.getConfig).then(result => {
        if(!result.code){
          if(result.data){
            this.copyProperties(result.data, this.form)
            if(result.data.columnConfig){
              this.dataSource = JSON.parse(result.data.columnConfig)
            }
          }
        }
      }).finally(() => {
        this.loading = false
      })
    },
    getRandomNum(){
      let num = ''
      for(let i = 0; i < 3; i++){
        num += Math.floor(Math.random() * 10)
      }
      return num
    },
    handleAddColumn(){
      this.$refs.editTable.clearValidate()
      const data = this.$refs.editTable.getValuesSkipValidate()
      data.push({id: new Date().getTime() + this.getRandomNum()})
      this.dataSource = data
    },
    handleDelteColumn(){
      /* this.$refs.editTable.getValues((data) => {
        this.dataSource = data.filter(item => this.selectedRowKeys.indexOf(item.id) == -1)
      }) */
      this.$refs.editTable.clearValidate()
      const data = this.$refs.editTable.getValuesSkipValidate()
      this.dataSource = data.filter(item => this.selectedRowKeys.indexOf(item.id) == -1)

      this.selectedRowKeys = []
      this.selectedRows = []
    },
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        if(success){
          const formData = {...this.form}

          this.$refs.editTable.getValues(data => {
            if(data){
              formData.columnConfig = JSON.stringify(data)
              console.log('formData', formData)

              this.$loading.show()
              httpAction(this.url.save, formData, 'post').then(result => {
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
        }
      })
    },
    // 选中行事件
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
  }
}
</script>
