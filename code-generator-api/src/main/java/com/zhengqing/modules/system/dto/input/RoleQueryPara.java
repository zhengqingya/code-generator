package com.zhengqing.modules.system.dto.input;

import com.zhengqing.modules.common.dto.input.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 系统管理-角色表 查询参数
 *
 * @author: zhengqing
 * @description:
 * @date: 2019-08-20
 */
@Data
@ApiModel(description = "系统管理-角色表 查询参数")
public class RoleQueryPara extends BasePageQuery{
    @ApiModelProperty(value = "角色id")
    private Integer id;
    @ApiModelProperty(value = "角色名称")
    private String name;
}
