package com.zhengqing.modules.shiro.config;

import com.zhengqing.modules.shiro.ShiroRealm;
import com.zhengqing.modules.shiro.ShiroSessionIdGenerator;
import com.zhengqing.modules.shiro.ShiroSessionManager;
import com.zhengqing.modules.shiro.filter.MyPermissionsAuthorizationFilter;
import com.zhengqing.modules.shiro.filter.MyRolesAuthorizationFilter;
import com.zhengqing.modules.shiro.filter.TokenCheckFilter;
import com.zhengqing.modules.shiro.service.impl.ShiroServiceImpl;
import com.zhengqing.modules.shiro.utils.SHA256Util;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 *  <p> Shiro配置类 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/23 15:52
 */
@Configuration
public class ShiroConfig {

    private final String CACHE_KEY = "shiro:cache:";
    private final String SESSION_KEY = "shiro:session:";
    private final int EXPIRE = 1800;

    /**
     *  Redis配置
     */
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private int port;
    @Value("${spring.redis.timeout}")
    private int timeout;
//    @Value("${spring.redis.password}")
//    private String password;

    /**
     * 开启Shiro-aop注解支持：使用代理方式所以需要开启代码支持
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * Shiro基础配置
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactory(SecurityManager securityManager, ShiroServiceImpl shiroConfig){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 自定义过滤器
        Map<String, Filter> filtersMap = new LinkedHashMap<>();
        // 定义过滤器名称 【注：map里面key值对于的value要为authc才能使用自定义的过滤器】
        filtersMap.put( "zqPerms", new MyPermissionsAuthorizationFilter() );
        filtersMap.put( "zqRoles", new MyRolesAuthorizationFilter() );
        filtersMap.put( "token", new TokenCheckFilter() );
        shiroFilterFactoryBean.setFilters(filtersMap);

        // 登录的路径: 如果你没有登录则会跳到这个页面中 - 如果没有设置值则会默认跳转到工程根目录下的"/login.jsp"页面 或 "/login" 映射
        shiroFilterFactoryBean.setLoginUrl("/api/auth/unLogin");
        // 登录成功后跳转的主页面 （这里没用，前端vue控制了跳转）
//        shiroFilterFactoryBean.setSuccessUrl("/index");
        // 设置没有权限时跳转的url
        shiroFilterFactoryBean.setUnauthorizedUrl("/api/auth/unauth");

        shiroFilterFactoryBean.setFilterChainDefinitionMap( shiroConfig.loadFilterChainDefinitionMap() );
        return shiroFilterFactoryBean;
    }

    /**
     * 安全管理器
     */
    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 自定义session管理
        securityManager.setSessionManager(sessionManager());
        // 自定义Cache实现缓存管理
        securityManager.setCacheManager(cacheManager());
        // 自定义Realm验证
        securityManager.setRealm(shiroRealm());
        return securityManager;
    }

    /**
     * 身份验证器
     */
    @Bean
    public ShiroRealm shiroRealm() {
        ShiroRealm shiroRealm = new ShiroRealm();
        shiroRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return shiroRealm;
    }

    /**
     *  自定义Realm的加密规则 -> 凭证匹配器：将密码校验交给Shiro的SimpleAuthenticationInfo进行处理,在这里做匹配配置
     */
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        // 散列算法:这里使用SHA256算法;
        shaCredentialsMatcher.setHashAlgorithmName(SHA256Util.HASH_ALGORITHM_NAME);
        // 散列的次数，比如散列两次，相当于 md5(md5(""));
        shaCredentialsMatcher.setHashIterations(SHA256Util.HASH_ITERATIONS);
        return shaCredentialsMatcher;
    }

    /**
     * Redis集群使用RedisClusterManager，单个Redis使用RedisManager
     * @param redisProperties
     */
//    @Bean
//    public RedisClusterManager redisManager(RedisProperties redisProperties) {
//        RedisClusterManager redisManager = new RedisClusterManager();
//        redisManager.setHost(redisProperties.getHost());
//        redisManager.setPassword(redisProperties.getPassword());
//        return redisManager;
//    }

    /**
     * 配置Redis管理器：使用的是shiro-redis开源插件
     */
    @Bean
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host);
        redisManager.setPort(port);
        redisManager.setTimeout(timeout);
//        redisManager.setPassword(password);
        return redisManager;
    }

    /**
     * 配置Cache管理器：用于往Redis存储权限和角色标识  (使用的是shiro-redis开源插件)
     */
    @Bean
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        redisCacheManager.setKeyPrefix(CACHE_KEY);
        // 配置缓存的话要求放在session里面的实体类必须有个id标识 注：这里id为用户表中的主键，否-> 报：User must has getter for field: xx
        redisCacheManager.setPrincipalIdFieldName("id");
        return redisCacheManager;
    }

    /**
     * SessionID生成器
     */
    @Bean
    public ShiroSessionIdGenerator sessionIdGenerator(){
        return new ShiroSessionIdGenerator();
    }

    /**
     * 配置RedisSessionDAO (使用的是shiro-redis开源插件)
     */
    @Bean
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setSessionIdGenerator(sessionIdGenerator());
        redisSessionDAO.setKeyPrefix(SESSION_KEY);
        redisSessionDAO.setExpire(EXPIRE);
        return redisSessionDAO;
    }

    /**
     * 配置Session管理器
     */
    @Bean
    public SessionManager sessionManager() {
        ShiroSessionManager shiroSessionManager = new ShiroSessionManager();
        shiroSessionManager.setSessionDAO(redisSessionDAO());
        return shiroSessionManager;
    }

}
