// 抽取公用的实例 - 操作成功与失败消息提醒内容等
export default {
  methods: {
    // 操作成功消息提醒内容
    submitOk(msg, cb) {
      this.$notify({
        title: '成功',
        message: msg,
        type: 'success',
        duration: 2000,
        onClose: function() {
          cb && cb()
        }
      })
    },
    // 操作失败消息提醒内容
    submitFail(msg) {
      this.$message({
        message: msg,
        type: 'error'
      })
    }

  }
}
