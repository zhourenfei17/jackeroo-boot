import { getAction, deleteAction } from '@/api/manage'

export const JackerooListMixins = {
  data(){
    return {
      useSTable: true,
      dataSource: [],
      // 展开/关闭
      advanced: false,
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
      // 加载数据方法 必须为 Promise 对象
      loadData: (parameter) => {
        if(!useSTable){
          return
        }
        if(!this.url.list){
          this.$message.error("请设置url.list属性!")
          return
        }
        const requestParameters = Object.assign({}, parameter, this.queryParam)
        return getAction(this.url.list, requestParameters)
          .then(res => {
            return res.data
          })
      },
    }
  },
  computed: {
    rowSelection () {
      return {
        selectedRowKeys: this.selectedRowKeys,
        onChange: this.onSelectChange
      }
    }
  },
  methods: {
    // 选中行事件
    onSelectChange (selectedRowKeys, selectedRows) {
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
    // 查询 展开/收起
    toggleAdvanced () {
      this.advanced = !this.advanced
    },
    // 重置
    resetSearchForm () {
      this.queryParam = {
        date: moment(new Date())
      }
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
          }).finally(() => {
            this.$loading.hide()
          })
        }
      });
    }
  }
}