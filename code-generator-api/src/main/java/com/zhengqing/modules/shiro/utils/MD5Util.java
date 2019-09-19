package com.zhengqing.modules.shiro.utils;

import org.apache.shiro.crypto.hash.SimpleHash;

/**
 *  <p> 根据传过来的密码进行加密 - 注意：加密算法必须一致(加密算法类型，加密次数，盐值  ex：MD5 10 zhengqing)</p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/8/24 0024 21:06
 */
public class MD5Util {

//    public static final String SALT = "zhengqing";
    public static final int HASHITERATIONS = 10;

    public static String createMD5Str(String password, String salt){
        SimpleHash hash = new SimpleHash("MD5", password, salt, HASHITERATIONS);
        return hash.toString();
    }

}
