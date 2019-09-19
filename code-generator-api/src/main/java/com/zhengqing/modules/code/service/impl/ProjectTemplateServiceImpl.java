package com.zhengqing.modules.code.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhengqing.modules.code.dto.input.TemplateQueryPara;
import com.zhengqing.modules.code.entity.BsTemplate;
import com.zhengqing.modules.code.entity.CodeProjectVelocityContext;
import com.zhengqing.modules.code.entity.ProjectPackage;
import com.zhengqing.modules.code.entity.ProjectTemplate;
import com.zhengqing.modules.code.mapper.BsTemplateMapper;
import com.zhengqing.modules.code.mapper.CodeProjectVelocityContextMapper;
import com.zhengqing.modules.code.mapper.ProjectPackageMapper;
import com.zhengqing.modules.code.mapper.ProjectTemplateMapper;
import com.zhengqing.modules.code.service.IProjectTemplateService;
import com.zhengqing.modules.common.exception.MyException;
import com.zhengqing.modules.shiro.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 *  <p> 项目代码模板表 服务实现类 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/20 15:22
 */
@Service
@Transactional
public class ProjectTemplateServiceImpl extends ServiceImpl<ProjectTemplateMapper, ProjectTemplate> implements IProjectTemplateService {

    @Autowired
    ProjectTemplateMapper projectTemplateMapper;
    @Autowired
    BsTemplateMapper bsTemplateMapper;
    @Autowired
    ProjectPackageMapper projectPackageMapper;
    @Autowired
    CodeProjectVelocityContextMapper contextMapper;

    @Override
    public void listPage(Page<ProjectTemplate> page, TemplateQueryPara filter) {
        filter.setUserId( ShiroUtils.getUserInfo().getId() );
        page.setRecords(projectTemplateMapper.selectTemplates(page, filter));
    }

    @Override
    public void listPageCodeProjectVelocityContext(Page<CodeProjectVelocityContext> page, TemplateQueryPara filter) {
        List<CodeProjectVelocityContext> result = contextMapper.selectCodeProjectVelocityContexts(page, filter);
        result.forEach( e -> {
            String context = e.getContext();
            if ( context.startsWith("{") ){
                // json字符串对象转对象
                JSONObject jsonObject = JSON.parseObject( context );
                e.setContextJsonObject(jsonObject);
            } else if ( context.startsWith("[") ) {
                // json数组转对象
                JSONArray jsonObject = JSONArray.parseArray( context );
                e.setContextJsonObject(jsonObject);
            } else {
                e.setContextJsonObject(context);
            }
        });
        page.setRecords(result);
    }

    @Override
    public List<ProjectTemplate> list(TemplateQueryPara filter) {
        filter.setUserId( ShiroUtils.getUserInfo().getId() );
        return projectTemplateMapper.selectTemplates(filter);
    }

    @Override
    public Integer save(ProjectTemplate para) {
        if (para.getId() != null) {
            projectTemplateMapper.updateById(para);
        } else {
            projectTemplateMapper.insert(para);
        }
        return para.getId();
    }

    @Override
    public void generateTemplate(Integer projectId) {
        if (projectId == null) {
            throw new MyException("请先选择项目！");
        }

        // 查询指定项目ID的包
        List<ProjectPackage> packageList = projectPackageMapper.selectList( new EntityWrapper<ProjectPackage>().eq( "project_id", projectId) );

        List<BsTemplate> bsTemplates = bsTemplateMapper.selectList(null);
        if (!CollectionUtils.isEmpty(bsTemplates)) {
            bsTemplates.forEach(bsTemplate -> packageList.forEach(p -> {
                // 如果初始模板的类型名称 与 项目包名称 一致 则生成项目模板
                if ( bsTemplate.getType().equals( p.getName() ) ){
                    ProjectTemplate template = new ProjectTemplate();
                    template.setProjectId(projectId);
                    template.setType( p.getId() );
                    template.setFileSuffix( bsTemplate.getFileSuffix() );
                    template.setContent( bsTemplate.getContent() );
                    projectTemplateMapper.insert(template);
                }
            }));
        }
    }

}
