package com.zhengqing.modules.basic.api;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.PageFans;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.PageFansBean;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.javabeans.weibo.Company;
import com.qq.connect.oauth.Oauth;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 *  <p> QQ 第三方登录 授权模块 </p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/8/17 0017 19:41
 */
@PropertySource(value = {"classpath:qqconnectconfig.properties"})
@RestController
@RequestMapping("/api/auth")
@Slf4j
public class QQLoginController {

    /**
     * 使用@value注解获取properties中的文件
     */
    @Value("${redirect_URI}")
    private String QQ_redirect_URI;

    /**
     * 在servlet初始化的时候加载，并且只加载一次，和构造代码块的作用类似
     */
    @PostConstruct
    private void init(){
        log.info( "QQ三方登录回调接口：" + QQ_redirect_URI );
    }


    @RequestMapping("/loginByQQ")
    public void loginByQQ( HttpServletRequest request, HttpServletResponse response ) throws IOException {
        // 响应编码
        response.setContentType("text/html;charset=utf-8");
        try {
            // 将页面重定向到qq第三方的登录页面
            response.sendRedirect( new Oauth().getAuthorizeURL( request ) );
            // 注意：一定要加上return 否则会报错！
            return;
        } catch ( QQConnectException e ) {
            e.printStackTrace();
        }
    }

    /**
     * 获取登录者的基础信息
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/afterlogin.do")
    public void QQAfterlogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.info(" QQ登录成功之后回调函数：======================================================= ");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();

        Enumeration<String> parameterNames = request.getParameterNames();
        while(parameterNames.hasMoreElements()){
            //code
            String parameterName = parameterNames.nextElement();
            //state
            System.out.println(parameterName+":"+request.getParameter(parameterName));
        }
        System.out.println("qq_connect_state:"+request.getSession().getAttribute("qq_connect_state"));

        try {
            // 获取AccessToken(AccessToken用于获取OppendID)
            AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);

            System.out.println("accessTokenObj:"+accessTokenObj);
            // 用于接收AccessToken
            String accessToken   = null,
                    openID        = null;
            // AccessToken有效时长
            long tokenExpireIn = 0L;

            if (accessTokenObj.getAccessToken().equals("")) {
                //                我们的网站被CSRF攻击了或者用户取消了授权
                //                做一些数据统计工作
                log.info("没有获取到响应参数");
            } else {
                // 获取AccessToken
                accessToken = accessTokenObj.getAccessToken();
                tokenExpireIn = accessTokenObj.getExpireIn();

                request.getSession().setAttribute("demo_access_token", accessToken);
                request.getSession().setAttribute("demo_token_expirein", String.valueOf(tokenExpireIn));

                // 利用获取到的accessToken 去获取当前用的openid -------- start
                OpenID openIDObj =  new OpenID(accessToken);
                // 通过对象获取[OpendId]（OpendID用于获取QQ登录用户的信息）
                openID = openIDObj.getUserOpenID();

                out.println("欢迎你，代号为 " + openID + " 的用户!");
                request.getSession().setAttribute("demo_openid", openID);
                out.println("<a href=" + "/shuoshuoDemo.html" +  " target=\"_blank\">去看看发表说说的demo吧</a>");
                // 利用获取到的accessToken 去获取当前用户的openid --------- end

                out.println("<p> start -----------------------------------利用获取到的accessToken,openid 去获取用户在Qzone的昵称等信息 ---------------------------- start </p>");
                // 通过OpenID获取QQ用户登录信息对象(Oppen_ID代表着QQ用户的唯一标识)
                UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
                // 获取用户信息对象(只获取nickename与Gender)
                UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                out.println("<br/>");
                if (userInfoBean.getRet() == 0) {
                    out.println(userInfoBean.getNickname() + "<br/>");
                    out.println(userInfoBean.getGender() + "<br/>");
                    out.println("黄钻等级： " + userInfoBean.getLevel() + "<br/>");
                    out.println("会员 : " + userInfoBean.isVip() + "<br/>");
                    out.println("黄钻会员： " + userInfoBean.isYellowYearVip() + "<br/>");
                    out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL30() + "/><br/>");
                    out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL50() + "/><br/>");
                    out.println("<image src=" + userInfoBean.getAvatar().getAvatarURL100() + "/><br/>");
                } else {
                    out.println("很抱歉，我们没能正确获取到您的信息，原因是： " + userInfoBean.getMsg());
                }
                out.println("<p> end -----------------------------------利用获取到的accessToken,openid 去获取用户在Qzone的昵称等信息 ---------------------------- end </p>");


                out.println("<p> start ----------------------------------- 验证当前用户是否为认证空间的粉丝------------------------------------------------ start <p>");
                PageFans pageFansObj = new PageFans(accessToken, openID);
                PageFansBean pageFansBean = pageFansObj.checkPageFans("97700000");
                if (pageFansBean.getRet() == 0) {
                    out.println("<p>验证您" + (pageFansBean.isFans() ? "是" : "不是")  + "QQ空间97700000官方认证空间的粉丝</p>");
                } else {
                    out.println("很抱歉，我们没能正确获取到您的信息，原因是： " + pageFansBean.getMsg());
                }
                out.println("<p> end ----------------------------------- 验证当前用户是否为认证空间的粉丝------------------------------------------------ end <p>");


                out.println("<p> start -----------------------------------利用获取到的accessToken,openid 去获取用户在微博的昵称等信息 ---------------------------- start </p>");
                com.qq.connect.api.weibo.UserInfo weiboUserInfo = new com.qq.connect.api.weibo.UserInfo(accessToken, openID);
                com.qq.connect.javabeans.weibo.UserInfoBean weiboUserInfoBean = weiboUserInfo.getUserInfo();
                if (weiboUserInfoBean.getRet() == 0) {
                    //获取用户的微博头像----------------------start
                    out.println("<image src=" + weiboUserInfoBean.getAvatar().getAvatarURL30() + "/><br/>");
                    out.println("<image src=" + weiboUserInfoBean.getAvatar().getAvatarURL50() + "/><br/>");
                    out.println("<image src=" + weiboUserInfoBean.getAvatar().getAvatarURL100() + "/><br/>");
                    //获取用户的微博头像 ---------------------end

                    //获取用户的生日信息 --------------------start
                    out.println("<p>尊敬的用户，你的生日是： " + weiboUserInfoBean.getBirthday().getYear()
                            +  "年" + weiboUserInfoBean.getBirthday().getMonth() + "月" +
                            weiboUserInfoBean.getBirthday().getDay() + "日");
                    //获取用户的生日信息 --------------------end

                    StringBuffer sb = new StringBuffer();
                    sb.append("<p>所在地:" + weiboUserInfoBean.getCountryCode() + "-" + weiboUserInfoBean.getProvinceCode() + "-" + weiboUserInfoBean.getCityCode()
                            + weiboUserInfoBean.getLocation());

                    //获取用户的公司信息---------------------------start
                    ArrayList<Company> companies = weiboUserInfoBean.getCompanies();
                    if (companies.size() > 0) {
                        //有公司信息
                        for (int i=0, j=companies.size(); i<j; i++) {
                            sb.append("<p>曾服役过的公司：公司ID-" + companies.get(i).getID() + " 名称-" +
                                    companies.get(i).getCompanyName() + " 部门名称-" + companies.get(i).getDepartmentName() + " 开始工作年-" +
                                    companies.get(i).getBeginYear() + " 结束工作年-" + companies.get(i).getEndYear());
                        }
                    } else {
                        //没有公司信息
                    }
                    //获取用户的公司信息---------------------------end
                    out.println(sb.toString());
                } else {
                    out.println("很抱歉，我们没能正确获取到您的信息，原因是： " + weiboUserInfoBean.getMsg());
                }
                out.println("<p> end -----------------------------------利用获取到的accessToken,openid 去获取用户在微博的昵称等信息 ---------------------------- end </p>");
            }
        } catch (QQConnectException e) {
        }
    }

}
