package com.zhengqing.modules.code.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.zhengqing.modules.code.dto.input.ProjectQueryPara;
import com.zhengqing.modules.code.entity.Project;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p> 代码生成器 - 项目管理 Mapper 接口 </p>
 *
 * @author : zhengqing
 * @date : 2019-09-09
 */
public interface ProjectMapper extends BaseMapper<Project> {

    /**
     * 列表分页
     *
     * @param page
     * @param filter
     * @return
     */
    List<Project> selectProjects(Pagination page, @Param("filter") ProjectQueryPara filter);

    /**
     * 列表
     *
     * @param filter
     * @return
     */
    List<Project> selectProjects(@Param("filter") ProjectQueryPara filter);
}
