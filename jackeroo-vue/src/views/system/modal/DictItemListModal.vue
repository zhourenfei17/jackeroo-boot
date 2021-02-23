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
        <a-button type="primary" icon="plus" v-action="'system:dict:add'" @click="handleAdd">新建</a-button>
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
              <j-link :type="actionType.view" :icon="actionIcon.view" @click="handleView(record)">详情</j-link>
              <j-link :type="actionType.edit" :icon="actionIcon.edit" v-action="'system:dict:update'" @click="handleEdit(record)" v-if="record.category != 0">编辑</j-link>
              <j-link :type="actionType.delete" :icon="actionIcon.delete" v-action="'system:dict:delete'" @click="handleDelete(record)" v-if="record.category != 0">删除</j-link>
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
        delete: '/system/dict/deleteDictItem'
      },
      category: undefined
    }
  },
  methods: {
    load(dictCode, category){
      this.queryParam.dictCode = dictCode
      this.category = category
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
      this.$refs.formModal.add(this.queryParam.dictCode, this.category)
    }
  }
}
</script>