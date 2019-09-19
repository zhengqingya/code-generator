package com.zhengqing.modules.code.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.zhengqing.modules.code.dto.input.TemplateQueryPara;
import com.zhengqing.modules.code.entity.ProjectTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *  <p> 项目代码模板表 Mapper 接口 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/20 15:23
 */
public interface ProjectTemplateMapper extends BaseMapper<ProjectTemplate> {

    /**
     * 列表分页
     *
     * @param page
     * @param filter
     * @return
     */
    List<ProjectTemplate> selectTemplates(Pagination page, @Param("filter") TemplateQueryPara filter);

    /**
     * 列表
     *
     * @param filter
     * @return
     */
    List<ProjectTemplate> selectTemplates(@Param("filter") TemplateQueryPara filter);

    /**
     * 根据项目id删除关联的项目模板
     *
     * @param projectId:
     * @return: void
     */
    void deleteTemplatesByProjectId(@Param("projectId") Integer projectId);

}
