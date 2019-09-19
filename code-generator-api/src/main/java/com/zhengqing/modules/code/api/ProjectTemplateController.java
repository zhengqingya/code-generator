package com.zhengqing.modules.code.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhengqing.modules.code.dto.input.TemplateQueryPara;
import com.zhengqing.modules.code.entity.CodeProjectVelocityContext;
import com.zhengqing.modules.code.entity.Project;
import com.zhengqing.modules.code.entity.ProjectPackage;
import com.zhengqing.modules.code.entity.ProjectTemplate;
import com.zhengqing.modules.code.service.IProjectTemplateService;
import com.zhengqing.modules.common.api.BaseController;
import com.zhengqing.modules.common.dto.output.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 *  <p> 代码生成器 - 项目代码模板接口 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/20 15:22
 */
@RestController
@RequestMapping("/api/code/project_template")
@Api(description = "代码生成器 - 项目代码模板接口")
public class ProjectTemplateController extends BaseController {

    @Autowired
    IProjectTemplateService templateService;

    @PostMapping(value = "/listPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取项目代码模板列表分页", httpMethod = "POST", response = ProjectTemplate.class, notes = "获取项目代码模板列表分页")
    public ApiResult listPage(@RequestBody TemplateQueryPara filter) {
        Page<ProjectTemplate> page = new Page<>(filter.getPage(), filter.getLimit());
        templateService.listPage(page, filter);
        return ApiResult.ok("获取项目代码模板列表分页成功", page);
    }

    @PostMapping(value = "/list", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取项目代码模板列表", httpMethod = "POST", response = ProjectTemplate.class, notes = "获取项目代码模板列表")
    public ApiResult list(@RequestBody TemplateQueryPara filter) {
        List<ProjectTemplate> result = templateService.list(filter);
        return ApiResult.ok("获取项目代码模板列表成功", result);
    }

    @PostMapping(value = "/save", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "保存项目代码模板", httpMethod = "POST", response = ApiResult.class, notes = "保存项目代码模板")
    public ApiResult save(@RequestBody @Validated ProjectTemplate input) {
        ProjectPackage projectPackage = new ProjectPackage();
        ProjectPackage model = (ProjectPackage) projectPackage.selectById( input.getType() );
        if ( model.getParentId() == 0 ){
            return ApiResult.fail("Sorry，您无法添加父包模板！");
        }
        Integer id = templateService.save(input);
        return ApiResult.ok("保存项目代码模板成功", id);
    }

    @PostMapping(value = "/delete", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除项目代码模板", httpMethod = "POST", response = ApiResult.class, notes = "删除项目代码模板")
    public ApiResult delete(@RequestBody TemplateQueryPara input) {
        templateService.deleteById(input.getId());
        return ApiResult.ok("删除项目代码模板成功");
    }

//    @PostMapping(value = "/getById", produces = "application/json;charset=utf-8")
//    @ApiOperation(value = "获取项目代码模板信息", httpMethod = "POST", response = ProjectTemplate.class, notes = "获取项目代码模板信息")
//    public ApiResult getById(@RequestBody TemplateQueryPara input) {
//        ProjectTemplate entity = templateService.selectById(input.getId());
//        return ApiResult.ok("获取项目代码模板信息成功", entity);
//    }

    @PostMapping(value = "/generateTemplate", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "生成项目代码模板", httpMethod = "POST", response = Project.class, notes = "生成项目代码模板")
    public ApiResult generateTemplate(@RequestBody TemplateQueryPara input) {
        templateService.generateTemplate(input.getProjectId());
        return ApiResult.ok("生成项目代码模板成功");
    }

    @PostMapping(value = "/listPageCodeProjectVelocityContext", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取项目代码模板对应数据源模板列表", httpMethod = "POST", response = CodeProjectVelocityContext.class, notes = "获取项目代码模板对应数据源模板列表")
    public ApiResult listPageCodeProjectVelocityContext(@RequestBody TemplateQueryPara filter) {
        if ( filter.getProjectId() == null ){
            return ApiResult.fail("请先选择项目！（提示：一个项目对应一套数据源哦~）");
        }
        Page<CodeProjectVelocityContext> page = new Page<>(filter.getPage(), filter.getLimit());
        templateService.listPageCodeProjectVelocityContext(page, filter);
        return ApiResult.ok("获取数据源模板列表成功！", page);
    }

}
