<template>
  <div class="social-signup-container">
    <div class="sign-btn" @click="wechatHandleClick('wechat')">
      <span class="wx-svg-container"><svg-icon icon-class="wechat" class="icon"/></span> 微信
    </div>
    <div class="sign-btn" @click="tencentHandleClick('tencent')">
      <span class="qq-svg-container"><svg-icon icon-class="qq" class="icon" id="qqLoginBtn"/></span> QQ
    </div>
  </div>
</template>

<script>
    // import openWindow from '@/utils/openWindow'

    export default {
        name: 'SocialSignin',
        methods: {
            wechatHandleClick(thirdpart) {
                alert('ok')
                // this.$store.commit('SET_AUTH_TYPE', thirdpart)
                // const appid = 'xxxxx'
                // const redirect_uri = encodeURIComponent('xxx/redirect?redirect=' + window.location.origin + '/auth-redirect')
                // const url = 'https://open.weixin.qq.com/connect/qrconnect?appid=' + appid + '&redirect_uri=' + redirect_uri + '&response_type=code&scope=snsapi_login#wechat_redirect'
                // openWindow(url, thirdpart, 540, 540)
            },
            // QQ 第三方登录
            tencentHandleClick(thirdpart) {
                // 直接弹出授权页面，授权过后跳转到回调页面进行登录处理
                QC.Login.showPopup({
                    appId:"xxx",
                    redirectURI:"xxx"  //登录成功后会自动跳往该地址
                });

                // 法二
                // var _self = this;// 先将vue这个对象保存在_self对象中
                // _self.$store.commit('SET_AUTH_TYPE', thirdpart)
                // const client_id = 'xxx';
                // const redirect_uri = "xxx";
                // const url = 'https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=' + client_id + '&redirect_uri=' + redirect_uri;
                // 打开QQ授权登录界面，授权成功后会重定向
                // openWindow(url, thirdpart, 540, 540);
            }
        }
    }
</script>
<!-- 三方登录样式  -->
<style rel="stylesheet/scss" lang="scss" scoped>
  .social-signup-container {
    margin: 20px 0;

  .sign-btn {
    display: inline-block;
    cursor: pointer;
  }

  .icon {
    color: #fff;
    font-size: 24px;
    margin-top: 8px;
  }

  .wx-svg-container,
  .qq-svg-container {
    display: inline-block;
    width: 40px;
    height: 40px;
    line-height: 40px;
    text-align: center;
    padding-top: 1px;
    border-radius: 4px;
    margin-bottom: 20px;
    margin-right: 5px;
  }

  .wx-svg-container {
    background-color: #24da70;
  }

  .qq-svg-container {
    background-color: #6BA2D6;
    margin-left: 50px;
  }

  }
</style>
