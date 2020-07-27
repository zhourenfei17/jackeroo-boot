<template>
  <page-header-wrapper>
    <a-card :bordered="false">
      <div class="table-page-search-wrapper">
        <a-form layout="inline" @keyup.enter.native="refreshData(true)">
          <a-row :gutter="48">
            <a-col :md="6" :sm="12">
              <a-form-item label="姓名">
                <a-input v-model="queryParam.name" placeholder="请输入姓名"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="12">
              <a-form-item label="账号">
                <a-input v-model="queryParam.account" placeholder="请输入账号"/>
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="12">
              <a-form-item label="状态">
                <a-select v-model="queryParam.status" placeholder="请选择状态" default-value="0">
                  <a-select-option value="" disabled>请选择</a-select-option>
                  <a-select-option value="0">正常</a-select-option>
                  <a-select-option value="1">冻结</a-select-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :md="!advanced && 6 || 24" :sm="12">
              <span class="table-page-search-submitButtons" :style="advanced && { float: 'right', overflow: 'hidden' } || {} ">
                <a-button type="primary" @click="refreshData(true)">查询</a-button>
                <a-button style="margin-left: 8px" @click="() => this.queryParam = {}">重置</a-button>
                <a @click="toggleAdvanced" style="margin-left: 8px">
                  {{ advanced ? '收起' : '展开' }}
                  <a-icon :type="advanced ? 'up' : 'down'"/>
                </a>
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
        <span slot="action" slot-scope="text, record">
          <template>
            <action-list>
              <a @click="handleView(record)">详情</a>
              <a @click="handleEdit(record)">编辑</a>
              <action-menu-list>
                <a @click="handleEdit(record)">添加下级菜单</a>
                <a @click="handleDelete(record)">删除</a>
              </action-menu-list>
            </action-list>
          </template>
        </span>
      </a-table>

      <menu-form-modal ref="formModal" @ok="handleOk"></menu-form-modal>
    </a-card>
  </page-header-wrapper>
</template>

<script>
import {  Ellipsis } from '@/components'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { putAction, getAction } from '@/api/manage'
import MenuFormModal from './modal/MenuFormModal'

export default {
  name: 'TableList',
  components: {
    Ellipsis,
    MenuFormModal
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
          dataIndex: 'icon'
        },
        {
          title: '权限',
          dataIndex: 'permission'
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      // 展开的行
      expandedRowKeys: [],
      url: {
        list: '/system/menu/list',
        delete: '/system/user/delete'
      }
    }
  },
  created(){
    this.loadDataSource()
  },
  methods: {
    loadDataSource(){
      this.dataSource = []
      getAction(this.url.list).then(res => {
        if(res.code == 0){
          this.dataSource = res.data
        }
      })
    },
    handleExpandedRowsChange(expandedRows){
      this.expandedRowKeys = expandedRows
    },
    // 删除
    handleDelete(record){
      this.$confirm({
        title: "删除用户",
        content: "确认删除用户【" + record.name + "】吗？",
        onOk: () => {
          this.$loading.show()
          deleteAction(this.url.delete, {id: record.id}).then(res => {
            if(res.code === 0){
              this.$message.success('操作成功')
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

<style lang="less" scoped>
  .warning{
    color: #dc8545
  }
  .careful{
    color: #8a8282
  }
</style>