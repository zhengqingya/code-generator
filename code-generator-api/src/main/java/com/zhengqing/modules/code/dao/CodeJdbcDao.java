package com.zhengqing.modules.code.dao;

import com.zhengqing.modules.code.dto.model.TableInfo;
import com.zhengqing.modules.code.entity.Database;

import java.util.List;

/**
 *  <p> JDBC方式访问接口 </p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/9/13 0013 19:46
 */
public interface CodeJdbcDao {

    /**
     * 更新数据库表字段信息
     *
     * @param tableInfo
     * @param database:
     * @return: void
     */
    void saveComment(TableInfo tableInfo, Database database);

    /**
     * 获取该数据库下所有表信息【表名、字段、注释信息等】
     *
     * @param database
     * @param tableName:
     * @return: java.util.List<com.zhengqing.modules.code.dto.model.TableInfo>
     */
    List<TableInfo> getAllTables(Database database, String tableName);

    /**
     * 获取该表下所有字段、类型、注释信息等
     *
     * @param database
     * @param tableName:表名
     * @return: com.zhengqing.modules.code.dto.model.TableInfo
     */
    TableInfo getAllColumns(Database database, String tableName);

}
