package com.zhengqing.modules.shiro.service.impl;

import com.zhengqing.modules.common.exception.MyException;
import com.zhengqing.modules.shiro.service.ShiroService;
import com.zhengqing.modules.shiro.utils.ShiroUtils;
import com.zhengqing.modules.system.entity.Menu;
import com.zhengqing.modules.system.entity.Role;
import com.zhengqing.modules.system.entity.User;
import com.zhengqing.modules.system.mapper.MenuMapper;
import com.zhengqing.modules.system.mapper.RoleMapper;
import com.zhengqing.modules.system.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 *  <p> shiro权限处理实现类 </p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/9/7 0007 13:53
 */
@Slf4j
@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public Map<String, String> loadFilterChainDefinitionMap() {
        // 权限控制map
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        // 配置过滤:不会被拦截的链接 -> 放行 start ----------------------------------------------------------
        // 放行Swagger2页面，需要放行这些
        filterChainDefinitionMap.put("/swagger-ui.html","anon");
        filterChainDefinitionMap.put("/swagger/**","anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**","anon");
        filterChainDefinitionMap.put("/v2/**","anon");
        filterChainDefinitionMap.put("/static/**", "anon");

        // 登陆
        filterChainDefinitionMap.put("/api/auth/login/**", "anon");
        // 三方登录
        filterChainDefinitionMap.put("/api/auth/loginByQQ", "anon");
        filterChainDefinitionMap.put("/api/auth/afterlogin.do", "anon");
        // 退出
        filterChainDefinitionMap.put("/api/auth/logout", "anon");
        // 放行未授权接口，重定向使用
        filterChainDefinitionMap.put("/api/auth/unauth", "anon");
        // token过期接口
        filterChainDefinitionMap.put("/api/auth/tokenExpired", "anon");
        // 被挤下线
        filterChainDefinitionMap.put("/api/auth/downline", "anon");
        // 放行 end ----------------------------------------------------------

        // 从数据库或缓存中查取出来的url与resources对应则不会被拦截 放行
        List<Menu> permissionList = menuMapper.selectList( null );
        if ( !CollectionUtils.isEmpty( permissionList ) ) {
            permissionList.forEach( e -> {
                if ( StringUtils.isNotBlank( e.getUrl() ) ) {
                    // 根据url查询相关联的角色名,拼接自定义的角色权限
                    List<Role> roleList = roleMapper.selectRoleByMenuId( e.getId() );
                    StringJoiner zqRoles = new StringJoiner(",", "zqRoles[", "]");
                    if ( !CollectionUtils.isEmpty( roleList ) ){
                        roleList.forEach( f -> {
                            zqRoles.add( f.getCode() );
                        });
                    }

                    // 注意过滤器配置顺序不能颠倒
                    // ① 认证登录
                    // ② 认证自定义的token过滤器 - 判断token是否有效
                    // ③ 角色权限 zqRoles：自定义的只需要满足其中一个角色即可访问  ;  roles[admin,guest] : 默认需要每个参数满足才算通过，相当于hasAllRoles()方法
                    // ④ zqPerms:认证自定义的url过滤器拦截权限  【注：多个过滤器用 , 分割】
//                    filterChainDefinitionMap.put( "/api" + e.getUrl(),"authc,token,roles[admin,guest],zqPerms[" + e.getResources() + "]" );
                    filterChainDefinitionMap.put( "/api" + e.getUrl(),"authc,token,"+ zqRoles.toString() +",zqPerms[" + e.getResources() + "]" );
//                        filterChainDefinitionMap.put("/api/system/user/listPage", "authc,token,zqPerms[user1]"); // 写死的一种用法
                }
            });
        }
        // ⑤ 认证登录  【注：map不能存放相同key】
        filterChainDefinitionMap.put("/**", "authc");
        return filterChainDefinitionMap;
    }

    @Override
    public void updatePermission(ShiroFilterFactoryBean shiroFilterFactoryBean, Integer roleId, Boolean isRemoveSession) {
        synchronized (this) {
            AbstractShiroFilter shiroFilter;
            try {
                shiroFilter = (AbstractShiroFilter) shiroFilterFactoryBean.getObject();
            } catch (Exception e) {
                throw new MyException("get ShiroFilter from shiroFilterFactoryBean error!");
            }
            PathMatchingFilterChainResolver filterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
            DefaultFilterChainManager manager = (DefaultFilterChainManager) filterChainResolver.getFilterChainManager();

            // 清空拦截管理器中的存储
            manager.getFilterChains().clear();
            // 清空拦截工厂中的存储,如果不清空这里,还会把之前的带进去
            //            ps:如果仅仅是更新的话,可以根据这里的 map 遍历数据修改,重新整理好权限再一起添加
            shiroFilterFactoryBean.getFilterChainDefinitionMap().clear();
            // 动态查询数据库中所有权限
            shiroFilterFactoryBean.setFilterChainDefinitionMap(loadFilterChainDefinitionMap());
            // 重新构建生成拦截
            Map<String, String> chains = shiroFilterFactoryBean.getFilterChainDefinitionMap();
            for (Map.Entry<String, String> entry : chains.entrySet()) {
                manager.createChain(entry.getKey(), entry.getValue());
            }
            log.info("--------------- 动态生成url权限成功！ ---------------");

            // 动态更新该角色相关联的用户shiro权限
            if(roleId != null){
                updatePermissionByRoleId(roleId,isRemoveSession);
            }
        }
    }

    @Override
    public void updatePermissionByRoleId(Integer roleId, Boolean isRemoveSession) {
        // 查询当前角色的用户shiro缓存信息 -> 实现动态权限
        List<User> userList = userMapper.selectUserByRoleId(roleId);
        // 删除当前角色关联的用户缓存信息,用户再次访问接口时会重新授权 ; isRemoveSession为true时删除Session -> 即强制用户退出
        if ( !CollectionUtils.isEmpty( userList ) ) {
            for (User user : userList) {
                ShiroUtils.deleteCache(user.getUsername(), isRemoveSession);
            }
        }
        log.info("--------------- 动态修改用户权限成功！ ---------------");
    }

}
