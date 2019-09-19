package com.zhengqing.modules.code.dto.input;

import com.zhengqing.modules.common.dto.input.BaseQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  <p> 项目代码模板表查询参数 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/22 11:13
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@ApiModel(description = "项目代码模板表查询参数")
public class TemplateQueryPara extends BaseQuery {
    @ApiModelProperty(value = "模板ID")
    private Integer id;

    @ApiModelProperty(value = "项目ID")
    private Integer projectId;
}
