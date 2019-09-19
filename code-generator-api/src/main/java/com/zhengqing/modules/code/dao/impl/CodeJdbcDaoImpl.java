package com.zhengqing.modules.code.dao.impl;

import com.zhengqing.modules.code.dao.CodeJdbcDao;
import com.zhengqing.modules.code.dto.model.ColumnInfo;
import com.zhengqing.modules.code.dto.model.TableInfo;
import com.zhengqing.modules.code.entity.Database;
import com.zhengqing.modules.common.enumeration.EnumDatabaseType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *  <p> JDBC方式访问实现 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/7/22 20:48
 */
@Slf4j
@Repository
public class CodeJdbcDaoImpl implements CodeJdbcDao {

    /**
     * 更新MySQL表注释
     */
    private final String ALTER_TABLE_COMMENT_MYSQL = "ALTER TABLE %s COMMENT '%s';";
    /**
     * 更新MySQL表字段信息
     */
    private final String ALTER_FIELD_COMMENT_MYSQL = "ALTER TABLE %s MODIFY %s %s %s %s COMMENT '%s';";
    /**
     * 更新oracle表注释
     */
    private final String ALTER_TABLE_COMMENT_ORACLE = "ALTER TABLE %s COMMENT '%s';";

    @Override
    public void saveComment(TableInfo tableInfo, Database dbConfig) {
        Connection conn = getConnection(dbConfig);
        try {
            Statement stmt = conn.createStatement();
            String strSql = "";
            String isNullable = "";
            String isAutoIncrement = "";

            if (dbConfig.getUrl().indexOf("mysql") > 0) {
                /*
                 * format(Locale locale, String format, Object... args):用于创建格式化的字符串以及连接多个字符串对象
                 * 简而言之: 转换符 %s -> 字符串类型
                 * 用法：ALTER TABLE %s COMMENT '%s'; -> 经格式化会变成 -> ALTER TABLE zq_demo COMMENT '代码生成器测试demo';
                 */
                strSql = String.format(ALTER_TABLE_COMMENT_MYSQL, tableInfo.getTableName(), tableInfo.getComments());
                log.info("更新表注释sql语句 -> " + strSql);
                stmt.executeUpdate(strSql);
                //stmt.executeUpdate("use information_schema;");
                for (ColumnInfo item : tableInfo.getColumnList()) {
                    isNullable = item.isNullable() ? "" : "NOT NULL";
                    isAutoIncrement = item.isAutoIncrement() ? "AUTO_INCREMENT" : "";
                    strSql = String.format(ALTER_FIELD_COMMENT_MYSQL, tableInfo.getTableName(), item.getColumnName(), item.getColumnType(), isNullable, isAutoIncrement, item.getComments());
                    log.info("更新表字段sql语句 -> " + strSql);
                    //strSql = "update information_schema.COLUMNS t set t.column_comment='"+item.getComments()+"' where t.TABLE_SCHEMA='数据库名' and t.table_name='"+tableInfo.getTableName()+"' and t.COLUMN_NAME='"+item.getColName()+"';");
                    stmt.executeUpdate(strSql);
                }
            } else {
                strSql = "COMMENT ON TABLE " + tableInfo.getTableName() + " IS '#" + tableInfo.getComments() + "'";
                stmt.executeUpdate(strSql);
                for (ColumnInfo item : tableInfo.getColumnList()) {
                    strSql = "COMMENT ON COLUMN " + tableInfo.getTableName() + "." + item.getColumnName() + " IS '" + item.getComments() + "'";
                    stmt.executeUpdate(strSql);
                }
            }
            // 关闭资源
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("execute sql occer error", e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }


    @Override
    public List<TableInfo> getAllTables(Database dbConfig, String tableName) {
        List<TableInfo> tableList = new ArrayList<>();
        // TODO 后期考虑采用PreparedStatement预编译语句做连接 原因：安全
        Connection conn = getConnection(dbConfig);
        try {
            // 获得sql的预编译对象
            Statement stmt = conn.createStatement();
            // 执行sql语句,获取表名和表注释，在information_schema库下的表tables中的table_name为表名、TABLE_COMMENT为表注释信息
            String strSql = "";
            // indexOf:返回指定字符[mysql]在字符串中第一次出现处的索引，如果此字符串中没有这样的字符，则返回 -1
            if (dbConfig.getUrl().indexOf("mysql") > 0) {
                strSql = "select table_name,TABLE_COMMENT from information_schema.tables where table_schema='" + dbConfig.getDbSchema() + "'";
                if (StringUtils.isNotBlank(tableName)) {
                    strSql += " AND table_name LIKE '%" + tableName + "%'";
                }
            } else {
                strSql = "select table_name,comments from user_tab_comments where table_type='TABLE'";
                if (StringUtils.isNotBlank(tableName)) {
                    strSql += " AND table_name LIKE '%" + tableName + "%'";
                }
                strSql += " order by table_name";
            }
            log.info("数据库表sql查询语句： =====>>>>>" + strSql);
            // 执行查询
            ResultSet rs = stmt.executeQuery(strSql);
            // 展开结果集数据库
            while (rs.next()) {
                TableInfo table = new TableInfo();
                table.setTableName(rs.getString(1));
                table.setComments(rs.getString(2));
                tableList.add(table);
            }
            // 释放资源(注意：关闭资源顺序 先打开后关闭) 完成 后关闭
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("execute sql occer error", e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return tableList;
    }

    @Override
    public TableInfo getAllColumns(Database dbConfig, String tableName) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setTableName(tableName);

        Connection conn = getConnection(dbConfig);
        try {
            Statement stmt = conn.createStatement();
            String strSql = "";
            // 根据表名【table_name】和库名【table_schema】获取表注释信息，在information_schema库下的表tables中的TABLE_COMMENT为表注释信息
            if (dbConfig.getUrl().indexOf("mysql") > 0) {
                strSql = "select TABLE_COMMENT from information_schema.tables where table_name='" + tableName + "' and table_schema='" + dbConfig.getDbSchema() + "'";
            } else {
                // 注：无效  原因：数据库不存在表user_tab_comments
                strSql = "select comments from user_tab_comments where table_name='" + tableName + "'";
            }
            ResultSet rs = stmt.executeQuery(strSql);
            while (rs.next()) {
                tableInfo.setComments(rs.getString(1));
            }

            // 获取该表所有字段信息【column_name：字段名】【column_comment：字段对应注释】【column_type：字段类型】【IS_NULLABLE：是否为空 no or yes】【COLUMN_KEY：主键则值为PRI】【EXTRA：是否自动递增 是：auto_increment】
            if (dbConfig.getUrl().indexOf("mysql") > 0) {
                strSql = "select column_name,column_comment,column_type,IS_NULLABLE,COLUMN_KEY,EXTRA from Information_schema.columns where table_Name = '" + tableName + "' and table_schema='" + dbConfig.getDbSchema() + "'";
            } else {
                strSql = "select z.COLUMN_NAME,c.comments,z.data_type from user_tab_columns z,user_col_comments c where z.TABLE_NAME=c.table_name and z.COLUMN_NAME=c.column_name and z.Table_Name='" + tableName + "'";
            }
            log.info("表字段信息查询sql语句 =====>>>>> " + strSql);
            List<ColumnInfo> colList = new ArrayList<>();
            rs = stmt.executeQuery(strSql);
            while (rs.next()) {
                ColumnInfo colInfo = new ColumnInfo();
                colInfo.setColumnName(rs.getString(1));
                colInfo.setComments(rs.getString(2));
                if (dbConfig.getUrl().indexOf("mysql") > 0) {
//                    if ("varchar".equalsIgnoreCase(rs.getString(3))) {
                    colInfo.setColumnType(rs.getString(3));
//                    }
                    colInfo.setNullable("YES".equals(rs.getString(4)) ? true : false);
                    colInfo.setPrimaryKey(StringUtils.isNotBlank(rs.getString(5)) ? true : false);
                    colInfo.setAutoIncrement(StringUtils.isNotBlank(rs.getString(6)) ? true : false);
                } else {
                    colInfo.setColumnType(rs.getString(3));
                }
                colList.add(colInfo);
            }
            tableInfo.setColumnList(colList);
            // 关闭资源
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("execute sql occer error", e);
        } finally {
            try {
                conn.close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return tableInfo;
    }

    /**
     * 连接数据库
     *
     * @param dbConfig:数据库连接配置信息
     * @return: java.sql.Connection
     */
    private Connection getConnection(Database dbConfig) {
        //创建用于连接数据库的Connection对象
        Connection con = null;
        try {
            EnumDatabaseType enumDatabaseType = EnumDatabaseType.getEnum(dbConfig.getDbType());
            // 加载JDBC驱动
            Class.forName(dbConfig.getDriver());
            // 根据数据库类型【mysql or oracle】，获取连接，与数据库建立连接
            switch (enumDatabaseType) {
                case Oracle:
                    // 连接所需参数【url：指向要访问的数据库名地址, user：用户名, password：密码】
                    con = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUser(), dbConfig.getPassword());
                    break;
                case MySQL:
                    con = DriverManager.getConnection(dbConfig.getUrl() + "?useUnicode=true&characterEncoding=UTF8", dbConfig.getUser(), dbConfig.getPassword());
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            System.out.println("数据库连接失败" + e.getMessage());
        }
        //返回所建立的数据库连接信息
        return con;
    }


}
