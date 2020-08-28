<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="refreshData(true)">
          <a-row :gutter="48">
            <a-col :md="6" :sm="12">
              <a-form-item label="权限组名">
                <a-input v-model="queryParam.groupName" placeholder="请输入权限组名"/>
              </a-form-item>
            </a-col>
            <a-col :md="!advanced && 6 || 24" :sm="12">
              <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                <a-button type="primary" @click="refreshData(true)">查询</a-button>
                <a-button style="margin-left: 8px" @click="() => this.queryParam = {}">重置</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <div class="table-operator">
        <a-button type="primary" icon="plus" @click="handleAdd">新建</a-button>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1"><a-icon type="delete" />删除</a-menu-item>
          </a-menu>
          <a-button style="margin-left: 8px">
            批量操作 <a-icon type="down" />
          </a-button>
        </a-dropdown>
      </div>

      <s-table
        ref="table"
        size="default"
        rowKey="id"
        :columns="columns"
        :data="loadData"
        :alert="tableAlert"
        :rowSelection="rowSelection"
        showPagination="auto"
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
              <action-menu-list>
                <a @click="handleDelete(record)">删除</a>
              </action-menu-list>
            </action-list>
          </template>
        </span>
      </s-table>

      <menu-permission-group-modal ref="formModal" @ok="handleOk"></menu-permission-group-modal>
    </a-card>
  </page-header-wrapper>
</template>

<script>
import { STable,JTag } from '@/components'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { putAction, getAction, deleteAction } from '@/api/manage'
import MenuPermissionGroupModal from './modal/MenuPermissionGroupModal'

export default {
  name: 'RoleList',
  components: {
    STable,
    JTag,
    MenuPermissionGroupModal
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
        delete: '/system/menu/permission/group/delete'
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
            if(res.code === 0){
              this.$message.success('操作成功')
              this.refreshData()
            }
          }).finally(() => {
            this.$loading.hide() 
          })
        }
      });
    }
  }
}
</script>