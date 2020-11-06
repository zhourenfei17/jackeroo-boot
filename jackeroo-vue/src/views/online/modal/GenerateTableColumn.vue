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

      <div class="generate-content">
        <a-tabs :animated="{inkBar: true, tabPane: false}" :activeKey="current" @change="handleStepChange">
          <a-tab-pane :key="0" style="background-color: #fff;padding-top:20px;">
            <span slot="tab">
              <a-icon type="global"></a-icon>
              1.基本信息
            </span>

            <table-form 
              ref="onlineTable"
              :dataSource="dataSource"
              :layout="layout"
              :rowSpan="rowSpan"
              :formTable="formTable"
              :formGutter="formGutter"
              >
            </table-form>
          </a-tab-pane>
          <a-tab-pane :key="1" forceRender style="background-color: #fff">
            <span slot="tab">
              <a-icon type="table"></a-icon>
              2.字段信息
            </span>

            <edit-table
              ref="onlineTableField"
              size="small"
              rowKey="dbFieldName"
              tableLayout="auto"
              :columns="columns"
              :dataSource="dataSource"
              :pagination="false"
              :scroll="{x: 1800}"
              >
            </edit-table>
          </a-tab-pane>
          <a-tab-pane :key="2" forceRender style="background-color: #fff;padding-top:20px;">
            <span slot="tab">
              <a-icon type="code"></a-icon>
              3.生成信息
            </span>

            <scheme-form
              ref="onlineScheme"
              :layout="layout"
              :rowSpan="rowSpan"
              :formScheme="formScheme"
              :formGutter="formGutter"
              >
            </scheme-form>
          </a-tab-pane>

          <a-tooltip slot="tabBarExtraContent" v-show="current == 1" title="调整字段的排序，将影响列表和表单的显示顺序">
            <a-button icon="drag" type="primary" @click="handleSortColumn">
              调整顺序
            </a-button>
          </a-tooltip>
        </a-tabs>
      </div>
        
    </j-spin>

    <draggle-table-column ref="draggleTableColumn" @ok="handleSortColumnOk"></draggle-table-column>

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
import {JackerooFormMixins} from '@/mixins/JackerooFormMixins'
import { getAction, postAction } from '@/api/manage'
import {EditTable, JSpin} from '@/components'
import TableForm from './form/TableForm'
import SchemeForm from './form/SchemeForm'
import DraggleTableColumn from './DraggleTableColumn'

export default {
  components:{
    EditTable,
    JSpin,
    TableForm,
    SchemeForm,
    DraggleTableColumn
  },
  mixins: [JackerooFormMixins],
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
        logicField: '',
        sortColumn: '',
        sortType: undefined
      },
      formScheme: {
        id: null,
        packageName: '',
        moduleId: null,
        showCheckbox: null,
        formStyle: null,
        author: '',
        template: null,
        enablePagination: null,
        enableSwagger: null,
        enableServerValid: null
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
          type: 'text',
          ellipsis: true,
          length: 20
        },
        {
          dataIndex: 'dbFieldDesc',
          title: '列描述',
          width: 150,
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
                  formData.onlineTable = {...this.formTable}
                  formData.onlineScheme = {...this.formScheme}
                  formData.onlineTableField = data

                  console.log('formData', formData)

                  postAction(this.url.save, formData).then(result => {
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
    syncDataSource(){
      this.$refs.onlineTableField.clearValidate()
      const data = this.$refs.onlineTableField.getData()
      this.dataSource = data
    },
    handleSortColumn(){
      this.syncDataSource()
      this.$refs.draggleTableColumn.edit(this.dataSource)
    },
    handleSortColumnOk(data){
      for(let i = 0; i < data.length; i++){
        data.sort = i + 1
      }
      this.dataSource = data
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
  background-color: #f0f2f5;
  padding: 10px;
}
</style>

<style lang="less">
.generate-modal{
  .ant-modal-body{
    background-color: #f0f2f5;
  }
}
</style>