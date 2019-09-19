package com.zhengqing.modules.shiro.filter;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 *  <p> 自定义角色过滤权限 zqRoles - 满足其中一个即可访问 - zqRoles[admin,guest] </p>
 *
 * @description: shiro原生的角色过滤器RolesAuthorizationFilter 默认是必须同时满足roles[admin,guest]才有权限
 * @author: zhengqing
 * @date: 2019/9/7 0007 17:01
 */
public class MyRolesAuthorizationFilter extends AuthorizationFilter {

    @Override
    protected boolean isAccessAllowed(ServletRequest req, ServletResponse resp, Object mappedValue) throws Exception {
        Subject subject = getSubject(req, resp);
        String[] rolesArray = (String[]) mappedValue;
        // 没有角色限制，有权限访问
        if (rolesArray == null || rolesArray.length == 0) {
            return true;
        }
        for (int i = 0; i < rolesArray.length; i++) {
            //若当前用户是rolesArray中的任何一个，则有权限访问
            if (subject.hasRole(rolesArray[i])) {
                return true;
            }
        }
        return false;
    }

}
