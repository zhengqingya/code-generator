package com.zhengqing.modules.shiro.filter;

import com.zhengqing.config.Constants;
import com.zhengqing.modules.shiro.utils.ShiroUtils;
import com.zhengqing.modules.system.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *  <p> 自定义token过滤 - 判断token是否过期失效等 </p>
 *
 * @author：  zhengqing <br/>
 * @date：  2019/8/28$ 14:51$ <br/>
 * @version：  <br/>
 */
@Slf4j
public class TokenCheckFilter extends UserFilter {

    /**
     * token过期、失效
     */
    private static final String TOKEN_EXPIRED_URL = "/api/auth/tokenExpired";
    /**
     * 被挤下线
     */
    private static final String DOWNLINE_URL = "/api/auth/downline";

    /**
     * 判断是否拥有权限 true:认证成功  false:认证失败
     * mappedValue 访问该url时需要的权限
     * subject.isPermitted 判断访问的用户是否拥有mappedValue权限
     */
    @Override
    public boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        // 根据请求头拿到token
        String token = WebUtils.toHttp(request).getHeader(Constants.REQUEST_HEADER);
        log.info("浏览器token：" + token );
        User userInfo = ShiroUtils.getUserInfo();
        String userToken = userInfo.getToken();
        // 账号在其他地方登录，被挤下线
//        if ( userInfo == null ){
//            return false;
//        }
        // 检查token是否过期
        if ( !token.equals(userToken) ){
            return false;
        }
        return true;
    }

    /**
     * 认证失败回调的方法: 如果登录实体为null就保存请求和跳转登录页面,否则就跳转无权限配置页面
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        User userInfo = ShiroUtils.getUserInfo();
        // 重定向错误提示处理 - 前后端分离情况下
        WebUtils.issueRedirect(request, response, TOKEN_EXPIRED_URL);
//        if ( userInfo == null ){
//            // 账号在其他地方登录，被挤下线
//            WebUtils.issueRedirect(request, response, DOWNLINE_URL);
//        }else{
//            // token过期 TODO 此处暂从缓存中获取数据 没从数据库拿
//            WebUtils.issueRedirect(request, response, TOKEN_EXPIRED_URL);
//        }
        return false;
    }

}
