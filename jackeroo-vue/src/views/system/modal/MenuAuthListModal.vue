<template>
  <a-drawer
      :title="title"
      :width="width"
      :visible="visible"
      @close="cancel">
    
    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleAdd">添加</a-button>
    </div>

    <a-table
      size="default"
      rowKey="value"
      :columns="columns"
      :data-source="dataSource"
      :loading="loading"
      :pagination="false"
    >

      <span slot="action" slot-scope="text, record">
        <template>
          <action-list>
            <a @click="handleEdit(record)">编辑</a>
            <a @click="handleDelete(record)">删除</a>
          </action-list>
        </template>
      </span>
    </a-table>

    <menu-auth-form-modal ref="formModal" @ok="handleOk"></menu-auth-form-modal>
  </a-drawer>
</template>

<script>
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import {JackerooFromMixins} from '@/mixins/JackerooFormMixins'
import { JDrawer} from '@/components'
import MenuAuthFormModal from './MenuAuthFormModal'

export default {
  name: 'TableList',
  components: {
    JDrawer,
    MenuAuthFormModal
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
          title: '权限标识',
          dataIndex: 'value',
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '180px',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/system/menu/list',
        delete: '/system/menu/delete'
      }
    }
  },
  methods: {
    edit(list){
      this.dataSource = list
      this.visible = true
    },
    handleEdit(record){
      this.$refs.formModal.visible = true
      this.$refs.formModal.flag.edit = true
      this.$refs.formModal.edit(record)
    },
    cancel(){
      this.visible = false
    },
    handleOk(auth){
      this.dataSource.push(auth)
    }
  }
}
</script>