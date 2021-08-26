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
          customRender: (text, record, index) => {
            return index + 1
          }
        },
        {
          title: '数据库类型',
          dataIndex: 'type',
          customRender: (text) => {
            return this.loadDictText(text, this.dictOptions.type)
          }
        },
        {
          title: 'ip地址',
          dataIndex: 'ip',
        },
        {
          title: '端口',
          dataIndex: 'port',
        },
        {
          title: '用户名',
          dataIndex: 'user',
        },
        {
          title: '数据库名',
          dataIndex: 'databaseName',
        },
        {
          title: '更新时间',
          dataIndex: 'updateTime',
          sort: true,
        },
        {
          title: '操作',
          dataIndex: 'action',
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