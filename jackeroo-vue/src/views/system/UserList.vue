<template>
  <div>
    <search-card :enter="refreshData">
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
      
      <template slot="more">
        <a-col :md="6" :sm="12">
          <a-form-item label="手机号">
            <a-input v-model="queryParam.phone" placeholder="请输入手机号"/>
          </a-form-item>
        </a-col>
        <a-col :md="6" :sm="12">
          <a-form-item label="性别">
            <j-dict-select v-model="queryParam.gender" placeholder="请选择性别" dictCode="COMMON.SEX"></j-dict-select>
          </a-form-item>
        </a-col>
      </template>
      
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
            <a-menu-item key="2" @click="handleFrozenBatch"><a-icon type="lock" />冻结</a-menu-item>
            <a-menu-item key="3" @click="handleUnfrozenBatch"><a-icon type="unlock" />解冻</a-menu-item>
          </a-menu>
          <a-button>
            批量操作 <a-icon type="down" />
          </a-button>
        </a-dropdown>
        <a-button type="primary" icon="plus" v-action="'system:user:add'" @click="handleAdd" style="margin-left: 8px">新建</a-button>
        <a-dropdown-button style="margin-left: 8px">
          <a-icon type="file-excel"></a-icon>
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="handleExport"><a-icon type="export" />导出</a-menu-item>
            <a-menu-item key="2"><a-icon type="import" />导入</a-menu-item>
          </a-menu>
          <a-icon type="down" slot="icon"></a-icon>
        </a-dropdown-button>
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
        <span slot="status" slot-scope="text">
          <a-badge :status="text | statusTypeFilter" :text="text | statusFilter" />
        </span>

        <span slot="action" slot-scope="text, record">
          <template>
            <action-list>
              <j-link :type="actionType.view" :icon="actionIcon.view" @click="handleView(record)">详情</j-link>
              <j-link :type="actionType.edit" :icon="actionIcon.edit" v-action="'system:user:update'" @click="handleEdit(record)">编辑</j-link>
              <a-popconfirm title="您确定要冻结该用户吗？" v-if="record.status == 0" v-action="'system:user:frozen'" @confirm="() => frozen(record)">
                <j-link icon="lock" type="info">冻结</j-link>
              </a-popconfirm>
              <a-popconfirm title="您确定要解冻该用户吗？" v-if="record.status == 1" v-action="'system:user:frozen'" @confirm="() => unfrozen(record)">
                <j-link icon="unlock" type="error">解冻</j-link>
              </a-popconfirm>
              <action-menu-list>
                <j-link @click="resetPwd(record)" v-action="'system:user:reset'">重置密码</j-link>
                <j-link @click="handleDelete(record)" v-action="'system:user:delete'">删除</j-link>
              </action-menu-list>
            </action-list>
          </template>
        </span>
      </s-table>

      <user-form-modal ref="formModal" @ok="handleOk"></user-form-modal>
    </data-card>
  </div>
</template>

<script>
import { STable, Ellipsis, DataCard, SearchCard, JDictSelect } from '@/components'
import UserFormModal from './modal/UserFormModal'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { putAction, deleteAction } from '@/api/manage'

const statusMap = {
  0: {
    status: 'success',
    text: '正常'
  },
  1: {
    status: 'error',
    text: '冻结'
  }
}

export default {
  name: 'TableList',
  components: {
    STable,
    Ellipsis,
    UserFormModal,
    DataCard,
    SearchCard,
    JDictSelect
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
          title: '姓名',
          dataIndex: 'name'
        },
        {
          title: '账号',
          dataIndex: 'account',
        },
        {
          title: '性别',
          dataIndex: 'gender',
          sorter: true,
          customRender: ((text) => {
            if(text == 1){
              return '男'
            }else{
              return '女'
            }
          })
        },
        {
          title: '手机',
          dataIndex: 'phone',
          sorter: false,
        },
        {
          title: '角色',
          dataIndex: 'roleName',
        },
        {
          title: '状态',
          dataIndex: 'status',
          scopedSlots: { customRender: 'status' }
        },
        {
          title: '更新时间',
          dataIndex: 'updateTime',
          sorter: true
        },
        {
          title: '操作',
          dataIndex: 'action',
          scopedSlots: { customRender: 'action' }
        }
      ],
      url: {
        list: '/system/user/list',
        frozen: '/system/user/frozen',
        frozenBatch: '/system/user/frozenBatch',
        unfrozen: '/system/user/unfrozen',
        unfrozenBatch: '/system/user/unfrozenBatch',
        resetPwd: '/system/user/resetPwd',
        delete: '/system/user/delete',
        deleteBatch: '/system/user/deleteBatch',
        exportExcel: '/system/user/exportExcel'
      }
    }
  },
  filters: {
    statusFilter (type) {
      return statusMap[type].text
    },
    statusTypeFilter (type) {
      return statusMap[type].status
    }
  },
  methods: {
    // 冻结用户
    frozen(record){
      this.$loading.show()
      putAction(this.url.frozen, {id: record.id}).then(res => {
        if(!res.code){
          this.$message.success('操作成功')
          this.refreshData()
        }
      }).finally(() => {
        this.$loading.hide()
      })
    },
    // 解冻用户
    unfrozen(record){
      this.$loading.show()
      putAction(this.url.unfrozen, {id: record.id}).then(res => {
        if(!res.code){
          this.$message.success('操作成功')
          this.refreshData()
        }
      }).finally(() => {
        this.$loading.hide()
      })
    },
    // 重置密码
    resetPwd(record){
      this.$confirm({
        title: "重置密码",
        content: "确认重置用户【" + record.name + "】密码为手机号后六位？",
        onOk: () => {
          this.$loading.show()
          putAction(this.url.resetPwd, {id: record.id}).then(res => {
            if(!res.code){
              this.$message.success('操作成功')
            }
          }).finally(() => {
            this.$loading.hide()
          })
        }
      });
    },
    // 删除
    handleDelete(record){
      this.$confirm({
        title: "删除用户",
        content: "确认删除用户【" + record.name + "】吗？",
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
    // 批量冻结
    handleFrozenBatch(){
      if(this.selectedRowKeys.length == 0){
        this.$message.warning('请选择需要冻结的用户')
        return
      }
      this.$confirm({
        title: "批量冻结用户",
        content: "确认冻结选中的用户吗？",
        onOk: () => {
          this.$loading.show()
          putAction(this.url.frozenBatch, {ids: this.selectedRowKeys}).then(res => {
            if(!res.code){
              this.$message.success('操作成功')
              this.refreshData()
            }else{
              this.$message.error(res.msg)
            }
          }).finally(() => {
            this.$loading.hide()
          })
        }
      });
    },
    // 批量解冻
    handleUnfrozenBatch(){
      if(this.selectedRowKeys.length == 0){
        this.$message.warning('请选择需要解冻的用户')
        return
      }
      this.$confirm({
        title: "批量解冻用户",
        content: "确认解冻选中的用户吗？",
        onOk: () => {
          this.$loading.show()
          putAction(this.url.unfrozenBatch, {ids: this.selectedRowKeys}).then(res => {
            if(!res.code){
              this.$message.success('操作成功')
              this.refreshData()
            }else{
              this.$message.error(res.msg)
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