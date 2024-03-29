<template>
  <a-layout class="jackeroo-body">
    <a-layout-sider v-model="collapsed" :trigger="null" collapsible width="256" :class="{'jackeroo-fixed-sider': settings.fixSiderbar}">
      <logo :title="title" :collapsed="!collapsed"></logo>
      
      <s-menu :menu="menus" :collapsed="collapsed"></s-menu>
    </a-layout-sider>
    <a-layout-content :class="{'jackeroo-fixed-right': settings.fixSiderbar, 'jackeroo-fixed-right-collapsed': settings.fixSiderbar && collapsed}">
      <a-layout>
        <a-layout-header :class="{'jackeroo-header': true, 'jackeroo-fixed-header': settings.fixedHeader, 'jackeroo-fixed-header-collapsed': settings.fixedHeader && collapsed}">
          <div class="jackeroo-header-content">
            <div class="jackeroo-header-collapse" @click="handleCollapse">
              <a-icon
                class="trigger"
                :type="collapsed ? 'menu-unfold' : 'menu-fold'"
              />
            </div>
            <right-content :top-menu="settings.layout === 'topmenu'" :theme="settings.theme" @globalSetting="handleGlobalSetting"></right-content>
            <global-setting :settings="settings" @change="handleSettingChange" ref="globalSetting"></global-setting>
          </div>
        </a-layout-header>
        <a-layout-content :class="{'jackeroo-fixed-content': settings.fixedHeader, 'jackeroo-content': true}">
          <tab-layout :multi-tab="settings.multiTab"></tab-layout>
        </a-layout-content>
        <a-layout-footer class="jackeroo-footer">
          <global-footer></global-footer>
        </a-layout-footer>
      </a-layout>
    </a-layout-content>
  </a-layout>
</template>

<script>
import PageFooter from '@/components/Layout/PageFooter'
import TabLayout from '@/components/Layout/TabLayout'
import GlobalFooter from '@/components/GlobalFooter/index'
import Logo from '@/components/Layout/Logo'
import { mapState } from 'vuex'
import SMenu from '@/components/Menu/Menu'
import RightContent from '@/components/GlobalHeader/RightContent'
import GlobalSetting from '@/components/Layout/GlobalSetting'
import defaultSettings from '@/config/defaultSettings'
import { CONTENT_WIDTH_TYPE, SIDEBAR_TYPE, TOGGLE_MOBILE_TYPE } from '@/store/mutation-types'

export default {
  components: {
    PageFooter,
    Logo,
    SMenu,
    RightContent,
    GlobalFooter,
    GlobalSetting,
    TabLayout
  },
  data() {
    return {
      menus: [],
      // 侧栏收起状态
      collapsed: false,
      title:  defaultSettings.title,
    }
  },
  computed: {
    ...mapState({
      // 动态主路由
      mainMenu: state => state.permission.addRouters,
      settings: state => {
        return {
          // 布局类型
          layout: state.app.layout,
          // CONTENT_WIDTH_TYPE
          contentWidth: state.app.layout === 'sidemenu' ? CONTENT_WIDTH_TYPE.Fluid : state.app.contentWidth,
          // 主题 'dark' | 'light'
          theme: state.app.theme,
          // 主色调
          primaryColor: state.app.color,
          // 固定头部
          fixedHeader: state.app.fixedHeader,
          // 固定侧边栏
          fixSiderbar: state.app.fixedSidebar,
          // 色弱模式
          colorWeak: state.app.weak,
          // 多页模式
          multiTab: state.app.multiTab,
          hideHintAlert: false,
          hideCopyButton: false
        }
      }
    })
  },
  /* watch: {
    collapsed(){
      this.$store.commit(SIDEBAR_TYPE, this.collapsed)
    }
  }, */
  created() {
    const routes = this.mainMenu.find(item => item.path === '/')
    this.menus = (routes && routes.children) || []
  },
  mounted () {
    const userAgent = navigator.userAgent
    if (userAgent.indexOf('Edge') > -1) {
      this.$nextTick(() => {
        this.collapsed = !this.collapsed
        setTimeout(() => {
          this.collapsed = !this.collapsed
        }, 16)
      })
    }
  },
  methods: {
    handleCollapse(){
      this.collapsed = !this.collapsed
    },
    handleSettingChange ({ type, value }) {
      type && (this.settings[type] = value)
      switch (type) {
        case 'contentWidth':
          this.settings[type] = value === 'Fixed'
          break
        case 'layout':
          if (value === 'sidemenu') {
            this.settings.contentWidth = false
          } else {
            this.settings.fixSiderbar = false
            this.settings.contentWidth = true
          }
          break
      }
    },
    handleGlobalSetting(){
      this.$refs.globalSetting.show()
    }
  }
}
</script>

<style lang="less" scoped>
@import "~ant-design-vue/es/style/themes/default.less";

@height: 59px;

.jackeroo-body{
  min-height: 100vh;

  .jackeroo-fixed-header{
    position: fixed;
    top: 0;
    width: calc(100% - 256px);
    transition: width .2s
  }

  .jackeroo-fixed-header-collapsed{
    width: calc(100% - 80px) !important;
  }

  .jackeroo-header{
    height: @height;
    line-height: @height;
    background-color: #fff;
    padding: 0;
    z-index: 999;

    .jackeroo-header-content{
      height: @height;
      line-height: @height;
      width: 100%;
      box-shadow: 0 1px 4px rgba(0,21,41,.08);
      display: flex;

      .jackeroo-header-collapse{
        padding: 0 22px;
        font-size: 20px;
      }

      .jackeroo-header-collapse:hover{
        background-color: rgb(243, 243, 243);
        cursor: pointer;
      }
    }
  }

  .jackeroo-fixed-sider{
    position: fixed;
    height: 100vh;
    left: 0;
    overflow: auto;
  }

  .jackeroo-fixed-right{
    margin-left: 256px;
    transition: all .2s
  }

  .jackeroo-fixed-right-collapsed{
    margin-left: 80px !important;
  }

  .jackeroo-content{
    min-height: calc(100vh - @height - 122px);
  }

  .jackeroo-fixed-content{
    margin-top: @height;
  }

  .jackeroo-footer{
    padding: 0;
  }
}
</style>