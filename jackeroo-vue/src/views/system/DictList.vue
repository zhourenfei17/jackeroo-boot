<template>
  <div>
      <search-card :enter="refreshData">
        <a-col :md="6" :sm="12">
        <a-form-item label="字典名称">
          <a-input v-model="queryParam.dictName" placeholder="请输入字典名称"/>
        </a-form-item>
      </a-col>
      <a-col :md="6" :sm="12">
        <a-form-item label="字典编码">
          <a-input v-model="queryParam.dictCode" placeholder="请输入字典编码"/>
        </a-form-item>
      </a-col>

        <template slot="operate">
            <a-button type="primary" @click="refreshData(true)">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
        </template>
    </search-card>
    <data-card
        :reload="refreshData"
        :tableSize.sync="tableSize"
        :columns.sync="columns"
        :tableAlign="tableAlign">

      <template slot="toolbar">
        <a-button type="primary" icon="plus" @click="handleAdd">新建</a-button>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1"><a-icon type="delete" />删除</a-menu-item>
          </a-menu>
          <a-button style="margin-left: 8px">
            批量操作 <a-icon type="down" />
          </a-button>
        </a-dropdown>
      </template>

      <s-table
        ref="table"
        :size="tableSize"
        rowKey="id"
        :columns="columns"
        :data="loadData"
        :alert="tableAlert"
        :rowSelection="rowSelection"
        :showPagination="showPagination"
        >
        <span slot="action" slot-scope="text, record">
          <template>
            <action-list>
              <a @click="handleView(record)">详情</a>
              <a @click="handleEdit(record)">编辑</a>
              <a @click="handleDictItem(record)">字典项配置</a>
              <action-menu-list>
                <a @click="handleDelete(record)">删除</a>
              </action-menu-list>
            </action-list>
          </template>
        </span>
      </s-table>
    </data-card>

    <dict-form-modal ref="formModal" @ok="handleOk"></dict-form-modal>
    <dict-item-list-modal ref="dictItemListModal"></dict-item-list-modal>
  </div>
</template>-

<script>
import { STable, DataCard, SearchCard } from '@/components'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { getAction, deleteAction } from '@/api/manage'
import DictFormModal from './modal/DictFormModal'
import DictItemListModal from './modal/DictItemListModal'

export default {
  name: 'RoleList',
  components: {
    STable,
    DataCard,
    SearchCard,
    DictFormModal,
    DictItemListModal
  },
  mixins:[JackerooListMixins],
  data () {
    return {
      columns: [
        {
          title: '#',
          customRender: (text, record, index) => {
            return index + 1
          }
        },
        {
          title: '字典名称',
          dataIndex: 'dictName',
        },
        {
          title: '字典编码',
          dataIndex: 'dictCode',
        },
        {
          title: '备注',
          dataIndex: 'remark',
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: 260,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/system/dict/list',
        delete: '/system/dict/delete'
      },
    }
  },
  methods: {
    handleDictItem(record){
      this.$refs.dictItemListModal.load(record.dictCode)
    }
  }
}
</script>