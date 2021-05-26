<template>
  <a-layout class="jackeroo-body">
    <a-layout-sider v-model="collapsed" :trigger="null" collapsible :class="{'jackeroo-sider': !collapsed}">
      <logo title="Jackeroo Boot" :show-title="!collapsed"></logo>
      
      <s-menu :menu="menus"></s-menu>
    </a-layout-sider>
    <a-layout-content>
      <a-layout>
        <a-layout-header class="jackeroo-header">
          <div class="jackeroo-header-content">
            <div class="jackeroo-header-collapse" @click="handleCollapse">
              <a-icon
                class="trigger"
                :type="collapsed ? 'menu-unfold' : 'menu-fold'"
              />
            </div>
            <right-content></right-content>
          </div>
        </a-layout-header>
        <a-layout-content>
          <router-view />
        </a-layout-content>
        <a-layout-footer>
          <page-footer></page-footer>
        </a-layout-footer>
      </a-layout>
    </a-layout-content>
  </a-layout>
</template>

<script>
import PageFooter from '@/components/Layout/PageFooter'
import Logo from '@/components/Layout/Logo'
import { mapState } from 'vuex'
import SMenu from '@/components/Menu/Menu'
import RightContent from '@/components/GlobalHeader/RightContent';

export default {
  components: {
    PageFooter,
    Logo,
    SMenu,
    RightContent
  },
  data() {
    return {
      menus: [],
      // 侧栏收起状态
      collapsed: false
    }
  },
  computed: {
    ...mapState({
      // 动态主路由
      mainMenu: state => state.permission.addRouters
    })
  },
  created() {
    const routes = this.mainMenu.find(item => item.path === '/')
    this.menus = (routes && routes.children) || []
    // 处理侧栏收起状态
    this.$watch('collapsed', () => {
      this.$store.commit(SIDEBAR_TYPE, this.collapsed)
    })
    this.$watch('isMobile', () => {
      this.$store.commit(TOGGLE_MOBILE_TYPE, this.isMobile)
    })
  },
  methods: {
    handleCollapse(){
      this.collapsed = !this.collapsed
    }
  }
}
</script>

<style lang="less" scoped>
@height: 59px;

.jackeroo-sider{
  width: 256px !important;
  min-width: 256px !important;
  max-width: 256px !important;
}


.jackeroo-header{
  height: @height;
  line-height: @height;
  background-color: #fff;
  padding: 0;
  z-index: 9;

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

.jackeroo-body{
  min-height: 100vh;
}
</style>