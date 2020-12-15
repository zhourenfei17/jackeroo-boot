<template>
  <a-form-model ref="formTable" :model="formData" v-if="toggle">
    <a-table :dataSource="dataSource" :columns="columnsCopy" :rowKey="rowKey" class="jackeroo-edit-table"
      v-bind="$attrs">
      <template v-for="col in columnsSlot" :slot="col.dataIndex" slot-scope="text, record, index" >
          
        <a-form-model-item 
          :prop="col.dataIndex + '_' + index" 
          :rules="(col.disabled || record.disabled || false) ? null : (rules[col.dataIndex] || null)" 
          :key="col.dataIndex + '_' + index">
          <a-input 
            v-if="col.type == 'input'" 
            v-model="formData[col.dataIndex + '_' + index]"
            :disabled="col.disabled || record.disabled || false"
            :size="size">
          </a-input>

          <j-dict-select 
            v-else-if="col.type == 'select' && col.dictCode" 
            v-model="formData[col.dataIndex + '_' + index]"
            :list="dictData[col.dictCode]"
            allowClear
            valueField="value" 
            textField="label"
            style="width: 100%;"
            :disabled="col.disabled || record.disabled || false" 
            :size="size">

          </j-dict-select>

          <a-select 
            v-else-if="col.type == 'select' && col.options" 
            v-model="formData[col.dataIndex + '_' + index]"
            allowClear
            style="width: 100%;" 
            :disabled="col.disabled || record.disabled || false"
            :size="size">
            <a-select-option v-for="opt in col.options" :key="opt.value" :value="opt.value">{{opt.text}}</a-select-option>
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
            :disabled="col.disabled || record.disabled || false" 
            :size="size">

          </j-dict-select>

          <a-select 
            v-else-if="col.type == 'multiple' && col.options" 
            mode="multiple"
            v-model="formData[col.dataIndex + '_' + index]"
            style="width: 100%;" 
            :disabled="col.disabled || record.disabled || false"
            :size="size">
            <a-select-option v-for="opt in col.options" :key="opt.value" :value="opt.value">{{opt.text}}</a-select-option>
          </a-select>

          <a-checkbox-group 
            v-else-if="col.type == 'checkbox'" 
            v-model="formData[col.dataIndex + '_' + index]"
            :disabled="col.disabled || record.disabled || false">
            <a-checkbox :value="1"></a-checkbox>
          </a-checkbox-group>

          <j-dict-code-select
            v-else-if="col.type == 'dictCodeSelector'" 
            v-model="formData[col.dataIndex + '_' + index]"
            :disabled="col.disabled || record.disabled || false">

          </j-dict-code-select>

          <template v-else-if="col.type != 'slot'">
            <ellipsis v-if="col.length" tooltip :length="col.length">{{text}}</ellipsis>
            <span v-else>{{text}}</span>
          </template>
        </a-form-model-item>
          
      </template>

      <template v-for="slotName of scopedSlotKeys" :slot="slotName">
        <slot :name="slotName"></slot>
      </template>
    </a-table>
  </a-form-model>
</template>

<script>
import JDictSelect from '@/components/Jackeroo/Form/Selector/Select'
import JDictCodeSelect from '@/components/Jackeroo/Form/Selector/DictCodeSelector/DictCodeSelect'
import {getAction} from '@/api/manage'
import {Ellipsis} from '@/components';

export default {
  name: 'EditTable',
  components: {
    JDictSelect,
    JDictCodeSelect,
    Ellipsis
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
    disable: {
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
    columnsCopy(){
      const data = [...this.columns]
      for(var item of data){
        item.scopedSlots = { customRender: item.dataIndex }
      }

      return data
    },
    columnsSlot(){
      const data = [...this.columns]
      return data.filter(item => item.type != 'slot')
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
    handleChange(value, record, colName){
      record[colName] = value
    },
    filterColumn(columnName){
      const column = this.columns.filter(item => item.dataIndex == columnName)
      return column[0] || null
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
}
</style>