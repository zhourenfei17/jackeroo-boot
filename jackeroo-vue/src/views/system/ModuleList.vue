<template>
  <div>
    <search-card :enter="refreshData">
      <a-col :md="6" :sm="12">
        <a-form-item label="模块名称">
          <a-input v-model="queryParam.name" placeholder="请输入模块名称"/>
        </a-form-item>
      </a-col>
      <a-col :md="6" :sm="12">
        <a-form-item label="模块代码">
          <a-input v-model="queryParam.code" placeholder="请输入模块代码"/>
        </a-form-item>
      </a-col>

      <template slot="operate">
        <a-button type="primary" icon="search" @click="refreshData(true)">查询</a-button>
        <a-button style="margin-left: 8px" icon="reload" @click="reset">重置</a-button>
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
        <a-button type="primary" icon="plus" v-action="'system:module:add'" @click="handleAdd">新建</a-button>
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
              <j-link :type="actionType.view" :icon="actionIcon.view" @click="handleView(record)">详情</j-link>
              <j-link :type="actionType.edit" :icon="actionIcon.edit" v-action="'system:module:update'" @click="handleEdit(record)">编辑</j-link>
              <j-link :type="actionType.delete" :icon="actionIcon.delete" v-action="'system:module:delete'" @click="handleDelete(record)">删除</j-link>
            </action-list>
          </template>
        </span>
      </s-table>
    </data-card>

    <module-form-modal ref="formModal" @ok="handleOk"></module-form-modal>
  </div>
</template>

<script>
import { STable, JTag, DataCard, SearchCard } from '@/components'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { putAction, getAction, deleteAction } from '@/api/manage'
import ModuleFormModal from './modal/ModuleFormModal'

export default {
  name: 'RoleList',
  components: {
    STable,
    JTag,
    DataCard,
    SearchCard,
    ModuleFormModal
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
          title: '模块名称',
          dataIndex: 'name'
        },
        {
          title: '模块代码',
          dataIndex: 'code',
        },
        {
          title: '排序号',
          dataIndex: 'sort',
        },
        {
          title: '描述',
          dataIndex: 'remark',
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/system/module/list',
        delete: '/system/module/delete',
        deleteBatch: '/system/module/deleteBatch'
      },
    }
  },
  methods: {
    
  }
}
</script>