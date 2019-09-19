<template>
  <div class="filter-wraper">
    <!-- <el-row :gutter="gutter"> -->
      <slot />
    <!-- </el-row> -->
  </div>
</template>
<script>
export default {
  name: 'FilterWraper',
  props: {
    gutter: {
      type: Number,
      default: 5
    }
  }
}
</script>
<style lang="scss" scoped>
.filter-wraper {
  margin-bottom: 20px;
  background: #fff;
}
</style>
