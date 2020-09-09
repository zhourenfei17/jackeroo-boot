<template>
  <a-card :bordered="false" :title="title" id="searchCard" :headStyle="headStyle" :bodyStyle="bodyStyle" :class="{screenFull: isFullscreen}">
    <div slot="extra">
      <slot name="table-operator" v-if="existTableOperator"></slot>
      <a-divider type="vertical" v-if="existTableOperator"></a-divider>
      <div class="icons-list">
        <a-tooltip title="刷新" :placement="placement" v-if="showRefresh">
          <a-icon type="redo" @click="reload"></a-icon>
        </a-tooltip>
        <a-tooltip title="行高" :placement="placement" v-if="showLineHeight">
          <a-dropdown :trigger="['click']">
            <a-icon type="column-height"></a-icon>
            <a-menu slot="overlay" style="width: 80px;" :selectedKeys="[tableSize]" @click="changeTableSize">
              <a-menu-item key="default">默认</a-menu-item>
              <a-menu-item key="middle">中等</a-menu-item>
              <a-menu-item key="small">紧凑</a-menu-item>
            </a-menu>
          </a-dropdown>
          
        </a-tooltip>
        <a-tooltip title="列设置" :placement="placement" v-if="showColumnSet && columns">
          <a-dropdown :trigger="['click']">
            <a-icon type="setting"></a-icon>
            <!-- <a-table :pagination="false" :showHeader="false" :columns="columnsCol" rowKey="title" :dataSource="columns" slot="overlay" style="width: 150px;"></a-table> -->

            <!-- <div slot="overlay" style="width: 150px;background-color: #fff;">
              <a-table 
                :pagination="false" 
                :showHeader="false" 
                :columns="columnsCol" 
                rowKey="title" 
                :dataSource="columns" 
                size="small"
                :rowSelection="rowSelection"></a-table>
            </div> -->
            <a-menu slot="overlay" style="width: 150px;" :multiple="true" :selectedKeys="columnKeys" @select="selectColumn" @deselect="deselectColumn">
              <a-menu-item v-for="col in columns" :key="col.dataIndex">
                <!-- <a-checkbox>{{col.title}}</a-checkbox> -->
                {{col.title}}
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </a-tooltip>
        <a-tooltip title="全屏" v-if="!isFullscreen && showFullScreen" :placement="placement">
          <a-icon type="fullscreen" @click="fullscreen"></a-icon>
        </a-tooltip>
        <a-tooltip title="退出全屏" v-if="isFullscreen && showFullScreen" :placement="placement">
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
  name: 'SearchCard',
  props: {
    title: {
      type: String,
      default: ''
    },
    reload: {
      type: Function
    },
    tableSize: {
      type: String
    },
    columns: {
      type: Array
    },
    showRefresh: {
      tpye: Boolean,
      default: true
    },
    showLineHeight: {
      tpye: Boolean,
      default: true
    },
    showColumnSet: {
      tpye: Boolean,
      default: true
    },
    showFullScreen: {
      tpye: Boolean,
      default: true
    }
  },
  data(){
    return {
      isFullscreen: false,
      placement: 'top',
      // 选中行的key
      selectedRowKeys: [],
      // 选中行的数据
      selectedRows: [],
      headStyle:{
        marginTop: '20px',
        borderBottom: '0px'
      },
      bodyStyle:{
        padding: '0px'
      },
      columnsCol: [
        {
          title: '列名',
          dataIndex: 'title'
        }
      ],
      columnKeys: []
    }
  },
  created(){
    if(this.columns){
      const keys = []
      for(const col of this.columns){
        keys.push(col.dataIndex)
      }
      this.columnKeys = keys
    }
  },
  watch:{
    isFullscreen(val){
      if(!val){
        this.placement = 'top'
      }else{
        this.placement = 'bottom'
      }
      screenfull.toggle(document.getElementById('searchCard'))
    }
  },
  computed: {
    existTableOperator(){
      let tableOperatorSlot = this.$slots['table-operator']
      if(tableOperatorSlot && tableOperatorSlot != undefined){
        return true
      }else{
        return false
      }
    },
    rowSelection () {
      return {
        selectedRowKeys: this.selectedRowKeys,
        onChange: this.onSelectChange
      }
    }
  },
  methods: {
    fullscreen(){
        // el.requestFullscreen()
      if(screenfull.isEnabled){
        this.isFullscreen = !this.isFullscreen
      }else{
        this.$message.warning('当前浏览器不支持全屏')
      }
    },
    changeTableSize({item, key}){
      this.$parent.$data.tableSize = key
    },
    selectColumn({item, key, selectedKeys}){
      //this.columnKeys.push(key)
      this.columnKeys = selectedKeys
    },
    deselectColumn({item, key, selectedKeys}){
      /* let index = -1
      for(let i = 0; i < this.columnKeys.length; i++){
        if(key == this.columnKeys[i]){
          index = i
          break
        }
      }
      this.columnKeys.splice(index, 1) */
      console.log('selectedKeys', selectedKeys)
      this.columnKeys = selectedKeys
    },
    // 选中行事件
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
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
    }
  }
  .screenFull{
    padding: 20px;
  }
</style>