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

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *  <p> 项目代码模板表 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/22 11:14
 */
@Data
@ApiModel(description = "项目代码模板表")
@TableName("t_code_project_template")
@FieldRepeatValidator(field = "type", message = "模板类型重复，请重新选择！")
public class ProjectTemplate extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
	@ApiModelProperty(value = "模板ID")
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 项目ID
     */
	@ApiModelProperty(value = "项目ID")
	@TableField("project_id")
	@NotNull(message = "请选择关联项目!")
	private Integer projectId;
    /**
     * 模板类型
     */
	@ApiModelProperty(value = "模板类型")
	@TableField("type")
	@NotNull(message = "请选择模板类型!")
	private Integer type;
    /**
     * 生成文件后缀名 ex:.java
     */
	@ApiModelProperty(value = "生成文件后缀名 ex:.java")
	@TableField("file_suffix")
	@NotBlank(message = "生成文件后缀名不能为空!  例如: \".java\" ")
	private String fileSuffix;
    /**
     * 模板内容
     */
	@ApiModelProperty(value = "模板内容")
	@TableField("content")
	@NotBlank(message = "模板内容不能为空!")
	private String content;

	@ApiModelProperty(value = "关联项目名")
	@TableField(exist = false)
	private String projectName;

	@ApiModelProperty(value = "模板类型名")
	@TableField(exist = false)
	private String packageName;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
