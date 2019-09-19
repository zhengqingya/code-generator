package com.zhengqing.modules.shiro;

import com.zhengqing.config.Constants;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.Serializable;

/**
 *  <p> 自定义获取Token </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/23 15:55
 */
public class ShiroSessionManager extends DefaultWebSessionManager {
    /**
     * 定义常量
     */
//    private static final String REFERENCED_SESSION_ID_SOURCE = "Stateless request";


    /**
     * 重写构造器
     */
    public ShiroSessionManager() {
        super();
        this.setDeleteInvalidSessions(true);
    }

    /**
     * 重写方法实现从请求头获取Token便于接口统一
     * 每次请求进来,Shiro会去从请求头找REQUEST_HEADER这个key对应的Value(Token)
     */
    @Override
    public Serializable getSessionId(ServletRequest request, ServletResponse response) {
        String token = WebUtils.toHttp(request).getHeader(Constants.REQUEST_HEADER);

        // 如果请求头中存在token 则从请求头中获取token
        if ( StringUtils.isNotBlank( token ) ) {
//            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_SOURCE, REFERENCED_SESSION_ID_SOURCE);
//            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID, token);
//            request.setAttribute(ShiroHttpServletRequest.REFERENCED_SESSION_ID_IS_VALID, Boolean.TRUE);
            return token;
        } else {
            // 否则按默认规则从cookie取token
            return super.getSessionId(request, response);
        }
    }
}
