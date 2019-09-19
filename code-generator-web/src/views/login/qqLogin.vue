<template>
  <div class="login-container">
    <!--    <span>QQ第三方登录 - 回调页面</span>-->
  </div>
</template>

<script>

    export default {
        name: 'qqLogin',
        data() {
            return {
                redirect: undefined
            }
        },
        watch: {
            $route: {
                handler: function (route) {
                    this.redirect = route.query && route.query.redirect
                },
                immediate: true
            }
        },
        created() {
            this.qqLogin()
        },
        methods: {
            // QQ第三方登录
            qqLogin() {
                var that = this;// 先将vue这个对象保存在_self对象中
                //检查是否登录
                if (QC.Login.check()) {
                    //该处的openId，accessToken就是后台需要的参数了，后台通过这些参数拿取临时登录凭证，然后就是自己的逻辑了
                    QC.Login.getMe(function (openId, accessToken) {
                        // 传参给后台进行登录验证
                        // that.$store.dispatch('LoginByUsername', {
                        //   username: '',
                        //   openId: openId,
                        //   accessToken: accessToken
                        // }).then(() => {
                        //   that.$router.push({ path: this.redirect || '/' })
                        // })
                        // 跳转路由 携带数据
                        that.$router.push({
                            path: `/login`,
                            query: {           //路由传参时push和query搭配使用 ，作用：传递参数
                                openId: openId,
                                accessToken: accessToken
                            }
                        });
                    });
                    console.log('已登录!')
                } else {
                    console.log('未登录!')
                }
            }
        }
    }
</script>
<style rel="stylesheet/scss" lang="scss"></style>
