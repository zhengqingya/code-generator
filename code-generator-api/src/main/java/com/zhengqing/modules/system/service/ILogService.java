package com.zhengqing.modules.system.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhengqing.modules.system.entity.SysLog;
import com.zhengqing.modules.system.dto.input.LogQueryPara;

import java.util.List;

/**
 * <p>  系统管理 - 日志表 服务类 </p>
 *
 * @author: zhengqing
 * @date: 2019-09-18 10:51:57
 */
public interface ILogService extends IService<SysLog> {

    /**
     * 系统管理 - 日志表列表分页
     *
     * @param page
     * @param para
     * @return
     */
    void listPage(Page<SysLog> page, LogQueryPara para);

    /**
     * 保存系统管理 - 日志表
     *
     * @param input
     */
    Integer save(SysLog input);

    /**
     * 系统管理 - 日志表列表
     *
     * @param para
     * @return
     */
    List<SysLog> list(LogQueryPara para);

}
