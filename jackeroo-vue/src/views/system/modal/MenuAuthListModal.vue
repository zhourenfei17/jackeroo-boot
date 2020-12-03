<template>
  <j-drawer
      :title="title"
      :width="width"
      :visible="visible"
      okText="确定"
      @ok="handleConfirm"
      @close="cancel">
    
    <data-card
        :icon="false"
        style="margin-top:-24px;">

      <template slot="toolbar">
        <a-button type="primary" icon="plus" @click="handleAdd">新建</a-button>

        <a-button icon="retweet" @click="handleChangePermissionGorup" style="margin-left:8px;">选择权限分组</a-button>
      </template>

      <a-table
        :size="tableSize"
        rowKey="value"
        :columns="columns"
        :data-source="dataSource"
        :loading="loading"
        bordered
        :pagination="false"
      >

        <template slot="action" slot-scope="text, record">
          <action-list v-if="!record.id">
            <j-link :type="actionType.edit" :icon="actionIcon.edit" @click="handleEdit(record)">编辑</j-link>
            <j-link :type="actionType.delete" :icon="actionIcon.delete" @click="handleDelete(record)">删除</j-link>
          </action-list>
        </template>
      </a-table>
    </data-card>

    <menu-auth-form-modal ref="formModal" @add="handleAddPermission" @edit="handleEditPermission"></menu-auth-form-modal>
    <change-permission-group-modal ref="changePermissionGroupModal" @ok="selectedPermission"></change-permission-group-modal>
  </j-drawer>
</template>

<script>
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { getAction } from '@/api/manage'
import { JDrawer,DataCard } from '@/components'
import MenuAuthFormModal from './MenuAuthFormModal'
import ChangePermissionGroupModal from './ChangePermissionGroupModal'

export default {
  name: 'TableList',
  components: {
    JDrawer,
    DataCard,
    MenuAuthFormModal,
    ChangePermissionGroupModal
  },
  mixins:[JackerooListMixins],
  data () {
    return {
      title: '权限列表',
      loading: false,
      useSTable: false,
      visible: false,
      width: '500px',
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
          title: '操作',
          dataIndex: 'action',
          width: '160px',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/system/menu/list',
        delete: '/system/menu/delete',
        findPermissionByGroupId: '/system/menu/permission/config/findPermissionByGroupId'
      },
      groupId: null,
      groupChange: false,
      // 已选中的权限标识
      checkedAuth: []
    }
  },
  methods: {
    edit(list, checkedAuth, groupId, permissionEdit){
      if(groupId && !permissionEdit){
        this.loadPermissionByGroupId(groupId)
      }else{
        this.dataSource = [...list]
      }
      this.checkedAuth = [...checkedAuth]
      this.groupId = groupId
      this.visible = true
    },
    loadPermissionByGroupId(groupId){
      getAction(this.url.findPermissionByGroupId, {groupId : groupId}).then(res => {
        this.dataSource = res.data
        const checked = []
        for (const item of this.dataSource) {
          if(item.checked){
            checked.push(item.value)
          }
        }
        this.checkedAuth = checked
      })
    },
    handleChangePermissionGorup(){
      this.$refs.changePermissionGroupModal.select(this.groupId)
    },
    selectedPermission(groupId){
      if(this.groupId != groupId){
        this.loadPermissionByGroupId(groupId)
        this.groupId = groupId
        this.groupChange = true
      }
    },
    handleConfirm(){
      this.$emit('change', this.dataSource, this.groupId, this.checkedAuth)
      this.cancel()
    },
    handleAdd(){
      this.$refs.formModal.visible = true
      this.$refs.formModal.flag.add = true
      this.$refs.formModal.add(this.dataSource)
    },
    handleEdit(record){
      this.$refs.formModal.visible = true
      this.$refs.formModal.flag.edit = true
      this.$refs.formModal.edit(record, this.dataSource)
    },
    handleDelete(record){
      let index = -1
      for(let i = 0; i < this.dataSource.length; i++){
        if(this.dataSource[i].value == record.value){
          index = i
          break
        }
      }
      this.dataSource.splice(index, 1)

      // 已选中的权限更新
      let i = this.checkedAuth.indexOf(record.value)
      if(i > -1){
        this.checkedAuth.splice(i, 1)
      }
    },
    cancel(){
      this.visible = false
      this.groupId = null
      this.groupChange = false
      this.checkedAuth = []
    },
    handleAddPermission(auth){
      this.checkedAuth.push(auth.value)
      this.dataSource.push(auth)
    },
    handleEditPermission(auth, oldValue){
      const data = [...this.dataSource]
      for(let i = 0; i < data.length; i++){
        if(data[i].value == oldValue){
          data[i].label = auth.label
          data[i].value = auth.value
          break
        }
      }
      this.dataSource = data
    }
  }
}
</script>