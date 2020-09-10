<template>
  <a-card :bordered="false" :title="title" id="searchCard" :headStyle="headStyle" :bodyStyle="bodyStyle" :class="{screenFull: isFullscreen}">
    <div slot="extra">
      <slot name="toolbar" v-if="existToolbar"></slot>
      <a-divider type="vertical" v-if="existToolbar"></a-divider>
      <div class="icons-list">
        <a-tooltip title="刷新" :placement="placement" v-if="icon.indexOf('refresh') > -1">
          <a-icon type="redo" @click="reload"></a-icon>
        </a-tooltip>
        <a-tooltip title="树展开操作" :placement="placement" v-if="icon.indexOf('treeExpand') > -1 && expandedRowKeys">
          <a-dropdown :trigger="['click']">
            <a-icon type="menu-unfold"></a-icon>

            <a-menu slot="overlay" style="width: 80px;" :selectable="false" @click="expandOperator">
              <a-menu-item key="expand">全部展开</a-menu-item>
              <a-menu-item key="merge">全部合并</a-menu-item>
            </a-menu>
          </a-dropdown>
        </a-tooltip>
        <a-tooltip title="行高" :placement="placement" v-if="icon.indexOf('lineHeight') > -1">
          <a-dropdown :trigger="['click']">
            <a-icon type="column-height"></a-icon>
            <a-menu slot="overlay" style="width: 80px;" :selectedKeys="[tableSize]" @click="changeTableSize">
              <a-menu-item key="default">默认</a-menu-item>
              <a-menu-item key="middle">中等</a-menu-item>
              <a-menu-item key="small">紧凑</a-menu-item>
            </a-menu>
          </a-dropdown>
        </a-tooltip>
        <a-tooltip title="对齐方式" :placement="placement" v-if="icon.indexOf('align') > -1">
          <a-dropdown :trigger="['click']">
            <a-icon type="align-center"></a-icon>
            <a-menu slot="overlay" style="width: 80px;" :selectedKeys="[align]" @click="changeTableAlign">
              <a-menu-item key="left">左对齐</a-menu-item>
              <a-menu-item key="center">居中对齐</a-menu-item>
              <a-menu-item key="right">右对齐</a-menu-item>
            </a-menu>
          </a-dropdown>
        </a-tooltip>
        <a-tooltip title="列设置" :placement="placement" v-if="icon.indexOf('columnSet') > -1 && columns">
          <a-dropdown :trigger="['click']" v-model="visibleColumn">
            <a-icon type="setting"></a-icon>

            <a-menu slot="overlay" style="width: 150px;" :multiple="true" :selectedKeys="columnKeys">
              
              <a-menu-item v-for="col in columnsCopy" :key="col.dataIndex || col.title">
                  <a-checkbox 
                    :checked="columnKeys.indexOf(col.dataIndex || col.title) > -1" 
                    @change="(e) => checkChange(e, col.dataIndex || col.title)" 
                    style="width:150px;"
                    >
                    {{col.title}}
                  </a-checkbox>
              </a-menu-item>
              
            </a-menu>
          </a-dropdown>
        </a-tooltip>
        <a-tooltip title="全屏" v-if="!isFullscreen && icon.indexOf('fullscreen') > -1" :placement="placement">
          <a-icon type="fullscreen" @click="fullscreen"></a-icon>
        </a-tooltip>
        <a-tooltip title="退出全屏" v-if="isFullscreen && icon.indexOf('fullscreen') > -1" :placement="placement">
          <a-icon type="fullscreen-exit" @click="fullscreen"></a-icon>
        </a-tooltip>
      </div>
    </div>

    <slot></slot>
  </a-card>
</template>

<script>
import screenfull from 'screenfull'

export default {
  name: 'DataCard',
  props: {
    // card标题
    title: {
      type: String,
      default: ''
    },
    // 数据刷新方法
    reload: {
      type: Function
    },
    // table的size属性
    tableSize: {
      type: String
    },
    // table的列对齐方式
    tableAlign:{
      type: String
    },
    // table的columns属性
    columns: {
      type: Array
    },
    // table的expandedRowKeys属性
    expandedRowKeys:{
      type: Array
    },
    // toolbar中的icon列表
    icon: {
      tpye: Array,
      default: () => {
        return ['refresh', 'lineHeight', 'align', 'columnSet', 'fullscreen']
      }
    }
  },
  data(){
    return {
      isFullscreen: false,
      placement: 'top',
      headStyle:{
        marginTop: '20px',
        borderBottom: '0px'
      },
      bodyStyle:{
        padding: '0px'
      },
      // 列设置中选中的行key
      columnKeys: [],
      // 列设置下拉菜单面板显示状态
      visibleColumn: false,
      // 复制后的原始列
      columnsCopy:[],
      // table表数据
      dataSource: [],
      // 对齐方式
      align: ''
    }
  },
  mounted(){
    const defaultSlot = this.$slots.default
    for(const slot of defaultSlot){
      const tags = slot.tag.split('-')
      if(tags[tags.length - 1].toLowerCase() == 'atable'){
        const table = slot.context
        if(table){
          if(table.dataSource){
            this.dataSource = table.dataSource
          }
        }
      }
    }
  },
  created(){
    if(this.columns){
      const keys = []
      for(const col of this.columns){
        keys.push(col.dataIndex || col.title)
      }
      this.columnKeys = keys

      this.columnsCopy = Object.assign(this.columns)
    }
    if(this.tableAlign){
      this.align = this.tableAlign
      if(this.columns){
        for(const col of this.columnsCopy){
          col.align = this.tableAlign
        }
        this.$emit('update:columns', this.columnsCopy)
      }
    }
  },
  watch:{
    isFullscreen(val){
      screenfull.toggle(document.getElementById('searchCard'))
      // screenfull.toggle(this.$parent.$el)
    },
    columnKeys(val){
      const columns = []
      for(const col of this.columnsCopy){
        if(val.indexOf(col.dataIndex || col.title) > -1){
          columns.push(col)
        }
      }
      // this.$parent.$data.columns = columns
      this.$emit('update:columns', columns)
    }
  },
  computed: {
    existToolbar(){
      let toolbarSlot = this.$slots['toolbar']
      if(toolbarSlot && toolbarSlot != undefined){
        return true
      }else{
        return false
      }
    },
  },
  methods: {
    // 列设置选择
    checkChange(e, val){
      let index = this.columnKeys.indexOf(val)
      if(e.target.checked){
        if(index == -1){
          this.columnKeys.push(val)
        }
      }else{
        if(index > -1){
          this.columnKeys.splice(index, 1)
        }
      }
    },
    // 全屏
    fullscreen(){
      if(screenfull.isEnabled){
        this.isFullscreen = !this.isFullscreen
      }else{
        this.$message.warning('当前浏览器不支持全屏')
      }
    },
    // 改变行高
    changeTableSize({item, key}){
      // this.$parent.$data.tableSize = key
      this.$emit('update:tableSize', key)
    },
    // 改变对齐方式
    changeTableAlign({key}){
      this.align = key
      if(key){
        const columns = []
        for(const col of this.columnsCopy){
          col.align = key

          if(this.columnKeys.indexOf(col.dataIndex || col.title) > -1){
            columns.push(col)
          }
        }
        this.$emit('update:columns', columns)
      }
    },
    // 数展开操作
    expandOperator({item, key}){
      if(key == 'expand'){
        const expandKeys = []
        this.loopAndFindNode(this.dataSource, expandKeys)

        // this.expandedRowKeys = expandKeys
        this.$emit('update:expandedRowKeys', expandKeys)
      }else if(key == 'merge'){
        // this.expandedRowKeys = []
        this.$emit('update:expandedRowKeys', [])
      }
    },
    loopAndFindNode(treeData, keyList){
      for(const node of treeData){
        if(node.children){
          keyList.push(node.id)
          this.loopAndFindNode(node.children, keyList)
        }else{
          keyList.push(node.id)
        }
      }
    },
  }
}
</script>

<style lang="less" scoped>
  .icons-list  {
    display: inline-block;
    .anticon{
      margin-right: 12px;
      font-size: 20px;
      cursor: pointer;
    }
  }
  .screenFull{
    padding: 20px;
  }
</style>