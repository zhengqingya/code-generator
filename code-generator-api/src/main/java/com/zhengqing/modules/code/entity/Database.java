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
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 *  <p> 数据库信息表 </p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/9/13 0013 2:42
 */
@Data
@ApiModel(description = "数据库信息表")
@TableName("t_code_database")
//@FieldRepeatValidator(field = "name", message = "数据库名称重复！")
public class Database extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 项目数据库ID
     */
	@ApiModelProperty(value = "数据库ID")
	@TableId(value="id", type= IdType.AUTO)
	private Integer id;
    /**
     * 所属项目ID
     */
	@ApiModelProperty(value = "所属项目ID")
	@TableField("project_id")
	@NotNull(message = "请选择所属项目!")
	private Integer projectId;
    /**
     * 数据库名称
     */
	@ApiModelProperty(value = "数据库名称")
	@TableField("name")
	@NotBlank(message = "数据库名称不能为空")
	@Length(max = 20, message = "数据库名称不能超过20个字符")
	private String name;
    /**
     * 数据库连接
     */
	@ApiModelProperty(value = "数据库连接")
	@TableField("url")
	@NotBlank(message = "url不能为空")
	private String url;
    /**
     * 用户名
     */
	@ApiModelProperty(value = "用户名")
	@TableField("user")
	@NotBlank(message = "用户名不能为空！")
	private String user;
    /**
     * 密码
     */
	@ApiModelProperty(value = "密码")
	@TableField("password")
	@NotBlank(message = "密码不能为空！")
	private String password;
    /**
     * 数据库
     */
	@ApiModelProperty(value = "数据库")
	@TableField("db_schema")
	@NotBlank(message = "schema不能为空！")
	private String dbSchema;
    /**
     * 数据库类型 1:mysql 2:oracle
     */
	@ApiModelProperty(value = "数据库类型 1:mysql 2:oracle")
	@TableField("db_type")
	@NotNull(message = "请选择数据库类型!")
	private Integer dbType;
	/**
	 * 数据库驱动 如MySQL驱动：com.mysql.jdbc.Driver
	 */
	@ApiModelProperty(value = "数据库驱动")
	@TableField("driver")
	private String driver;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
