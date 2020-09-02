<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="loadDataSource()">
          <a-row :gutter="48">
            <a-col :md="6" :sm="12">
              <a-form-item label="菜单名称">
                <a-input v-model="queryParam.name" placeholder="请输入菜单名称"/>
              </a-form-item>
            </a-col>
            <a-col :md="!advanced && 6 || 24" :sm="12">
              <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                <a-button type="primary" @click="loadDataSource()">查询</a-button>
                <a-button style="margin-left: 8px" @click="() => this.queryParam = {}">重置</a-button>
              </span>
            </a-col>
          </a-row>
        </a-form>
      </div>

      <div class="table-operator">
        <a-button type="primary" icon="plus" @click="handleAdd">新建菜单</a-button>
      </div>

      <a-table
        size="default"
        rowKey="id"
        :columns="columns"
        :data-source="dataSource"
        :loading="loading"
        :pagination="false"
        :expandedRowKeys="expandedRowKeys"
        @expandedRowsChange="handleExpandedRowsChange"
      >
        <template slot="iconSlot" slot-scope="text">
          <a-icon v-if="text != '' && text != null" :type="text" style="font-size:18px;"></a-icon>
        </template>

        <template slot="permission" slot-scope="text,record">
          <a-tag v-for="(auth, index) of record.auth" :key="auth.value" :color="getColor(index)">{{auth.label}}</a-tag>
        </template>

        <span slot="action" slot-scope="text, record">
          <template>
            <action-list>
              <a @click="handleView(record)">详情</a>
              <a @click="handleEdit(record)">编辑</a>
              <action-menu-list>
                <a @click="handleAdd(record)" v-if="record.leaf == 0">添加下级菜单</a>
                <a @click="handleEditPermission(record)" v-if="record.leaf == 1">权限列表</a>
                <a @click="handleDelete(record)">删除</a>
              </action-menu-list>
            </action-list>
          </template>
        </span>
      </a-table>

      <menu-form-modal ref="formModal" @ok="handleOk"></menu-form-modal>
      <permission-list-modal ref="permissionListModal" @change="handlePermissionChange"></permission-list-modal>
    </a-card>
  </page-header-wrapper>
</template>

<script>
import {  Ellipsis } from '@/components'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { putAction, getAction, deleteAction } from '@/api/manage'
import MenuFormModal from './modal/MenuFormModal'
import PermissionListModal from './modal/PermissionListModal'

export default {
  name: 'TableList',
  components: {
    Ellipsis,
    MenuFormModal,
    PermissionListModal
  },
  mixins:[JackerooListMixins],
  data () {
    return {
      loading: false,
      useSTable: false,
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
          width: '180px',
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
        if(res.code == 0){
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
            if(res.code === 0){
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