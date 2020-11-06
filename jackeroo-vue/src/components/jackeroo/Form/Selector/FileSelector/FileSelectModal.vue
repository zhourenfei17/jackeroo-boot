<template>
  <j-modal
    ref="modal"
    :title="title"
    :width="width"
    :visible="visible"
    :fullscreen.sync="fullscreen"
    :switchFullscreen="showFullscreenBtn"
    :confirmLoading="false"
    okText="确定"
    @ok="handleSubmit"
    @cancel="cancel"
    :autoHeight="false"
  >
    <a-directory-tree 
      :treeData="treeData" 
      :loadData="loadFileList" 
      :selectedKeys="selectedKeys"
      @select="handleSelect">

    </a-directory-tree>
  </j-modal>
</template>

<script>
import { getAction } from '@/api/manage'
import {JackerooFormMixins} from '@/mixins/JackerooFormMixins'

export default {
  mixins: [JackerooFormMixins],
  props:{
    // 仅可以选择目录
    onlyDirectory: {
      type: Boolean,
      default: true
    },
    // 是否可多选
    multiple: {
      tpye: Boolean,
      default: false
    }
  },
  data(){
    return {
      title: '请选择文件',
      width: '40vw',
      selectedKeys: [],
      treeData: [],
      url: {
        rootFiles: '/online/generate/rootFiles',
        listFiles: '/online/generate/listFiles'
      }
    }
  },
  created(){
    this.loadRootFiles()
  },
  methods: {
    loadRootFiles(){
      getAction(this.url.rootFiles).then(result => {
        if(!result.code){
          this.treeData = result.data
        }
      })
    },
    loadFileList(treeNode){
      return new Promise(resolve => {
        if(treeNode.dataRef.children){
          resolve()
          return
        }
        getAction(this.url.listFiles, {path: treeNode.dataRef.key}).then(result => {
          if(!result.code){
            treeNode.dataRef.children = result.data
            this.treeData = [...this.treeData]
            resolve()
          }
        })
      })
    },
    handleSelect(selectedKeys, e){
      if(this.onlyDirectory && e.node.dataRef.isLeaf){
        this.$message.warning('只能选择目录')
        return
      }
      this.selectedKeys = selectedKeys
    },
    handleSubmit(){
      if(this.selectedKeys.length == 0){
        this.$message.warning('请选择文件/目录')
        return
      }
      this.$emit('change', this.selectedKeys)
      this.cancel()
    },
  }
}
</script>
