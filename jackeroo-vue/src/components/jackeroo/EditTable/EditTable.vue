<template>
  <a-table :dataSource="dataSource" :columns="columnsCopy"
    v-bind="$attrs">
    <template v-for="col in columnsSlot" :slot="col.dataIndex" slot-scope="text, record, index">
      <div :key="col.dataIndex">
        <a-form-model :ref="col.dataIndex + index" :rules="col.rule || {}">
          <a-form-model-item>
            <a-input 
              v-if="col.type == 'input'" 
              :value="text" 
              @change="e => handleChange(e.target.value, record, col.dataIndex)" 
              :disabled="col.disabled || !record.enable || false"
              :size="size">
            </a-input>

            <a-select 
              v-else-if="col.type == 'select'" 
              :value="text" 
              @change="(v) => handleChange(v, record, col.dataIndex)" 
              allowClear
              style="width: 100%;" 
              :disabled="col.disabled || !record.enable || false"
              :size="size">
              <a-select-option v-for="opt in col.options" :key="opt.value" :value="opt.value">{{opt.text}}</a-select-option>
            </a-select>

            <a-select 
              v-else-if="col.type == 'multiple'" 
              mode="multiple"
              :value="text ? text.split(',') : []" 
              @change="(v) => handleChange(v.join(','), record, col.dataIndex)" 
              style="width: 100%;" 
              :disabled="col.disabled || !record.enable || false"
              :size="size">
              <a-select-option v-for="opt in col.options" :key="opt.value" :value="opt.value">{{opt.text}}</a-select-option>
            </a-select>

            <a-checkbox 
              v-else-if="col.type == 'checkbox'" 
              :checked="text == 1" 
              @change="(e) => handleChange(e.target.checked ? 1 : 0, record, col.dataIndex)"
              :disabled="col.disabled || !record.enable || false">

            </a-checkbox>

            <template v-else-if="col.type != 'slot'">
              {{text}}
            </template>
          </a-form-model-item>
        </a-form-model>
      </div>
    </template>

    <template v-for="slotName of scopedSlotKeys" :slot="slotName">
      <slot :name="slotName"></slot>
    </template>
  </a-table>
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
      size: 'default'
    }
  },
  computed: {
    _attrs(){
      return this.$attrs
    },
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
    }
  },
  methods: {
    handleChange(value, record, colName){
      record[colName] = value
      // const target = this.dataSource.filter(item => key === item[this.rowKey])[0]
      // if(target){
        // target[colName] = value
        //this.dataSource = newData
        // this.validRowOne(target, value, colName)

        //this.$emit('update', newData)
      // }
    },
    validate(){
      const refs = this.$refs
      console.log('refs', refs)

      this.$refs.dbFieldName0.validate((success) => {
        console.log('success')
      })
      /* for(let ref in refs){
        console.log(ref)
        this.$refs[ref].validate((success) => {
          console.log(success)
        })
      } */
    }
  }
}
</script>