package com.zhengqing.modules.code.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhengqing.modules.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *  <p> 项目数据库信息表 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/19 14:12
 */
@Data
@ApiModel(description = "项目数据库信息表")
@TableName("t_code_table_config")
public class TableConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
	@ApiModelProperty(value = "ID")
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 所属项目ID
     */
	@ApiModelProperty(value = "所属项目ID")
	@TableField("project_id")
	private Integer projectId;
    /**
     * 表名
     */
	@ApiModelProperty(value = "表名")
	@TableField("table_name")
	private String tableName;
    /**
     * 用户检索的字段
     */
	@ApiModelProperty(value = "用户检索的字段")
	@TableField("query_columns")
	private String queryColumns;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
