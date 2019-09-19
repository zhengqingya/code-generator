package com.zhengqing.modules.system.entity;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.zhengqing.modules.common.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>  系统管理 - 用户角色关联表  </p>
 *
 * @author: zhengqing
 * @date: 2019-08-20
 */
@Data
@ApiModel(description = "系统管理 - 用户角色关联表 ")
@TableName("t_sys_user_role")
public class UserRole extends BaseEntity<UserRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@ApiModelProperty(value = "主键")
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 用户ID
     */
	@ApiModelProperty(value = "用户ID")
	@TableField("user_id")
	private Integer userId;
    /**
     * 角色ID
     */
	@ApiModelProperty(value = "角色ID")
	@TableField("role_id")
	private Integer roleId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
