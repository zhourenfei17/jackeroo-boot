<template>
  <a-form-model ref="formTable" :model="formData">
    <a-table :dataSource="dataSource" :columns="columnsCopy" class="jackeroo-edit-table"
      v-bind="$attrs">
      <template v-for="col in columnsSlot" :slot="col.dataIndex" slot-scope="text, record, index" >
          
        <a-form-model-item 
          :prop="col.dataIndex + '_' + index" 
          :rules="(col.disabled || !record.enable || false) ? null : (rules[col.dataIndex] || null)" 
          :key="col.dataIndex + '_' + index">
          <a-input 
            v-if="col.type == 'input'" 
            v-model="formData[col.dataIndex + '_' + index]"
            :disabled="col.disabled || !record.enable || false"
            :size="size">
          </a-input>

          <a-select 
            v-else-if="col.type == 'select'" 
            v-model="formData[col.dataIndex + '_' + index]"
            allowClear
            style="width: 100%;" 
            :disabled="col.disabled || !record.enable || false"
            :size="size">
            <a-select-option v-for="opt in col.options" :key="opt.value" :value="opt.value">{{opt.text}}</a-select-option>
          </a-select>

          <a-select 
            v-else-if="col.type == 'multiple'" 
            mode="multiple"
            v-model="formData[col.dataIndex + '_' + index]"
            style="width: 100%;" 
            :disabled="col.disabled || !record.enable || false"
            :size="size">
            <a-select-option v-for="opt in col.options" :key="opt.value" :value="opt.value">{{opt.text}}</a-select-option>
          </a-select>

          <a-checkbox-group 
            v-else-if="col.type == 'checkbox'" 
            v-model="formData[col.dataIndex + '_' + index]"
            :disabled="col.disabled || !record.enable || false">
            <a-checkbox :value="1"></a-checkbox>
          </a-checkbox-group>

          <template v-else-if="col.type != 'slot'">
            {{text}}
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
export default {
  name: 'EditTable',
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
      formData: {}
    }
  },
  watch: {
    dataSource(newVal){
      this.buildFormTalbe(newVal)
    }
  },
  mounted(){
    if(this.dataSource && this.dataSource.length > 0){
      this.buildFormTalbe(this.dataSource)
    }
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
    buildFormTalbe(newVal){
      const formData = {}
      for(let i = 0; i < newVal.length; i++){
        for(let prop in newVal[i]){
          for(const col of this.columns){
            if(prop == col.dataIndex){
              if(col.type == 'checkbox' || col.type == 'multiple'){
                formData[prop + '_' + i] = newVal[i][prop] ? [newVal[i][prop]] : []
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
    async validate(){
      var success = await this.$refs.formTable.validate()
      return success
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