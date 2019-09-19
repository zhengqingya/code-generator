package com.zhengqing.modules.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhengqing.modules.system.entity.RoleMenu;
import com.zhengqing.modules.system.dto.input.RoleMenuQueryPara;

import java.util.List;

/**
 * <p>  系统管理 - 角色-菜单关联表  服务类 </p>
 *
 * @author: zhengqing
 * @date: 2019-08-20
 */
public interface IRoleMenuService extends IService<RoleMenu> {

    /**
     * 系统管理 - 角色-菜单关联表 列表分页
     *
     * @param page
     * @param filter
     * @return
     */
    void listPage(Page<RoleMenu> page, RoleMenuQueryPara filter);

    /**
     * 保存系统管理 - 角色-菜单关联表
     *
     * @param input
     */
    Integer save(RoleMenu input);

    /**
     * 系统管理 - 角色-菜单关联表 列表
     *
     * @param filter
     * @return
     */
    List<RoleMenu> list(RoleMenuQueryPara filter);

    /**
     * 保存角色相关联菜单
     *
     * @param filter:
     * @return: void
     */
    void saveRoleMenu(RoleMenuQueryPara filter);
}
