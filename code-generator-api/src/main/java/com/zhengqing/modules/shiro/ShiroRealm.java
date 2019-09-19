package com.zhengqing.modules.shiro;

import com.zhengqing.modules.common.exception.MyException;
import com.zhengqing.modules.shiro.utils.ShiroUtils;
import com.zhengqing.modules.system.entity.Menu;
import com.zhengqing.modules.system.entity.Role;
import com.zhengqing.modules.system.entity.User;
import com.zhengqing.modules.system.mapper.MenuMapper;
import com.zhengqing.modules.system.mapper.RoleMapper;
import com.zhengqing.modules.system.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  <p> 自定义的Realm - Shiro权限匹配和账号密码匹配 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/23 15:20
 */
@Slf4j
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public String getName() {
        return "shiroRealm";
    }

    /**
     * 赋予角色和权限:用户进行权限验证时候Shiro会去缓存中找,如果查不到数据,会执行这个方法去查权限,并放入缓存中
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        // 获取用户
        User user = (User) principalCollection.getPrimaryPrincipal();
        Integer userId =user.getId();
        // 这里可以进行授权和处理
        Set<String> rolesSet = new HashSet<>();
        Set<String> permsSet = new HashSet<>();
        // 获取当前用户对应的权限(这里根据业务自行查询)
        List<Role> roleList = roleMapper.selectRoleByUserId( userId );
        for (Role role:roleList) {
            rolesSet.add( role.getCode() );
            List<Menu> menuList = menuMapper.selectMenuByRoleId( role.getId() );
            for (Menu menu :menuList) {
                permsSet.add( menu.getResources() );
            }
        }
        //将查到的权限和角色分别传入authorizationInfo中
        authorizationInfo.setStringPermissions(permsSet);
        authorizationInfo.setRoles(rolesSet);
        log.info("--------------- 赋予角色和权限成功！ ---------------");
        return authorizationInfo;
    }

    /**
     * 身份认证 - 之后走上面的 授权
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken tokenInfo = (UsernamePasswordToken)authenticationToken;
        // 获取用户输入的账号
        String username = tokenInfo.getUsername();
        // 获取用户输入的密码
        String password = String.valueOf( tokenInfo.getPassword() );

        // 通过username从数据库中查找 User对象，如果找到进行验证
        // 实际项目中,这里可以根据实际情况做缓存,如果不做,Shiro自己也是有时间间隔机制,2分钟内不会重复执行该方法
        User user = userMapper.selectUserByUsername(username);
        // 判断账号是否存在
        if (user == null) {
            //返回null -> shiro就会知道这是用户不存在的异常
            return null;
//            throw new UnknownAccountException();
        }
        // 验证密码 【注：这里不采用shiro自身密码验证 ， 采用的话会导致用户登录密码错误时，已登录的账号也会自动下线！  如果采用，移除下面的清除缓存到登录处 处理】
        if ( !password.equals( user.getPwd() ) ){
            throw new IncorrectCredentialsException("用户名或者密码错误");
        }

        // 判断账号是否被冻结
        if (user.getFlag()==null|| "0".equals(user.getFlag())){
            throw new LockedAccountException();
        }
        /**
         * 进行验证 -> 注：shiro会自动验证密码
         * 参数1：principal -> 放对象就可以在页面任意地方拿到该对象里面的值
         * 参数2：hashedCredentials -> 密码
         * 参数3：credentialsSalt -> 设置盐值
         * 参数4：realmName -> 自定义的Realm
         */
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
        // 验证成功开始踢人(清除缓存和Session)
        ShiroUtils.deleteCache(username,true);

        // 认证成功后更新token
        String token = ShiroUtils.getSession().getId().toString();
        user.setToken( token );
        userMapper.updateById(user);
        return authenticationInfo;
    }

}
