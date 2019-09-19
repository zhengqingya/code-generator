package com.zhengqing.modules.system.mapper;

import com.zhengqing.modules.system.entity.SysLog;
import com.zhengqing.modules.system.dto.input.LogQueryPara;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p> 系统管理 - 日志表 Mapper 接口 </p>
 *
 * @author : zhengqing
 * @date : 2019-09-18 10:51:57
 */
public interface LogMapper extends BaseMapper<SysLog> {

    /**
     * 列表分页
     *
     * @param page
     * @param filter
     * @return
     */
    List<SysLog> selectLogs(Pagination page, @Param("filter") LogQueryPara filter);

    /**
     * 列表
     *
     * @param filter
     * @return
     */
    List<SysLog> selectLogs(@Param("filter") LogQueryPara filter);

}
