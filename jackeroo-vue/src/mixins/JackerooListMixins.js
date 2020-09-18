import { getAction, deleteAction } from '@/api/manage'

export const JackerooListMixins = {
  data(){
    return {
      useSTable: true,
      dataSource: [],
      // 查询参数
      queryParam: {},
      // 选中行的key
      selectedRowKeys: [],
      // 选中行的数据
      selectedRows: [],
      // table的选中行的提示信息
      tableAlert: {
        // 是否显示
        show: true,
        // 是否显示清除按钮 
        clear: true,
        // 是否跨页选择
        multiPageSelect: true
      },
      // 主键字段
      tableKey: 'id',
      rowSelection : {
        selectedRowKeys: this.selectedRowKeys,
        onChange: this.onSelectChange
      },
      customRow: (record, index) => {
        return {
          on: {
            click: (event) => {
              let i = this.selectedRowKeys.indexOf(record[this.tableKey])
              if(i > -1){
                this.selectedRowKeys.splice(i, 1)
                this.selectedRows.splice(i, 1)
              }else{
                this.selectedRowKeys.push(record[this.tableKey])
                this.selectedRows.push(record)
              }
            }
          }
        }
      },
      // 表大小
      tableSize: 'default',
      // 分页，支持['auto', true, false]
      showPagination: 'auto',
      // 加载数据方法 必须为 Promise 对象
      loadData: (parameter) => {
        if(!this.useSTable){
          return
        }
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        const requestParameters = Object.assign({}, parameter, this.queryParam)
        if(!parameter){
          for(const col of this.columns){
            if(col.defaultSortOrder){
              requestParameters.sortField = col.dataIndex
              requestParameters.sortOrder = col.defaultSortOrder
            }
          }
        }
        return getAction(this.url.list, requestParameters)
          .then(res => {
            if(res.code == 0){
              return res.data
            }else{
              return undefined
            }
          })
      },
    }
  },
  computed: {
    
  },
  methods: {
    // 选中行事件
    onSelectChange (selectedRowKeys, selectedRows) {
      console.log('selectedRowKeys', selectedRowKeys)
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },
    // 添加
    handleAdd () {
      this.$refs.formModal.visible = true
      this.$refs.formModal.flag.add = true
      this.$refs.formModal.add()
    },
    // 编辑
    handleEdit (record) {
      this.$refs.formModal.visible = true
      this.$refs.formModal.flag.edit = true
      this.$refs.formModal.edit(record.id)
    },
    // 详情
    handleView (record){
      this.$refs.formModal.visible = true
      this.$refs.formModal.flag.view = true
      this.$refs.formModal.edit(record.id)
    },
    // 重置
    reset () {
      this.queryParam = {}
    },
    // formModal保存后刷新table
    handleOk(){
      this.refreshData()
    },
    // 刷新表数据，true返回首页并刷新，false仅刷新当前页，默认只刷新当前页
    refreshData(backFirstPage = false){
      this.$refs.table.refresh(backFirstPage)
    },
    // 删除
    handleDelete(record){
      this.$confirm({
        title: "删除数据",
        content: "确认删除该条数据吗？",
        onOk: () => {
          this.$loading.show()
          deleteAction(this.url.delete, {id: record.id}).then(res => {
            this.$message.success('操作成功')
            this.refreshData()
          }).finally(() => {
            this.$loading.hide()
          })
        }
      });
    },
    syncDataSource(data){
      this.dataSource = data
    }
  }
}