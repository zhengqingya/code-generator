<template>
  <el-popover v-model="visible" placement="top" width="200">
    <p>确定删除此条数据吗？</p>
    <div style="text-align: right; margin: 0">
      <el-button size="mini" type="text" @click="visible = false">取消</el-button>
      <el-button type="primary" size="mini" @click="handleClick">确定</el-button>
    </div>
<!--    <el-button slot="reference" style="margin-left:10px;color:#f56c6c" type="text">删除</el-button>-->
<!--    <el-button size="mini" type="danger" slot="reference" icon="el-icon-delete" plain>删除</el-button>-->
    <el-button size="mini" type="danger" slot="reference" icon="el-icon-delete" style="margin-left: 10px" plain>{{ $t('table.delete') }}</el-button>
  </el-popover>
</template>
<script>
export default {
  name: 'DelBtn',
  data() {
    return {
      visible: false
    }
  },
  methods: {
    handleClick() {
      this.$emit('ok');
      this.visible = false
    }
  }
}
</script>
<style lang="scss" scoped></style>
