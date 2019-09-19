package com.zhengqing.modules.code.dto.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 *  <p> 表字段信息 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/22 11:13
 */
@Data
@ApiModel(description = "表字段信息")
public class TableInfo implements Serializable {

    private static final long serialVersionUID = 3148176768559230877L;

    @ApiModelProperty(value = "关联项目ID")
    private Integer projectId;

    @ApiModelProperty(value = "数据库ID")
    private Integer dataBaseId;

    @ApiModelProperty(value = "表名")
    private String tableName;

    @ApiModelProperty(value = "表描述")
    private String comments;

    @ApiModelProperty(value = "列信息")
    private List<ColumnInfo> columnList;

    @ApiModelProperty(value = "可用于检索的字段")
    private String[] queryColumns;

    @ApiModelProperty(value = "包信息")
//    private PackageConfig packageConfig;
    private Map<String,String> packageConfig;

//    @ApiModelProperty(value = "包信息")
//    private ProjectPackage packageInfo;

//    @Data
//    public static class PackageConfig {
//        /**
//         * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
//         */
//        private String parent = "com.zhengqing.demo";
//
//        /**
//         * 父包模块名。
//         */
//        private String moduleName = "test";
//
//        /**
//         * Entity包名
//         */
//        private String entity = "entity";
//
//        /**
//         * Service包名
//         */
//        private String service = "service";
//
//        /**
//         * Service Impl包名
//         */
//        private String serviceImpl = "service.impl";
//        /**
//         * Mapper包名
//         */
//        private String mapper = "mapper";
//
//        /**
//         * Mapper XML包名
//         */
//        private String xml = "mapper.xml";
//
//        /**
//         * Controller包名
//         */
//        private String controller = "web";
//    }

}

