<template>
  <div>
    <a-page-header :breadcrumb="{props: {routes: breadcrumb}}" :ghost="false" class="jackeroo-page-header"></a-page-header>
    <div class="jackeroo-page-content">
      <router-view />
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      
    }
  },
  created(){
    console.log('breadcrumb', this.$route.matched)
  },
  computed: {
    breadcrumb(){
      return this.$route.matched.concat().map((route) => {
        return {
          path: route.path,
          breadcrumbName: route.meta.title
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.jackeroo-page-header{
  background-color: #fff;
  padding: 12px 24px;
}

.jackeroo-page-content{
  padding: 20px;
}
</style>