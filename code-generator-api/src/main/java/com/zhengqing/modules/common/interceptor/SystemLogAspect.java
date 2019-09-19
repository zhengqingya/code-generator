package com.zhengqing.modules.common.interceptor;

import com.zhengqing.config.Constants;
import com.zhengqing.modules.common.dto.output.ApiResult;
import com.zhengqing.modules.shiro.utils.ShiroUtils;
import com.zhengqing.modules.system.entity.SysLog;
import com.zhengqing.modules.system.entity.User;
import com.zhengqing.modules.system.mapper.UserMapper;
import com.zhengqing.utils.DateTimeUtils;
import com.zhengqing.utils.IpUtils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 *  <p> 系统日志处理 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/9/18 15:25
 */
@Aspect
@Configuration
@Slf4j
public class SystemLogAspect {

    @Autowired
    UserMapper userMapper;

    @Pointcut("execution(* com.zhengqing.modules.*.api.*Controller.*(..)))")
    public void systemLog() {}

    @Around(value = "systemLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        HttpServletResponse response = attributes.getResponse();

        // 拿到ip地址、请求路径、token
        String url = request.getRequestURL().toString();
        String ip = IpUtils.getIpAdrress(request);
        String token = request.getHeader(Constants.REQUEST_HEADER);

        // 从切面织入点处通过反射机制获取织入点处的方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        // 获取切入点所在的方法
        Method method = signature.getMethod();
        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        String methodName = "";
        if(apiOperation != null){
            methodName = apiOperation.value();
        }

        // 记录执行时间
        long startTime = System.currentTimeMillis();
        ApiResult result = (ApiResult)joinPoint.proceed(joinPoint.getArgs());
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        log.info("**********   Url: {}, Start: {}, End: {}, Total: {}ms, Code: {}   **********", url, DateTimeUtils.dateFormat( new Date( startTime ), "yyyy-MM-dd HH:mm:ss:SSS" ), DateTimeUtils.dateFormat( new Date( endTime ), "yyyy-MM-dd HH:mm:ss:SSS" ), totalTime, result.getCode() );

        // 插入系统日志表
        SysLog sysLog = new SysLog();
        sysLog.setName(methodName);
        sysLog.setUrl(url);
        sysLog.setIp(ip);
        // 获取用户信息
        if (token == null){
            User userInfo = ShiroUtils.getUserInfo();
            if (userInfo!=null){
                sysLog.setUserId( userInfo.getId() );
            } else {
                sysLog.setName(result.getMessage());
            }
        } else {
            if (userMapper.getUserInfoByToken(token)!=null){
                sysLog.setUserId( userMapper.getUserInfoByToken(token).getId() );
            }
        }
        sysLog.setStatus( result.getCode() );
        sysLog.setExecuteTime( totalTime + " ms" );
        sysLog.insert();
        return result;
    }

}
