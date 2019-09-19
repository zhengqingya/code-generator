<template>
  <div class="wraper">
    <slot />
  </div>
</template>
<script>
export default {
  name: 'Wraper',
  data() {
    return {
    }
  },
  methods: {
  }
}
</script>
<style lang="scss" scoped>
.wraper{
  padding: 15px;
  background:#fff;
  border-radius:2px;
}
</style>
