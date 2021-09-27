<template>
  <div>
    <search-card :enter="refreshData">
      <a-col :md="6" :sm="12">
        <a-form-item label="数据库类型">
          <j-dict-select
            v-model="queryParam.type"
            placeholder="请选择数据库类型"
            dictCode="GEN_DATABASE_TYPE"
            >
          </j-dict-select>
        </a-form-item>
      </a-col>
      <a-col :md="6" :sm="12">
        <a-form-item label="数据库名">
          <a-input v-model="queryParam.databaseName" placeholder="请输入数据库名"></a-input>
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
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="handleDeleteBatch"><a-icon type="delete" />删除</a-menu-item>
          </a-menu>
          <a-button style="margin-right: 8px">
            批量操作 <a-icon type="down" />
          </a-button>
        </a-dropdown>
        <a-button type="primary" icon="plus" v-action="'online:datasource:add'" @click="handleAdd">新建</a-button>
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
        <template slot="action" slot-scope="text, record">
          <action-list>
            <j-link :type="actionType.view" :icon="actionIcon.view" @click="handleView(record)">详情</j-link>
            <j-link :type="actionType.edit" :icon="actionIcon.edit" v-action="'online:datasource:update'" @click="handleEdit(record)">编辑</j-link>
            <j-link :type="actionType.delete" :icon="actionIcon.delete" v-action="'online:datasource:delete'" @click="handleDelete(record)">删除</j-link>
          </action-list>
        </template>
      </s-table>
    </data-card>

    <online-datasource-form-modal ref="formModal" @ok="handleOk"></online-datasource-form-modal>
  </div>
</template>-

<script>
import { STable, DataCard, SearchCard, JDictSelect} from '@/components'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import OnlineDatasourceFormModal from './modal/OnlineDatasourceFormModal'

export default {
  components: {
    STable,
    DataCard,
    SearchCard,
    JDictSelect,
    OnlineDatasourceFormModal
  },
  mixins:[JackerooListMixins],
  data () {
    return {
      columns: [
        {
          title: '#',
          width: 60,
          customRender: (text, record, index) => {
            return index + 1
          }
        },
        {
          title: '数据源名称',
          dataIndex: 'name'
        },
        {
          title: '数据库类型',
          dataIndex: 'driverClassName',
          customRender: (text) => {
            return this.loadDictText(text, this.dictOptions.type)
          }
        },
        {
          title: '连接地址',
          dataIndex: 'url',
          width: 380,
          ellipsis: true,
        },
        {
          title: '用户名',
          dataIndex: 'username',
        },
        {
          title: '更新时间',
          dataIndex: 'updateTime',
          width: 180,
          sort: true,
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: 220,
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/online/datasource/list',
        delete: '/online/datasource/delete',
        deleteBatch: '/online/datasource/deleteBatch',
        exportExcel: '/online/datasource/exportExcel'
      },
      dictOptions: {
        // 数据库类型
        type: {
          code: 'GEN_DATABASE_TYPE',
          options: []
        }
      }
    }
  },
  methods: {

  }
}
</script>