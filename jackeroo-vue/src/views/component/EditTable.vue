<template>
  <data-card 
    :icon="[]"
    >

      <template slot="toolbar">
        <a-button type="primary" @click="handleAdd">添加</a-button>
        行编辑状态：<a-switch checkedChildren="开" unCheckedChildren="关" @change="handleRowEditChange"></a-switch>
      </template>

      <edit-table
        ref="editTable"
        rowKey="id"
        :scroll="{x: 2000}"
        :columns="columns"
        :editSwitch="editSwitch"
        :dataSource="dataSource">

        <template v-slot:action="{record, index}">
          <action-list>
            <j-link v-if="editSwitch && !record.editAble" @click="handleEdlt(record)">编辑</j-link>
            <j-link v-if="editSwitch && record.editAble" @click="handleSave(record, index)">保存</j-link>
            <a-popconfirm title="您确定要删除该行数据吗？" @confirm="() => handleDelete(record)">
              <j-link>删除</j-link>
            </a-popconfirm>
          </action-list>
        </template>
      </edit-table>
  </data-card>
</template>

<script>
import {EditTable, DataCard} from '@/components'
import {ValidatorMixins} from '@/mixins/ValidatorMixins'

export default {
  components: {
    EditTable,
    DataCard
  },
  mixins:[ValidatorMixins],
  data() {
    return {
      dataSource: [],
      nativePlace: [
        {
          label: '上海',
          value: 'shanghai',
          children: [
            {
              label: '上海市',
              value: 'shanghai',
              children: [
                {
                  label: '徐汇区',
                  value: 'xuhui'
                }
              ]
            }
          ]
        },
        {
          label: '江苏',
          value: 'jiangsu',
          children: [
            {
              label: '南京市',
              value: 'nanjing',
              children: [
                {
                  label: '鼓楼区',
                  value: 'gulou'
                }
              ]
            },
            {
              label: '南通市',
              value: 'nantong',
              children: [
                {
                  label: '崇川区',
                  value: 'chongchuan'
                }
              ]
            }
          ]
        }
      ],
      multiOptions:[
        {
          label: '北京',
          value: 'beijing'
        },
        {
          label: '上海',
          value: 'shanghai'
        },
        {
          label: '广州',
          value: 'guangzhou'
        },
        {
          label: '深圳',
          value: 'shenzhen'
        }
      ],
      editSwitch: false
    }
  },
  computed: {
    columns() {
      return [
        {
          title: '#',
          width: 60,
          customRender: (text, record, index) => {
            return index + 1
          }
        },
        {
          dataIndex: 'input',
          title: '输入框',
          type: 'input',
          width: 120,
          attrs:{
            placeholder: '请输入内容'
          }
        },
        {
          dataIndex: 'inputNumber',
          title: '数字输入框',
          type: 'inputNumber',
          width: 120,
          attrs: {
            placeholder: '请输入数字'
          }
        },
        {
          dataIndex: 'select',
          title: '下拉框',
          type: 'select',
          width: 120,
          options: [
            {
              label: '男',
              value: 1
            },
            {
              label: '女',
              value: 2
            }
          ],
          attrs: {
            placeholder: '请选择'
          }
        },
        {
          dataIndex: 'cascader',
          title: '级联框',
          type: 'cascader',
          width: 200,
          options: this.nativePlace,
          attrs: {
            placeholder: '请选择省份/城市/区县'
          }
        },
        {
          dataIndex: 'multiple',
          title: '多选框',
          type: 'multiple',
          width: 180,
          options: this.multiOptions,
          attrs: {
            placeholder: '请选择'
          }
        },
        {
          dataIndex: 'checkbox',
          title: '复选框',
          type: 'checkbox',
          width: 80,
        },
        {
          dataIndex: 'component',
          title: '自定义组件',
          type: 'component',
          width: 200,
          component: 'components/Jackeroo/Form/Selector/DictCodeSelector/DictCodeSelect',
          events:{
            change: (arg, record, index) => {
              this.$message.info('自定义change事件：' + JSON.stringify(arg[0]))
            }
          }
        },
        {
          dataIndex: 'selectEvent',
          title: '自定义事件',
          width: 150,
          type: 'select',
          options: [
            {
              label: '优秀',
              value: 1
            },
            {
              label: '良好',
              value: 2
            }
          ],
          attrs: {
            placeholder: '选择改变后一列值'
          },
          events: {
            change: (arg, record, index) => {
              this.$refs.editTable.setFormFieldValue('disable', index, arg[0] || '')
            }
          }
        },
        {
          dataIndex: 'disable',
          title: '禁用列',
          width: 100,
          type: 'input',
          disabled: true
        },
        {
          dataIndex: 'validate',
          title: '自定义验证',
          width: 120,
          type: 'input',
          attrs: {
            allowClear: false,
            placeholder: '手机号'
          },
          rule:[
            {
              required: true,
              message: '请填写内容'
            },
            {
              validator: this.validMobile
            }
          ]
        },
        {
          title: '操作',
          type: 'slot',
          width: 150,
          scopedSlots:{ customRender: 'action' }
        }
      ]
    }
  },
  methods: {
    handleRowEditChange(){
      this.editSwitch = !this.editSwitch
    },
    handleAdd(){
      const data = this.$refs.editTable.getData()
      data.push({id: this.$refs.editTable.generateId(), editAble: false})

      this.dataSource = data 
    },
    handleEdlt(record){
      // record.editAble = true
      // this.$set(record, 'editAble', true)
      record.editAble = true
    },
    handleSave(record, index){
      // this.$set(record, 'editAble', false)
      this.$refs.editTable.getValidateRow(index, (error, values) => {
        if(!error){
          alert(JSON.stringify(values))
          record.editAble = false
        }
      })
    },
    handleDelete(record){
      const data = this.$refs.editTable.getData()
      const dataSource = data.filter(item => item.id != record.id)

      this.dataSource = dataSource
    }
  }
}
</script>