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
        <a-button type="primary" icon="plus" v-action="'system:menu:add'" @click="handleAdd">新建</a-button>
      </template>

      <s-table
        ref="table"
        size="default"
        rowKey="id"
        :columns="columns"
        :data="loadData"
        :lazy="true"
        bordered
        showPagination="auto"
        @update="syncDataSource"
      >

        <template slot="action" slot-scope="text, record">
          <action-list>
            <j-link :type="actionType.edit" :icon="actionIcon.edit" v-action="'system:menu:update'" @click="handleEdit(record)">编辑</j-link>
            <j-link :type="actionType.delete" :icon="actionIcon.delete" v-action="'system:menu:delete'" @click="handleDelete(record)">删除</j-link>
          </action-list>
        </template>
      </s-table>
    </data-card>

    <permission-form-modal ref="formModal" @ok="handleOk"></permission-form-modal>
  </a-drawer>
</template>

<script>
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { getAction } from '@/api/manage'
import { JDrawer, STable, DataCard} from '@/components'
import PermissionFormModal from './PermissionFormModal'

export default {
  name: 'TableList',
  components: {
    JDrawer,
    STable,
    DataCard,
    PermissionFormModal,
  },
  mixins:[JackerooListMixins],
  data () {
    return {
      title: '权限列表',
      loading: false,
      visible: false,
      width: '650px',
      queryParam: {
        parentId: null
      },
      columns: [
        {
          title: '权限名称',
          dataIndex: 'name'
        },
        {
          title: '权限标识',
          dataIndex: 'permission',
        },
        {
          title: '排序',
          dataIndex: 'sort',
          sorter: true,
          defaultSortOrder: 'ascend'
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '160px',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/system/menu/permission',
        delete: '/system/menu/delete',
      },
      permissionChange: false
    }
  },
  methods: {
    load(parentId){
      this.queryParam.parentId = parentId
      this.permissionChange = false
      this.visible = true
      this.$nextTick(() => {
        this.refreshData(true, false)
      })
    },
    handleAdd(){
      this.$refs.formModal.visible = true
      this.$refs.formModal.flag.add = true
      let sort = 0
      for(const item of this.dataSource){
        if(item.sort > sort){
          sort = item.sort
        }
      }
      this.$refs.formModal.add(this.queryParam.parentId, sort + 10)
    },
    refreshData(backFirstPage = false, permissionChange = true){
      this.$refs.table.refresh(backFirstPage)
      if(permissionChange){
        this.permissionChange = permissionChange
      }
    },
    cancel(){
      this.visible = false
      this.queryParam.parentId = null
      if(this.permissionChange){
        this.$emit('change')
      }
    }
  }
}
</script>