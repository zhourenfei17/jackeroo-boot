<template>
  <div class="logo">
    <img src="~@/assets/logo.png"/>
    <span v-show="showTitle">{{title}}</span>
  </div>
</template>

<script>
export default {
  props: {
    title: {
      type: String,
      default: ''
    },
    collapsed: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      showTitle: true
    }
  },
  watch: {
    collapsed: {
      immediate: true,
      handler(v1){
        if(v1){
          setTimeout(() => {
            this.showTitle = true
          }, 200);
        }else{
          this.showTitle = false
        }
      }
    }
  }
}
</script>


<style lang="less" scoped>
@height: 59px;

.logo {
  height: @height !important;
  line-height: @height !important;
  box-shadow: none !important;
  transition: background 300ms;
  padding: 0 10px;
  display: flex;
  align-items: center;

  img{
    height: 32px;
    width: 32px;
    margin-left: 14px;
  }
  span{
    margin-left: 10px;
    color: #fff;
    font-size: 20px;
    font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
    font-weight: 600;
  }

  a {
    color: white;
    &:hover {
      color: rgba(255, 255, 255, 0.8);
    }
  }
}
</style>