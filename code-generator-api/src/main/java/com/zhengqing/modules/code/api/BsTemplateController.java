package com.zhengqing.modules.code.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhengqing.modules.code.dto.input.BsTemplateQueryPara;
import com.zhengqing.modules.code.entity.BsTemplate;
import com.zhengqing.modules.code.service.IBsTemplateService;
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

/**
 *  <p> 项目代码初始模板表 接口 </p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/8/18 0018 18:24
 */
@RestController
@RequestMapping("/api/code/bsTemplate")
@Api(description = "项目代码初始模板表接口")
public class BsTemplateController extends BaseController {

    @Autowired
    IBsTemplateService bsTemplateService;

    @PostMapping(value = "/listPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取项目代码模板表列表分页", httpMethod = "POST", response = BsTemplate.class, notes = "获取项目代码模板表列表分页")
    public ApiResult listPage(@RequestBody BsTemplateQueryPara filter) {
        Page<BsTemplate> page = new Page<>(filter.getPage(), filter.getLimit());
        bsTemplateService.listPage(page, filter);
        return ApiResult.ok("获取项目代码模板表列表分页成功", page);
    }

//    @PostMapping(value = "/list", produces = "application/json;charset=utf-8")
//    @ApiOperation(value = "获取项目代码模板表列表", httpMethod = "POST", response = BsTemplate.class, notes = "获取项目代码模板表列表")
//    public ApiResult list(@RequestBody BsTemplateQueryPara filter) {
//        List result = bsTemplateService.list(filter);
//        return ApiResult.ok("获取项目代码模板表列表成功", result);
//    }

    @PostMapping(value = "/save", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "保存项目代码模板表", httpMethod = "POST", response = ApiResult.class, notes = "保存项目代码模板表")
    public ApiResult save(@RequestBody @Validated BsTemplate input) {
        Integer id = bsTemplateService.save(input);
        return ApiResult.ok("保存项目代码模板表成功", id);
    }

    @PostMapping(value = "/delete", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除项目代码模板表", httpMethod = "POST", response = ApiResult.class, notes = "删除项目代码模板表")
    public ApiResult delete(@RequestBody BsTemplateQueryPara input) {
        bsTemplateService.deleteById(input.getId());
        return ApiResult.ok("删除项目代码模板表成功");
    }

//    @RequestMapping(value = "/getById", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
//    @ApiOperation(value = "获取项目代码模板表信息", httpMethod = "POST", response = BsTemplate.class, notes = "获取项目代码模板表信息")
//    public ApiResult getById(@RequestBody BsTemplateQueryPara input) {
//        BsTemplate entity = bsTemplateService.selectById(input.getId());
//        return ApiResult.ok("获取项目代码模板表信息成功", entity);
//    }

}
