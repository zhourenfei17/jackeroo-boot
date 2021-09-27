<template>
  <a-tooltip :title="value">
    <div class="jackeroo-text-nowrap">
      <j-link>{{value}}</j-link>
    </div>
  </a-tooltip>
</template>

<script>
import JLink from '../../General/Link';

export default {
  props: ['value'],
  components: {
    JLink
  }
}
</script>

<style lang="less" scoped>
  .jackeroo-text-nowrap{
    overflow: hidden;
    text-overflow:ellipsis;
    white-space: nowrap;
  }
</style>