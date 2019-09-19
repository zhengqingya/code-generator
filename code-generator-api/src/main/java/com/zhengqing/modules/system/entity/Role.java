package com.zhengqing.modules.system.entity;

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

/**
 * <p>  系统管理-角色表  </p>
 *
 * @author: zhengqing
 * @date: 2019-08-20
 */
@Data
@ApiModel(description = "系统管理-角色表 ")
@TableName("t_sys_role")
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 角色编码
     */
	@ApiModelProperty(value = "角色编码")
	@TableField("code")
	@NotBlank(message = "角色编码不能为空")
	@Length(max = 20, message = "角色编码不能超过20个字符")
	private String code;
    /**
     * 角色名称
     */
	@ApiModelProperty(value = "角色名称")
	@TableField("name")
	@NotBlank(message = "角色名称不能为空")
	private String name;
    /**
     * 角色描述
     */
	@ApiModelProperty(value = "角色描述")
	@TableField("remarks")
	private String remarks;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
