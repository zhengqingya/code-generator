package com.zhengqing.modules.code.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.zhengqing.modules.code.dto.input.DatabaseQueryPara;
import com.zhengqing.modules.code.dto.model.TableInfo;
import com.zhengqing.modules.code.dto.output.DatabaseView;
import com.zhengqing.modules.code.entity.Database;

import java.util.List;

/**
 *  <p> 项目数据库信息表 服务类 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/20 16:08
 */
public interface IDatabaseService extends IService<Database> {

    /**
     * 项目数据库信息表列表分页
     *
     * @param page
     * @param filter
     * @return
     */
    void listPage(Page<DatabaseView> page, DatabaseQueryPara filter);

    /**
     * 保存、更新数据库配置
     *
     * @param input
     */
    Integer save(Database input);

    /**
     * 项目数据库信息表列表
     *
     * @param filter
     * @return
     */
    List<DatabaseView> list(DatabaseQueryPara filter);

    /**
     * 数据库表列表
     *
     * @param filter
     * @return
     */
    List<TableInfo> tableList(DatabaseQueryPara filter);

    /**
     * 获取数据表字段信息
     *
     * @param filter
     * @return
     */
    TableInfo columnList(DatabaseQueryPara filter);

    /**
     * 保存表信息
     *
     * @param input
     */
    void saveTable(TableInfo input);
}
