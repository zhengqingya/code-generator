package com.zhengqing.modules.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhengqing.modules.system.dto.input.UserQueryPara;
import com.zhengqing.modules.system.dto.model.UserInfoVO;
import com.zhengqing.modules.system.entity.User;

import java.util.List;

/**
 * <p>  系统管理-用户基础信息表 服务类 </p>
 *
 * @author: zhengqing
 * @date: 2019-08-19
 */
public interface IUserService extends IService<User> {

    /**
     * 系统管理-用户基础信息表列表分页
     *
     * @param page
     * @param filter
     * @return
     */
    void listPage(Page<User> page, UserQueryPara filter);

    /**
     * 保存系统管理-用户基础信息表
     *
     * @param input:
     * @return: java.lang.Integer
     */
    Integer save(User input);

    /**
     * 修改用户个人信息
     *
     * @param para:
     * @return: java.lang.Integer
     */
    Integer updatePersonalInfo(User para);

    /**
     * 系统管理-用户基础信息表列表
     *
     * @param filter
     * @return
     */
    List<User> list(UserQueryPara filter);

    /**
     * 通过token获取用户信息
     *
     * @param token:
     * @return: com.zhengqing.modules.system.dto.model.UserInfoVO
     */
    UserInfoVO getCurrentUserInfo(String token);

}
