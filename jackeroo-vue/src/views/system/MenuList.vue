<template>
  <div>
    <search-card :enter="loadDataSource">
      <a-col :md="6" :sm="12">
        <a-form-item label="菜单名称">
          <a-input v-model="queryParam.name" placeholder="请输入菜单名称"/>
        </a-form-item>
      </a-col>
      <template slot="operate">
        <a-button type="primary" icon="search" @click="loadDataSource()">查询</a-button>
        <a-button style="margin-left: 8px" icon="reload" @click="reset">重置</a-button>
      </template>
    </search-card>
    
    <data-card 
      :reload="loadDataSource" 
      :tableSize.sync="tableSize" 
      :columns.sync="columns" 
      :expandedRowKeys.sync="expandedRowKeys"
      :dataSource="dataSource"
      :icon="['refresh', 'lineHeight', 'treeExpand', 'columnSet', 'fullscreen']">

      <template slot="toolbar">
        <a-button type="primary" icon="plus" v-action="'system:menu:add'" @click="handleAdd">新建菜单</a-button>
      </template>
      <!-- <div class="table-operator">
        <a-button type="primary" icon="plus" @click="handleAdd">新建菜单</a-button>
        <a-divider type="vertical" />
        <div class="icons-list">
          <a-icon type="redo"></a-icon>
          <a-icon type="column-height"></a-icon>
          <a-icon type="setting"></a-icon>
          <a-icon type="fullscreen" @click="fullscreen"></a-icon>
        </div>
      </div> -->

      <a-table
        rowKey="id"
        :columns="columns"
        :data-source="dataSource"
        :loading="loading"
        :pagination="false"
        :expandedRowKeys="expandedRowKeys"
        :size="tableSize"
        expandRowByClick
        @expandedRowsChange="handleExpandedRowsChange"
      >
        <template slot="iconSlot" slot-scope="text">
          <!-- <a-icon v-if="text && text.indexOf('icon-') != 0" :type="text" style="font-size:18px;"></a-icon> -->
          <j-icon v-if="text" :type="text" style="font-size:18px;"></j-icon>
        </template>

        <template slot="permission" slot-scope="text,record">
          <a-tag v-for="(auth, index) of record.auth" :key="auth.value" :color="getColor(index)">{{auth.label}}</a-tag>
        </template>

        <span slot="action" slot-scope="text, record">
          <template>
            <action-list>
              <j-link :type="actionType.view" :icon="actionIcon.view" @click="handleView(record)">详情</j-link>
              <j-link :type="actionType.edit" :icon="actionIcon.edit" v-action="'system:menu:update'" @click="handleEdit(record)">编辑</j-link>
              <action-menu-list>
                <j-link v-action="'system:menu:update'" @click="handleAdd(record)" v-if="record.leaf == 0">添加下级菜单</j-link>
                <j-link @click="handleEditPermission(record)" v-if="record.leaf == 1">权限列表</j-link>
                <j-link v-action="'system:menu:delete'" @click="handleDelete(record)">删除</j-link>
              </action-menu-list>
            </action-list>
          </template>
        </span>
      </a-table>

      <menu-form-modal ref="formModal" @ok="handleOk"></menu-form-modal>
      <permission-list-modal ref="permissionListModal" @change="handlePermissionChange"></permission-list-modal>
    </data-card>
  </div>
</template>

<script>
import {  Ellipsis, DataCard, SearchCard } from '@/components'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { putAction, getAction, deleteAction } from '@/api/manage'
import MenuFormModal from './modal/MenuFormModal'
import PermissionListModal from './modal/PermissionListModal'

export default {
  name: 'TableList',
  components: {
    Ellipsis,
    MenuFormModal,
    PermissionListModal,
    DataCard,
    SearchCard
  },
  mixins:[JackerooListMixins],
  data () {
    return {
      loading: false,
      useSTable: false,
      tableSize: 'default',
      columns: [
        {
          title: '菜单名称',
          dataIndex: 'name'
        },
        {
          title: 'url路径',
          dataIndex: 'href',
        },
        {
          title: '组件地址',
          dataIndex: 'component'
        },
        {
          title: '图标',
          dataIndex: 'icon',
          scopedSlots: {customRender: 'iconSlot'}
        },
        {
          title: '权限',
          dataIndex: 'permission',
          scopedSlots: {customRender: 'permission'}
        },
        {
          title: '操作',
          dataIndex: 'action',
          width: '220px',
          scopedSlots: { customRender: 'action' }
        }
      ],
      // 展开的行
      expandedRowKeys: [],
      tagColor: ['#1890ff', '#cf1322', '#fa541c', '#faad14', '#13c2c2', '#52c41a', '#2f54eb', '#722ed1'],
      url: {
        list: '/system/menu/list',
        delete: '/system/menu/delete'
      }
    }
  },
  created(){
    this.loadDataSource()
  },
  methods: {
    loadDataSource(){
      this.dataSource = []
      getAction(this.url.list, this.queryParam).then(res => {
        if(!res.code){
          this.dataSource = res.data
        }
      })
    },
    handleExpandedRowsChange(expandedRows){
      this.expandedRowKeys = expandedRows
    },
    // 添加
    handleAdd (record) {
      if(record.id){
        // 添加下级菜单
        this.$refs.formModal.visible = true
        this.$refs.formModal.flag.add = true
        this.$refs.formModal.form.leaf = 1
        this.$refs.formModal.add(record.id, record.children ? record.children[record.children.length - 1].sort + 10 : 10)
      }else{
        // 添加一级菜单
        this.$refs.formModal.visible = true
        this.$refs.formModal.flag.add = true
        this.$refs.formModal.form.leaf = 0
        this.$refs.formModal.add('0', this.dataSource.length > 0 ? this.dataSource[this.dataSource.length - 1].sort + 10 : 10)
      }
    },
    // 编辑
    handleEdit (record) {
      this.$refs.formModal.visible = true
      this.$refs.formModal.flag.edit = true
      this.$refs.formModal.edit(record.id)
    },
    // 删除
    handleDelete(record){
      this.$confirm({
        title: "删除菜单",
        content: "确认删除菜单【" + record.name + "】吗？",
        onOk: () => {
          this.$loading.show()
          deleteAction(this.url.delete, {id: record.id}).then(res => {
            if(!res.code){
              this.$message.success('操作成功')
              this.loadDataSource()
            }
          }).finally(() => {
            this.$loading.hide()
          })
        }
      });
    },
    handlePermissionChange(){
      this.loadDataSource()
    },
    // 编辑权限列表
    handleEditPermission(record){
      this.$refs.permissionListModal.load(record.id)
    },
    handleOk(){
      this.loadDataSource()
    },
    getColor(index){
      return this.tagColor[index % 8]
    }
  }
}
</script>

<style lang="less" scoped>
  .warning{
    color: #dc8545
  }
  .careful{
    color: #8a8282
  }
</style>