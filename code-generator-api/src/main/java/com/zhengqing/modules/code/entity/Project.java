package com.zhengqing.modules.code.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhengqing.modules.common.entity.BaseEntity;
import com.zhengqing.modules.common.validator.FieldRepeatValidator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>  代码生成器 - 项目管理表 </p>
 *
 * @author: zhengqing
 * @date: 2019-09-09
 */
@Data
@ApiModel(description = "代码生成器 - 项目管理表")
@TableName("t_code_project")
//@FieldRepeatValidator(field = "name", message = "项目重复！")
public class Project extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
	@ApiModelProperty(value = "项目ID")
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 项目名称
     */
	@ApiModelProperty(value = "项目名称")
	@TableField("name")
	@NotBlank(message = "项目名称不能为空")
	@Length(max = 20, message = "项目名称不能超过20个字符")
	private String name;
    /**
     * 所属用户ID
     */
	@ApiModelProperty(value = "所属用户ID")
	@TableField("user_id")
	private Integer userId;
    /**
     * 状态：是否启用  0:停用  1:启用
     */
	@ApiModelProperty(value = "状态：是否启用  0:停用  1:启用")
	@TableField("status")
	private Integer status;

	@ApiModelProperty(value = "所属用户名")
	@TableField(exist = false)
	private String username;



//	@JsonIgnore
//	@TableField(exist = false)
//	private String parentPackage;
//	@JsonIgnore
//	@TableField(exist = false)
//	private String moduleName;
//	@JsonIgnore
//	@TableField(exist = false)
//	private String entity;
//	@JsonIgnore
//	@TableField(exist = false)
//	private String service;
//	@JsonIgnore
//	@TableField(exist = false)
//	private String serviceImpl ;
//	@JsonIgnore
//	@TableField(exist = false)
//	private String mapper;
//	@JsonIgnore
//	@TableField(exist = false)
//	private String mapperXml ;
//	@JsonIgnore
//	@TableField(exist = false)
//	private String controller;


	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
