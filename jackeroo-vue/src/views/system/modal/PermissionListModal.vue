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
      size="default"
      rowKey="id"
      :columns="columns"
      :data="loadData"
      :alert="tableAlert"
      :rowSelection="rowSelection"
      :lazy="true"
      showPagination="auto"
      @update="syncDataSource"
    >

      <template slot="action" slot-scope="text, record">
        <action-list>
          <a @click="handleEdit(record)">编辑</a>
          <a @click="handleDelete(record)">删除</a>
        </action-list>
      </template>
    </s-table>

    <permission-form-modal ref="formModal" @ok="handleOk"></permission-form-modal>
  </a-drawer>
</template>

<script>
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { getAction } from '@/api/manage'
import { JDrawer, STable} from '@/components'
import PermissionFormModal from './PermissionFormModal'

export default {
  name: 'TableList',
  components: {
    JDrawer,
    STable,
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
          width: '140px',
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
        console.log(item.sort)
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