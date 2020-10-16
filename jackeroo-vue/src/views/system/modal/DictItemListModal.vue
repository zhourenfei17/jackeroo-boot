<template>
  <a-drawer
      :title="title"
      :width="width"
      :visible="visible"
      @close="cancel">
    
    <data-card
        :reload="refreshData"
        :tableSize.sync="tableSize"
        :columns.sync="columns"
        :icon="['refresh', 'lineHeight', 'align', 'columnSet']"
        :tableAlign="tableAlign"
        style="margin-top:-24px;">
      <template slot="toolbar">
        <a-button type="primary" icon="plus" @click="handleAdd">新建</a-button>
      </template>

      <s-table
          ref="table"
          :size="tableSize"
          rowKey="id"
          :columns="columns"
          :data="loadData"
          bordered
          lazy
          :showPagination="showPagination"
        >

        <span slot="action" slot-scope="text, record">
          <template>
            <action-list>
              <a @click="handleView(record)">详情</a>
              <a @click="handleEdit(record)">编辑</a>
              <action-menu-list>
                <a @click="handleDelete(record)">删除</a>
              </action-menu-list>
            </action-list>
          </template>
        </span>
      </s-table>
    </data-card>

    <dict-item-form-modal ref="formModal" @ok="handleOk"></dict-item-form-modal>
  </a-drawer>
</template>

<script>
import { getAction, postAction, httpAction } from '@/api/manage'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { STable, DataCard} from '@/components'
import DictItemFormModal from './DictItemFormModal'

export default {
  components: {
    STable,
    DataCard,
    DictItemFormModal
  },
  mixins: [JackerooListMixins],
  data() {
    return {
      title: '字典项列表',
      width: '40vw',
      visible: false,
      dataSource: [],
      columns: [
        {
          title: '#',
          customRender: (text, record, index) => {
            return index + 1
          }
        },
        {
          title: '字典项名称',
          dataIndex: 'label',
        },
        {
          title: '字典项值',
          dataIndex: 'value',
        },
        {
          title: '排序号',
          dataIndex: 'sort',
          sorter: true,
          defaultSortOrder: 'ascend'
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/system/dict/itemList',
        delete: '/system/dict/delete'
      },
    }
  },
  methods: {
    load(dictCode){
      this.queryParam.dictCode = dictCode
      this.visible = true
      this.$nextTick(() => {
        this.refreshData()
      })
    },
    cancel(){
      this.visible = false
    },
    // 添加
    handleAdd () {
      this.$refs.formModal.visible = true
      this.$refs.formModal.flag.add = true
      this.$refs.formModal.add(this.queryParam.dictCode)
    }
  }
}
</script>