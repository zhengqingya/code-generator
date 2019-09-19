package com.zhengqing.modules.code.service.impl;

import cn.hutool.core.util.ZipUtil;
import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.DbType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.zhengqing.config.Constants;
import com.zhengqing.modules.code.dto.input.CodeGenerateInput;
import com.zhengqing.modules.code.dto.input.ProjectQueryPara;
import com.zhengqing.modules.code.dto.input.TableConfigQueryPara;
import com.zhengqing.modules.code.dto.input.TemplateQueryPara;
import com.zhengqing.modules.code.dto.model.TableInfo;
import com.zhengqing.modules.code.entity.*;
import com.zhengqing.modules.code.mapper.*;
import com.zhengqing.modules.code.service.IProjectService;
import com.zhengqing.modules.common.enumeration.EnumDatabaseType;
import com.zhengqing.modules.common.exception.MyException;
import com.zhengqing.modules.common.generator.MyGenerator;
import com.zhengqing.modules.common.service.ICosFileService;
import com.zhengqing.modules.shiro.utils.ShiroUtils;
import com.zhengqing.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p> 代码生成器 - 项目管理 服务实现类 </p>
 *
 * @author: zhengqing
 * @date: 2019-09-09
 */
@Service
@Transactional
public class ProjectServiceImpl extends ServiceImpl<ProjectMapper, Project> implements IProjectService {

    @Autowired
    ProjectMapper projectMapper;
    @Autowired
    ProjectPackageMapper projectPackageMapper;
    @Autowired
    DatabaseMapper databaseMapper;
    @Autowired
    TableConfigMapper tableConfigMapper;
    @Autowired
    ProjectTemplateMapper projectTemplateMapper;
    @Autowired
    ICosFileService cosFileService;

    @Override
    public void listPage(Page<Project> page, ProjectQueryPara filter) {
        filter.setUserId( ShiroUtils.getUserInfo().getId() );
        List<Project> result = projectMapper.selectProjects(page, filter);
        // 根据项目名称去重
//        result = result.stream().collect( Collectors.collectingAndThen( Collectors.toCollection( () -> new TreeSet<>( Comparator.comparing( Project::getName ) ) ), ArrayList::new ) );
        page.setRecords( result );
    }

    @Override
    public List<Project> list(ProjectQueryPara filter) {
        filter.setUserId( ShiroUtils.getUserInfo().getId() );
        return projectMapper.selectProjects(filter);
    }

    @Override
    public List<ProjectPackage> listPackage(ProjectQueryPara filter) {
        return projectPackageMapper.selectProjectPackages(filter);
    }

    @Override
    public List<ProjectPackage> treeProjectPackage(ProjectQueryPara filter) {
        // ①、拿到所有包
        List<ProjectPackage> allPackage = projectPackageMapper.selectProjectPackages(filter);
        if ( CollectionUtils.isEmpty( allPackage ) ){
            return null;
        }
        // ②、准备一个空的父包集合
        List<ProjectPackage> parentPackageList = new ArrayList<>();
        // ③、遍历子包 -> 进行对父包的设置
        for ( ProjectPackage parent : allPackage ) {
            if ( parent.getParentId() == 0 ) {
                parentPackageList.add(parent);
            }
        }
        // ④、遍历出父包对应的子包
        for (ProjectPackage parent : parentPackageList) {
            List<ProjectPackage> child = getChild(parent.getId(), parent.getName(), allPackage);
            parent.setChildren(child);
        }
        return parentPackageList;
    }

    public List<ProjectPackage> getChild(Integer id,String parentPackageName, List<ProjectPackage> allPackage){
        // ⑤、存放子包的集合
        List<ProjectPackage> listChild = new ArrayList<>();
        for (ProjectPackage e : allPackage) {
            if ( e.getParentId() != null && e.getParentId().equals( id ) ) {
                e.setParentPackageName( parentPackageName );
                listChild.add( e );
            }
        }
        // ⑥、递归
        for (ProjectPackage e : listChild) {
            e.setChildren( getChild( e.getId(), e.getName(), allPackage ) );
        }
        if (listChild.size() == 0) {
            return null;
        }
        return listChild;
    }

    @Override
    public Integer saveProject(Project para) {
        para.setUserId( ShiroUtils.getUserInfo().getId() );
        ProjectQueryPara projectQueryPara = new ProjectQueryPara();
        projectQueryPara.setName( para.getName() );
        projectQueryPara.setUserId( para.getUserId() );
        List<Project> projectList = projectMapper.selectProjects( projectQueryPara );
        if ( para.getId() != null ) {
            if ( !CollectionUtils.isEmpty( projectList ) ){
                Project project = projectMapper.selectById( para.getId() );
                if ( !para.getName().equals( project.getName() ) ){
                    throw new MyException( "项目名称重复，请重新输入！" );
                }
            }
            projectMapper.updateById(para);
        } else {
            if ( !CollectionUtils.isEmpty( projectList ) ){
                throw new MyException( "项目名称重复，请重新输入！" );
            }
            projectMapper.insert(para);
            // 新建项目是给一个初始化包
            ProjectPackage projectPackage = new ProjectPackage();
            projectPackage.setName( "com.zhengqing.demo" );
            projectPackage.setParentId( 0 );
            projectPackage.setProjectId( para.getId() );
            projectPackageMapper.insert(projectPackage);
        }
        return para.getId();
    }

    @Override
    public Integer savePackage(ProjectPackage para) {
        ProjectQueryPara projectQueryPara = new ProjectQueryPara();
        projectQueryPara.setId( para.getProjectId() );
        projectQueryPara.setName( para.getName() );
        projectQueryPara.setUserId( ShiroUtils.getUserInfo().getId() );
        List<ProjectPackage> projectPackageList = projectPackageMapper.selectProjectPackages( projectQueryPara );
        if ( para.getId() != null ) {
            if ( !CollectionUtils.isEmpty( projectPackageList ) ){
                ProjectPackage projectPackage = projectPackageMapper.selectById( para.getId() );
                if ( !para.getName().equals( projectPackage.getName() ) ){
                    throw new MyException( "包名重复，请重新输入！" );
                }
            }
            projectPackageMapper.updateById(para);
        } else {
            if ( !CollectionUtils.isEmpty( projectPackageList ) ){
                throw new MyException( "包名重复，请重新输入！" );
            }
            projectPackageMapper.insert(para);
        }
        return para.getProjectId();
    }

    @Override
    public String generateCode(CodeGenerateInput input) throws IOException {
        // 根据数据库ID获取数据库信息
        Database database = databaseMapper.selectById( input.getTableInfo().getDataBaseId() );
        TableConfig tableConfig = saveTableConfig( database, input.getTableInfo() );
        generate( database, input.getTableInfo(), input.getPackageConfig(), tableConfig );
        return zipCode();
    }

    /**
     * 保存该表可检索字段 -> 下次生成代码时做回显使用 -> 也可取消该功能，根据个人需求来 ~
     * @param database：拿到该数据库关联的项目ID
     * @param tableInfo: 拿到前端传过来的检索字段
     * @return
     */
    private TableConfig saveTableConfig(Database database, TableInfo tableInfo) {
        if (tableInfo.getQueryColumns() == null || tableInfo.getQueryColumns().length <= 0) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        int size = tableInfo.getQueryColumns().length;
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                sb.append(tableInfo.getQueryColumns()[i]);
            } else {
                sb.append(tableInfo.getQueryColumns()[i]).append(",");
            }
        }

        List<TableConfig> tableConfigList = tableConfigMapper.selectTableConfigs(TableConfigQueryPara.builder().projectId(database.getProjectId()).tableName(tableInfo.getTableName()).build());
        TableConfig tableConfig;
        if ( CollectionUtils.isEmpty(tableConfigList) ) {
            tableConfig = new TableConfig();
            tableConfig.setProjectId(database.getProjectId());
            tableConfig.setTableName(tableInfo.getTableName());
            tableConfig.setQueryColumns(sb.toString());
            tableConfigMapper.insert(tableConfig);
        } else {
            tableConfig = tableConfigList.get(0);
            tableConfig.setQueryColumns( sb.toString() );
            tableConfigMapper.updateAllColumnById( tableConfig );
        }
        return tableConfig;
    }

    /**
     * 调用代码生成器引擎生成代码 -> 可参考官网demo：https://mp.baomidou.com/guide/generator.html
     *
     * @param database：数据库信息
     * @param tableInfo：表信息
     * @param packageConfig：包配置信息
     * @param tableConfig：可检索字段
     */
    private void generate(Database database, TableInfo tableInfo, Map<String,String> packageConfig, TableConfig tableConfig) {
        List<ProjectTemplate> templateList = projectTemplateMapper.selectTemplates(TemplateQueryPara.builder().projectId(database.getProjectId()).build());
        // model -> 表注释
        String model = tableInfo.getComments().substring(tableInfo.getComments().indexOf("#") + 1);

        // 1、代码生成器
        MyGenerator mpg = new MyGenerator();

        // 2、全局配置
        GlobalConfig gc = new GlobalConfig();
        // 生成code所在位置
        gc.setOutputDir(Constants.PROJECT_ROOT_DIRECTORY + "/document/upload/code");
        // 是否覆盖同名文件，默认是false
        gc.setFileOverride(true);
        // 不需要ActiveRecord特性的请改为false
        gc.setActiveRecord(true);
        // XML 二级缓存
        gc.setEnableCache(false);
        // XML ResultMap
        gc.setBaseResultMap(true);
        // XML columList
        gc.setBaseColumnList(false);
        gc.setAuthor("zhengqing");
        gc.setOpen(false);
        // gc.setSwagger2(true); 实体属性 Swagger2 注解
        /*
        自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sDao");
        gc.setXmlName("%sDao");
        gc.setServiceName("MP%sService");
        gc.setServiceImplName("%sServiceDiy");
        gc.setControllerName("%sAction");
        */
        gc.setIdType(IdType.AUTO);
        mpg.setGlobalConfig(gc);

        // 3、数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        EnumDatabaseType enumDatabaseType = EnumDatabaseType.getEnum( database.getDbType() );
        switch (enumDatabaseType) {
            case MySQL:
                dsc.setDbType(DbType.MYSQL);
                break;
            case Oracle:
                dsc.setDbType(DbType.ORACLE);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + enumDatabaseType);
        }
        // com.mysql.jdbc.Driver
        dsc.setDriverName( database.getDriver() );
        dsc.setUsername( database.getUser() );
        dsc.setPassword( database.getPassword() );
        // jdbc:mysql://localhost:3306/ant?useUnicode=true&useSSL=false&characterEncoding=utf8
        dsc.setUrl( database.getUrl() );
        mpg.setDataSource(dsc);

        // 4、包配置【模块名，包名】
        mpg.setPackageInfo( packageConfig );

        // 5、策略配置
        StrategyConfig strategy = new StrategyConfig();
        String tablePrefix = "";
        if ( tableInfo.getTableName().contains("T_") ) {
            tablePrefix = tableInfo.getTableName().replace("T_", "");
            tablePrefix = "T_" + tablePrefix.substring( 0, tablePrefix.indexOf("_") );

        } else if (tableInfo.getTableName().contains("_")) {
            tablePrefix = tableInfo.getTableName();
            tablePrefix = tablePrefix.substring( 0, tablePrefix.indexOf("_") );
        }
        // 此处可以修改为您的表前缀  -> 下面代码目的：拿到表前缀并设置  ex:zq_demo -> zq
        strategy.setTablePrefix( tablePrefix );

        strategy.entityTableFieldAnnotationEnable(true);
        // 表名生成策略
        strategy.setNaming( NamingStrategy.underline_to_camel );
        // 需要生成的表
        strategy.setInclude( tableInfo.getTableName() );
        // 表名和字段名是否使用下划线命名
        strategy.setDbColumnUnderline(true);
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表
        // 自定义实体父类
        // strategy.setSuperEntityClass("com.baomidou.demo.TestEntity");
        // 自定义实体，公共字段
        // strategy.setSuperEntityColumns(new String[] { "test_id", "age" });
        // 自定义 mapper 父类
        // strategy.setSuperMapperClass("com.baomidou.demo.TestMapper");
        // 自定义 service 父类
        // strategy.setSuperServiceClass("com.baomidou.demo.TestService");
        // 自定义 service 实现类父类
        // strategy.setSuperServiceImplClass("com.baomidou.demo.TestServiceImpl");
        // 自定义 controller 父类
        // strategy.setSuperControllerClass("com.baomidou.demo.TestController");
        // 【实体】是否生成字段常量（默认 false）
        // public static final String ID = "test_id";
        // strategy.setEntityColumnConstant(true);
        // 【实体】是否为构建者模型（默认 false）
        // public User setName(String name) {this.name = name; return this;}
        // strategy.setEntityBuilderModel(true);
        mpg.setStrategy(strategy);


        //模板列表
        mpg.setTemplateList(templateList);
        //生成页面配置参数
        mpg.setMyTableConfig(tableConfig);
        // 6、执行生成
        mpg.execute();
    }

    /**
     * 采用hutool工具类进行打包文件
     *
     * @param :
     * @return: java.lang.String
     */
    private String zipCode(){
        // 将code目录以及其目录下的所有文件目录打包到/document/upload/目录下的code.zip文件中 true:表示带目录显示
        File zipFile = ZipUtil.zip(Constants.PROJECT_ROOT_DIRECTORY + "/document/upload/code", Constants.PROJECT_ROOT_DIRECTORY + "/document/upload/code.zip", true);
        // 删除目录 -> 保证下次生成代码的时候不会累计上次留下的代码
        FileUtils.DeleteFolder(Constants.PROJECT_ROOT_DIRECTORY + "/document/upload/code");
        // 1、采用腾讯oss对象储存下载
        return cosFileService.uploadFile("code.zip", zipFile);// "http://jxi-1251616562.cos.ap-chengdu.myqcloud.com/code.zip?sign=q-sign-algorithm%3Dsha1%26q-ak%3DAKIDoTv9B0wzA7rNgmJMr8B1d2ECwTmrjNo1%26q-sign-time%3D1561101804%3B1561104804%26q-key-time%3D1561101804%3B1561104804%26q-header-list%3D%26q-url-param-list%3D%26q-signature%3Db6bf47b4e6cbde71df6b064e289b0d00f6150823";
        // 2、采用fastdfs上传并返回地址下载文件
//        return FastDfsApiUtils.upload(Constants.PROJECT_ROOT_DIRECTORY + "/document/upload/code.zip", "zip");
    }

    /**
     * 下载生成好的代码 - 原生
     */
//    private String zipCode2() {
//        try {
//            String outZipFileName = "/document/upload/src.zip";
//            File zipFile = new File(outZipFileName);
//            if (!zipFile.exists()) {
//                zipFile.createNewFile();
//            }
//            ZipFileUtils zip = new ZipFileUtils();
//            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
//            String fileName = "/document/upload/code";
//            File ff = new File(fileName);
//            if (!ff.exists()) {
//                ff.mkdirs();
//            }
//            zip.zip(ff, zos, "");
//
//            zos.flush();
//            zos.close();
//
//            //删除目录
//            FileUtils.DeleteFolder(fileName);
//            return cosFileService.uploadFile("code.zip", zipFile);
////            return myProperties.getUpload().getStaticDomain() + outZipFileName;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
