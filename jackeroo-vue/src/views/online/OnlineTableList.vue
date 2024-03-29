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
        <a-button type="primary" icon="search" @click="refreshData(true)">查询</a-button>
        <a-button style="margin-left: 8px" icon="reload" @click="reset">重置</a-button>
      </template>
    </search-card>

    <data-card 
        :reload="refreshData" 
        :tableSize.sync="tableSize" 
        :columns.sync="columns"
        tableAlign="left">

      <template slot="toolbar">
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="handleDeleteBatch"><a-icon type="delete" />删除</a-menu-item>
          </a-menu>
          <a-button style="margin-right: 8px">
            批量操作 <a-icon type="down" />
          </a-button>
        </a-dropdown>
        <a-button @click="handleOpenDefaultConfigModal" icon="setting" v-action="'online:generate:setting'" style="margin-right:8px;">默认配置</a-button>
        <a-button type="primary" icon="plus" v-action="'online:generate:add'" @click="handleAdd">新建</a-button>
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
              <j-link :type="actionType.view" :icon="actionIcon.edit" v-action="'online:generate:update'" @click="handleEdit(record)">编辑</j-link>
              <j-link icon="code" v-action="'online:generate:generate'" @click="handleGenerate(record.id)">生成代码</j-link>
              <j-link :type="actionType.delete" :icon="actionIcon.delete" v-action="'online:generate:delete'" @click="handleDelete(record)">删除</j-link>
            </action-list>
          </template>
        </span>
      </s-table>
    </data-card>

    <select-table-modal ref="selectTableModal" @ok="handleSelectOk"></select-table-modal>
    <generate-table-column ref="generateTableColumn" @ok="handleGenerateOk" @generate="handleGenerate"></generate-table-column>
    <generate-file-select ref="generateFileSelect"></generate-file-select>
    <default-config-modal ref="defaultConfigModal"></default-config-modal>
  </div>
</template>

<script>
import { STable, JTag, DataCard, SearchCard } from '@/components'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import SelectTableModal from './modal/SelectTableModal'
import GenerateTableColumn from './modal/GenerateTableColumn'
import GenerateFileSelect from './modal/GenerateFileSelect'
import DefaultConfigModal from './modal/DefaultConfigModal'

export default {
  name: 'OnlineTableList',
  components: {
    STable,
    JTag,
    DataCard,
    SearchCard,
    SelectTableModal,
    GenerateTableColumn,
    GenerateFileSelect,
    DefaultConfigModal
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
          customRender: (text) => {
            return this.loadDictText(text, this.dictOptions.idStrategy)
          }
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
        delete: '/online/table/delete',
        generateCode: '/online/generate/generateCode',
        deleteBatch: '/online/table/deleteBatch'
      },
      dictOptions: {
        idStrategy: {
          code: 'GEN_ID_STRATEGY',
          options: []
        }
      }
    }
  },
  methods: {
    /* initDictionary(){
      loadDictItemByCode('GEN_ID_STRATEGY').then(result => {
        this.dictOptions.idStrategy = result
      })
    }, */
    handleAdd(){
      this.$refs.selectTableModal.visible = true
      this.$refs.selectTableModal.add()
    },
    handleSelectOk({table, dataSource}){
      this.$refs.generateTableColumn.add(table, dataSource)
    },
    handleGenerateOk(){
      this.refreshData()
    },
    handleEdit(record){
      this.$refs.generateTableColumn.visible = true
        this.$refs.generateTableColumn.edit(record.id)
    },
    handleOpenDefaultConfigModal(){
      this.$refs.defaultConfigModal.visible = true
      this.$refs.defaultConfigModal.edit()
    },
    // 生成代码
    handleGenerate(id){
      this.$refs.generateFileSelect.show(id)
    }
  }
}
</script>