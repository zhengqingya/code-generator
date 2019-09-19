package com.zhengqing.modules.code.dto.input;

import com.zhengqing.modules.common.dto.input.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  <p> 项目数据库信息表查询参数 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/22 10:04
 */
@Data
@ApiModel(description = "项目数据库信息表查询参数")
public class DatabaseQueryPara extends BaseQuery {
    @ApiModelProperty(value = "数据库ID")
    private Integer id;

    @ApiModelProperty(value = "项目ID")
    private Integer projectId;

    @ApiModelProperty(value = "数据表名")
    private String tableName;

    @ApiModelProperty(value = "数据库名称")
    private String name;
}
