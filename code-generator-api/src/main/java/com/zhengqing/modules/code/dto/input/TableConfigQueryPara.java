package com.zhengqing.modules.code.dto.input;

import com.zhengqing.modules.common.dto.input.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 *  <p> 项目表配置表查询参数 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/22 11:12
 */
@AllArgsConstructor
@Builder
@Data
@ApiModel(description = "项目表配置表查询参数")
public class TableConfigQueryPara extends BasePageQuery {
    @ApiModelProperty(value = "项目表配置ID")
    private Integer id;

    @ApiModelProperty(value = "项目ID")
    private Integer projectId;

    @ApiModelProperty(value = "数据表名")
    private String tableName;
}
