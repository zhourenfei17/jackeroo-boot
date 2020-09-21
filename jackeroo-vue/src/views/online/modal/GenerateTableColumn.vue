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
    <a-steps :current="current" @change="handleStepChange" size="small">
      <a-step title="基本信息"></a-step>
      <a-step title="字段信息"></a-step>
      <a-step title="生成信息"></a-step>
    </a-steps>

    <div class="generate-content">
      <a-form-model ref="onlineTable" :model="form" :rules="rules" v-bind="layout" v-show="current == 0">
        <a-row :gutter="formGutter">
          <a-col :span="rowSpan">
            <a-form-model-item label="表名" prop="tableName">
              <a-input v-model="form.tableName" placeholder="请输入表名" disabled></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="表说明" prop="comment">
              <a-input v-model="form.comment" placeholder="请输入表说明" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="实体类名称" prop="className">
              <a-input v-model="form.className" placeholder="请输入实体类名称" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="主键策略" prop="idStrategy">
              <a-select v-model="form.idStrategy" placeholder="请选择主键策略">
                <a-select-option value="ASSIGN_ID">生成ID(雪花算法)</a-select-option>
                <a-select-option value="AUTO">自增</a-select-option>
                <a-select-option value="ASSIGN_UUID">生成UUID</a-select-option>
                <a-select-option value="NONE">未设置主键类型</a-select-option>
                <a-select-option value="INPUT">用户输入</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="删除策略" prop="delStrategy">
              <a-radio-group v-model="form.delStrategy">
                <a-radio :value="0">物理删除</a-radio>
                <a-radio :value="1">逻辑删除</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>

      <a-table
        ref="onlineTableField"
        size="default"
        rowKey="id"
        :columns="columns"
        :dataSource="dataSource"
        :showPagination="false"
        v-show="current == 1">

      </a-table>

      <a-form-model ref="onlineScheme" :model="form" :rules="rules" v-bind="layout" v-show="current == 2">
        <a-row :gutter="formGutter">
          <a-col :span="rowSpan">
            <a-form-model-item label="生成模板" prop="template">
              <a-select v-model="form.template" placeholder="请选择生成模板">
                
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="生成作者" prop="author">
              <a-input v-model="form.author" placeholder="请输入生成作者" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="生成包路径" prop="packageName">
              <a-input v-model="form.packageName" placeholder="请输入生成包路径" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="生成模块名" prop="moduleName">
              <a-input v-model="form.moduleName" placeholder="请输入生成模块名" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="生成功能名" prop="funName">
              <a-input v-model="form.funName" placeholder="请输入生成功能名" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="表单风格" prop="formStyle">
              <a-select v-model="form.formStyle" placeholder="请选择表单风格">
                <a-select-option :value="1">单列</a-select-option>
                <a-select-option :value="2">两列</a-select-option>
                <a-select-option :value="3">三列</a-select-option>
                <a-select-option :value="4">四列</a-select-option>
              </a-select>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="是否显示复选框" prop="showCheckbox">
              <a-radio-group v-model="form.showCheckbox">
                <a-radio :value="0">否</a-radio>
                <a-radio :value="1">是</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="代码生成路径" prop="outputDir">
              <a-input v-model="form.outputDir" placeholder="请输入代码生成路径" :disabled="flag.view"></a-input>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="是否分页" prop="enablePagination">
              <a-radio-group v-model="form.enablePagination">
                <a-radio :value="0">否</a-radio>
                <a-radio :value="1">是</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="是否生成文档" prop="enableSwagger">
              <a-radio-group v-model="form.enableSwagger">
                <a-radio :value="0">否</a-radio>
                <a-radio :value="1">是</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
          <a-col :span="rowSpan">
            <a-form-model-item label="服务器端校验" prop="enableServerValid">
              <a-radio-group v-model="form.enableServerValid">
                <a-radio :value="0">否</a-radio>
                <a-radio :value="1">是</a-radio>
              </a-radio-group>
            </a-form-model-item>
          </a-col>
        </a-row>
      </a-form-model>
    </div>

    <div style="text-align:center;">
      <a-button @click="handlePrev" v-show="current > 0">上一步</a-button>
      <a-button type="primary" @click="handleNext" v-show="current < 2">下一步</a-button>
    </div>
  </j-modal>
</template>

<script>
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'

export default {
  mixins: [JackerooFromMixins],
  data(){
    return {
      title: '生成代码',
      width: '80vw',
      fullscreen: true,
      current: 0,
      form:{
        tableName: '',
        comment: '',
        className: '',
        idStrategy: null,
        delStrategy: null
      },
      rules:{
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
        ]
      },
      dataSource: [],
      columns: [
        {
          dataIndex: 'dbFieldName',
          title: '列名'
        },
        {
          dataIndex: 'dbFieldDesc',
          title: '列描述'
        },
        {
          dataIndex: 'dbFieldType',
          title: '字段类型'
        },
        {
          dataIndex: 'entityFieldName',
          title: '属性名称',
        },
        {
          dataIndex: 'entityFieldType',
          title: '属性类型'
        },
        {
          dataIndex: 'dbFieldLength',
          title: '字段长度'
        },
        {
          dataIndex: 'primaryKey',
          title: '主键'
        },
        {
          dataIndex: 'nullable',
          title: '非空'
        }
      ],
      url: {
        list: '/online/generate/findTableListFromDataSource'
      },
      tableName: ''
    }
  },
  methods: {
    add(tableName){
      this.tableName = tableName
      this.loading = false
      this.visible = true
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
    handleSubmit(){

    }
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