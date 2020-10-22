<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :fullscreen.sync="fullscreen"
    :switchFullscreen="showFullscreenBtn"
    :confirmLoading="false"
    okText="确定"
    @ok="handleSubmit"
    @cancel="cancel"
    :autoHeight="false"
  >
    <search-card :enter="refreshData">
      <a-col :md="9" :sm="12">
        <a-form-item label="字典名称">
          <a-input v-model="queryParam.dictName" placeholder="请输入字典名称"/>
        </a-form-item>
      </a-col>
      <a-col :md="9" :sm="12">
        <a-form-item label="字典编码">
          <a-input v-model="queryParam.dictCode" placeholder="请输入字典编码"/>
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
      rowKey="id"
      :columns="columns"
      :data="loadData"
      borderd
      lazy
      :alert="tableAlert"
      :rowSelection="rowSelection"
      :showPagination="showPagination"
      >
    </s-table>

    <template slot="footer">
      <a-button @click="cancel">取消</a-button>
      <a-button @click="clear">清除</a-button>
      <a-button type="primary" @click="handleSubmit">确定</a-button>
    </template>
  </j-modal>
</template>

<script>
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { STable, SearchCard } from '@/components'

export default {
  name: 'JDictCodeSelectModal',
  mixins: [JackerooFromMixins, JackerooListMixins],
  components: {
    STable,
    SearchCard
  },
  props:{

  },
  data(){
    return {
      title: '请选择数据字典',
      width: '800px',
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
        }
      ],
      rowSelection : {
        selectedRowKeys: this.selectedRowKeys,
        onChange: this.onSelectChange,
        type: 'radio'
      },
      firstLoad: true,
      url: {
        list: '/system/dict/list',
      },
    }
  },
  methods: {
    show(){
      this.visible = true
      if(this.firstLoad){
        this.$nextTick(() => {
          this.refreshData()
        })
      }
      this.firstLoad = false
    },
    cancel(){
      this.visible = false
    },
    handleSubmit(){
      if(this.selectedRows.length == 0){
        this.$message.warning('请选择一条数据字典数据')
        return
      }
      this.$emit('ok', this.selectedRows[0])
      this.visible = false
    },
    clear(){
      this.selectedRowKeys = []
      this.selectedRows = []
      this.$emit('ok', null)
      this.visible = false
    }
  }
}
</script>