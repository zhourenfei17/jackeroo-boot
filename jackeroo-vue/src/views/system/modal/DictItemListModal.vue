<template>
  <a-drawer
      :title="title"
      :width="width"
      :visible="visible"
      @close="cancel">
    
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleAdd">新建</a-button>
    </div>

    <s-table
        ref="table"
        :size="tableSize"
        rowKey="id"
        :columns="columns"
        :data="loadData"
        :alert="tableAlert"
        :rowSelection="rowSelection"
        lazy
        showPagination="auto"
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

    <dict-item-form-modal ref="formModal" @ok="handleOk"></dict-item-form-modal>
  </a-drawer>
</template>

<script>
import { getAction, postAction, httpAction } from '@/api/manage'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { STable} from '@/components'
import DictItemFormModal from './DictItemFormModal'

export default {
  components: {
    STable,
    DictItemFormModal
  },
  mixins: [JackerooListMixins],
  data() {
    return {
      title: '字典项列表',
      width: '40vw',
      visible: false,
      dataSource: [],
      columns: [
        {
          title: '#',
          customRender: (text, record, index) => {
            return index + 1
          }
        },
        {
          title: '字典项名称',
          dataIndex: 'label',
        },
        {
          title: '字典项值',
          dataIndex: 'value',
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/system/dict/itemList',
        add: '/system/menu/add',
        update: '/system/menu/update',
      },
    }
  },
  methods: {
    load(dictCode){
      this.queryParam.dictCode = dictCode
      this.visible = true
      this.$nextTick(() => {
        this.refreshData()
      })
    },
    cancel(){
      this.visible = false
    },
    // 添加
    handleAdd () {
      this.$refs.formModal.visible = true
      this.$refs.formModal.flag.add = true
      this.$refs.formModal.add(this.queryParam.dictCode)
    }
  }
}
</script>