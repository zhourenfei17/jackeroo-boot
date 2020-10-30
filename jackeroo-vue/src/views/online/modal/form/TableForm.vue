<template>
  <a-form-model ref="onlineTable" :model="formTable" :rules="tableRules" v-bind="layout">
    <a-row :gutter="formGutter">
      <a-col :span="rowSpan">
        <a-form-model-item label="表名" prop="tableName">
          <a-input v-model="formTable.tableName" placeholder="请输入表名" disabled></a-input>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan">
        <a-form-model-item label="表说明" prop="comment">
          <a-input v-model="formTable.comment" placeholder="请输入表说明"></a-input>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan">
        <a-form-model-item label="实体类名称" prop="className">
          <a-input v-model="formTable.className" placeholder="请输入实体类名称"></a-input>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan">
        <a-form-model-item label="主键策略" prop="idStrategy">
          <j-dict-select v-model="formTable.idStrategy" placeholder="请选择主键策略" dictCode="GEN_ID_STRATEGY"></j-dict-select>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan">
        <a-form-model-item label="默认排序字段" prop="sortColumn">
          <j-select v-model="formTable.sortColumn" :list="dataSource" valueField="dbFieldName" textField="entityFieldName" placeholder="请选择默认排序字段">

          </j-select>
        </a-form-model-item>
      </a-col>
      <a-col :span="rowSpan">
        <a-form-model-item label="排序方式" prop="sortType">
          <j-dict-select v-model="formTable.sortType" dictCode="GEN_SORT_TYPE" type="radio"></j-dict-select>
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
</template>

<script>
import { JSelect, JDictSelect} from '@/components'

export default {
  components: {
    JSelect,
    JDictSelect
  },
  props:['dataSource', 'layout', 'formTable', 'formGutter', 'rowSpan'],
  data(){
    return {
      
    }
  },
  computed: {
    tableRules() {
      return {
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
        ],
        sortFiled: [],
        sortType: [
          {required: this.formTable.sortColumn, message: '请选择排序方式'}
        ]
      }
    }
  },
  methods: {
    validate(callback){
      if(callback && typeof callback == 'function'){
        this.$refs.onlineTable.validate(callback)
      }else{
        return this.$refs.onlineTable.validate()
      }
    },
    resetFields(){
      this.$refs.onlineTable.resetFields()
    },
    clearValidate(){
      this.$refs.onlineTable.clearValidate()
    }
  }
}
</script>