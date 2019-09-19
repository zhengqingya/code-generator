package com.zhengqing.modules.shiro.constant;

/**
 *  <p> redis常量类 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/23 15:47
 */
public interface RedisConstant {
    /**
     * TOKEN前缀
     */
    String REDIS_PREFIX_LOGIN = "code-generator_token_%s";
    /**
     * 过期时间2小时
     */
    Integer REDIS_EXPIRE_TWO = 7200;
    /**
     * 过期时间15分
     */
    Integer REDIS_EXPIRE_EMAIL = 900;
    /**
     * 过期时间5分钟
     */
    Integer REDIS_EXPIRE_KAPTCHA = 300;
    /**
     * 暂无过期时间
     */
    Integer REDIS_EXPIRE_NULL = -1;
}
