<template>
  <a-drawer :visible="visible" @close="() => {visible = false}" width="300">
    <a-row>
      <a-col :span="24" class="jackeroo-setting-title">
        整体风格设置
      </a-col>
      <a-col :span="24" class="jackeroo-setting-check-img">
        <a-row>
          <a-col :span="6" v-for="style in styleList" :key="style.name">
            <a-tooltip :title="style.label">
              <img :src="style.url" @click="() => checkStyle = style.name"/>

              <a-icon type="check" v-show="checkStyle == style.name" class="jackeroo-setting-check-icon"></a-icon>
            </a-tooltip>
          </a-col>
        </a-row>
      </a-col>

      <a-col :span="24" class="jackeroo-setting-title jackeroo-setting-mt-24">
        主题色
      </a-col>
      <a-col :span="24" class="jackeroo-setting-check-color">
        <a-tooltip :title="color.name" v-for="color in colorList" :key="color.name">
          <div class="jackeroo-setting-color-item" :style="{backgroundColor: color.color}" @click="() => checkColor = color.color">
            <a-icon type="check" v-show="checkColor == color.color" class="jackeroo-setting-check-icon"></a-icon>
          </div>
        </a-tooltip>
      </a-col>

      <a-col :span="24" class="jackeroo-setting-mt-24">
        <a-divider style="margin:0;"></a-divider>
      </a-col>

      <a-col :span="24" class="jackeroo-setting-title jackeroo-setting-mt-24">
        导航模式
      </a-col>
      <a-col :span="24" class="jackeroo-setting-check-img">
        <a-row>
          <a-col :span="6" v-for="nav in navList" :key="nav.name">
            <a-tooltip :title="nav.label">
              <img :src="nav.url" @click="() => checkNav = nav.name"/>

              <a-icon type="check" v-show="checkNav == nav.name" class="jackeroo-setting-check-icon"></a-icon>
            </a-tooltip>
          </a-col>
        </a-row>
      </a-col>

      <a-col :span="24" class="jackeroo-setting-switch">
        <div class="jackeroo-setting-switch-label">固定头部</div>
        <div class="jackeroo-setting-switch-item"><a-switch v-model="fixedHeader" size="small"></a-switch></div>
      </a-col>

      <a-col :span="24" class="jackeroo-setting-switch">
        <div class="jackeroo-setting-switch-label">固定侧边栏</div>
        <div class="jackeroo-setting-switch-item"><a-switch v-model="fixedSider" size="small"></a-switch></div>
      </a-col>
    </a-row>
  </a-drawer>
</template>

<script>
export default {
  data() {
    return {
      visible: false,
      // 整体布局风格
      styleList: [
        {
          name: 'light',
          label: '亮色风格',
          url: 'https://gw.alipayobjects.com/zos/antfincdn/NQ%24zoisaD2/jpRkZQMyYRryryPNtyIC.svg'
        },
        {
          name: 'dark',
          label: '暗色风格',
          url: 'https://gw.alipayobjects.com/zos/antfincdn/XwFOFbLkSM/LCkqqYNmvBEbokSDscrm.svg'
        }
      ],
      // 当前选中的风格
      checkStyle: undefined,
      // 主题色
      colorList:[
        {
          name: '拂晓蓝',
          color: 'rgb(24, 144, 255)'
        },
        {
          name: '薄暮',
          color: 'rgb(245, 34, 45)'
        },
        {
          name: '火山',
          color: 'rgb(250, 84, 28)'
        },
        {
          name: '日暮',
          color: 'rgb(250, 173, 20)'
        },
        {
          name: '明青',
          color: 'rgb(19, 194, 194)'
        },
        {
          name: '极光绿',
          color: 'rgb(82, 196, 26)'
        },
        {
          name: '极客蓝',
          color: 'rgb(47, 84, 235)'
        },
        {
          name: '酱紫',
          color: 'rgb(114, 46, 209)'
        }
      ],
      // 当前选中的主题色
      checkColor: undefined,
      // 导航模式
      navList: [
        {
          name: 'sideMenu',
          label: '侧边菜单布局',
          url: 'https://gw.alipayobjects.com/zos/antfincdn/XwFOFbLkSM/LCkqqYNmvBEbokSDscrm.svg'
        },
        {
          name: 'topMenu',
          label: '顶部菜单布局',
          url: 'https://gw.alipayobjects.com/zos/antfincdn/URETY8%24STp/KDNDBbriJhLwuqMoxcAr.svg'
        }
      ],
      // 当前选中的导航模式
      checkNav: undefined,
      // 固定头部
      fixedHeader: false,
      // 固定侧边栏
      fixedSider: false
    }
  },
  methods: {
    show(){
      this.visible = true
    }
  }
}
</script>

<style lang="less" scoped>
.jackeroo-setting-title{
  color: rgba(0,0,0,.85);
  font-size: 14px;
  line-height: 22px;
  font-weight: 500;
  margin-bottom: 12px;
}
.jackeroo-setting-mt-24{
  margin-top: 24px;
}

.jackeroo-setting-check-img{
  img{
    cursor: pointer;
    width: 48px;
  }
  .jackeroo-setting-check-icon{
    color: #f5222d;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    cursor: pointer;
  }
}

.jackeroo-setting-check-color{
  display: flex;

  .jackeroo-setting-color-item{
    width: 20px;
    height: 20px;
    margin-right: 8px;
    cursor: pointer;
    border-radius: 2px;
    display: flex;
    justify-content: center;
    align-items: center;

    .jackeroo-setting-check-icon{
      color: #fff;
      cursor: pointer;
    }
  }
}

.jackeroo-setting-switch{
  display: flex;
  justify-content: space-between;
  margin-top: 24px;

  .jackeroo-setting-switch-label{
    font-size: 14px;
    color: rgba(0,0,0,.65);
    font-family: -apple-system,BlinkMacSystemFont,Segoe UI,PingFang SC,Hiragino Sans GB,Microsoft YaHei,Helvetica Neue,Helvetica,Arial,sans-serif,Apple Color Emoji,Segoe UI Emoji,Segoe UI Symbol;
  }
}

</style>