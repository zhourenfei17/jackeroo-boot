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
        :lazy="true"
        showPagination="auto"
      >

      <template slot="checked" slot-scope="text">
        <j-tag :type="text == 0 ? 'warning' : 'info'" :text="text == 0 ? '否' : '是'"></j-tag>
      </template>

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

    <menu-permission-config-form-modal ref="formModal" @ok="handleOk"></menu-permission-config-form-modal>
  </a-drawer>
</template>

<script>
import { STable, JTag } from '@/components'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { JDrawer} from '@/components'
import MenuPermissionConfigFormModal from './MenuPermissionConfigFormModal'

export default {
  name: 'TableList',
  components: {
    JDrawer,
    MenuPermissionConfigFormModal,
    STable,
    JTag
  },
  mixins:[JackerooListMixins],
  data () {
    return {
      title: '权限列表',
      loading: false,
      visible: false,
      width: '800px',
      columns: [
        {
          title: '权限名称',
          dataIndex: 'label'
        },
        {
          title: '权限标识后缀',
          dataIndex: 'value',
        },
        {
          title: '是否选中',
          dataIndex: 'checked',
          scopedSlots: {customRender: 'checked'}
        },
        {
          title: '排序号',
          dataIndex: 'sort',
          sorter: true,
          defaultSortOrder: 'ascend'
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '180px',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/system/menu/permission/config/list',
        delete: '/system/menu/permission/config/delete'
      }
    }
  },
  methods: {
    load(groupId){
      this.queryParam.groupId = groupId
      this.visible = true
      this.$nextTick(() => {
        this.refreshData(true)
      })
    },
    edit(list){
      this.dataSource = list
      this.visible = true
    },
    // 添加
    handleAdd () {
      this.$refs.formModal.visible = true
      this.$refs.formModal.flag.add = true
      this.$refs.formModal.add(this.queryParam.groupId)
    },
    cancel(){
      this.visible = false
    }
  }
}
</script>