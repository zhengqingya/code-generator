package com.zhengqing.modules.common.dto.input;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  <p> 应用基础传入参数 </p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/9/13 0013 1:57
 */
@ApiModel(description = "应用基础传入参数")
@Data
public class BaseInput {
    @ApiModelProperty(value = "令牌")
    @JSONField(name = "token")
    private String token;
}
