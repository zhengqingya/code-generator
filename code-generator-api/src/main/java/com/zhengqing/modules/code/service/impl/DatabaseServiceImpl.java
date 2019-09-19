package com.zhengqing.modules.code.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhengqing.modules.code.dao.CodeJdbcDao;
import com.zhengqing.modules.code.dto.input.DatabaseQueryPara;
import com.zhengqing.modules.code.dto.input.TableConfigQueryPara;
import com.zhengqing.modules.code.dto.input.TemplateQueryPara;
import com.zhengqing.modules.code.dto.model.TableInfo;
import com.zhengqing.modules.code.dto.output.DatabaseView;
import com.zhengqing.modules.code.entity.Database;
import com.zhengqing.modules.code.entity.ProjectPackage;
import com.zhengqing.modules.code.entity.ProjectTemplate;
import com.zhengqing.modules.code.entity.TableConfig;
import com.zhengqing.modules.code.mapper.*;
import com.zhengqing.modules.code.service.IDatabaseService;
import com.zhengqing.modules.common.exception.MyException;
import com.zhengqing.modules.shiro.utils.ShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  <p> 项目数据库信息表 服务实现类 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/22 11:14
 */
@Service
@Transactional
public class DatabaseServiceImpl extends ServiceImpl<DatabaseMapper, Database> implements IDatabaseService {

    @Autowired
    DatabaseMapper databaseMapper;
    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    TableConfigMapper tableConfigMapper;
    @Autowired
    CodeJdbcDao codeJdbcDao;
    @Autowired
    ProjectTemplateMapper projectTemplateMapper;
    @Autowired
    ProjectPackageMapper projectPackageMapper;

    @Override
    public void listPage(Page<DatabaseView> page, DatabaseQueryPara filter) {
        filter.setUserId( ShiroUtils.getUserInfo().getId() );
        page.setRecords(databaseMapper.selectDatabases(page, filter));
    }

    @Override
    public List<DatabaseView> list(DatabaseQueryPara filter) {
        filter.setUserId( ShiroUtils.getUserInfo().getId() );
        return databaseMapper.selectDatabases(filter);
    }

    @Override
    public Integer save(Database para) {
        DatabaseQueryPara databaseQueryPara = new DatabaseQueryPara();
        databaseQueryPara.setName( para.getName() );
        databaseQueryPara.setUserId( ShiroUtils.getUserInfo().getId() );
        List<DatabaseView> databaseList = databaseMapper.selectDatabases( databaseQueryPara );
        if (para.getId() != null) {
            if ( !CollectionUtils.isEmpty( databaseList ) ){
                Database database = databaseMapper.selectById( para.getId() );
                if ( !para.getName().equals( database.getName() ) ){
                    throw new MyException( "数据库名称重复，请重新输入！" );
                }
            }
            databaseMapper.updateById(para);
        } else {
            if ( !CollectionUtils.isEmpty( databaseList ) ){
                throw new MyException( "数据库名称重复，请重新输入！" );
            }
            databaseMapper.insert(para);
        }
        return para.getId();
    }

    @Override
    public List<TableInfo> tableList(DatabaseQueryPara filter) {
        Database database = selectById(filter.getId());
        return codeJdbcDao.getAllTables(database, filter.getTableName());
    }

    @Override
    public TableInfo columnList(DatabaseQueryPara filter) {
        // ① 拿到该数据库信息
        Database database = selectById( filter.getId() );
        // ② 根据表名和库 拿到表字段基本信息
        TableInfo tableInfo = codeJdbcDao.getAllColumns( database, filter.getTableName() );
        if (tableInfo != null && database != null) {
            tableInfo.setDataBaseId( database.getId() );
            tableInfo.setProjectId( database.getProjectId() );
            // ③ 查询是否存在之前生成代码时的检索字段
            List<TableConfig> tableConfigList = tableConfigMapper.selectTableConfigs( TableConfigQueryPara.builder().projectId( database.getProjectId() ).tableName( filter.getTableName() ).build() );
            if (!CollectionUtils.isEmpty( tableConfigList) && !StringUtils.isEmpty( tableConfigList.get(0).getQueryColumns() ) ) {
                tableInfo.setQueryColumns( tableConfigList.get(0).getQueryColumns().split(",") );
            }
            // ④ 获取库的关联项目包信息

//            TableInfo.PackageConfig packageConfig = new TableInfo.PackageConfig();
//            Project project = projectMapper.selectById( database.getProjectId() );
//            packageConfig.setParent(project.getParentPackage());
//            packageConfig.setModuleName(project.getModuleName());
//            packageConfig.setEntity(project.getEntity());
//            packageConfig.setMapper(project.getMapper());
//            packageConfig.setXml(project.getMapperXml());
//            packageConfig.setService(project.getService());
//            packageConfig.setServiceImpl(project.getServiceImpl());
//            packageConfig.setController(project.getController());

            // 改为动态获取数据库包配置信息
            Map<String,String> packageConfig = new HashMap<>(16);
            // 1.先拿到关联项目模板
            TemplateQueryPara templateQueryPara = new TemplateQueryPara();
            templateQueryPara.setUserId( ShiroUtils.getUserInfo().getId() );
            templateQueryPara.setProjectId( database.getProjectId() );
            List<ProjectTemplate> projectTemplateList = projectTemplateMapper.selectTemplates( templateQueryPara );
            // 父包
            List<ProjectPackage> projectPackageList = projectPackageMapper.selectList( new EntityWrapper<ProjectPackage>().eq( "parent_id", 0 ).eq( "project_id", database.getProjectId() ) );
            packageConfig.put( "parent", projectPackageList.get(0).getName() );
            if ( !CollectionUtils.isEmpty( projectTemplateList ) ){
                projectTemplateList.forEach( e -> {
                    // 2.再通过模板拿到包返回给前端
                    ProjectPackage projectPackage = projectPackageMapper.selectById( e.getType() );
                    packageConfig.put( projectPackage.getName(), projectPackage.getName() );
                });
            }
            tableInfo.setPackageConfig(packageConfig);
        }
        return tableInfo;
    }

    @Override
    public void saveTable(TableInfo input) {
        Database database = databaseMapper.selectById(input.getDataBaseId());
        if (database != null) {
            codeJdbcDao.saveComment(input, database);
        }
    }

}
