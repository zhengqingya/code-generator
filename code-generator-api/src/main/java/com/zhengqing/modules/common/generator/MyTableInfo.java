package com.zhengqing.modules.common.generator;

import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.Data;

import java.util.Map;

/**
 *  <p> 数据库表信息 </p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/9/14 0014 18:24
 */
@Data
public class MyTableInfo extends TableInfo {

//    private String htmlName;
//
//    private String formQueryParaName;

    /**
     * 封装包信息  ex:entity、mapper、service
     */
    private Map<String,String> packageInfo;

}
