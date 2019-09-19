package com.zhengqing.modules.code.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import com.zhengqing.modules.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>  代码生成器 - 项目包管理表 </p>
 *
 * @author: zhengqing
 * @date: 2019-09-09
 */
@Data
@ApiModel(description = "代码生成器 - 项目包管理表")
@TableName("t_code_project_package")
//@FieldRepeatValidator(field = "name", message = "包名重复！")
public class ProjectPackage extends BaseEntity<ProjectPackage> {

    private static final long serialVersionUID = 1L;

    /**
     * 包ID
     */
	@ApiModelProperty(value = "包ID")
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 包名
     */
	@ApiModelProperty(value = "包名")
	@TableField("name")
	@NotBlank(message = "包名称不能为空")
	@Length(max = 50, message = "包名不能超过50个字符")
	private String name;
    /**
     * 父包ID
     */
	@ApiModelProperty(value = "父包ID")
	@TableField("parent_id")
	private Integer parentId;
	/**
	 * 包关联项目ID
	 */
	@ApiModelProperty(value = "包关联项目ID")
	@TableField("project_id")
	private Integer projectId;

	@ApiModelProperty(value = "父包名")
	@TableField(exist = false)
	private String parentPackageName;

	@ApiModelProperty(value = "前端所需children值 -> 即子包")
	@TableField(exist = false)
	private List<ProjectPackage> children = new ArrayList<>();

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
