package com.zhengqing.utils;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.StringUtils;

/**
 *  <p> 字符串工具类 </p>
 *
 * @author：  zhengqing <br/>
 * @date：  2019/9/14 0014$ 19:49$ <br/>
 * @version：  <br/>
 */
public class MyStringUtils {

    /**
     * 将驼峰式命名的字符串转换为下划线大写方式。如果转换前的驼峰式命名的字符串为空，则返回空字符串。</br>
     * 例如：HelloWorld->HELLO_WORLD
     * @param name 转换前的驼峰式命名的字符串
     * @return 转换后下划线大写方式命名的字符串
     */
    public static String humpToMark(String name) {
        StringBuilder result = new StringBuilder();
        if (name != null && name.length() > 0) {
            // 将第一个字符处理成大写
            result.append(name.substring(0, 1).toUpperCase());
            // 循环处理其余字符
            for (int i = 1; i < name.length(); i++) {
                String s = name.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                // 其他字符直接转成大写
                result.append(s.toUpperCase());
            }
        }
        return result.toString();
    }

    /**
     * 将字符串转换为驼峰式 </br>
     * 例如：HELLO.WORLD -> HelloWorld
     *
     *         System.out.println(CaseFormat.LOWER_HYPHEN.to(CaseFormat.LOWER_CAMEL, "test-data"));//testData
     *         System.out.println(CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, "test_data"));//testData
     *         System.out.println(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, "test_data"));//TestData
     *
     *         System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "testdata"));//testdata
     *         System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, "TestData"));//test_data
     *         System.out.println(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_HYPHEN, "testData"));//test-data
     *
     * @param str 转换前的字符串
     * @param mark 以xx符号分割  ex: `,`   `_`  `-`  `.`
     * @return 转换后的驼峰式命名的字符串
     */
    public static String strToHumpByMark(String str, String mark) {
        // 替换指定的符号mark为`_`
        String replaceStr = StringUtils.replace(str, mark, "_");
        // 再使用谷歌开发工具包转驼峰命名   ex:test_data -> TestData
        String result = CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, replaceStr);
        return result;
    }

}
