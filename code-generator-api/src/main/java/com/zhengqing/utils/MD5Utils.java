package com.zhengqing.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *  <p> MD5加密工具类 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/21 19:10
 */
public class MD5Utils {

    /**
     * MD5加密
     */
    public static String encrypt(String str) {
        return encrypt(str, "utf-8");
    }

    /**
     * 明文密码
     *
     * @param str: 明文密码
     * @param salt: 暂无用
     * @return: java.lang.String
     */
    public static String encodePassword(String str, String salt) {
        return encrypt(str, "utf-8");
    }

    /**
     * MD5加密
     */
    public static String encrypt(String str, String encode) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(str.getBytes(encode));
            byte[] digest = md5.digest();

            StringBuilder hexString = new StringBuilder();
            String strTemp;
            for (byte b : digest) {
                strTemp = Integer.toHexString((b & 0x000000FF) | 0xFFFFFF00).substring(6);
                hexString.append(strTemp);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public final static String getMessageDigest(byte[] buffer) {
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(buffer);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }
}
