package com.zhengqing.modules.basic.api;

import com.alibaba.fastjson.JSONObject;
import com.qq.connect.QQConnectException;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.zhengqing.config.Constants;
import com.zhengqing.modules.common.api.BaseController;
import com.zhengqing.modules.common.dto.output.ApiResult;
import com.zhengqing.modules.shiro.utils.SHA256Util;
import com.zhengqing.modules.shiro.utils.ShiroUtils;
import com.zhengqing.modules.system.dto.input.UserQueryPara;
import com.zhengqing.modules.system.entity.User;
import com.zhengqing.modules.system.entity.UserRole;
import com.zhengqing.modules.system.mapper.UserMapper;
import com.zhengqing.modules.system.mapper.UserRoleMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *  <p> 授权模块 </p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/8/17 0017 19:41
 */
@RestController
@RequestMapping("/api/auth")
@Api(description = "系统登录接口")
public class LoginController extends BaseController{

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserRoleMapper userRoleMapper;

    @PostMapping("/login")
    @ApiOperation(value = "登录系统", httpMethod = "POST", response = ApiResult.class, notes = "登录系统")
    public ApiResult login(@RequestBody UserQueryPara para) throws QQConnectException {
        String openID = para.getOpenId();
        String accessToken = para.getAccessToken();
        // QQ 第三方登录
        if( StringUtils.isNotBlank( openID ) && StringUtils.isNotBlank( accessToken ) ){
            UserQueryPara userQueryPara = qqLogin(openID, accessToken);
            if ( userQueryPara == null ){
                return ApiResult.fail("账号异常,请联系管理员!");
            }
            para = userQueryPara;
        }
        // 账号登录
        if(StringUtils.isBlank( para.getUsername() ) || StringUtils.isBlank( para.getPassword() )){
            return ApiResult.fail( "参数不全" );
        }
        // 拿到当前用户(可能还是游客，没有登录)
        Subject currentUser = SecurityUtils.getSubject();
        // 如果这个用户没有登录,进行登录功能
        if( !currentUser.isAuthenticated() ) {
            try{
                // 验证身份和登陆
                UsernamePasswordToken token = new UsernamePasswordToken(para.getUsername(), para.getPassword());
                // String token = MD5Utils.encrypt( String.valueOf( System.currentTimeMillis() ) );
                currentUser.login(token);
            }catch (UnknownAccountException e) {
                return ApiResult.fail("账号不存在!");
            } catch (IncorrectCredentialsException e) {
                return ApiResult.fail("用户名或者密码错误!");
            } catch (LockedAccountException e) {
                return ApiResult.fail("登录失败，该用户已被冻结!");
            } catch (AuthenticationException e) {
                return ApiResult.fail("未知错误!");
            }
        }
        JSONObject json = new JSONObject();
        json.put( "token", ShiroUtils.getSession().getId().toString() );
        return ApiResult.ok("登录成功",json);
    }

    /**
     * QQ 授权登录
     * @param openID
     * @param accessToken
     * @return
     * @throws QQConnectException
     */
    public UserQueryPara qqLogin( String openID, String accessToken ) throws QQConnectException {
        UserQueryPara result = new UserQueryPara();
        // 通过OpenID获取QQ用户登录信息对象(Oppen_ID代表着QQ用户的唯一标识)
        UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
        // 获取用户信息对象(只获取nickename、Gender、头像)
        UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
        if (userInfoBean.getRet() == 0) {
            String avatar = userInfoBean.getAvatar().getAvatarURL100();
            // 判断qq授权登录的用户是否拥有账号，如果没有则自动注册一个
            User userInfo = userMapper.getUserInfoByQQ( openID );
            if ( userInfo == null) {
                User user = new User();
                // 默认将QQ注册的用户账号设置为openID TODO 后期改为QQ号
                user.setUsername( openID );
                user.setQqOppenId( openID );
                user.setPwd( "123456" );
                user.setPassword( SHA256Util.sha256( user.getPwd(), Constants.SALT ) );
                user.setNickName( StringUtils.isBlank( userInfoBean.getNickname() ) ? "无名氏" : userInfoBean.getNickname() );
                user.setAvatar( avatar );
                user.setSex( StringUtils.isBlank( userInfoBean.getGender() ) ? "0" : ( "男".equals( userInfoBean.getGender() ) ? "0" : "1") );
                user.setSalt( Constants.SALT );
                user.setFlag("1");
                userMapper.insert( user );

                // 分配权限
                UserRole userRole = new UserRole();
                userRole.setRoleId( 2 );
                userRole.setUserId( user.getId() );
                userRoleMapper.insert( userRole );

                result.setUsername( user.getUsername() );
                result.setPassword( user.getPwd() );
            } else {
                userInfo.setAvatar( avatar );
                userMapper.updateById( userInfo );

                result.setUsername( userInfo.getUsername() );
                result.setPassword( userInfo.getPwd() );
            }
        }
        return result;
    }

    @PostMapping("/logout")
    @ApiOperation(value = "退出系统", httpMethod = "POST", response = ApiResult.class, notes = "退出系统")
    public ApiResult logout(HttpServletRequest request, HttpServletResponse response) {
        // 更新token
//        User user = ShiroUtils.getUserInfo();
//        User userNew = userMapper.selectUserByUsername( user.getUsername() );
//        userNew.setToken( MD5Utils.encrypt( String.valueOf( System.currentTimeMillis() ) ) );
//        userMapper.updateById( userNew );
        // 用户登出
        ShiroUtils.logout();
        return ApiResult.ok("退出系统成功");
    }

    /**
     * 未登录
     */
    @RequestMapping("/unLogin")
    @ApiOperation(value = "未登录", response = ApiResult.class, notes = "未登录")
    public ApiResult unLogin(){
        return ApiResult.fail(401, "未登录");
    }

    /**
     * 未授权
     */
    @RequestMapping("/unauth")
    @ApiOperation(value = "未授权", response = ApiResult.class, notes = "未授权")
    public ApiResult unauth(){
        return ApiResult.ok(500,"未授权");
    }

    /**
     * token过期
     */
    @RequestMapping("/tokenExpired")
    @ApiOperation(value = "token过期", response = ApiResult.class, notes = "token过期")
    public ApiResult tokenExpired(){
        return ApiResult.ok(401,"token过期，请重新登录");
    }

    /**
     * 被挤下线
     */
    @RequestMapping("/downline")
    @ApiOperation(value = "被挤下线", response = ApiResult.class, notes = "被挤下线")
    public ApiResult downline(){
        return ApiResult.ok(401,"您的账号已在其他地方登录，被挤下线，请重新登录！");
    }











//    @GetMapping("/unauth2")
//    public void index(HttpServletRequest request, HttpServletResponse response) throws IOException {
//        StringBuilder builder = new StringBuilder();
//        builder.append( "<html>" )
//                .append( "<body>" )
//                .append( "<h2 style=\"margin: 50px 0 0 80px;\">" )
//                .append( "您好，您暂时没有权限访问此页面！" )
//                .append( "</h2>" )
//                .append( "</body>" )
//                .append( "</html>" );
//        response.setHeader( "content-type", "text/html;charset=UTF-8" );
//        response.getWriter().write( builder.toString() );
//    }

}
