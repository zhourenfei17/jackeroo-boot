<template>
  <j-drawer
      :title="title"
      :width="width"
      :visible.sync="visible"
      @close="cancel"
      @ok="handleSubmit"
    >
    
    <j-spin :spinning="loading">
      <div class="table-operator">
        <a-dropdown>
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="handleExpandAll">全部展开</a-menu-item>
            <a-menu-item key="2" @click="handleMergeAll">全部合并</a-menu-item>
            <a-menu-item key="3" @click="handleExpandRoot">仅展开根</a-menu-item>
            <a-menu-item key="4" @click="handleMergeLeaf">合并叶子</a-menu-item>
          </a-menu>
          <a-button icon="column-height">展开操作<a-icon type="down"></a-icon></a-button>
        </a-dropdown>

        <a-dropdown>
          <a-menu slot="overlay">
            <a-menu-item key="1" @click="handleSelectAll">全部选择</a-menu-item>
            <a-menu-item key="2" @click="handleUnSelectAll">取消全选</a-menu-item>
            <a-menu-item key="3" @click="handleContrarySelect">全部反选</a-menu-item>
          </a-menu>
          <a-button icon="check">选择操作<a-icon type="down"></a-icon></a-button>
        </a-dropdown>
      </div>

      <a-divider></a-divider>

      <a-tree 
        v-model="checkedKeys"
        checkable
        showLine
        :expandedKeys="expandKeys"
        :treeData="treeData"
        @expand="handleExpand"
        @check="handleCheck"
      >
      </a-tree>
    </j-spin>

  </j-drawer>
</template>

<script>
import { getAction, postAction } from '@/api/manage'
import { JDrawer} from '@/components'


export default {
  components: {
    JDrawer,
  },
  mixins: [],
  data(){
    return {
      title: '菜单信息',
      tableName: 'sys_user',
      width: '30vw',
      url: {
        getTreeData: '/system/menu/findRolePermissionTree',
        getMenuAndPermissionList: '/system/role/findRoleMenuAndPermission',
        saveRolePermission: '/system/role/saveRolePermission'
      },
      treeData: [],
      checkedKeys:[],
      halfCheckedKeys: [],
      visible: false,
      loading: true,
      roleId: null,
      expandKeys:[]
    }
  },
  created(){
    this.loadTreeData()
  },
  methods: {
    load(roleId){
      this.roleId = roleId
      this.visible = true
      this.loading = false
      this.$nextTick(() => {
        this.loadMenuAndPermissionByRole()
      })
    },
    loadTreeData(){
      getAction(this.url.getTreeData).then(res => {
        if(!res.code){
          this.treeData = res.data
        }
      })
    },
    loadMenuAndPermissionByRole(){
      getAction(this.url.getMenuAndPermissionList, {roleId: this.roleId}).then(res => {
        if(!res.code){
          //this.checkedKeys = res.data
          if(res.data.length > 0){
            const checkedKeys = [], halfCheckedKeys = [], uncheckNodeKeys = []
            this.loopAndCheckNode(this.treeData, res.data, checkedKeys, halfCheckedKeys, uncheckNodeKeys)
            this.checkedKeys = checkedKeys
            this.halfCheckedKeys = halfCheckedKeys

            // 只展开已选择的节点
            const expandKeys = []
            this.loopAndFindNode(this.treeData, expandKeys)
            this.expandKeys = expandKeys.filter(item => uncheckNodeKeys.indexOf(item) == -1)
          }else{
            // 只展开根节点
            this.handleExpandRoot()
          }
        }
      })
    },
    loopAndCheckNode(treeData, permissionList, checkedKeys, halfCheckedKeys, uncheckNodeKeys, parentKey = null){
      let childrenCheckedNum = 0
      for(const node of treeData){
        let checked = false
        if(permissionList.indexOf(node.key) > -1){
          childrenCheckedNum++
          checked = true
        }
        if(node.children){
          if(!checked){
            // 如果当前节点存在子节点，但是当前节点为选中，则直接添加至unCheckNodeKeys，不再继续检查其子节点状态
            uncheckNodeKeys.push(node.key)
          }else{
            this.loopAndCheckNode(node.children, permissionList, checkedKeys, halfCheckedKeys, uncheckNodeKeys, node.key)
            // 如果当前节点在遍历子节点的时候，已经存在于halfCheckedKeys中，则表示该节点的父节点也会存在halfCheckedKeys中
            if(halfCheckedKeys.indexOf(node.key) > -1){
              childrenCheckedNum--
            }
          }
        }else{
          // 如果当前节点没有子节点，且当前节点已选中的，则直接添加到checkedKeys
          if(checked){
            checkedKeys.push(node.key)
          }
        }
      }
      // 如果当前节点为子节点，则判断所有兄弟节点是否都已选中，如果是则添加到checkedKeys，否则添加到halfCheckedKeys
      if(parentKey){
        if(childrenCheckedNum == treeData.length){
          checkedKeys.push(parentKey)
        }else{
          halfCheckedKeys.push(parentKey)
        }
      }
    },
    //全部展开
    handleExpandAll(){
      const expandKey = []
      this.loopAndFindNode(this.treeData, expandKey)

      this.expandKeys = expandKey
    },
    //全部合并
    handleMergeAll(){
      this.expandKeys = []
    },
    // 仅展开根
    handleExpandRoot(){
      const expandKey = []
      for(const node of this.treeData){
        expandKey.push(node.key)
      }
      this.expandKeys = expandKey
    },
    // 合并叶子
    handleMergeLeaf(){
      let leafParentKeys = []
      this.loopAndFindLeafParent(this.treeData, leafParentKeys)
      leafParentKeys = [...new Set(leafParentKeys)]

      this.expandKeys = this.expandKeys.filter(item => leafParentKeys.indexOf(item) == -1)
    },
    //全部选择
    handleSelectAll(){
      const checkedKeys = []
      this.loopAndFindNode(this.treeData, checkedKeys, true)

      this.checkedKeys = checkedKeys
      this.halfCheckedKeys = []
    },
    //取消全选
    handleUnSelectAll(){
      this.checkedKeys = []
      this.halfCheckedKeys = []
    },
    //反选
    handleContrarySelect(){
      const all = []
      this.loopAndFindNode(this.treeData, all, true)

      const contrarySelect = all.filter(item => this.checkedKeys.indexOf(item) == -1 && this.halfCheckedKeys.indexOf(item) == -1)
      this.parentLink = false
      this.checkedKeys = contrarySelect
    },
    handleParentLink(){
      this.parentLink = false
    },
    handleParentUnlink(){
      this.parentLink = true
    },
    loopAndFindNode(treeData, keyList, findAll = false){
      for(const node of treeData){
        if(node.children){
          keyList.push(node.key)
          this.loopAndFindNode(node.children, keyList, findAll)
        }else{
          if(findAll){
            keyList.push(node.key)
          }
        }
      }
    },
    loopAndFindLeafParent(treeData, keyList, parentKey = null){
      for(const node of treeData){
        if(node.children){
          this.loopAndFindLeafParent(node.children, keyList, node.key)
        }else{
          if(parentKey){
            keyList.push(parentKey)
          }
        }
      }
    },
    handleExpand(expandedKeys){
      this.expandKeys = expandedKeys
    },
    handleCheck(checkedKeys, info){
      this.checkedKeys = checkedKeys
      this.halfCheckedKeys = info.halfCheckedKeys
    },
    // 保存
    handleSubmit(){
      const checkedKeys = [...this.checkedKeys, ...this.halfCheckedKeys]
      postAction(this.url.saveRolePermission, {roleId: this.roleId, permissionList: checkedKeys}).then(res => {
        if(!this.code){
          this.$message.success('操作成功')
          this.cancel()
        }
      })
    },
    cancel(){
      this.visible = false
      
      this.loading = true
      this.checkedKeys = []
      this.expandKeys = []
      this.halfCheckedKeys = []
    }
  }
}
</script>

<style lang="less" scoped>
.tooltip-red{
  color: #ff4545;
}
.jackeroo-drawer-footer{
  position: absolute;
  right: 0;
  bottom: 0;
  width: 100%;
  border-top: 1px solid #e9e9e9;
  padding: 10px 16px;
  background: #fff;
  text-align: right;
  z-index: 1;
}
</style>