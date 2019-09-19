package com.zhengqing.modules.common.generator;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.zhengqing.modules.code.entity.ProjectPackage;
import com.zhengqing.modules.code.entity.ProjectTemplate;
import com.zhengqing.utils.MyStringUtils;
import lombok.Data;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 *  <p> 配置汇总 传递给文件生成工具 </p>
 *
 * @description :
 * @author : zhengqing
 * @date : 2019/8/22 11:10
 */
@Data
public class MyConfigBuilder {

    /**
     * 模板路径配置信息
     */
    private final List<ProjectTemplate> templateList;
    /**
     * 数据库配置
     */
    private final DataSourceConfig dataSourceConfig;
    /**
     * SQL连接
     */
    private Connection connection;
    /**
     * SQL语句类型
     */
    private IDbQuery dbQuery;
    private String superEntityClass;
    private String superMapperClass;
    /**
     * service超类定义
     */
    private String superServiceClass;
    private String superServiceImplClass;
    private String superControllerClass;
    /**
     * 数据库表信息
     */
    private List<MyTableInfo> tableInfoList;
    /**
     * 包配置详情
     */
    private Map<String, String> packageInfo;
    /**
     * 路径配置信息
     */
    private Map<String, String> pathInfo;
    /**
     * 策略配置
     */
    private StrategyConfig strategyConfig;
    /**
     * 全局配置信息
     */
    private GlobalConfig globalConfig;
    /**
     * 注入配置信息
     */
    private InjectionConfig injectionConfig;


    /**
     * <p> 在构造器中处理配置 </p>
     *
     * @param packageConfig    包配置
     * @param dataSourceConfig 数据源配置
     * @param strategyConfig   表配置
     * @param templateList     模板配置
     * @param globalConfig     全局配置
     */
    public MyConfigBuilder(Map<String,String> packageConfig, DataSourceConfig dataSourceConfig, StrategyConfig strategyConfig, List<ProjectTemplate> templateList, GlobalConfig globalConfig) {
        // 全局配置
        if (null == globalConfig) {
            this.globalConfig = new GlobalConfig();
        } else {
            this.globalConfig = globalConfig;
        }
        // 模板配置
        this.templateList = templateList;
        // 包配置
        if (null == packageConfig) {
            handlerPackage( this.globalConfig.getOutputDir(), new HashMap<String, String> () );
        } else {
            handlerPackage( this.globalConfig.getOutputDir(), packageConfig );
        }
        this.dataSourceConfig = dataSourceConfig;
        handlerDataSource(dataSourceConfig);
        // 策略配置
        if (null == strategyConfig) {
            this.strategyConfig = new StrategyConfig();
        } else {
            this.strategyConfig = strategyConfig;
        }
        handlerStrategy(this.strategyConfig);
    }

    /**
     * 处理包配置
     *
     * @param outputDir
     * @param config
     */
    private void handlerPackage(String outputDir, Map<String,String> config) {
        // 父包
        String parent = "parent";
        // 模块名
        String moduleName = "moduleName";
        // 父包值
        String parentValue = config.get(parent);
        // 模块值
        String moduleNameValue = config.get(moduleName);


        // 获取包配置信息 【 ex: service -> com.zhengqing.modules.service 】
        packageInfo = getPackageInfo(parentValue, moduleNameValue);

        // 生成路径信息 【 ex: service -> E:\IT_zhengqing\code\me_workspace\demo\code-generator-api/document/upload/code\service 】
        pathInfo = new HashMap<>(16);

        packageInfo.forEach( (key,value) -> pathInfo.put( key, joinPath(outputDir, packageInfo.get(key) ) ) );


        // 加入模块 名 - 值
        packageInfo.put(moduleName, moduleNameValue);



//        // 父包
//        String parent = "parent";
//        // 模块名
//        String moduleName = "moduleName";
//
//        // 拿到父包值
//        String parentValue = config.get( parent );
//        // 拿到模块值
//        String moduleValue = config.get( moduleName );
//
//        for ( Map.Entry<String, String> e : config.entrySet() ) {
//            if ( moduleName.equals( e.getKey() ) ){
//                packageInfo.put(e.getKey(), e.getValue() );
//            } else {
//                if ( !parent.equals( e.getKey() ) ){
//                    // 如果模块名不为空则加入父路径
//                    if ( StringUtils.isNotEmpty( moduleValue ) ){
//                        packageInfo.put( e.getKey(), joinPackage( joinPackage( parentValue, moduleValue ), e.getValue() ) );
//                    } else {
//                        packageInfo.put( e.getKey(), joinPackage( parentValue, e.getValue() ) );
//                    }
//                    pathInfo.put( e.getKey(), joinPath(outputDir, packageInfo.get( e.getKey() ) ) );
//                }
//            }
//        }


//        packageInfo.put( MyConstVal.MODULENAME, config.getModuleName() );
//        packageInfo.put( MyConstVal.ENTITY, joinPackage( config.getParent(), config.getEntity() ) );
//        packageInfo.put( MyConstVal.MAPPER, joinPackage( config.getParent(), config.getMapper() ) );
//        packageInfo.put( MyConstVal.XML, joinPackage( config.getParent(), config.getXml() ) );
//        packageInfo.put( MyConstVal.SERIVCE, joinPackage( config.getParent(), config.getService() ) );
//        packageInfo.put( MyConstVal.SERVICEIMPL, joinPackage( config.getParent(), config.getServiceImpl() ) );
//        packageInfo.put( MyConstVal.CONTROLLER, joinPackage( config.getParent(), config.getController() ) );
//        packageInfo.put( MyConstVal.QUERY_PARA_DTO, joinPackage( config.getParent(), "dto.input") );


//        if ( CollectionUtils.isNotEmpty( templateList ) ) {
//            templateList.forEach(template -> {
//                EnumTemplateType enumTemplateType = EnumTemplateType.getEnum(template.getType());
//                switch (enumTemplateType) {
//                    case ENTITY:
//                        pathInfo.put(MyConstVal.ENTITY_PATH, joinPath(outputDir, packageInfo.get(MyConstVal.ENTITY)));
//                        break;
//                    case MAPPER:
//                        pathInfo.put(MyConstVal.MAPPER_PATH, joinPath(outputDir, packageInfo.get(MyConstVal.MAPPER)));
//                        break;
//                    case MAPPER_XML:
//                        pathInfo.put(MyConstVal.XML_PATH, joinPath(outputDir, packageInfo.get(MyConstVal.XML)));
//                        break;
//                    case SERVICE:
//                        pathInfo.put(MyConstVal.SERIVCE_PATH, joinPath(outputDir, packageInfo.get(MyConstVal.SERIVCE)));
//                        break;
//                    case SERVICE_IMPL:
//                        pathInfo.put(MyConstVal.SERVICEIMPL_PATH, joinPath(outputDir, packageInfo.get(MyConstVal.SERVICEIMPL)));
//                        break;
//                    case CONTROLLER:
//                        pathInfo.put(MyConstVal.CONTROLLER_PATH, joinPath(outputDir, packageInfo.get(MyConstVal.CONTROLLER)));
//                        break;
//                    case VUE_LIST:
//                        pathInfo.put(MyConstVal.VUE_LIST_PATH, joinPath(outputDir, File.separator + config.getModuleName()));
//                        break;
//                    case VUE_DETAIL:
//                        pathInfo.put(MyConstVal.VUE_DETAIL_PATH, joinPath(outputDir, File.separator + config.getModuleName()));
//                        break;
//                    case VUE_API:
//                        pathInfo.put(MyConstVal.VUE_API_PATH, joinPath(outputDir, "api" + File.separator + config.getModuleName()));
//                        break;
//                    case QUERY_DTO:
//                        pathInfo.put(MyConstVal.QUERY_DTO_PATH, joinPath(outputDir, packageInfo.get(MyConstVal.QUERY_PARA_DTO)));
//                        break;
//                }
//            });
//        }

    }


    /**
     * <p>
     * 处理数据源配置
     * </p>
     *
     * @param config DataSourceConfig
     */
    private void handlerDataSource(DataSourceConfig config) {
        connection = config.getConn();
        dbQuery = config.getDbQuery();
    }


    /**
     * <p>
     * 处理数据库表 加载数据库表、列、注释相关数据集
     * </p>
     *
     * @param config StrategyConfig
     */
    private void handlerStrategy(StrategyConfig config) {
        processTypes(config);
        tableInfoList = getTablesInfo(config);
    }


    /**
     * <p>
     * 处理superClassName,IdClassType,IdStrategy配置
     * </p>
     *
     * @param config 策略配置
     */
    private void processTypes(StrategyConfig config) {
        if (StringUtils.isEmpty(config.getSuperServiceClass())) {
            superServiceClass = ConstVal.SUPERD_SERVICE_CLASS;
        } else {
            superServiceClass = config.getSuperServiceClass();
        }
        if (StringUtils.isEmpty(config.getSuperServiceImplClass())) {
            superServiceImplClass = ConstVal.SUPERD_SERVICEIMPL_CLASS;
        } else {
            superServiceImplClass = config.getSuperServiceImplClass();
        }
        if (StringUtils.isEmpty(config.getSuperMapperClass())) {
            superMapperClass = ConstVal.SUPERD_MAPPER_CLASS;
        } else {
            superMapperClass = config.getSuperMapperClass();
        }
        superEntityClass = config.getSuperEntityClass();
        superControllerClass = config.getSuperControllerClass();
    }


    /**
     * <p>
     * 处理表对应的类名称
     * </P>
     *
     * @param tableList 表名称 -> 目前暂对单表进行操作，遍历tableList：对多表进行操作
     * @param strategy  命名策略
     * @param config    策略配置项
     * @return 补充完整信息后的表
     */
    private List<MyTableInfo> processTable(List<MyTableInfo> tableList, NamingStrategy strategy, StrategyConfig config) {
        String[] tablePrefix = config.getTablePrefix();
        String[] fieldPrefix = config.getFieldPrefix();

        // 设置文件名
        for (MyTableInfo tableInfo : tableList) {
            Map<String, String> packageName = new HashMap<>(16);
            // ① 获取数据库表名去掉前缀的纯实体名  ex:User
            tableInfo.setEntityName(strategyConfig, NamingStrategy.capitalFirst( processName( tableInfo.getName(), strategy, tablePrefix) ) );
            for ( Map.Entry<String, String> e : pathInfo.entrySet() ) {
                // ② 存放其他类型文件名 , 转换成驼峰式命名
                packageName.put( e.getKey(),  tableInfo.getEntityName() + MyStringUtils.strToHumpByMark(e.getKey(), ".") );
            }
            tableInfo.setPackageInfo( packageName );
            // 强制开启字段注解
            checkTableIdTableFieldAnnotation(config, tableInfo, fieldPrefix);
        }


//        for (MyTableInfo tableInfo : tableList) {
//            tableInfo.setEntityName(strategyConfig, NamingStrategy.capitalFirst(processName(tableInfo.getName(), strategy, tablePrefix)));
//
//            tableInfo.setFormQueryParaName(tableInfo.getEntityName() + MyConstVal.QUERY_PARA_DTO);
//            if (StringUtils.isNotEmpty(globalConfig.getMapperName())) {
//                tableInfo.setMapperName(String.format(globalConfig.getMapperName(), tableInfo.getEntityName()));
//            } else {
//                tableInfo.setMapperName(tableInfo.getEntityName() + ConstVal.MAPPER);
//            }
//            if (StringUtils.isNotEmpty(globalConfig.getXmlName())) {
//                tableInfo.setXmlName(String.format(globalConfig.getXmlName(), tableInfo.getEntityName()));
//            } else {
//                tableInfo.setXmlName(tableInfo.getEntityName() + ConstVal.MAPPER);
//            }
//            if (StringUtils.isNotEmpty(globalConfig.getServiceName())) {
//                tableInfo.setServiceName(String.format(globalConfig.getServiceName(), tableInfo.getEntityName()));
//            } else {
//                tableInfo.setServiceName("I" + tableInfo.getEntityName() + ConstVal.SERIVCE);
//            }
//            if (StringUtils.isNotEmpty(globalConfig.getServiceImplName())) {
//                tableInfo.setServiceImplName(String.format(globalConfig.getServiceImplName(), tableInfo.getEntityName()));
//            } else {
//                tableInfo.setServiceImplName(tableInfo.getEntityName() + ConstVal.SERVICEIMPL);
//            }
//            if (StringUtils.isNotEmpty(globalConfig.getControllerName())) {
//                tableInfo.setControllerName(String.format(globalConfig.getControllerName(), tableInfo.getEntityName()));
//            } else {
//                tableInfo.setControllerName(tableInfo.getEntityName() + ConstVal.CONTROLLER);
//            }
//            // 强制开启字段注解
//            checkTableIdTableFieldAnnotation(config, tableInfo, fieldPrefix);
//        }
        return tableList;
    }


    /**
     * <p>
     * 检查是否有
     * {@link com.baomidou.mybatisplus.annotations.TableId}
     * {@link com.baomidou.mybatisplus.annotations.TableField}
     * 注解
     * </p>
     *
     * @param config
     * @param tableInfo
     * @param fieldPrefix
     */
    private void checkTableIdTableFieldAnnotation(StrategyConfig config, TableInfo tableInfo, String[] fieldPrefix) {
        boolean importTableFieldAnnotaion = false;
        boolean importTableIdAnnotaion = false;
        if (config.isEntityTableFieldAnnotationEnable()) {
            for (TableField tf : tableInfo.getFields()) {
                tf.setConvert(true);
                importTableFieldAnnotaion = true;
                importTableIdAnnotaion = true;
            }
        } else if (fieldPrefix != null && fieldPrefix.length != 0) {
            for (TableField tf : tableInfo.getFields()) {
                if (NamingStrategy.isPrefixContained(tf.getName(), fieldPrefix)) {
                    if (tf.isKeyFlag()) {
                        importTableIdAnnotaion = true;
                    }
                    tf.setConvert(true);
                    importTableFieldAnnotaion = true;
                }
            }
        }
        if (importTableFieldAnnotaion && !tableInfo.getImportPackages().contains(com.baomidou.mybatisplus.annotations.TableField.class.getCanonicalName())) {
            tableInfo.getImportPackages().add(com.baomidou.mybatisplus.annotations.TableField.class.getCanonicalName());
        }

        if (globalConfig.getIdType() != null) {
            if (importTableIdAnnotaion && !tableInfo.getImportPackages().contains(com.baomidou.mybatisplus.annotations.TableId.class.getCanonicalName())) {
                tableInfo.getImportPackages().add(com.baomidou.mybatisplus.annotations.TableId.class.getCanonicalName());
            }
            tableInfo.getImportPackages().add(com.baomidou.mybatisplus.enums.IdType.class.getCanonicalName());
        }
    }


    /**
     * <p>
     * 获取所有的数据库表信息
     * </p>
     */
    private List<MyTableInfo> getTablesInfo(StrategyConfig config) {
        boolean isInclude = (null != config.getInclude() && config.getInclude().length > 0);
        boolean isExclude = (null != config.getExclude() && config.getExclude().length > 0);
        if (isInclude && isExclude) {
            throw new RuntimeException("<strategy> 标签中 <include> 与 <exclude> 只能配置一项！");
        }
        //所有的表信息
        List<MyTableInfo> tableList = new ArrayList<>();

        //需要反向生成或排除的表信息
        List<MyTableInfo> includeTableList = new ArrayList<>();
        List<MyTableInfo> excludeTableList = new ArrayList<>();

        //不存在的表名
        Set<String> notExistTables = new HashSet<>();
        PreparedStatement preparedStatement = null;
        try {
            String tablesSql = dbQuery.tablesSql();
            if (DbType.POSTGRE_SQL == dbQuery.dbType()) {
                tablesSql = String.format(tablesSql, dataSourceConfig.getSchemaname());
            }
            //oracle数据库表太多，出现最大游标错误
            else if (DbType.ORACLE == dbQuery.dbType()) {
                if (isInclude) {
                    StringBuilder sb = new StringBuilder(tablesSql);
                    sb.append(" WHERE ").append(dbQuery.tableName()).append(" IN (");
                    for (String tbname : config.getInclude()) {
                        sb.append("'").append(tbname.toUpperCase()).append("',");
                    }
                    sb.replace(sb.length() - 1, sb.length(), ")");
                    tablesSql = sb.toString();
                } else if (isExclude) {
                    StringBuilder sb = new StringBuilder(tablesSql);
                    sb.append(" WHERE ").append(dbQuery.tableName()).append(" NOT IN (");
                    for (String tbname : config.getExclude()) {
                        sb.append("'").append(tbname.toUpperCase()).append("',");
                    }
                    sb.replace(sb.length() - 1, sb.length(), ")");
                    tablesSql = sb.toString();
                }
            }
            preparedStatement = connection.prepareStatement(tablesSql);
            ResultSet results = preparedStatement.executeQuery();
            MyTableInfo tableInfo;
            while (results.next()) {
                String tableName = results.getString(dbQuery.tableName());
                if (StringUtils.isNotEmpty(tableName)) {
                    String tableComment = results.getString(dbQuery.tableComment());
                    if (config.isSkipView() && "VIEW".equals(tableComment)) {
                        // 跳过视图
                        continue;
                    }
                    tableInfo = new MyTableInfo();
                    tableInfo.setName(tableName);
                    tableInfo.setComment(tableComment);
                    if (isInclude) {
                        for (String includeTab : config.getInclude()) {
                            if (includeTab.equalsIgnoreCase(tableName)) {
                                includeTableList.add(tableInfo);
                            } else {
                                notExistTables.add(includeTab);
                            }
                        }
                    } else if (isExclude) {
                        for (String excludeTab : config.getExclude()) {
                            if (excludeTab.equalsIgnoreCase(tableName)) {
                                excludeTableList.add(tableInfo);
                            } else {
                                notExistTables.add(excludeTab);
                            }
                        }
                    }
                    tableList.add(tableInfo);
                } else {
                    System.err.println("当前数据库为空！！！");
                }
            }
            // 将已经存在的表移除，获取配置中数据库不存在的表
            for (TableInfo tabInfo : tableList) {
                notExistTables.remove(tabInfo.getName());
            }

            if (notExistTables.size() > 0) {
                System.err.println("表 " + notExistTables + " 在数据库中不存在！！！");
            }

            // 需要反向生成的表信息
            if (isExclude) {
                tableList.removeAll(excludeTableList);
                includeTableList = tableList;
            }
            if (!isInclude && !isExclude) {
                includeTableList = tableList;
            }
            /**
             * 性能优化，只处理需执行表字段 github issues/219
             */
            for (TableInfo ti : includeTableList) {
                this.convertTableFields(ti, config.getColumnNaming());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return processTable(includeTableList, config.getNaming(), config);
    }


    /**
     * <p>
     * 将字段信息与表信息关联
     * </p>
     *
     * @param tableInfo 表信息
     * @param strategy  命名策略
     * @return
     */
    private TableInfo convertTableFields(TableInfo tableInfo, NamingStrategy strategy) {
        boolean haveId = false;
        List<TableField> fieldList = new ArrayList<>();
        List<TableField> commonFieldList = new ArrayList<>();
        try {
            String tableFieldsSql = dbQuery.tableFieldsSql();
            if (DbType.POSTGRE_SQL == dbQuery.dbType()) {
                tableFieldsSql = String.format(tableFieldsSql, dataSourceConfig.getSchemaname(), tableInfo.getName());
            } else {
                tableFieldsSql = String.format(tableFieldsSql, tableInfo.getName());
            }
            PreparedStatement preparedStatement = connection.prepareStatement(tableFieldsSql);
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                TableField field = new TableField();
                String key = results.getString(dbQuery.fieldKey());
                // 避免多重主键设置，目前只取第一个找到ID，并放到list中的索引为0的位置
                boolean isId = StringUtils.isNotEmpty(key) && key.toUpperCase().equals("PRI");
                // 处理ID
                if (isId && !haveId) {
                    field.setKeyFlag(true);
                    if (dbQuery.isKeyIdentity(results)) {
                        field.setKeyIdentityFlag(true);
                    }
                    haveId = true;
                } else {
                    field.setKeyFlag(false);
                }
                // 自定义字段查询
                String[] fcs = dbQuery.fieldCustom();
                if (null != fcs) {
                    Map<String, Object> customMap = new HashMap<>();
                    for (String fc : fcs) {
                        customMap.put(fc, results.getObject(fc));
                    }
                    field.setCustomMap(customMap);
                }
                // 处理其它信息
                field.setName(results.getString(dbQuery.fieldName()));
                field.setType(results.getString(dbQuery.fieldType()));
                field.setPropertyName(strategyConfig, processName(field.getName(), strategy));
                field.setColumnType(dataSourceConfig.getTypeConvert().processTypeConvert(field.getType()));
                field.setComment(results.getString(dbQuery.fieldComment()));
                if (strategyConfig.includeSuperEntityColumns(field.getName())) {
                    // 跳过公共字段
                    commonFieldList.add(field);
                    continue;
                }
                // 填充逻辑判断
                List<TableFill> tableFillList = this.getStrategyConfig().getTableFillList();
                if (null != tableFillList) {
                    for (TableFill tableFill : tableFillList) {
                        if (tableFill.getFieldName().equals(field.getName())) {
                            field.setFill(tableFill.getFieldFill().name());
                            break;
                        }
                    }
                }
                fieldList.add(field);
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception：" + e.getMessage());
        }
        tableInfo.setFields(fieldList);
        tableInfo.setCommonFields(commonFieldList);
        return tableInfo;
    }


    /**
     * <p>
     * 连接路径字符串
     * </p>
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isEmpty(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }


    /**
     * <p>
     * 连接父子包名
     * </p>
     *
     * @param parent     父包名
     * @param subPackage 子包名
     * @return 连接后的包名
     */
    private String joinPackage(String parent, String subPackage) {
        if (StringUtils.isEmpty(parent)) {
            return subPackage;
        }
        return parent + "." + subPackage;
    }


    /**
     * <p>
     * 处理字段名称
     * </p>
     *
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy) {
        return processName(name, strategy, this.strategyConfig.getFieldPrefix());
    }


    /**
     * <p>
     * 处理表/字段名称
     * </p>
     *
     * @param name
     * @param strategy
     * @param prefix
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy, String[] prefix) {
        boolean removePrefix = false;
        if (prefix != null && prefix.length >= 1) {
            removePrefix = true;
        }
        String propertyName;
        if (removePrefix) {
            if (strategy == NamingStrategy.underline_to_camel) {
                // 删除前缀、下划线转驼峰
                propertyName = NamingStrategy.removePrefixAndCamel(name, prefix);
            } else {
                // 删除前缀
                propertyName = NamingStrategy.removePrefix(name, prefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            // 下划线转驼峰
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            // 不处理
            propertyName = name;
        }
        return propertyName;
    }

    // --------------------------------------------

    /**
     * 存入 包 <-> 子包所在路径、配置   /和.路径
     *
     * @param parentValue:父包值
     * @param moduleNameValue:模块名值
     * @return: java.util.Map<java.lang.String,java.lang.String>
     */
    public Map<String, String> getPackageInfo(String parentValue, String moduleNameValue){
        Map<String, String> map = new HashMap<>(16);
        templateList.forEach( e -> {
            List<String> list = new LinkedList<>();
            String packageName = getRecursionPackage(e.getType(), list);
            // 反转list数据
            Collections.reverse(list);
            String path = String.join(".", list);

            // 前端动态父包和模块
            if ( StringUtils.isNotEmpty( moduleNameValue ) ){
                path = org.apache.commons.lang3.StringUtils.replace(path, list.get(0), parentValue);
            }

            StringBuilder newPath =new StringBuilder(path);
            if ( StringUtils.isNotEmpty( moduleNameValue ) ){
                // 如果父包为空 则直接在头部插入
                if( StringUtils.isEmpty( parentValue ) ){
                    newPath.insert(0, moduleNameValue);
                } else {
                    // 否则在父包后插入
                    newPath = newPath.insert( newPath.indexOf( parentValue ) + parentValue.length() + 1, moduleNameValue + "." );
                }
            }

            map.put(packageName, newPath.toString());
        });
        return map;
    }



    /**
     * 根据项目包ID 获取 包路径
     *
     * @param projectPackageId:包ID
     * @param packagePathList:存放包名倒序  由下往上
     * @return: java.lang.String
     */
    public String getRecursionPackage(Integer projectPackageId, List<String> packagePathList){
        ProjectPackage p = new ProjectPackage().selectById( projectPackageId );
        packagePathList.add( p.getName() );
        // 如果不是父包则递归
        if ( p.getParentId() != 0 ){
            getRecursionPackage( p.getParentId(), packagePathList );
        }
        return p.getName();
    }


}
