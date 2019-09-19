package com.zhengqing.modules.code.dto.output;

import com.zhengqing.modules.code.entity.Database;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *  <p> 项目数据库信息展示视图 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/22 11:13
 */
@Data
@ApiModel(description = "项目数据库信息展示视图")
public class DatabaseView extends Database {

    @ApiModelProperty(value = "项目名")
    private String project;
}
