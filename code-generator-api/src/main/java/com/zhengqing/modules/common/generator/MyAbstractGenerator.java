package com.zhengqing.modules.common.generator;

import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.zhengqing.modules.code.entity.ProjectTemplate;
import com.zhengqing.modules.code.entity.TableConfig;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 插件基类，用于属性配置 设计成抽象类主要是用于后期可扩展，共享参数配置。
 * </p>
 */
@Data
public abstract class MyAbstractGenerator {

    /**
     * 数据源配置
     */
    private DataSourceConfig dataSource;

    /**
     * 数据库表配置
     */
    private StrategyConfig strategy;

    /**
     * 包 相关配置  TODO 改用map封装动态包
     */
//    private PackageConfig packageInfo;
    private Map<String,String> packageInfo;

    /**
     * 项目模板列表
     */
    protected List<ProjectTemplate> templateList;

    /**
     * 项目中生成配置参数（如搜索条件的字段）
     */
    protected TableConfig myTableConfig;

    /**
     * 全局 相关配置
     */
    private GlobalConfig globalConfig;

    protected MyConfigBuilder config;

    protected InjectionConfig injectionConfig;

    /**
     * 初始化配置
     */
    protected void initConfig() {
        if (null == config) {
            config = new MyConfigBuilder(packageInfo, dataSource, strategy, templateList, globalConfig);
//            if (null != injectionConfig) {
//                injectionConfig.setConfig(config);
//            }
        }
    }

}
