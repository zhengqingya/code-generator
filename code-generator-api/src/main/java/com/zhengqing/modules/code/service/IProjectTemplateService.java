package com.zhengqing.modules.code.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhengqing.modules.code.dto.input.TemplateQueryPara;
import com.zhengqing.modules.code.entity.CodeProjectVelocityContext;
import com.zhengqing.modules.code.entity.ProjectTemplate;

import java.util.List;

/**
 *  <p> 项目代码模板表 服务类 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/20 16:07
 */
public interface IProjectTemplateService extends IService<ProjectTemplate> {

    /**
     * 项目代码模板表列表分页
     *
     * @param page
     * @param filter
     * @return
     */
    void listPage(Page<ProjectTemplate> page, TemplateQueryPara filter);

    /**
     * 根据项目ID获取模板数据源配置信息
     *
     * @param page
     * @param filter:
     * @return: void
     */
    void listPageCodeProjectVelocityContext(Page<CodeProjectVelocityContext> page, TemplateQueryPara filter);

    /**
     * 保存项目代码模板表
     *
     * @param input
     */
    Integer save(ProjectTemplate input);

    /**
     * 项目代码模板表列表
     *
     * @param filter
     * @return
     */
    List<ProjectTemplate> list(TemplateQueryPara filter);

    /**
     * 生成项目代码模板
     *
     * @param projectId
     */
    void generateTemplate(Integer projectId);
}
