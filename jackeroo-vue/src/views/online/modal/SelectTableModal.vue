<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :fullscreen.sync="fullscreen"
    :switchFullscreen="showFullscreenBtn"
    :confirmLoading="loading"
    :autoHeight="false"
    maxHeight="60vh"
    okText="下一步"
    @ok="handleSubmit"
    @cancel="cancel"
  >
    <search-card :enter="refreshData" :gutter="24">
      <a-col :md="8" :sm="12">
        <a-form-item label="数据源">
          <!-- <a-select v-model="queryParam.dataSourceName" placeholder="请选择数据源">
            <a-select-option key="1" value="1"></a-select-option>
          </a-select> -->
          <a-select v-model="queryParam.dataSourceName" placeholder="请选择数据源" default-value="0" @change="refreshData(true)">
            <a-select-option v-for="item in dataSourceList" :key="item.id" :value="item.id">{{item.name}}</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
      <a-col :md="8" :sm="12">
        <a-form-item label="表名">
          <a-input v-model="queryParam.tableName" placeholder="请输入表名"/>
        </a-form-item>
      </a-col>

      <template slot="more">
        <a-col :md="8" :sm="12">
          <a-form-item label="表说明">
            <a-input v-model="queryParam.comment" placeholder="请输入表说明"/>
          </a-form-item>
        </a-col>
      </template>

      <template slot="operate">
        <a-button type="primary" @click="refreshData(true)">查询</a-button>
        <a-button @click="reset">重置</a-button>
      </template>
    </search-card>

    <s-table
      ref="table"
      :size="tableSize"
      :rowKey="tableKey"
      :columns="columns"
      :data="loadData"
      :alert="{show: false}"
      :rowSelection="rowSelection"
      :showPagination="showPagination"
    >
      
    </s-table>
  </j-modal>
</template>

<script>
import { STable, SearchCard } from '@/components'
import {JackerooFormMixins} from '@/mixins/JackerooFormMixins'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { getAction } from '@/api/manage';

export default {
  components: {
    STable,
    SearchCard
  },
  mixins: [JackerooFormMixins, JackerooListMixins],
  data(){
    return {
      title: '选择数据库业务表',
      width: '900px',
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
      dataSourceList: [],
      url: {
        list: '/online/generate/findTableListFromDataSource',
        dataSourceList: '/online/datasource/allList'
      }
    }
  },
  computed: {
    rowSelection() {
      return {
        selectedRowKeys: this.selectedRowKeys,
        onChange: this.onSelectChange,
        type: 'radio'
      }
    }
  },
  created() {
    this.initDatasource()
  },
  methods: {
    initDatasource(){
      getAction(this.url.dataSourceList).then(res => {
        this.dataSourceList = [{id: '0', name: '默认数据源'}, ...res.data]
        this.$nextTick(() => {
          this.$set(this.queryParam, 'dataSourceName', '0')
        })
      })
    },
    add(){
      this.loading = false
      this.selectedRowKeys = []
    },
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
    },
    handleSubmit(){
      if(this.selectedRowKeys.length != 1){
        this.$message.warning('请选择数据库表')
        return
      }
      // this.$refs.generateTableColumn.add(this.selectedRowKeys[0])
      this.$emit('ok', {table: this.selectedRowKeys[0], dataSource: this.queryParam.dataSourceName})
      this.selectedRowKeys = []
      this.cancel()
    },
  }
}
</script>