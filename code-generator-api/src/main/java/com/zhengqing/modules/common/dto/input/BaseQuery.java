package com.zhengqing.modules.common.dto.input;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  <p> 基类查询参数 </p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/9/13 0013 1:57
 */
@ApiModel(description = "基类查询参数")
@Data
public class BaseQuery extends BasePageQuery{
    @ApiModelProperty(value = "用户ID")
    // @JSONField: 解决由于json转成类时字段不一致的问题  前端：name值  后端：userId
    @JSONField(name = "userId")
    private Integer userId;
}
