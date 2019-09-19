package com.zhengqing.modules.code.api;

import com.baomidou.mybatisplus.plugins.Page;
import com.zhengqing.modules.code.dto.input.DatabaseQueryPara;
import com.zhengqing.modules.code.dto.model.TableInfo;
import com.zhengqing.modules.code.dto.output.DatabaseView;
import com.zhengqing.modules.code.entity.Database;
import com.zhengqing.modules.code.service.IDatabaseService;
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

import java.io.IOException;
import java.util.List;

/**
 *  <p> 数据库信息表 接口 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/20 16:08
 */
@RestController
@RequestMapping("/api/code/database")
@Api(description = "数据库信息表接口")
public class DatabaseController extends BaseController {

    @Autowired
    IDatabaseService databaseService;

    @PostMapping(value = "/listPage", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取数据库信息表列表分页", httpMethod = "POST", response = DatabaseView.class, notes = "获取数据库信息表列表分页")
    public ApiResult listPage(@RequestBody DatabaseQueryPara filter) {
        Page<DatabaseView> page = new Page<>(filter.getPage(), filter.getLimit());
        databaseService.listPage(page, filter);
        return ApiResult.ok("获取数据库列表分页成功", page);
    }

    @PostMapping(value = "/list", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取数据库信息表列表", httpMethod = "POST", response = DatabaseView.class, notes = "获取数据库信息表列表")
    public ApiResult list(@RequestBody DatabaseQueryPara filter) {
        List<DatabaseView> result = databaseService.list(filter);
        return ApiResult.ok("获取数据库列表成功", result);
    }

    @PostMapping(value = "/save", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "保存数据库信息表", httpMethod = "POST", response = ApiResult.class, notes = "保存数据库信息表")
    public ApiResult save(@RequestBody @Validated Database input) {
        Integer id = databaseService.save(input);
        return ApiResult.ok("保存数据库成功", id);
    }

    @PostMapping(value = "/delete", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "删除数据库信息表", httpMethod = "POST", response = ApiResult.class, notes = "删除数据库信息表")
    public ApiResult delete(@RequestBody DatabaseQueryPara input) {
        databaseService.deleteById(input.getId());
        return ApiResult.ok("删除数据库成功");
    }

    @PostMapping(value = "/tableList", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "获取数据表", httpMethod = "POST", response = TableInfo.class, notes = "获取数据表")
    public ApiResult tableList(@RequestBody(required = false) DatabaseQueryPara filter) throws IOException {
        List<TableInfo> tableInfoList = databaseService.tableList(filter);
        return ApiResult.ok("获取数据表成功", tableInfoList);
    }

    @PostMapping(value = "/columnList", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "表字段列表", httpMethod = "POST", response = TableInfo.class, notes = "表字段列表")
    public ApiResult columnList(@RequestBody(required = false) DatabaseQueryPara filter) throws IOException {
        TableInfo tableInfo = databaseService.columnList(filter);
        return ApiResult.ok("获取表字段成功", tableInfo);
    }

    @PostMapping(value = "/saveTable", produces = "application/json;charset=utf-8")
    @ApiOperation(value = "保存表字段信息", httpMethod = "POST", response = TableInfo.class, notes = "保存表字段信息")
    public ApiResult saveTable(@RequestBody(required = false) TableInfo input) throws IOException {
        databaseService.saveTable(input);
        return ApiResult.ok("保存表字段信息成功");
    }

}
