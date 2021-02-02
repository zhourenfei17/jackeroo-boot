<template>
  <a-form-model ref="formTable" :model="formData" v-if="toggle">
    <a-table :dataSource="dataSource" :columns="columnsCopy" :rowKey="rowKey" v-bind="attrs" class="jackeroo-edit-table">
      <template v-for="col in columnsSlot" :slot="col.dataIndex" slot-scope="text, record, index" >
          
        <a-form-model-item 
          :prop="col.dataIndex + '_' + index" 
          :rules="(col.disabled || record.disabled || false) ? null : (rules[col.dataIndex] || null)" 
          :key="col.dataIndex + '_' + index">
          <span v-if="!editSwitch || (editSwitch && record.editAble)">
            <a-input 
              v-if="col.type == 'input'" 
              v-model="formData[col.dataIndex + '_' + index]"
              :disabled="col.disabled || record.disabled || disabled || false"
              v-on="formEvnentsConvert(col.events, record, index)"
              v-bind="col.attrs">
            </a-input>

            <a-input-number
              v-else-if="col.type == 'inputNumber'"
              v-model="formData[col.dataIndex + '_' + index]"
              :disabled="col.disabled || record.disabled || disabled || false"
              v-on="formEvnentsConvert(col.events, record, index)"
              v-bind="col.attrs">

            </a-input-number>

            <j-dict-select 
              v-else-if="col.type == 'select' && col.dictCode" 
              v-model="formData[col.dataIndex + '_' + index]"
              :list="dictData[col.dictCode]"
              allowClear
              valueField="value" 
              textField="label"
              style="width: 100%;"
              :disabled="col.disabled || record.disabled || disabled || false" 
              v-on="formEvnentsConvert(col.events, record, index)"
              v-bind="col.attrs">

            </j-dict-select>

            <a-select 
              v-else-if="col.type == 'select' && col.options" 
              v-model="formData[col.dataIndex + '_' + index]"
              style="width: 100%;" 
              :options="col.options"
              :disabled="col.disabled || record.disabled || disabled || false"
              v-on="formEvnentsConvert(col.events, record, index)"
              v-bind="col.attrs">
            </a-select>

            <j-dict-select 
              v-else-if="col.type == 'multiple' && col.dictCode" 
              v-model="formData[col.dataIndex + '_' + index]"
              :list="dictData[col.dictCode]"
              allowClear
              valueField="value" 
              textField="label"
              multi
              style="width: 100%;"
              :disabled="col.disabled || record.disabled || disabled || false" 
              v-on="formEvnentsConvert(col.events, record, index)"
              v-bind="col.attrs">

            </j-dict-select>

            <a-select 
              v-else-if="col.type == 'multiple' && col.options" 
              mode="multiple"
              v-model="formData[col.dataIndex + '_' + index]"
              style="width: 100%;" 
              :options="col.options"
              :disabled="col.disabled || record.disabled || disabled || false"
              v-on="formEvnentsConvert(col.events, record, index)"
              v-bind="col.attrs">
            </a-select>

            <a-cascader
              v-else-if="col.type == 'cascader'"
              v-model="formData[col.dataIndex + '_' + index]"
              :options="col.options"
              :disabled="col.disabled || record.disabled || disabled || false"
              v-on="formEvnentsConvert(col.events, record, index)"
              v-bind="col.attrs">

            </a-cascader>

            <a-checkbox-group 
              v-else-if="col.type == 'checkbox'" 
              v-model="formData[col.dataIndex + '_' + index]"
              :disabled="col.disabled || record.disabled || disabled || false"
              v-on="formEvnentsConvert(col.events, record, index)"
              v-bind="col.attrs">
              <a-checkbox :value="1"></a-checkbox>
            </a-checkbox-group>

            <j-dict-code-select
              v-else-if="col.type == 'dictCodeSelector'" 
              v-model="formData[col.dataIndex + '_' + index]"
              :disabled="col.disabled || record.disabled || disabled || false"
              v-on="formEvnentsConvert(col.events, record, index)"
              v-bind="col.attrs">

            </j-dict-code-select>

            <!-- 自定义组件 -->
            <dynamic-component
              v-else-if="col.type == 'component' && col.component"
              style="width: 100%;" 
              v-model="formData[col.dataIndex + '_' + index]"
              :props="{...col.attrs, disabled: (col.disabled || record.disabled || disabled || false)}"
              :events="col.events"
              :component="col.component"
              v-on="componentEventsConvert(col.events, record, index)"
              >

            </dynamic-component>

            <template v-else-if="!col.type || col.type == 'text'">
              <ellipsis v-if="col.length" tooltip :length="col.length">{{text}}</ellipsis>
              <span v-else>{{text}}</span>
            </template>
          </span>
          <span v-else>
            <span v-if="col.type == 'input' || col.type == 'inputNumber'">
              <ellipsis v-if="col.length" tooltip :length="col.length">{{formData[col.dataIndex + '_' + index]}}</ellipsis>
              <span v-else>{{formData[col.dataIndex + '_' + index]}}</span>
            </span>
            <span v-else-if="col.type == 'select' && col.options">
              {{getSelectLabel(formData[col.dataIndex + '_' + index], col.options)}}
            </span>
            <span v-else-if="col.type == 'multiple' && col.options">
              {{getSelectLabel(formData[col.dataIndex + '_' + index], col.options)}}
            </span>
            <span v-else-if="col.type == 'cascader'">
              {{getCascaderLabel(formData[col.dataIndex + '_' + index], col.options)}}
            </span>
            <span v-else-if="col.type == 'checkbox'">
              {{getCheckboxLabel(formData[col.dataIndex + '_' + index])}}
            </span>
          </span>
        </a-form-model-item>
          
      </template>

      <!-- <template v-for="slotName of scopedSlotKeys" :slot="slotName" slot-scope="text, record">
        <slot :name="slotName" :record="record"></slot>
      </template> -->
      
      <template v-for="slotName of scopedSlotKeys" :slot="slotName" slot-scope="text, record, index">
        <slot :name="slotName" :text="text" :record="record" :index="index"></slot>
      </template>
    </a-table>
  </a-form-model>
</template>

<script>
import JDictSelect from '@/components/Jackeroo/Form/Selector/Select'
import JDictCodeSelect from '@/components/Jackeroo/Form/Selector/DictCodeSelector/DictCodeSelect'
import {getAction} from '@/api/manage'
import {Ellipsis} from '@/components'
import DynamicComponent from './Component'
import { call } from 'lodash.pick'

export default {
  name: 'EditTable',
  components: {
    JDictSelect,
    JDictCodeSelect,
    Ellipsis,
    DynamicComponent
  },
  props:{
    // 列信息
    columns: {
      type: Array,
      required: true
    },
    // 数据源
    dataSource: {
      type: Array,
      required: true,
      default: () => []
    },
    rowKey: {
      type: String,
      required: true
    },
    disabled: {
      type: Boolean,
      default: false
    },
    // 行编辑状态开关，如果开启，则根据行数据的editAble属性决定当前行是否可编辑，默认为false
    editSwitch: {
      type: Boolean,
      default: false
    }
  },
  data(){
    return {
      size: 'default',
      formData: {},
      toggle: false,
      // 用于存储select中包含dictCode的数据，避免多次请求dictCode接口，提高效率
      dictData:{},
    }
  },
  watch: {
    dataSource(newVal){
      this.buildFormTalbe(newVal)
    },
    columns(newVal){
      this.loadColumnDictCode(newVal)
    }
  },
  beforeMount(){
    if(this.dataSource && this.dataSource.length > 0){
      this.buildFormTalbe(this.dataSource)
    }
    this.loadColumnDictCode(this.columns)
  },
  computed: {
    attrs(){
      const attrs = this.$attrs
      if(!attrs.rowClassName){
        attrs.rowClassName = () => 'jackeroo-row'
      }
      return attrs
    },
    columnsCopy(){
      const data = [...this.columns]
      this.loopColumnsCopy(data)
      return data
    },
    // 所包含的表单列
    formFields(){
      const data = []
      for(var item of this.columns){
        if(item.dataIndex && item.type && item.type != 'solt'){
          data.push(item.dataIndex)
        }
      }
      return data
    },
    columnsSlot(){
      return this.loopColumnsSlot([...this.columns])
    },
    scopedSlotKeys(){
      return Object.keys(this.$scopedSlots)
    },
    rules(){
      const rules = {}
      for(const col of this.columns){
        if(col.rule){
          rules[col.dataIndex] = col.rule
        }
      }
      return rules
    }
  },
  methods: {
    loopColumnsCopy(columns){
      for (const column of columns) {
        if(!column.children){
          if(column.type != 'slot' && column.dataIndex){
            column.scopedSlots = { customRender: column.dataIndex }
          }
        }else{
          this.loopColumnsCopy(column.children)
        }
      }
    },
    loopColumnsSlot(columns){
      const slot = columns.filter(item => (item.dataIndex && item.type != 'slot') || (!item.dataIndex && item.children))
      for (const item of slot) {
        if(!item.children){
          const attrs = item.attrs || {}
          // 指定默认大小
          if(!attrs.size){
            attrs.size = this.size
          }
          // 对于可清除的表单组件，默认启用清除
          if(attrs.allowClear != false){
            attrs.allowClear = true
          }
          item.attrs = attrs
        }else{
          slot.push(...this.loopColumnsSlot(item.children))
        }
      }
      return slot
    },
    // 加载列中包含dictCode的数据
    async loadColumnDictCode(columns){
      for(const col of columns){
        if((col.type == 'select' || col.type =='multiple') && col.dictCode){
          /* getAction('/system/dict/getDictItemList', {dictCode: col.dictCode}).then(result => {
            if(!result.code){
              this.dictData[col.dictCode] = result.data
            }
          }) */

          const result = await getAction('/system/dict/getDictItemList', {dictCode: col.dictCode})
          if(!result.code){
            this.dictData[col.dictCode] = result.data
          }
        }
      }
      this.$nextTick(() => {
        this.toggle = true
      })
    },
    buildFormTalbe(newVal){
      const formData = {}
      for(let i = 0; i < newVal.length; i++){
        for(let prop in newVal[i]){
          for(const col of this.columns){
            if(prop == col.dataIndex){
              if(col.type == 'checkbox' || col.type == 'multiple'){
                if(typeof newVal[i][prop] == 'string' && newVal[i][prop].indexOf(',') > -1){
                  formData[prop + '_' + i] = newVal[i][prop].split(',')
                }else{
                  formData[prop + '_' + i] = newVal[i][prop] ? [newVal[i][prop]] : []
                }
              }else{
                formData[prop + '_' + i] = newVal[i][prop]
              }
            }
          }
        }
      }
      this.formData = formData
    },
    // 自定义组件动态事件构建
    componentEventsConvert(events, record, index){
      const event = {}
      for (const e in events) {
        event[e] = (arg) => {
          events[e](arg, record, index)
        }
      }
      return event
    },
    // form组件动态事件转换
    formEvnentsConvert(events, record, index){
      const event = {}
      for (const e in events) {
        event[e] = function(){
          events[e](arguments, record, index)
        }
      }
      return event
    },
    handleChange(value, record, colName){
      record[colName] = value
    },
    filterColumn(columnName){
      const column = this.columns.filter(item => item.dataIndex == columnName)
      return column[0] || null
    },
    // 获取checkbox的label值
    getCheckboxLabel(value){
      if(value == 1){
        return '√'
      }else{
        return ''
      }
    },
    // 获取select组件的label值
    getSelectLabel(value, options){
      if(Array.isArray(value)){
        const select = options.filter(item => value.indexOf(item.value) > -1)
        if(select.length > 0){
          const val = []
          for (const item of select) {
            val.push(item.label)
          }
          return val.join(',')
        }else{
          return ''
        }
      }else{
        const select = options.filter(item => item.value == value)
        if(select.length == 1){
          return select[0].label
        }else{
          return ''
        }
      }
    },
    // 获取级联下拉的label值
    getCascaderLabel(value, options){
      if(!value || value.length == 0){
        return ''
      }
      const label = []
      let optionList = options
      for (const val of value) {
        let seleected = optionList.filter(item => item.value == val)[0]
        label.push(seleected.label)

        optionList = seleected.children
      }
      return label.join(',')
    },
    // 获取某个表单组件的值
    getFormFieldValue(columnName, rowIndex){
      return this.formData[columnName + '_' + rowIndex]
    },
    // 设置某个表单组件的值
    setFormFieldValue(columnName, rowIndex, value){
      this.$set(this.formData, columnName + '_' + rowIndex, value)
    },
    // 不通过校验，直接获取行数据
    getRowValues(rowIndex){
      const row = {...this.dataSource[rowIndex]}
      for (const key of this.formFields) {
        const col = this.filterColumn(key)
        if(col){
          if(col.type == 'checkbox'){
            row[key] = (this.formData[key + '_' + rowIndex] ? this.formData[key + '_' + rowIndex][0] : '') || ''
          }else if(col.type == 'multiple'){
            row[key] = this.formData[key + '_' + rowIndex] ? this.formData[key + '_' + rowIndex].join(',') : ''
          }else{
            row[key] = this.formData[key + '_' + rowIndex]
          }
        }
      }
      return row
    },
    // 验证指定行数据，并返回结果
    getValidateRow(rowIndex, callback){
      const props = []
      for (const key of this.formFields) {
          props.push(key + '_' + rowIndex)
      }
      const errors = []
      this.$refs.formTable.validateField(props, (error) => {
        if(error){
          errors.push(error)
        }
      })

      if(errors.length == 0){
        callback(null, this.getRowValues(rowIndex))
      }else{
        callback(errors, null)
      }
    },
    // 验证table中的表单
    async validate(callback){
      var success = await this.$refs.formTable.validate()
      if(callback && typeof callback === 'function'){
        callback(success)
      }else{
        return success
      }
    },
    /**
     * 获取EditTable所有的行数据；
     * callback为回掉函数，并传入一个参数: 所有行数据，callback: Function(data)；
     * 如果不传callback，则返回一个promise函数
     */
    async getValues(callback){
      var success = await this.$refs.formTable.validate().catch((e) => {
        return false
      })
      if(success != undefined && success){
        const data = this.getData()
        if (callback && typeof callback === 'function') {
          callback(data)
        }else{
          return data
        }
      }else{
        if (callback && typeof callback === 'function') {
          callback(null)
        }else{
          return null
        }
      }
    },
    // 生成临时的行id
    generateId(){
      return 'EditTable_' + new Date().getTime() + Math.random().toString().substring(2)
    },
    // 清除表单验证
    clearValidate(){
      this.$refs.formTable.clearValidate()
    },
    // 跳过表单校验，并获取数据
    getData(){
      const data = [...this.dataSource]
      for(const prop in this.formData){
        let index = prop.lastIndexOf('_')
        let i = prop.substring(index + 1)
        let p = prop.substring(0, index)

        const col = this.filterColumn(p)
        if(col){
          if(col.type == 'checkbox'){
            data[i][p] = this.formData[prop][0]
          }else if(col.type == 'multiple'){
            data[i][p] = this.formData[prop].join(',')
          }else{
            data[i][p] = this.formData[prop]
          }
        }
      }
      return data
    }
  }
}
</script>

<style lang="less" scoped>
.jackeroo-edit-table{
  .ant-form-item{
    margin-bottom: 0;
  }

  .jackeroo-row{
    td{
      padding: 12px !important;
    }
  }
}
</style>

<style lang="less">
.jackeroo-edit-table{
  .ant-form-item-control{
    line-height: normal !important;
  }
}
</style>