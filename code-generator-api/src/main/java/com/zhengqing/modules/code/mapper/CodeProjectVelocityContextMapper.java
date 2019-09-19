package com.zhengqing.modules.code.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.zhengqing.modules.code.dto.input.TemplateQueryPara;
import com.zhengqing.modules.code.entity.CodeProjectVelocityContext;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p> 代码生成器 - 项目 - 模板数据源 Mapper 接口 </p>
 *
 * @author : zhengqing
 * @date : 2019-09-17 14:34:18
 */
public interface CodeProjectVelocityContextMapper extends BaseMapper<CodeProjectVelocityContext> {

    /**
     * 列表分页
     *
     * @param page
     * @param filter
     * @return
     */
    List<CodeProjectVelocityContext> selectCodeProjectVelocityContexts(Pagination page, @Param("filter") TemplateQueryPara filter);

    /**
     * 列表
     *
     * @param filter
     * @return
     */
    List<CodeProjectVelocityContext> selectCodeProjectVelocityContexts(@Param("filter") TemplateQueryPara filter);

}
