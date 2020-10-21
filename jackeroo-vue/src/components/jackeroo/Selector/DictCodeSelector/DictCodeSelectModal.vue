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
    <s-table
      ref="table"
      :size="tableSize"
      rowKey="id"
      :columns="columns"
      :data="loadData"
      lazy
      :alert="tableAlert"
      :rowSelection="rowSelection"
      :showPagination="showPagination"
      >
    </s-table>
  </j-modal>
</template>

<script>
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { STable } from '@/components'

export default {
  name: 'JDictCodeSelectModal',
  mixins: [JackerooFromMixins, JackerooListMixins],
  components: {
    STable
  },
  props:{

  },
  data(){
    return {
      title: '请选择数据字典',
      width: '50vw',
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
      url: {
        list: '/system/dict/list',
      },
    }
  },
  methods: {
    show(){
      this.visible = true
      this.$nextTick(() => {
        this.refreshData()
      })
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
    }
  }
}
</script>