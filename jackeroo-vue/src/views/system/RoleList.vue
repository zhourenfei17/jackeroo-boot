<template>
  <div>
    <search-card :enter="refreshData">
      <a-col :md="6" :sm="12">
        <a-form-item label="角色名">
          <a-input v-model="queryParam.roleName" placeholder="请输入角色名"/>
        </a-form-item>
      </a-col>
      <a-col :md="6" :sm="12">
        <a-form-item label="角色代码">
          <a-input v-model="queryParam.roleCode" placeholder="请输入角色代码"/>
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
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="handleDeleteBatch"><a-icon type="delete" />删除</a-menu-item>
          </a-menu>
          <a-button style="margin-right: 8px">
            批量操作 <a-icon type="down" />
          </a-button>
        </a-dropdown>
        <a-button type="primary" icon="plus" v-action="'system:role:add'" @click="handleAdd">新建</a-button>
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
              <j-link :type="actionType.view" :icon="actionIcon.view" @click="handleView(record)">详情</j-link>
              <j-link :type="actionType.edit" :icon="actionIcon.edit" v-action="'system:role:update'" @click="handleEdit(record)">编辑</j-link>
              <j-link icon="safety" v-action="'system:role:setAuth'" @click="handleSetPermission(record)">配置权限</j-link>
              <j-link :type="actionType.delete" v-action="'system:role:delete'" :icon="actionIcon.delete" @click="handleDelete(record)">删除</j-link>
            </action-list>
          </template>
        </span>
      </s-table>

      <role-form-modal ref="formModal" @ok="handleOk"></role-form-modal>
      <role-permission-set-modal ref="rolePermissionSetModal" ></role-permission-set-modal>
    </data-card>
  </div>
</template>

<script>
import { STable, DataCard, SearchCard } from '@/components'
import RoleFormModal from './modal/RoleFormModal'
import RolePermissionSetModal from './modal/RolePermissionSetModal'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { putAction, getAction, deleteAction } from '@/api/manage'

export default {
  name: 'RoleList',
  components: {
    STable,
    RoleFormModal,
    RolePermissionSetModal,
    DataCard,
    SearchCard
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
          title: '角色名',
          dataIndex: 'roleName'
        },
        {
          title: '角色代码',
          dataIndex: 'roleCode',
        },
        {
          title: '备注',
          dataIndex: 'remark'
        },
        {
          title: '更新时间',
          dataIndex: 'updateTime'
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/system/role/list',
        delete: '/system/role/delete',
        deleteBatch: '/system/role/deleteBatch'
      },
    }
  },
  methods: {
    // 删除
    handleDelete(record){
      this.$confirm({
        title: "删除角色",
        content: "确认删除角色【" + record.roleName + "】吗？",
        onOk: () => {
          this.$loading.show()
          deleteAction(this.url.delete, {id: record.id}).then(res => {
            if(!res.code){
              this.$message.success('操作成功')
              this.refreshData()
            }
          }).finally(() => {
            this.$loading.hide() 
          })
        }
      });
    },
    handleSetPermission(record){
      this.$refs.rolePermissionSetModal.load(record.id)
    }
  }
}
</script>