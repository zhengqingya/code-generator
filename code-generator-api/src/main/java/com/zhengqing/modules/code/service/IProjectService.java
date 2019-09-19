package com.zhengqing.modules.code.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhengqing.modules.code.dto.input.CodeGenerateInput;
import com.zhengqing.modules.code.dto.input.ProjectQueryPara;
import com.zhengqing.modules.code.entity.Project;
import com.zhengqing.modules.code.entity.ProjectPackage;

import java.io.IOException;
import java.util.List;

/**
 * <p>  代码生成器 - 项目管理 服务类 </p>
 *
 * @author: zhengqing
 * @date: 2019-09-09
 */
public interface IProjectService extends IService<Project> {

    /**
     * 代码生成器 - 项目管理列表分页
     *
     * @param page
     * @param filter
     * @return
     */
    void listPage(Page<Project> page, ProjectQueryPara filter);

    /**
     * 代码生成器 - 项目管理列表
     *
     * @param filter
     * @return
     */
    List<Project> list(ProjectQueryPara filter);

    /**
     * 代码生成器 - 项目包类型列表
     * @param filter
     * @return
     */
    List<ProjectPackage> listPackage(ProjectQueryPara filter);

    /**
     * 保存代码生成器 - 项目
     *
     * @param input
     */
    Integer saveProject(Project input);

    /**
     * 保存代码生成器 - 项目包
     *
     * @param input
     */
    Integer savePackage(ProjectPackage input);

    /**
     * 项目包架构树
     * @param filter
     * @return
     */
    List<ProjectPackage> treeProjectPackage(ProjectQueryPara filter);

    /**
     * 生成代码
     *
     * @param input
     * @return
     */
    String generateCode(CodeGenerateInput input) throws IOException;

}
