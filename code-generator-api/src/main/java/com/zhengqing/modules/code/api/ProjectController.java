package com.zhengqing.modules.code.api;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhengqing.modules.code.dto.input.CodeGenerateInput;
import com.zhengqing.modules.code.dto.input.ProjectQueryPara;
import com.zhengqing.modules.code.entity.Project;
import com.zhengqing.modules.code.entity.ProjectPackage;
import com.zhengqing.modules.code.mapper.ProjectPackageMapper;
import com.zhengqing.modules.code.mapper.ProjectTemplateMapper;
import com.zhengqing.modules.code.service.IProjectService;
import com.zhengqing.modules.common.api.BaseController;
import com.zhengqing.modules.common.dto.output.ApiResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;


/**
 * <p> 代码生成器 - 项目管理 接口 </p>
 *
 * @author: zhengqing
 * @description:
 * @date: 2019-09-09
 *
 */
@RestController
@RequestMapping("/api/code/project")
@Api(description = "代码生成器 - 项目管理接口")
public class ProjectController extends BaseController {

    @Autowired
    IProjectService projectService;
    @Autowired
    ProjectTemplateMapper projectTemplateMapper;
    @Autowired
    ProjectPackageMapper projectPackageMapper;

//    @RequiresPermissions("code:project:list")
    @PostMapping(value = "/listPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取代码生成器 - 项目管理列表分页", httpMethod = "POST", response = Project.class)
    public ApiResult listPage(@RequestBody ProjectQueryPara filter) {
        Page<Project> page = new Page<>(filter.getPage(),filter.getLimit());
        projectService.listPage(page, filter);
        return ApiResult.ok("获取项目列表分页成功", page);
    }

    @PostMapping(value = "/list", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取代码生成器 - 项目管理列表", httpMethod = "POST", response = Project.class)
    public ApiResult list(@RequestBody ProjectQueryPara filter) {
        List<Project> result = projectService.list(filter);
        return ApiResult.ok("获取项目列表成功",result);
    }

    @PostMapping(value = "/saveOrUpdateProject", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "保存或更新代码生成器 - 项目管理", httpMethod = "POST", response = ApiResult.class)
    public ApiResult saveOrUpdateProject(@RequestBody @Validated Project input) {
        Integer id = projectService.saveProject(input);
        return ApiResult.ok("保存项目成功", id);
    }

    @PostMapping(value = "/deleteProject", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除代码生成器 - 项目管理", httpMethod = "POST", response = ApiResult.class)
    public ApiResult deleteProject(@RequestBody ProjectQueryPara input) {
        // 一、删除关联的项目模板
        projectTemplateMapper.deleteTemplatesByProjectId(input.getId());
        // 二、删除关联的项目包
        HashMap<String, Object> map = new HashMap<>();
        map.put( "project_id", input.getId() );
        projectPackageMapper.deleteByMap( map );
        // 三、删除该项目
        projectService.deleteById(input.getId());
        return ApiResult.ok("删除项目成功");
    }

    // 上：项目管理   下：项目包管理   ==========================================================================

    @PostMapping(value = "/tree", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取项目包架构树", httpMethod = "POST", response = Project.class, notes = "获取项目包架构树")
    public ApiResult tree(@RequestBody ProjectQueryPara filter) {
        if( filter.getId() == null ){
            return ApiResult.fail("关联项目ID为空不能刷新，请关闭弹出窗重新进入！");
        }
        List<ProjectPackage> result = projectService.treeProjectPackage(filter);
        return ApiResult.ok("获取项目包架构树成功",result);
    }

    @PostMapping(value = "/listPackage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取项目包类型列表", httpMethod = "POST", response = Project.class, notes = "获取项目包类型列表")
    public ApiResult listPackage(@RequestBody ProjectQueryPara filter) {
        if( filter.getId() == null ){
            return ApiResult.fail("关联项目ID为空不能刷新，请关闭弹出窗重新进入！");
        }
        List<ProjectPackage> result = projectService.listPackage(filter);
        return ApiResult.ok("获取项目包类型成功",result);
    }

    @PostMapping(value = "/saveOrUpdatePackage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "保存或更新代码生成器 - 项目包", httpMethod = "POST", response = ApiResult.class)
    public ApiResult saveOrUpdatePackage(@RequestBody @Validated ProjectPackage input) {
        Integer projectId = projectService.savePackage(input);
        return ApiResult.ok("保存包成功", projectId);
    }

    @PostMapping(value = "/deletePackage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除代码生成器 - 项目包", httpMethod = "POST", response = ApiResult.class)
    public ApiResult deletePackage(@RequestBody ProjectQueryPara input) {
        List<ProjectPackage> projectPackageList = projectPackageMapper.selectList( new EntityWrapper<ProjectPackage>().eq( "parent_id", input.getId() ) );
        if( !CollectionUtils.isEmpty( projectPackageList ) ){
            return ApiResult.fail("该包下存在子包，请先删除子包！");
        }
        ProjectPackage projectPackage = projectPackageMapper.selectById( input.getId() );
        if ( projectPackage.getParentId() == 0 ){
            return ApiResult.fail("Sorry，您不能删除顶级父包！");
        }
        projectPackageMapper.deleteById( input.getId() );
        return ApiResult.ok("删除包成功");
    }


//    @PostMapping(value = "/detail", produces = "application/json;charset=utf-8")
//    @ApiOperation(value = "根据ID获取代码生成器 - 项目管理信息", httpMethod = "POST", response = ApiResult.class)
//    public ApiResult detail(@RequestBody ProjectQueryPara input) {
//        Project2 entity = projectService.selectById(input.getId());
//        return ApiResult.ok("根据ID获取代码生成器 - 项目管理信息成功", entity);
//    }

    @PostMapping(value = "/generate", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "生成代码", httpMethod = "POST", response = ApiResult.class, notes = "生成代码")
    public ApiResult generate(@RequestBody CodeGenerateInput input) throws IOException {
        // 代码生成到指定目录
        return ApiResult.ok("生成代码成功", projectService.generateCode( input ) );
    }

}
