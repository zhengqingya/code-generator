package com.zhengqing.modules.common.generator;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.toolkit.StringUtils;
import com.zhengqing.modules.code.entity.CodeProjectVelocityContext;
import com.zhengqing.modules.code.entity.Project;
import lombok.Data;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.apache.velocity.runtime.parser.ParseException;
import org.apache.velocity.runtime.parser.node.SimpleNode;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  <p> 自定义MyGenerator -> 原本AutoGenerator generator = new AutoGenerator(); </p>
 *
 * @description:
 * @author: zhengqing
 * @date: 2019/6/20 16:56
 */
@Data
public class MyGenerator extends MyAbstractGenerator {

    private static final Log logger = LogFactory.getLog(MyGenerator.class);

    /**
     * velocity引擎
     */
    private VelocityEngine engine;

    /**
     * 项目信息
     */
    private Project project;

    /**
     * 生成代码
     */
    public void execute() {
        logger.debug("==========================准备生成文件...==========================");
        // 初始化配置
        initConfig();
        // 创建输出文件路径
        mkdirs(config.getPathInfo());
        // 获取上下文
        Map<String, VelocityContext> ctxData = analyzeData(config);
        // 循环生成文件
        for (Map.Entry<String, VelocityContext> ctx : ctxData.entrySet()) {
            batchOutput(ctx.getKey(), ctx.getValue());
        }
        // 打开输出目录
        if (config.getGlobalConfig().isOpen()) {
            try {
                String osName = System.getProperty("os.name" );
                if (osName != null) {
                    if (osName.contains("Mac" )) {
                        Runtime.getRuntime().exec("open " + config.getGlobalConfig().getOutputDir());
                    } else if (osName.contains("Windows" )) {
                        Runtime.getRuntime().exec("cmd /c start " + config.getGlobalConfig().getOutputDir());
                    } else {
                        logger.debug("文件输出目录:" + config.getGlobalConfig().getOutputDir());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.debug("==========================文件生成完成！！！==========================");
    }

    /**
     * 分析数据
     *
     * @param config 总配置信息
     * @return 解析数据结果集
     */
    private Map<String, VelocityContext> analyzeData(MyConfigBuilder config) {
        List<MyTableInfo> tableList = config.getTableInfoList();
        Map<String, String> packageInfo = config.getPackageInfo();
        Map<String, VelocityContext> ctxData = new HashMap<String, VelocityContext>();
        String superEntityClass = getSuperClassName(config.getSuperEntityClass());
        String superMapperClass = getSuperClassName(config.getSuperMapperClass());
        String superServiceClass = getSuperClassName(config.getSuperServiceClass());
        String superServiceImplClass = getSuperClassName(config.getSuperServiceImplClass());
        String superControllerClass = getSuperClassName(config.getSuperControllerClass());
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        VelocityContext ctx;
        for (MyTableInfo tableInfo : tableList) {
            ctx = new VelocityContext();
            if (null != injectionConfig) {
                /**
                 * 注入自定义配置
                 */
                injectionConfig.initMap();
                ctx.put("cfg", injectionConfig.getMap());
            }
            /* ---------- 添加导入包 ---------- */
            if (config.getGlobalConfig().isActiveRecord()) {
                // 开启 ActiveRecord 模式
                tableInfo.setImportPackages("com.baomidou.mybatisplus.activerecord.Model");
            }
            if (tableInfo.isConvert()) {
                // 表注解
                tableInfo.setImportPackages("com.baomidou.mybatisplus.annotations.TableName");
            }
            if (StringUtils.isNotEmpty(config.getSuperEntityClass())) {
                // 父实体
                tableInfo.setImportPackages(config.getSuperEntityClass());
            } else {
                tableInfo.setImportPackages("java.io.Serializable");
            }


            // ------------- 模板中可用 替换数据源 ex:${key} -> 替换为 key对应的value值 TODO 后期模板较多需更灵活的话可考虑将map存入redis中，前端多态更新redis源以获取... ------------
            for (Map.Entry<String, String> entry : packageInfo.entrySet()) {
                ctx.put( entry.getKey(), entry.getValue() );
            }

            // package：存包的项目路径【mapper.xml -> com.zhengqing.modules.mapper.xml】  tableInfo：存包名【驼峰命名：mapper.xml -> CodeProjectMapperXml】 table:表的所有信息
            // 使用方式： ex：package.mapper.xml 可获取值 com.zhengqing.modules.mapper.xml
            ctx.put("package", packageInfo);
            ctx.put("table", tableInfo);
            ctx.put("tableInfo", tableInfo.getPackageInfo());
            ctx.put("author", config.getGlobalConfig().getAuthor());
            ctx.put("activeRecord", config.getGlobalConfig().isActiveRecord());
            ctx.put("date", date);
            ctx.put("enableCache", config.getGlobalConfig().isEnableCache());
            ctx.put("baseResultMap", config.getGlobalConfig().isBaseResultMap());
            ctx.put("baseColumnList", config.getGlobalConfig().isBaseColumnList());
            ctx.put("entity", tableInfo.getEntityName());
            ctx.put("entityPropertyName", tableInfo.getEntityPath());
//            ctx.put("formQueryPara", tableInfo.getFormQueryParaName());
            ctx.put("entityColumnConstant", config.getStrategyConfig().isEntityColumnConstant());
            ctx.put("entityBuilderModel", config.getStrategyConfig().isEntityBuilderModel());
            ctx.put("superEntityClass", superEntityClass);
            ctx.put("superMapperClassPackage", config.getSuperMapperClass());
            ctx.put("superMapperClass", superMapperClass);
            ctx.put("superServiceClassPackage", config.getSuperServiceClass());
            ctx.put("superServiceClass", superServiceClass);
            ctx.put("superServiceImplClassPackage", config.getSuperServiceImplClass());
            ctx.put("superServiceImplClass", superServiceImplClass);
            ctx.put("superControllerClassPackage", config.getSuperControllerClass());
            ctx.put("superControllerClass", superControllerClass);

            // 可检索字段列表
            List<TableField> queryFields = new ArrayList<>();
            if (myTableConfig != null && StringUtils.isNotEmpty(myTableConfig.getQueryColumns())) {
                String[] columns = myTableConfig.getQueryColumns().split(",");
                List<TableField> fields = tableInfo.getFields();
                if (CollectionUtils.isNotEmpty(fields)) {
                    fields.forEach(field -> {
                        for (String column : columns) {
                            if (field.getName().equalsIgnoreCase(column)) {
                                queryFields.add(field);
                            }
                        }
                    });
                }
            }
            ctx.put("queryFields", queryFields);

            ctxData.put(tableInfo.getEntityName(), ctx);
        }

        // ---------------- 将模板配置存入数据库提供给前端页面展示使用 ----------------
        Integer projectId = templateList.get(0).getProjectId();
        new CodeProjectVelocityContext().delete( new EntityWrapper().eq("project_id", projectId) );
        ctxData.values().forEach( e -> {
            for (Object key : e.getKeys()) {
                Object value = e.get((String) key);
                // 先删除 再插入
                CodeProjectVelocityContext velocityContext = new CodeProjectVelocityContext();
                velocityContext.setProjectId( projectId );
                velocityContext.setVelocity( (String) key );
                String valueStr = JSONObject.toJSONString(value);
                velocityContext.setContext( valueStr );
                velocityContext.insert();
            }
        });

        return ctxData;
    }

    /**
     * 获取类名
     *
     * @param classPath
     * @return
     */
    private String getSuperClassName(String classPath) {
        if (StringUtils.isEmpty(classPath)) {
            return null;
        }
        return classPath.substring(classPath.lastIndexOf(".") + 1);
    }

    /**
     * 处理输出目录
     *
     * @param pathInfo 路径信息
     */
    private void mkdirs(Map<String, String> pathInfo) {
        for (Map.Entry<String, String> entry : pathInfo.entrySet()) {
            File dir = new File(entry.getValue());
            if (!dir.exists()) {
                boolean result = dir.mkdirs();
                if (result) {
                    logger.debug("创建目录： [" + entry.getValue() + "]");
                }
            }
        }
    }

    /**
     * 合成上下文与模板
     *
     * @param context vm上下文
     */
    private void batchOutput(String entityName, VelocityContext context) {
        MyTableInfo tableInfo = (MyTableInfo) context.get("table");
        Map<String, String> pathInfo = config.getPathInfo();
        if (CollectionUtils.isNotEmpty(templateList)) {
            templateList.forEach(template -> {
                // TODO 由下面改为动态  注：文件后缀名需要前端模板处设置 然后保存到数据库中动态获取
                String outputPath = pathInfo.get( template.getPackageName() ) + File.separator + tableInfo.getPackageInfo().get( template.getPackageName() ) + template.getFileSuffix();
                String templateText = template.getContent();
//                EnumTemplateType enumTemplateType = EnumTemplateType.getEnum(template.getType());
//                String outputPath = null;
//                String templateText = template.getContent();
//                switch (enumTemplateType) {
//                    case ENTITY:
//                        outputPath = pathInfo.get(MyConstVal.ENTITY_PATH) + File.separator + tableInfo.getEntityName() + MyConstVal.JAVA_SUFFIX;
//                        break;
//                    case MAPPER:
//                        outputPath = pathInfo.get(MyConstVal.MAPPER_PATH) + File.separator + tableInfo.getMapperName() + MyConstVal.JAVA_SUFFIX;
//                        break;
//                    case MAPPER_XML:
//                        outputPath = pathInfo.get(MyConstVal.XML_PATH) + File.separator + tableInfo.getXmlName() + MyConstVal.XML_SUFFIX;
//                        break;
//                    case SERVICE:
//                        outputPath = pathInfo.get(MyConstVal.SERIVCE_PATH) + File.separator + tableInfo.getServiceName() + MyConstVal.JAVA_SUFFIX;
//                        break;
//                    case SERVICE_IMPL:
//                        outputPath = pathInfo.get(MyConstVal.SERVICEIMPL_PATH) + File.separator + tableInfo.getServiceImplName() + MyConstVal.JAVA_SUFFIX;
//                        break;
//                    case CONTROLLER:
//                        outputPath = pathInfo.get(MyConstVal.CONTROLLER_PATH) + File.separator + tableInfo.getControllerName() + MyConstVal.JAVA_SUFFIX;
//                        break;
//                    case VUE_LIST:
//                        outputPath = pathInfo.get(MyConstVal.VUE_LIST_PATH) + File.separator + "list" + MyConstVal.VUE_SUFFIX;
//                        break;
//                    case VUE_DETAIL:
//                        outputPath = pathInfo.get(MyConstVal.VUE_LIST_PATH) + File.separator + "form" + MyConstVal.VUE_SUFFIX;
//                        break;
//                    case VUE_API:
//                        outputPath = pathInfo.get(MyConstVal.VUE_API_PATH) + File.separator + tableInfo.getEntityPath() + MyConstVal.JS_SUFFIX;
//                        break;
//                    case QUERY_DTO:
//                        outputPath = pathInfo.get(MyConstVal.QUERY_DTO_PATH) + File.separator + tableInfo.getFormQueryParaName() + MyConstVal.JAVA_SUFFIX;
//                        break;
//                }
                try {
                    vmToFile(context, templateText, outputPath);
                } catch (Exception e) {
                    logger.error("无法创建文件，请检查配置信息！", e);
                }
            });
        }
    }

    /**
     * 将模板转化成为文件
     *
     * @param context      内容对象
     * @param templateText 模板内容
     * @param outputFile   文件生成的目录
     */
    private void vmToFile(VelocityContext context, String templateText, String outputFile) throws IOException, ParseException {
        if (StringUtils.isEmpty(templateText) || StringUtils.isEmpty(outputFile)) {
            return;
        }
        RuntimeServices runtimeServices = RuntimeSingleton.getRuntimeServices();
        StringReader reader = new StringReader(templateText);
        SimpleNode node = runtimeServices.parse(reader, templateText);
        Template template = new Template();
        template.setRuntimeServices(runtimeServices);
        template.setData(node);
        template.initDocument();

        File file = new File(outputFile);
        if (!file.getParentFile().exists()) {
            // 如果文件所在的目录不存在，则创建目录
            if (!file.getParentFile().mkdirs()) {
                logger.debug("创建文件所在的目录失败!");
                return;
            }
        }
        FileOutputStream fos = new FileOutputStream(outputFile);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos, MyConstVal.UTF8));
        template.merge(context, writer);
        writer.close();
        logger.debug("文件:" + outputFile);
    }

}
