package com.zhengqing.modules.system.dto.output;

import com.zhengqing.modules.system.entity.Role;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 *  <p> 系统管理-角色表 输出内容 </p>
 *
 * @author：  zhengqing <br/>
 * @date：  2019/8/20$ 16:59$ <br/>
 * @version：  <br/>
 */
@Data
@ApiModel(description = "系统管理 - 角色表 输出内容")
public class RoleView extends Role {

    @ApiModelProperty(value = "是否已关联系统用户")
    private String isRelatedSysUser;

    @ApiModelProperty(value = "是否已关联系统菜单")
    private String isRelatedSysMenu;

    @ApiModelProperty(value = "是否已关联微信账号")
    private String isRelatedWxAccount;

}
