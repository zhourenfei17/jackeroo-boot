<template>
  <a-drawer
      :title="title"
      :width="width"
      :visible="visible"
      @close="cancel">
    
    <data-card
        :reload="refreshData"
        :tableSize.sync="tableSize"
        :columns.sync="columns"
        :icon="['refresh', 'lineHeight', 'align', 'columnSet']"
        :tableAlign="tableAlign"
        style="margin-top:-24px;">

      <template slot="toolbar">
        <a-button type="primary" icon="plus" v-action="'system:permission:add'" @click="handleAdd">新建</a-button>
      </template>

      <s-table
          ref="table"
          :size="tableSize"
          rowKey="id"
          :columns="columns"
          :data="loadData"
          :lazy="true"
          bordered
          showPagination="auto"
        >

        <template slot="checked" slot-scope="text">
          <j-tag :type="text == 0 ? 'info' : 'primary'" :text="text == 0 ? '否' : '是'"></j-tag>
        </template>

        <span slot="action" slot-scope="text, record">
          <template>
            <action-list>
              <j-link :type="actionType.view" :icon="actionIcon.view" @click="handleView(record)">详情</j-link>
              <j-link :type="actionType.edit" :icon="actionIcon.edit" v-action="'system:permission:update'" @click="handleEdit(record)">编辑</j-link>
              <j-link :type="actionType.delete" :icon="actionIcon.delete" v-action="'system:permission:delete'" @click="handleDelete(record)">删除</j-link>
            </action-list>
          </template>
        </span>
      </s-table>
    </data-card>

    <menu-permission-config-form-modal ref="formModal" @ok="handleOk"></menu-permission-config-form-modal>
  </a-drawer>
</template>

<script>
import { STable, JTag } from '@/components'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { JDrawer, DataCard} from '@/components'
import MenuPermissionConfigFormModal from './MenuPermissionConfigFormModal'

export default {
  name: 'TableList',
  components: {
    JDrawer,
    MenuPermissionConfigFormModal,
    STable,
    DataCard,
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
          title: '权限代码',
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
          width: '200px',
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