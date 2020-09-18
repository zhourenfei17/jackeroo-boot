<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :fullscreen.sync="fullscreen"
    :switchFullscreen="showFullscreenBtn"
    :confirmLoading="loading"
    okText="下一步"
    @ok="handleSubmit"
    @cancel="cancel"
  >
    <search-card :enter="refreshData" :gutter="24">
      <a-col :md="9" :sm="12">
        <a-form-item label="表名">
          <a-input v-model="queryParam.tableName" placeholder="请输入表名"/>
        </a-form-item>
      </a-col>
      <a-col :md="9" :sm="12">
        <a-form-item label="表说明">
          <a-input v-model="queryParam.comment" placeholder="请输入表说明"/>
        </a-form-item>
      </a-col>

      <template slot="operate">
        <a-button type="primary" @click="refreshData(true)">查询</a-button>
        <a-button style="margin-left: 8px" @click="reset">重置</a-button>
      </template>
    </search-card>

    <s-table
      ref="table"
      :size="tableSize"
      :rowKey="tableKey"
      :columns="columns"
      :data="loadData"
      :alert="tableAlert"
      :rowSelection="rowSelection"
      :showPagination="showPagination"
      :customRow="customRow"
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
  </j-modal>
</template>

<script>
import { STable, SearchCard } from '@/components'
import { getAction, postAction, httpAction } from '@/api/manage'
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'

export default {
  components: {
    STable,
    SearchCard
  },
  mixins: [JackerooFromMixins, JackerooListMixins],
  data(){
    return {
      title: '选择数据库业务表',
      width: '50vw',
      tableKey: 'tableName',
      columns:[
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
        }
      ],
      url: {
        list: '/online/generate/findTableListFromDataSource'
      }
    }
  },
  methods: {
    add(){
      this.loading = false
    },
    handleSubmit(){
      this.$refs.formModel.validate((success) => {
        
      })
    },
  }
}
</script>