<template>
  <span>
    <a-tag v-if="type == 'default' || !color[type]">
      {{text}}
    </a-tag>
    <a-tag v-else :color="color[type]">
      {{text}}
    </a-tag>
  </span>
</template>

<script>
export default {
  name: 'JTag',
  props: {
    type: {
      type: String,
      default: 'default'
    },
    text: {
      type: String,
      required: true
    }
  },
  data(){
    return {
      color: {
        success: 'green',
        info: 'blue',
        warning: 'orange',
        error: 'red'
      }
    }
  }
}
</script>