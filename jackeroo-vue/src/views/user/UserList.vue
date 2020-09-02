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
            <template v-if="advanced">
              <a-col :md="6" :sm="12">
                <a-form-item label="手机号">
                  <a-input v-model="queryParam.phone" placeholder="请输入手机号"/>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="12">
                <a-form-item label="性别">
                  <a-select v-model="queryParam.gender" placeholder="请选择性别" default-value="0">
                    <a-select-option value="" disabled>请选择</a-select-option>
                    <a-select-option value="1">男</a-select-option>
                    <a-select-option value="2">女</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </template>
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
        <a-button type="primary" icon="plus" @click="handleAdd">新建</a-button>
        <a-dropdown v-if="selectedRowKeys.length > 0">
          <a-menu slot="overlay">
            <a-menu-item key="1"><a-icon type="delete" />删除</a-menu-item>
            <!-- lock | unlock -->
            <a-menu-item key="2"><a-icon type="lock" />锁定</a-menu-item>
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
        <span slot="status" slot-scope="text">
          <a-badge :status="text | statusTypeFilter" :text="text | statusFilter" />
        </span>

        <span slot="action" slot-scope="text, record">
          <template>
            <action-list>
              <a @click="handleView(record)">详情</a>
              <a @click="handleEdit(record)">编辑</a>
              <a-popconfirm title="您确定要冻结该用户吗？" v-if="record.status == 0" @confirm="() => frozen(record)">
                <a class="careful">冻结</a>
              </a-popconfirm>
              <a-popconfirm title="您确定要解冻该用户吗？" v-if="record.status == 1" @confirm="() => unfrozen(record)">
                <a class="warning">解冻</a>
              </a-popconfirm>
              <action-menu-list>
                <a @click="resetPwd(record)">重置密码</a>
                <a @click="handleDelete(record)">删除</a>
              </action-menu-list>
            </action-list>
          </template>
        </span>
      </s-table>

      <user-form-modal ref="formModal" @ok="handleOk"></user-form-modal>
    </a-card>
  </page-header-wrapper>
</template>

<script>
import { STable, Ellipsis } from '@/components'
import UserFormModal from './modal/UserFormModal'
import {JackerooListMixins} from '@/mixins/JackerooListMixins'
import { putAction, getAction, deleteAction } from '@/api/manage'
import JSelect from '@/components/jackeroo/JSelect'

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
    JSelect,
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
        unfrozen: '/system/user/unfrozen',
        resetPwd: '/system/user/resetPwd',
        delete: '/system/user/delete'
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
        if(res.code === 0){
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
        if(res.code === 0){
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
            if(res.code === 0){
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

<style lang="less" scoped>
  .warning{
    color: #dc8545
  }
  .careful{
    color: #8a8282
  }
</style>