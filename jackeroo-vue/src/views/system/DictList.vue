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
      <a-col :md="6" :sm="12">
        <a-form-item label="字典类别">
          <j-select v-model="queryParam.category" placeholder="请选择字典类别" :list="[{id:0, name: '系统字典'},{id: 1, name: '自定义'}]"></j-select>
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
        <a-button type="primary" icon="plus" v-action="'system:dict:add'" @click="handleAdd">新建</a-button>
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
        <template slot="categorySlot" slot-scope="text">
          <j-tag :type="text == 0 ? 'error' : 'primary'" :text="text == 0 ? '系统字典' : '自定义'"></j-tag>
        </template>
        <span slot="action" slot-scope="text, record">
          <template>
            <action-list>
              <j-link :type="actionType.view" :icon="actionIcon.view" @click="handleView(record)">详情</j-link>
              <j-link :type="actionType.edit" :icon="actionIcon.edit" v-action="'system:dict:update'" @click="handleEdit(record)" v-if="record.category != 0">编辑</j-link>
              <j-link icon="bars" @click="handleDictItem(record)">字典项配置</j-link>
              <j-link :type="actionType.delete" :icon="actionIcon.delete" v-action="'system:dict:update'" @click="handleDelete(record)" v-if="record.category != 0">删除</j-link>
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
import { STable, DataCard, SearchCard, JTag, JSelect } from '@/components'
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
    DictItemListModal,
    JTag,
    JSelect
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
          title: '字典类别',
          dataIndex: 'category',
          scopedSlots: {customRender: 'categorySlot'}
        },
        {
          title: '备注',
          dataIndex: 'remark',
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/system/dict/list',
        delete: '/system/dict/deleteDict'
      },
    }
  },
  methods: {
    handleDictItem(record){
      this.$refs.dictItemListModal.load(record.dictCode, record.category)
    }
  }
}
</script>