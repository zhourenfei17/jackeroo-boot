<template>
  <div><#if existQuery>
    <search-card :enter="refreshData">
        <#list columnList as column>
            <#if column.enableQuery == 1>
      <a-col :md="6" :sm="12">
        <a-form-item label="${column.dbFieldDesc}">
                <#if column.formType == "input">
          <a-input v-model="queryParam.${column.entityFieldName}" placeholder="请输入${column.dbFieldDesc}"/>
                <#elseif column.formType == "select">
          <a-select v-model="queryParam.${column.entityFieldName}" placeholder="请选择${column.dbFieldDesc}"/>
                <#elseif column.formType == "input">
                <#elseif column.formType == "input">
                </#if>
        </a-form-item>
      </a-col>
            </#if>
        </#list>

        <template slot="operate">
            <a-button type="primary" @click="refreshData(true)">查询</a-button>
            <a-button style="margin-left: 8px" @click="reset">重置</a-button>
        </template>
    </search-card></#if>
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
              <action-menu-list>
                <a @click="handleDelete(record)">删除</a>
              </action-menu-list>
            </action-list>
          </template>
        </span>
      </s-table>
    </data-card>

    <${componentName}-form-modal ref="formModal" @ok="handleOk"></${componentName}-form-modal>
  </div>
</template>-

<script>
import { STable, DataCard, SearchCard } from '@/components'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { getAction, deleteAction } from '@/api/manage'
import ${table.className}FormModal from './modal/${table.className}FormModal'

export default {
  name: 'RoleList',
  components: {
    STable,
    DataCard,
    SearchCard,
    ${table.className}FormModal
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
        <#list columnList as column>
            <#if column.enableList == 1>
        {
          title: '${column.dbFieldDesc}',
          dataIndex: '${column.entityFieldName}',
                <#if column.enableSort == 1>
          sort: true
                </#if>
        },
            </#if>
        </#list>
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/${module.code}/${pathName}/list',
        delete: '/${module.code}/${pathName}/delete'
      },
    }
  },
  methods: {

  }
}
</script>