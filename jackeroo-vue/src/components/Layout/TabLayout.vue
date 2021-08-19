<template>
  <div>
    <a-tabs v-if="multiTab" v-model="activePage" type="editable-card" size="small" hideAdd :tabBarStyle="tabbarStyle" @change="handleChange" @edit="handleEdit">
      <template slot="tabBarExtraContent">
        <a-dropdown>
          <span class="tab-more">
            <a-icon type="more"></a-icon>
          </span>

          <a-menu slot="overlay">
            <a-menu-item @click="refresh">刷新</a-menu-item>
            <a-menu-item>关闭当前页</a-menu-item>
            <a-menu-item>关闭其他页</a-menu-item>
            <a-menu-item>关闭所有页</a-menu-item>
          </a-menu>
        </a-dropdown>
      </template>
      <a-tab-pane v-for="page in pageList" :key="page.fullPath">
        <span slot="tab">
          {{page.meta.title}} <a-icon v-show="page.fullPath === activePage" type="reload" class="jackeroo-tab-refresh" @click="refresh"></a-icon>
        </span>
      </a-tab-pane>
    </a-tabs>
    <div v-if="visible">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  props: {
    multiTab: {
      type: Boolean
    }
  },
  data() {
    return {
      // 页面列表
      pageList: [],
      // 当前活动页
      activePage: '',
      tabbarStyle: {
        'margin-bottom': '0px', 
        'padding-left': '10px'
      },
      visible: true
    }
  },
  watch: {
    '$route': {
      immediate: true,
      handler(route) {
        this.activePage = route.fullPath

        if(this.multiTab){
          if(this.pageList.filter(page => page.fullPath === route.fullPath).length == 0){
            this.pageList.push({...route})
          }
        }else{
          this.pageList = [{...route}]
        }
      }
    }
  },
  methods: {
    handleChange(activeKey){
      this.activePage = activeKey
      let page = this.pageList.filter(item => item.fullPath === activeKey)[0]
      this.$router.push({path: page.fullPath})
    },
    handleEdit(targetKey, action){
      if(action === 'remove'){
        const pageList = []
        let index
        for (let i = 0; i < this.pageList.length; i++) {
          if(this.pageList[i].fullPath === targetKey){
            index = i
          }else{
            pageList.push(this.pageList[i])
          }
        }
        index = (index >= pageList.length) ? pageList.length - 1 : index
        this.pageList = pageList
        this.activePage = pageList[index].fullPath
        this.$router.push({path: pageList[index].fullPath})
      }
    },
    refresh(){
      this.visible = false
      this.$nextTick(() => {
        this.visible = true
      })
    }
  }
}
</script>

<style lang="less" scoped>
@import "../index.less";
@import '~ant-design-vue/dist/antd.less';

.tab-more{
  display: inline-block;
  width: 40px;
  height: 40px;
  cursor: pointer;
  text-align: center;
}

.tab-more:hover{
  color: @primary-color;
  background-color: #fff;
}

.jackeroo-tab-refresh{
  margin-left: 5px;
  margin-right: 0px;
  font-size: 13px;
  color: rgba(0, 0, 0, 0.45);
}

.jackeroo-tab-refresh:hover{
  color: @primary-color;
}
</style>