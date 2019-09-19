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
 * <p>  代码生成器 - 项目 - 模板数据源 </p>
 *
 * @author: zhengqing
 * @date: 2019-09-17 14:34:18
 */
@Data
@ApiModel(description = "代码生成器 - 项目 - 模板数据源")
@TableName("t_code_project_velocity_context")
public class CodeProjectVelocityContext extends BaseEntity<CodeProjectVelocityContext> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 模板数据源
     */
	@ApiModelProperty(value = "模板数据源")
	@TableField("velocity")
	private String velocity;
    /**
     * 内容
     */
	@ApiModelProperty(value = "内容")
	@TableField("context")
	private String context;
    /**
     * 所属项目
     */
	@ApiModelProperty(value = "所属项目")
	@TableField("project_id")
	private Integer projectId;

	/**
	 * 部分内容以对象输出格式
	 */
	@TableField(exist = false)
	private Object contextJsonObject;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
