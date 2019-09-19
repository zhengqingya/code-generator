package com.zhengqing.modules.code.dto.input;

import com.baomidou.mybatisplus.annotations.TableField;
import com.zhengqing.modules.common.dto.input.BasePageQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 *  <p> 默认代码模板表查询参数 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/22 11:12
 */
@Data
@ApiModel(description = "默认代码模板表查询参数")
public class BsTemplateQueryPara extends BasePageQuery {
    @ApiModelProperty(value = "id")
    private Integer id;
    @ApiModelProperty(value = "用户id")
    private Integer userId;
    @ApiModelProperty(value = "模板类型")
    private String type;
}
