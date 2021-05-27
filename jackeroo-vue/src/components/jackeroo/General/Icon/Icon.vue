<template>
  <span>
    <icon-font v-if="type.indexOf('icon-') === 0" :type="type" v-bind="$attrs"></icon-font>
    <a-icon v-else :type="type" v-bind="$attrs"></a-icon>
  </span>
</template>

<script>
export default {
  name: 'JIcon',
  props: {
    type: {
      type: String,
      required: true
    }
  }
}
</script>