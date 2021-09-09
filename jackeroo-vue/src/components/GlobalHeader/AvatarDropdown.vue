<template>
  <div v-if="nickname">
    <change-pwd ref="changePwd"></change-pwd>
    <a-dropdown  placement="bottomRight">
      <span class="ant-pro-account-avatar">
        <a-avatar v-if="avatar" size="small" :src="avatar" class="antd-pro-global-header-index-avatar" />
        <img v-else src="~@/assets/avatar.png" class="jackeroo-header-default-avatar">
        <span>{{ nickname }}</span>
      </span>
      
      <template v-slot:overlay>
        <a-menu class="ant-pro-drop-down menu" :selected-keys="[]">
          <a-menu-item v-if="menu" key="center" @click="handleToCenter">
            <a-icon type="user" />
            个人中心
          </a-menu-item>
          <a-menu-item v-if="menu" key="key" @click="handleChangePwd">
            <a-icon type="key" />
            修改密码
          </a-menu-item>
          <a-menu-item v-if="menu" key="globalSetting" @click="handleGlobalSetting">
            <a-icon type="global" />
            系统设置
          </a-menu-item>
          <a-menu-divider v-if="menu" />
          <a-menu-item key="logout" @click="handleLogout">
            <a-icon type="logout" />
            退出登录
          </a-menu-item>
        </a-menu>
      </template>
    </a-dropdown>
  </div>
  <span v-else>
    <a-spin size="small" :style="{ marginLeft: 8, marginRight: 8 }" />
  </span>
</template>

<script>
import { Modal } from 'ant-design-vue'
import { mapActions, mapGetters } from 'vuex'
import ChangePwd from './ChangePwd'

export default {
  name: 'AvatarDropdown',
  props: {
    currentUser: {
      type: Object,
      default: () => null
    },
    menu: {
      type: Boolean,
      default: true
    }
  },
  components: {
    ChangePwd
  },
  computed: {
    ...mapGetters(['nickname', 'avatar'])
  },
  methods: {
    ...mapActions(['Login', 'Logout']),
    handleToCenter () {
      this.$router.push({ path: '/account/center' })
    },
    handleChangePwd() {
      this.$refs.changePwd.show()
    },
    handleGlobalSetting(){
      this.$emit('globalSetting')
    },
    handleLogout (e) {
      Modal.confirm({
        title: this.$t('退出登录'),
        content: this.$t('您确认要退出登录吗?'),
        onOk: () => {
          this.Logout().then(() => {
            this.$message.success('退出登录成功')
            this.$router.push({ path: '/user/login' })
          })
        },
        onCancel () {}
      })
    }
  }
}
</script>

<style lang="less" scoped>
.ant-pro-drop-down {
  /deep/ .action {
    margin-right: 8px;
  }
  /deep/ .ant-dropdown-menu-item {
    min-width: 160px;
  }
}

.jackeroo-header-default-avatar{
  width: 24px;
  height: 24px;
  border-radius: 50%;
  margin-right: 8px;
}
</style>
