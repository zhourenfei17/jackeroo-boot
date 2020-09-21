<template>
  <div>
    <search-card :enter="refreshData">
      <a-col :md="6" :sm="12">
        <a-form-item label="表名">
          <a-input v-model="queryParam.tableName" placeholder="请输入表名"/>
        </a-form-item>
      </a-col>
      <a-col :md="6" :sm="12">
        <a-form-item label="表说明">
          <a-input v-model="queryParam.code" placeholder="请输入表说明"/>
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
        tableAlign="left">

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
              <action-menu-list>
                <a @click="handleDelete(record)">删除</a>
              </action-menu-list>
            </action-list>
          </template>
        </span>
      </s-table>
    </data-card>

    <select-table-modal ref="selectTableModal"></select-table-modal>
  </div>
</template>

<script>
import { STable, JTag, DataCard, SearchCard } from '@/components'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { putAction, getAction, deleteAction } from '@/api/manage'
import SelectTableModal from './modal/SelectTableModal'

export default {
  name: 'OnlineTableList',
  components: {
    STable,
    JTag,
    DataCard,
    SearchCard,
    SelectTableModal
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
          title: '表名',
          dataIndex: 'tableName'
        },
        {
          title: '表说明',
          dataIndex: 'comment',
        },
        {
          title: '主键策略',
          dataIndex: 'idStrategy',
        },
        {
          title: '创建时间',
          dataIndex: 'createTime',
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/online/table/list',
        delete: '/online/table/delete'
      },
    }
  },
  methods: {
    handleAdd(){
      this.$refs.selectTableModal.visible = true
      this.$refs.selectTableModal.add()
    }
  }
}
</script>