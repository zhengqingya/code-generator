package com.zhengqing.modules.system.dto.input;

import com.zhengqing.modules.common.dto.input.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 系统管理 - 日志表查询参数
 *
 * @author: zhengqing
 * @description:
 * @date: 2019-09-18 10:51:57
 */
@Data
@ApiModel(description = "系统管理 - 日志表查询参数")
public class LogQueryPara extends BasePageQuery{
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "访问人")
    private String username;
    @ApiModelProperty(value = "访问url")
    private String url;
    @ApiModelProperty(value = "访问开始时间")
    private Date startTime;
    @ApiModelProperty(value = "访问结束时间")
    private Date endTime;
}
