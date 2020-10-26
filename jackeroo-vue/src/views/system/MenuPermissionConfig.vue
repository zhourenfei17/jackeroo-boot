<template>
  <div>
    <search-card :enter="refreshData">
      <a-col :md="6" :sm="12">
        <a-form-item label="权限组名">
          <a-input v-model="queryParam.groupName" placeholder="请输入权限组名"/>
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
        <template slot="isDefault" slot-scope="text">
          <j-tag :type="text == 0 ? 'warning' : 'info'" :text="text == 0 ? '否' : '是'"></j-tag>
        </template>
        
        <template slot="disabled" slot-scope="text">
          <j-tag :type="text == 0 ? 'info' : 'error'" :text="text == 0 ? '启用' : '禁用'"></j-tag>
        </template>

        <span slot="action" slot-scope="text, record">
          <template>
            <action-list>
              <a @click="handleView(record)">详情</a>
              <a @click="handleEdit(record)">编辑</a>
              <a @click="handleOpenPermissionListModal(record)">权限列表</a>
              <a @click="handleSetDefault(record)" v-if="record.isDefault == 0">设为默认</a>
              <action-menu-list>
                <a @click="handleDisable(record)" v-if="record.disabled == 0">禁用</a>
                <a @click="handleEnable(record)" v-if="record.disabled == 1">启用</a>
                <a @click="handleDelete(record)">删除</a>
              </action-menu-list>
            </action-list>
          </template>
        </span>
      </s-table>

      <menu-permission-group-modal ref="formModal" @ok="handleOk"></menu-permission-group-modal>
      <menu-permission-list-modal ref="permissionListModal"></menu-permission-list-modal>
    </data-card>
  </div>
</template>

<script>
import { STable, JTag, DataCard, SearchCard } from '@/components'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { putAction, getAction, deleteAction } from '@/api/manage'
import MenuPermissionGroupModal from './modal/MenuPermissionGroupModal'
import MenuPermissionListModal from './modal/MenuPermissionListModal'

export default {
  name: 'RoleList',
  components: {
    STable,
    JTag,
    DataCard,
    SearchCard,
    MenuPermissionGroupModal,
    MenuPermissionListModal
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
          title: '权限组名',
          dataIndex: 'groupName'
        },
        {
          title: '默认组别',
          dataIndex: 'isDefault',
          scopedSlots: {customRender: 'isDefault'}
        },
        {
          title: '是否禁用',
          dataIndex: 'disabled',
          scopedSlots: {customRender: 'disabled'}
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/system/menu/permission/group/list',
        delete: '/system/menu/permission/group/delete',
        setDefault: '/system/menu/permission/group/setDefault',
        disable: '/system/menu/permission/group/disable',
        enable: '/system/menu/permission/group/enable'
      },
    }
  },
  methods: {
    // 权限列表
    handleOpenPermissionListModal(record){
      this.$refs.permissionListModal.load(record.id)
    },
    // 设为默认
    handleSetDefault(record){
      this.$confirm({
        title: '设为默认',
        content: '确认将【' + record.groupName + '】设置为默认吗？',
        onOk: () => {
          this.$loading.show()
          putAction(this.url.setDefault, {id: record.id}).then(res => {
            if(res.code == 0){
              this.$message.success('操作成功')
              this.refreshData()
            }
          }).finally(() => {
            this.$loading.hide()
          })
        }
      })
    },
    // 禁用
    handleDisable(record){
      this.$confirm({
        title: '禁用',
        content: '确认将【' + record.groupName + '】禁用吗？',
        onOk: () => {
          this.$loading.show()
          putAction(this.url.disable, {id: record.id}).then(res => {
            if(res.code == 0){
              this.$message.success('操作成功')
              this.refreshData()
            }
          }).finally(() => {
            this.$loading.hide()
          })
        }
      })
    },
    // 启用
    handleEnable(record){
      this.$confirm({
        title: '启用',
        content: '确认将【' + record.groupName + '】启用吗？',
        onOk: () => {
          this.$loading.show()
          putAction(this.url.enable, {id: record.id}).then(res => {
            if(res.code == 0){
              this.$message.success('操作成功')
              this.refreshData()
            }
          }).finally(() => {
            this.$loading.hide()
          })
        }
      })
    }
  }
}
</script>