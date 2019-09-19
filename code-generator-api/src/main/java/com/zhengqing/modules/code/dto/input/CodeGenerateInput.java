package com.zhengqing.modules.code.dto.input;

import com.zhengqing.modules.code.dto.model.TableInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Map;

/**
 *  <p> 代码生成传入参数 </p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/7/25 16:42
 */
@Data
@ApiModel(description = "代码生成传入参数")
public class CodeGenerateInput {

    @ApiModelProperty(value = "表信息")
    private TableInfo tableInfo;

    @ApiModelProperty(value = "包配置")
    private Map<String,String> packageConfig;

}
