/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : localhost:3306
 Source Schema         : code-generator

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 19/09/2019 10:44:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_code_bs_template
-- ----------------------------
DROP TABLE IF EXISTS `t_code_bs_template`;
CREATE TABLE `t_code_bs_template`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '初始模板ID',
  `type` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '模板类型 ex: entity、service、mapper...',
  `file_suffix` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '生成文件后缀名 .java',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '模板内容',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户ID',
  `gmt_create` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '初始模板表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_code_bs_template
-- ----------------------------
INSERT INTO `t_code_bs_template` VALUES (1, 'entity', '.java', 'package ${package.Entity};\n\n#foreach($pkg in ${table.importPackages})\nimport ${pkg};\n#end\nimport io.swagger.annotations.ApiModel;\nimport io.swagger.annotations.ApiModelProperty;\nimport lombok.Data;\n\n/**\n * <p>  ${table.comment} </p>\n *\n * @author: ${author}\n * @date: ${date}\n */\n#if(${table.convert})\n@Data\n@ApiModel(description = \"${table.comment}\")\n@TableName(\"${table.name}\")\n#end\n#if(${superEntityClass})\npublic class ${entity} extends ${superEntityClass}#if(${activeRecord})<${entity}>#end {\n#elseif(${activeRecord})\npublic class ${entity} extends Model<${entity}> {\n#else\npublic class ${entity} implements Serializable {\n#end\n\n    private static final long serialVersionUID = 1L;\n\n#foreach($field in ${table.fields})\n#if(${field.keyFlag})\n#set($keyPropertyName=${field.propertyName})\n#end\n#if(\"$!field.comment\" != \"\")\n    /**\n     * ${field.comment}\n     */\n	@ApiModelProperty(value = \"${field.comment}\")\n#end\n#if(${field.keyFlag})\n	@TableId(value=\"${field.name}\", type= IdType.AUTO)\n#else\n	@TableField(\"${field.name}\")\n#end\n	private ${field.propertyType} ${field.propertyName};\n#end\n\n#if(${entityColumnConstant})\n#foreach($field in ${table.fields})\n	public static final String ${field.name.toUpperCase()} = \"${field.name}\";\n\n#end\n#end\n#if(${activeRecord})\n	@Override\n	protected Serializable pkVal() {\n#if(${keyPropertyName})\n		return this.${keyPropertyName};\n#else\n		return this.id;\n#end\n	}\n\n#end\n}\n', 1, '2019-08-20 14:55:29', '2019-09-18 09:04:22');
INSERT INTO `t_code_bs_template` VALUES (2, ' mapper.xml', '.xml', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n<mapper namespace=\"${package.Mapper}.${table.mapperName}\">\n\n#if(${enableCache})\n	<!-- 开启二级缓存 -->\n	<cache type=\"org.mybatis.caches.ehcache.LoggingEhcache\"/>\n\n#end\n#if(${baseResultMap})\n	<!-- 通用查询映射结果 -->\n	<resultMap id=\"BaseResultMap\" type=\"${package.Entity}.${entity}\">\n#foreach($field in ${table.fields})\n#if(${field.keyFlag})##生成主键排在第一位\n		<id column=\"${field.name}\" property=\"${field.propertyName}\" />\n#end\n#end\n#foreach($field in ${table.fields})\n#if(!${field.keyFlag})##生成普通字段\n		<result column=\"${field.name}\" property=\"${field.propertyName}\" />\n#end\n#end\n	</resultMap>\n\n#end\n#if(${baseColumnList})\n    <!-- 通用查询结果列 -->\n    <sql id=\"Base_Column_List\">\n        ${table.fieldNames}\n    </sql>\n\n#end\n\n    <select id=\"select${entity}s\" resultMap=\"BaseResultMap\">\n        SELECT\n             *\n        FROM ${table.name}\n        WHERE\n             1 = 1\n        <if test=\"filter.id!=null and filter.id!=\'\'\">\n            AND	${entity}_ID= #{filter.id}\n        </if>\n        ORDER BY ${entity}_ID DESC\n    </select>\n\n</mapper>\n', 1, '2019-08-20 14:55:10', '2019-09-18 09:04:08');
INSERT INTO `t_code_bs_template` VALUES (3, 'mapper', '.java', 'package ${package.Mapper};\n\nimport ${package.Entity}.${entity};\nimport ${package.QueryPara}.${formQueryPara};\nimport ${superMapperClassPackage};\nimport com.baomidou.mybatisplus.plugins.pagination.Pagination;\nimport org.apache.ibatis.annotations.Param;\n\nimport java.util.List;\n\n/**\n * <p> ${table.comment} Mapper 接口 </p>\n *\n * @author : zhengqing\n * @date : ${date}\n */\npublic interface ${table.mapperName} extends ${superMapperClass}<${entity}> {\n\n    /**\n     * 列表分页\n     *\n     * @param page\n     * @param filter\n     * @return\n     */\n    List<${entity}> select${entity}s(Pagination page, @Param(\"filter\") ${formQueryPara} filter);\n\n    /**\n     * 列表\n     *\n     * @param filter\n     * @return\n     */\n    List<${entity}> select${entity}s(@Param(\"filter\") ${formQueryPara} filter);\n}', 1, '2019-08-20 14:54:39', '2019-09-18 09:03:47');
INSERT INTO `t_code_bs_template` VALUES (4, ' service', '.java', 'package ${package.Service};\n\nimport com.baomidou.mybatisplus.plugins.Page;\nimport ${superServiceClassPackage};\nimport ${package.Entity}.${entity};\nimport ${package.QueryPara}.${formQueryPara};\n\nimport java.util.List;\n\n/**\n * <p>  ${table.comment} 服务类 </p>\n *\n * @author: ${author}\n * @date: ${date}\n */\npublic interface ${table.serviceName} extends ${superServiceClass}<${entity}> {\n\n    /**\n     * ${table.comment}列表分页\n     *\n     * @param page\n     * @param filter\n     * @return\n     */\n    void listPage(Page<${entity}> page, ${formQueryPara} filter);\n\n    /**\n     * 保存${table.comment}\n     *\n     * @param input\n     */\n    Integer save(${entity} input);\n\n    /**\n     * ${table.comment}列表\n     *\n     * @param filter\n     * @return\n     */\n    List<${entity}> list(${formQueryPara} filter);\n}\n', 1, '2019-08-20 14:54:14', '2019-09-18 09:03:31');
INSERT INTO `t_code_bs_template` VALUES (5, ' service.impl', '.java', 'package ${package.ServiceImpl};\n\nimport ${package.Entity}.${entity};\nimport ${package.QueryPara}.${formQueryPara};\nimport ${package.Mapper}.${table.mapperName};\nimport ${package.Service}.${table.serviceName};\nimport ${superServiceImplClassPackage};\nimport com.baomidou.mybatisplus.plugins.Page;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.stereotype.Service;\nimport org.springframework.transaction.annotation.Transactional;\n\nimport java.util.List;\n\n/**\n * <p> ${table.comment} 服务实现类 </p>\n *\n * @author: ${author}\n * @date: ${date}\n */\n@Service\n@Transactional\npublic class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {\n\n    @Autowired\n    ${table.mapperName} ${entityPropertyName}Mapper;\n\n    @Override\n    public void listPage(Page<${entity}> page, ${formQueryPara} filter) {\n        page.setRecords(${entityPropertyName}Mapper.select${entity}s(page, filter));\n    }\n\n    @Override\n    public List<${entity}> list(${formQueryPara} filter) {\n        return ${entityPropertyName}Mapper.select${entity}s(filter);\n    }\n\n    @Override\n    public Integer save(${entity} para) {\n        if (para.get${entity}Id()!=null) {\n            ${entityPropertyName}Mapper.updateById(para);\n        } else {\n            ${entityPropertyName}Mapper.insert(para);\n        }\n        return para.get${entity}Id();\n    }\n}\n', 1, '2019-08-20 14:53:45', '2019-09-18 09:03:18');
INSERT INTO `t_code_bs_template` VALUES (6, 'api', '.java', 'package ${package.Controller};\n\nimport com.zhengqing.modules.common.api.BaseController;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.web.bind.annotation.*;\n\nimport java.util.List;\n\nimport com.baomidou.mybatisplus.plugins.Page;\nimport com.zhengqing.modules.common.dto.output.ApiResult;\nimport ${package.Entity}.${entity};\nimport ${package.QueryPara}.${formQueryPara};\nimport ${package.Service}.${table.serviceName};\nimport io.swagger.annotations.Api;\nimport io.swagger.annotations.ApiOperation;\n\n\n/**\n * <p> ${table.comment} 接口 </p>\n *\n * @author: zhengqing\n * @description:\n * @date: ${date}\n *\n */\n@RestController\n@RequestMapping(\"/api#if(${package.ModuleName})/${package.ModuleName}#end/${table.entityPath}\")\n@Api(description = \"${table.comment}接口\")\npublic class ${table.controllerName} extends BaseController {\n\n    @Autowired\n    ${table.serviceName} ${entityPropertyName}Service;\n\n    @PostMapping(value = \"/listPage\", produces = \"application/json;charset=utf-8\")\n    @ApiOperation(value = \"获取${table.comment}列表分页\", httpMethod = \"POST\", response = ApiResult.class)\n    public ApiResult listPage(@RequestBody ${formQueryPara} filter) {\n        Page<${entity}> page = new Page<>(filter.getPage(),filter.getLimit());\n        ${entityPropertyName}Service.listPage(page, filter);\n        return ApiResult.ok(\"获取${table.comment}列表分页成功\", page);\n    }\n\n    @PostMapping(value = \"/list\", produces = \"application/json;charset=utf-8\")\n    @ApiOperation(value = \"获取${table.comment}列表\", httpMethod = \"POST\", response = ApiResult.class)\n    public ApiResult list(@RequestBody ${formQueryPara} filter) {\n        List<${entity}> result = ${entityPropertyName}Service.list(filter);\n        return ApiResult.ok(\"获取${table.comment}列表成功\",result);\n    }\n\n    @PostMapping(value = \"/save\", produces = \"application/json;charset=utf-8\")\n    @ApiOperation(value = \"保存${table.comment}\", httpMethod = \"POST\", response = ApiResult.class)\n    public ApiResult save(@RequestBody ${entity} input) {\n        Integer id = ${entityPropertyName}Service.save(input);\n        return ApiResult.ok(\"保存${table.comment}成功\", id);\n    }\n\n    @PostMapping(value = \"/delete\", produces = \"application/json;charset=utf-8\")\n    @ApiOperation(value = \"删除${table.comment}\", httpMethod = \"POST\", response = ApiResult.class)\n    public ApiResult delete(@RequestBody ${formQueryPara} input) {\n        ${entityPropertyName}Service.deleteById(input.getId());\n        return ApiResult.ok(\"删除${table.comment}成功\");\n    }\n\n    @PostMapping(value = \"/getById\", produces = \"application/json;charset=utf-8\")\n    @ApiOperation(value = \"获取${table.comment}信息\", httpMethod = \"POST\", response = ApiResult.class)\n    public ApiResult getById(@RequestBody ${formQueryPara} input) {\n        ${entity} entity = ${entityPropertyName}Service.selectById(input.getId());\n        return ApiResult.ok(\"获取${table.comment}信息成功\", entity);\n    }\n\n}', 1, '2019-08-20 14:53:20', '2019-09-18 09:02:59');
INSERT INTO `t_code_bs_template` VALUES (7, 'input', '.java', 'package ${package.QueryPara};\n\nimport com.zhengqing.modules.common.dto.input.BasePageQuery;\nimport io.swagger.annotations.ApiModel;\nimport io.swagger.annotations.ApiModelProperty;\nimport lombok.Data;\n\n/**\n * ${table.comment}查询参数\n *\n * @author: zhengqing\n * @description:\n * @date: ${date}\n */\n@Data\n@ApiModel(description = \"${table.comment}查询参数\")\npublic class ${formQueryPara} extends BasePageQuery{\n    @ApiModelProperty(value = \"id\")\n    private int id;\n}\n', 1, '2019-08-20 14:52:44', '2019-09-18 09:02:41');
INSERT INTO `t_code_bs_template` VALUES (8, 'vue', '.vue', '<template>\n  <div class=\"app-container\">\n    <cus-wraper>\n      <cus-filter-wraper>\n        #if(${queryFields})\n        #foreach($field in ${queryFields})\n        <el-input v-model=\"listQuery.${field.propertyName}\" placeholder=\"请输入${field.comment}\" style=\"width:200px\" clearable></el-input>\n        #end\n        <el-button type=\"primary\" @click=\"getList\" icon=\"el-icon-search\" v-waves>查询</el-button>\n        <el-button type=\"primary\" @click=\"handleCreate\" icon=\"el-icon-plus\" v-waves>添加</el-button>        \n        #end\n      </cus-filter-wraper>\n      <div class=\"table-container\">\n        <el-table v-loading=\"listLoading\" :data=\"list\" size=\"mini\" element-loading-text=\"Loading\" fit border highlight-current-row>\n	        #foreach($field in ${table.fields})\n	        #if(${field.propertyType.equals(\"Date\")})\n	        <el-table-column label=\"${field.comment}\" align=\"center\">\n	            <template slot-scope=\"scope\">\n	                <span>{{scope.row.${field.propertyName}|dateTimeFilter}}</span>\n	            </template>\n	        </el-table-column>\n	        #else\n	        <el-table-column label=\"${field.comment}\" prop=\"${field.propertyName}\" align=\"center\"></el-table-column>\n	        #end\n	        #end\n          <el-table-column align=\"center\" label=\"操作\">\n            <template slot-scope=\"scope\">\n              <el-button size=\"mini\" type=\"primary\" @click=\"handleUpdate(scope.row)\" icon=\"el-icon-edit\" plain v-waves>编辑</el-button>\n              <cus-del-btn @ok=\"handleDelete(scope.row)\"></cus-del-btn>\n            </template>\n          </el-table-column>\n        </el-table>\n        <!-- 分页 -->\n        <cus-pagination v-show=\"total>0\" :total=\"total\" :page.sync=\"listQuery.page\" :limit.sync=\"listQuery.limit\" @pagination=\"getList\"/>\n      </div>\n\n      <el-dialog :title=\"titleMap[dialogStatus]\" :visible.sync=\"dialogVisible\" width=\"40%\" @close=\"handleDialogClose\">\n        <el-form ref=\"dataForm\" :model=\"form\" :rules=\"rules\" label-width=\"100px\" class=\"demo-ruleForm\">\n        #foreach($field in ${table.fields})\n        <el-form-item label=\"${field.comment}:\" prop=\"${field.propertyName}\">\n            <el-input v-model=\"form.${field.propertyName}\"></el-input>\n        </el-form-item>\n        #end\n        </el-form>\n        <span slot=\"footer\" class=\"dialog-footer\">\n          <el-button @click=\"dialogVisible = false\" v-waves>取 消</el-button>\n          <el-button type=\"primary\" @click=\"submitForm\" v-waves>确 定</el-button>\n        </span>\n      </el-dialog>\n    </cus-wraper>\n  </div>\n</template>\n\n<script>\n import { get${entity}Page, save${entity}, delete${entity} } from \"@/api/${package.ModuleName}/${entityPropertyName}\";\n\nexport default {\n  data() {\n    return {\n      dialogVisible: false,\n      list: [],\n      listLoading: true,\n      total: 0,\n      listQuery: {\n        page: 1,\n        limit: 10,\n	    #if(${queryFields})\n	    #foreach($field in ${queryFields})\n	    ${field.propertyName}:undefined,\n	    #end\n	    #end\n      },\n      input: \'\',\n      form: {\n	     #foreach($field in ${table.fields})\n	     ${field.propertyName}: undefined, //${field.comment}\n	     #end\n      },\n     dialogStatus: \"\",\n     titleMap: {\n        update: \"编辑\",\n        create: \"创建\"\n     },\n     rules: {\n         name: [\n            { required: true, message: \'请输入项目名称\', trigger: \'blur\' }\n         ]\n      }\n    }\n  },\n  created() {\n    this.getList();\n  },\n  methods: {\n    getList() {\n      this.listLoading = true;\n      get${entity}Page(this.listQuery).then(response => {\n        this.list = response.data.records;\n    	this.total = response.data.total;\n    	this.listLoading = false;\n		});\n    },\n    handleCreate() {\n        this.resetForm();\n        this.dialogStatus = \"create\";\n        this.dialogVisible = true;\n    },\n    handleUpdate(row) {\n        this.form = Object.assign({}, row);\n        this.dialogStatus = \"update\";\n        this.dialogVisible = true;\n    },\n    handleDelete(row) {\n      #foreach($field in ${table.fields})\n		#if(${field.keyFlag})\n		 let id = row.${field.propertyName};\n		#end\n	  #end\n      delete${entity}(id).then(response => {\n            if (response.code == 200) {\n            this.getList();\n            this.submitOk(response.message);\n        } else {\n            this.submitFail(response.message);\n        }\n    });\n    },\n    submitForm() {\n    this.#[[$refs]]#.[\'dataForm\'].validate(valid => {\n        if (valid) {\n            save${entity}(this.form).then(response => {\n                if (response.code == 200) {\n                    this.getList();\n                    this.submitOk(response.message);\n                    this.dialogVisible = false;\n                } else {\n                     this.submitFail(response.message);\n                }\n        }).catch(err => { console.log(err);  });\n            }\n        });\n    },\n    resetForm() {\n        this.form = {\n            #foreach($field in ${table.fields})\n                ${field.propertyName}: undefined, //${field.comment}\n            #end\n        };\n    },\n    // 监听dialog关闭时的处理事件\n    handleDialogClose(){\n        if(this.$refs[\'dataForm\']){\n             this.$refs[\'dataForm\'].clearValidate(); // 清除整个表单的校验\n        }\n    }\n  }\n}\n</script>', 1, '2019-08-20 14:52:19', '2019-09-18 09:02:12');
INSERT INTO `t_code_bs_template` VALUES (9, 'js', '.js', 'import request from \'@/utils/request\';\n\nexport function get${entity}Page(query) {\n    return request({\n        url: \'/api/${package.ModuleName}/${entityPropertyName}/listPage\',\n        method: \'post\',\n        data: query\n    });\n}\n\nexport function save${entity}(form) {\n    return request({\n        url: \'/api/${package.ModuleName}/${entityPropertyName}/save\',\n        method: \'post\',\n        data: form\n    });\n}\n\nexport function delete${entity}(id) {\n    return request({\n        url: \'/api/${package.ModuleName}/${entityPropertyName}/delete\',\n        method: \'post\',\n        data: { \'id\': id }\n    });\n}\n\nexport function get${entity}ById(id) {\n    return request({\n        url: \'/api/${package.ModuleName}/${entityPropertyName}/getById\',\n        method: \'post\',\n        data: { \'id\': id }\n    });\n}', 1, '2019-08-17 18:13:10', '2019-09-17 17:57:43');

-- ----------------------------
-- Table structure for t_code_database
-- ----------------------------
DROP TABLE IF EXISTS `t_code_database`;
CREATE TABLE `t_code_database`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '数据库ID',
  `project_id` int(11) DEFAULT NULL COMMENT '所属项目ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据库名称',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '数据库连接',
  `user` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用户名',
  `password` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `db_schema` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'SCHEMA',
  `db_type` tinyint(2) DEFAULT 1 COMMENT '数据库类型 1:mysql  2:oracle',
  `driver` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '驱动程序',
  `gmt_create` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '数据库信息表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_code_database
-- ----------------------------
INSERT INTO `t_code_database` VALUES (2, 1, 'hello', 'jdbc:mysql://www.zhengqing520.com:3306/hello', 'hello', 'root', 'hello', 1, 'com.mysql.jdbc.Driver', '2019-09-13 03:01:15', '2019-09-13 16:49:46');

-- ----------------------------
-- Table structure for t_code_project
-- ----------------------------
DROP TABLE IF EXISTS `t_code_project`;
CREATE TABLE `t_code_project`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '项目ID',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT '' COMMENT '项目名称',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户ID',
  `status` bit(1) DEFAULT NULL COMMENT '状态：是否启用  0:停用  1:启用',
  `gmt_create` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成器 - 项目管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_code_project
-- ----------------------------
INSERT INTO `t_code_project` VALUES (1, '项目demo', 1, b'1', '2019-09-10 13:56:35', '2019-09-12 19:25:28');

-- ----------------------------
-- Table structure for t_code_project_package
-- ----------------------------
DROP TABLE IF EXISTS `t_code_project_package`;
CREATE TABLE `t_code_project_package`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '包ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '包名',
  `parent_id` int(11) DEFAULT NULL COMMENT '父包ID',
  `project_id` int(11) DEFAULT NULL COMMENT '关联项目ID',
  `gmt_create` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 39 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成器 - 项目包管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_code_project_package
-- ----------------------------
INSERT INTO `t_code_project_package` VALUES (1, 'com.zhengqing.modules', 0, 1, '2019-09-10 13:56:35', '2019-09-12 19:05:12');
INSERT INTO `t_code_project_package` VALUES (2, 'common', 1, 1, '2019-09-10 13:56:35', '2019-09-10 13:56:35');
INSERT INTO `t_code_project_package` VALUES (3, 'entity', 1, 1, '2019-09-10 13:56:35', '2019-09-10 13:56:35');
INSERT INTO `t_code_project_package` VALUES (4, 'service', 1, 1, '2019-09-10 13:56:35', '2019-09-10 13:56:35');
INSERT INTO `t_code_project_package` VALUES (5, 'service.impl', 1, 1, '2019-09-10 13:56:35', '2019-09-17 12:41:00');
INSERT INTO `t_code_project_package` VALUES (6, 'mapper', 1, 1, '2019-09-10 13:56:35', '2019-09-10 13:56:35');
INSERT INTO `t_code_project_package` VALUES (7, 'mapper.xml', 1, 1, '2019-09-10 13:56:35', '2019-09-17 12:41:07');
INSERT INTO `t_code_project_package` VALUES (18, 'api', 1, 1, '2019-09-12 19:08:07', '2019-09-12 19:08:07');
INSERT INTO `t_code_project_package` VALUES (19, 'js', 1, 1, '2019-09-12 21:20:04', '2019-09-12 21:21:18');
INSERT INTO `t_code_project_package` VALUES (20, 'vue', 1, 1, '2019-09-12 21:21:27', '2019-09-12 21:21:27');
INSERT INTO `t_code_project_package` VALUES (21, 'dto', 1, 1, '2019-09-12 21:58:47', '2019-09-12 21:58:47');
INSERT INTO `t_code_project_package` VALUES (22, 'input', 21, 1, '2019-09-12 21:58:58', '2019-09-12 21:58:58');

-- ----------------------------
-- Table structure for t_code_project_template
-- ----------------------------
DROP TABLE IF EXISTS `t_code_project_template`;
CREATE TABLE `t_code_project_template`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '模板ID',
  `project_id` int(11) DEFAULT NULL COMMENT '项目ID',
  `type` int(11) DEFAULT NULL COMMENT '模板类型 - 对应包ID',
  `file_suffix` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '生成文件后缀名 .java',
  `content` text CHARACTER SET utf8 COLLATE utf8_general_ci COMMENT '模板内容',
  `gmt_create` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目代码模板表' ROW_FORMAT = Compact;

-- ----------------------------
-- Records of t_code_project_template
-- ----------------------------
INSERT INTO `t_code_project_template` VALUES (19, 1, 3, '.java', 'package ${package.entity};\n\n#foreach($pkg in ${table.importPackages})\nimport ${pkg};\n#end\nimport io.swagger.annotations.ApiModel;\nimport io.swagger.annotations.ApiModelProperty;\nimport lombok.Data;\n\n/**\n * <p>  ${table.comment} </p>\n *\n * @author: ${author}\n * @date: ${date}\n */\n#if(${table.convert})\n@Data\n@ApiModel(description = \"${table.comment}\")\n@TableName(\"${table.name}\")\n#end\n#if(${superEntityClass})\npublic class ${entity} extends ${superEntityClass}#if(${activeRecord})<${entity}>#end {\n#elseif(${activeRecord})\npublic class ${entity} extends Model<${entity}> {\n#else\npublic class ${entity} implements Serializable {\n#end\n\n    private static final long serialVersionUID = 1L;\n\n#foreach($field in ${table.fields})\n#if(${field.keyFlag})\n#set($keyPropertyName=${field.propertyName})\n#end\n#if(\"$!field.comment\" != \"\")\n    /**\n     * ${field.comment}\n     */\n	@ApiModelProperty(value = \"${field.comment}\")\n#end\n#if(${field.keyFlag})\n	@TableId(value=\"${field.name}\", type= IdType.AUTO)\n#else\n	@TableField(\"${field.name}\")\n#end\n	private ${field.propertyType} ${field.propertyName};\n#end\n\n#if(${entityColumnConstant})\n#foreach($field in ${table.fields})\n	public static final String ${field.name.toUpperCase()} = \"${field.name}\";\n\n#end\n#end\n#if(${activeRecord})\n	@Override\n	protected Serializable pkVal() {\n#if(${keyPropertyName})\n		return this.${keyPropertyName};\n#else\n		return this.id;\n#end\n	}\n\n#end\n}\n', '2019-09-11 21:40:06', '2019-09-18 09:16:01');
INSERT INTO `t_code_project_template` VALUES (20, 1, 7, '.xml', '<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n<mapper namespace=\"${package.mapper}.${tableInfo.mapper}\">\n\n#if(${enableCache})\n	<!-- 开启二级缓存 -->\n	<cache type=\"org.mybatis.caches.ehcache.LoggingEhcache\"/>\n\n#end\n#if(${baseResultMap})\n	<!-- 通用查询映射结果 -->\n	<resultMap id=\"BaseResultMap\" type=\"${package.entity}.${entity}\">\n#foreach($field in ${table.fields})\n#if(${field.keyFlag})##生成主键排在第一位\n		<id column=\"${field.name}\" property=\"${field.propertyName}\" />\n#end\n#end\n#foreach($field in ${table.fields})\n#if(!${field.keyFlag})##生成普通字段\n		<result column=\"${field.name}\" property=\"${field.propertyName}\" />\n#end\n#end\n	</resultMap>\n\n#end\n#if(${baseColumnList})\n    <!-- 通用查询结果列 -->\n    <sql id=\"Base_Column_List\">\n        ${table.fieldNames}\n    </sql>\n\n#end\n\n  <!--    <resultMap id=\"ResultMap\" type=\"com.zhengqing.modules.system.dto.output.RoleView\" extends=\"BaseResultMap\"></resultMap>-->\n\n    <select id=\"select${entity}s\" resultMap=\"BaseResultMap\">\n        SELECT\n             *\n        FROM ${table.name}\n        WHERE\n             1 = 1\n        <if test=\"filter.id!=null and filter.id!=\'\'\">\n            AND id = #{filter.id}\n        </if>\n        ORDER BY id DESC\n    </select>\n\n</mapper>\n', '2019-09-11 21:40:06', '2019-09-18 09:23:40');
INSERT INTO `t_code_project_template` VALUES (21, 1, 6, '.java', 'package ${package.mapper};\n\nimport ${package.entity}.${entity};\nimport ${package.input}.${tableInfo.input};\nimport ${superMapperClassPackage};\nimport com.baomidou.mybatisplus.plugins.pagination.Pagination;\nimport org.apache.ibatis.annotations.Param;\n\nimport java.util.List;\n\n/**\n * <p> ${table.comment} Mapper 接口 </p>\n *\n * @author : zhengqing\n * @date : ${date}\n */\npublic interface ${tableInfo.mapper} extends ${superMapperClass}<${entity}> {\n\n    /**\n     * 列表分页\n     *\n     * @param page\n     * @param filter\n     * @return\n     */\n    List<${entity}> select${entity}s(Pagination page, @Param(\"filter\") ${tableInfo.input} filter);\n\n    /**\n     * 列表\n     *\n     * @param filter\n     * @return\n     */\n    List<${entity}> select${entity}s(@Param(\"filter\") ${tableInfo.input} filter);\n\n}', '2019-09-11 21:40:06', '2019-09-18 10:57:06');
INSERT INTO `t_code_project_template` VALUES (22, 1, 4, '.java', 'package ${package.service};\n\nimport com.baomidou.mybatisplus.plugins.Page;\nimport ${superServiceClassPackage};\nimport ${package.entity}.${entity};\nimport ${package.input}.${tableInfo.input};\n\nimport java.util.List;\n\n/**\n * <p>  ${table.comment} 服务类 </p>\n *\n * @author: ${author}\n * @date: ${date}\n */\npublic interface ${tableInfo.service} extends ${superServiceClass}<${entity}> {\n\n    /**\n     * ${table.comment}列表分页\n     *\n     * @param page\n     * @param para\n     * @return\n     */\n    void listPage(Page<${entity}> page, ${tableInfo.input} para);\n\n    /**\n     * 保存${table.comment}\n     *\n     * @param input\n     */\n    Integer save(${entity} input);\n\n    /**\n     * ${table.comment}列表\n     *\n     * @param para\n     * @return\n     */\n    List<${entity}> list(${tableInfo.input} para);\n\n}\n', '2019-09-11 21:40:06', '2019-09-18 09:39:08');
INSERT INTO `t_code_project_template` VALUES (23, 1, 5, '.java', 'package ${service}.impl.${tableInfo.service}Impl;\n\nimport ${package.entity}.${entity};\nimport ${package.input}.${tableInfo.input};\nimport ${package.mapper}.${tableInfo.mapper};\nimport ${package.service}.${tableInfo.service};\nimport ${superServiceImplClassPackage};\nimport com.baomidou.mybatisplus.plugins.Page;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.stereotype.Service;\nimport org.springframework.transaction.annotation.Transactional;\n\nimport java.util.List;\n\n/**\n * <p> ${table.comment} 服务实现类 </p>\n *\n * @author: ${author}\n * @date: ${date}\n */\n@Service\n@Transactional\npublic class ${tableInfo.service}Impl extends ${superServiceImplClass}<${tableInfo.mapper}, ${entity}> implements ${tableInfo.service} {\n\n    @Autowired\n    ${tableInfo.mapper} ${entityPropertyName}Mapper;\n\n    @Override\n    public void listPage(Page<${entity}> page, ${tableInfo.input} para) {\n        page.setRecords(${entityPropertyName}Mapper.select${entity}s(page, para));\n    }\n\n    @Override\n    public List<${entity}> list(${tableInfo.input} para) {\n        return ${entityPropertyName}Mapper.select${entity}s(para);\n    }\n\n    @Override\n    public Integer save(${entity} para) {\n        if (para.getId()!=null) {\n            ${entityPropertyName}Mapper.updateById(para);\n        } else {\n            ${entityPropertyName}Mapper.insert(para);\n        }\n        return para.getId();\n    }\n}\n', '2019-09-11 21:40:06', '2019-09-18 10:56:23');
INSERT INTO `t_code_project_template` VALUES (24, 1, 18, '.java', 'package ${package.api};\n\nimport com.zhengqing.modules.common.api.BaseController;\nimport org.springframework.beans.factory.annotation.Autowired;\nimport org.springframework.web.bind.annotation.*;\n\nimport java.util.List;\n\nimport com.baomidou.mybatisplus.plugins.Page;\nimport com.zhengqing.modules.common.dto.output.ApiResult;\nimport ${package.entity}.${entity};\nimport ${package.input}.${tableInfo.input};\nimport ${package.service}.${tableInfo.service};\nimport io.swagger.annotations.Api;\nimport io.swagger.annotations.ApiOperation;\n\n\n/**\n * <p> ${table.comment} 接口 </p>\n *\n * @author: zhengqing\n * @description:\n * @date: ${date}\n *\n */\n@RestController\n@RequestMapping(\"/api#if(${package.moduleName})/${package.moduleName}#end/${table.entityPath}\")\n@Api(description = \"${table.comment}接口\")\npublic class ${tableInfo.api} extends BaseController {\n\n    @Autowired\n    ${tableInfo.service} ${entityPropertyName}Service;\n\n    @PostMapping(value = \"/listPage\", produces = \"application/json;charset=utf-8\")\n    @ApiOperation(value = \"获取${table.comment}列表分页\", httpMethod = \"POST\", response = ApiResult.class)\n    public ApiResult listPage(@RequestBody ${tableInfo.input} filter) {\n        Page<${entity}> page = new Page<>(filter.getPage(),filter.getLimit());\n        ${entityPropertyName}Service.listPage(page, filter);\n        return ApiResult.ok(\"获取${table.comment}列表分页成功\", page);\n    }\n\n    @PostMapping(value = \"/list\", produces = \"application/json;charset=utf-8\")\n    @ApiOperation(value = \"获取${table.comment}列表\", httpMethod = \"POST\", response = ApiResult.class)\n    public ApiResult list(@RequestBody ${tableInfo.input} filter) {\n        List<${entity}> result = ${entityPropertyName}Service.list(filter);\n        return ApiResult.ok(\"获取${table.comment}列表成功\",result);\n    }\n\n    @PostMapping(value = \"/saveOrUpdate\", produces = \"application/json;charset=utf-8\")\n    @ApiOperation(value = \"保存或更新${table.comment}\", httpMethod = \"POST\", response = ApiResult.class)\n    public ApiResult saveOrUpdate(@RequestBody ${entity} input) {\n        Integer id = ${entityPropertyName}Service.save(input);\n        return ApiResult.ok(\"保存${table.comment}成功\", id);\n    }\n\n    @PostMapping(value = \"/delete\", produces = \"application/json;charset=utf-8\")\n    @ApiOperation(value = \"删除${table.comment}\", httpMethod = \"POST\", response = ApiResult.class)\n    public ApiResult delete(@RequestBody ${tableInfo.input} input) {\n        ${entityPropertyName}Service.deleteById(input.getId());\n        return ApiResult.ok(\"删除${table.comment}成功\");\n    }\n\n    @PostMapping(value = \"/detail\", produces = \"application/json;charset=utf-8\")\n    @ApiOperation(value = \"根据ID获取${table.comment}信息\", httpMethod = \"POST\", response = ApiResult.class)\n    public ApiResult detail(@RequestBody ${tableInfo.input} input) {\n        ${entity} entity = ${entityPropertyName}Service.selectById(input.getId());\n        return ApiResult.ok(\"根据ID获取${table.comment}信息成功\", entity);\n    }\n\n}', '2019-09-11 21:40:06', '2019-09-18 09:20:46');
INSERT INTO `t_code_project_template` VALUES (25, 1, 22, '.java', 'package ${package.input};\n\nimport com.zhengqing.modules.common.dto.input.BasePageQuery;\nimport io.swagger.annotations.ApiModel;\nimport io.swagger.annotations.ApiModelProperty;\nimport lombok.Data;\n\n/**\n * ${table.comment}查询参数\n *\n * @author: zhengqing\n * @description:\n * @date: ${date}\n */\n@Data\n@ApiModel(description = \"${table.comment}查询参数\")\npublic class ${tableInfo.input} extends BasePageQuery{\n    @ApiModelProperty(value = \"id\")\n    private Integer id;\n}\n', '2019-09-11 21:40:06', '2019-09-18 09:09:56');
INSERT INTO `t_code_project_template` VALUES (26, 1, 20, '.vue', '<template>\n  <div class=\"app-container\">\n    <cus-wraper>\n      <cus-filter-wraper>\n        #if(${queryFields})\n        #foreach($field in ${queryFields})\n        <el-input v-model=\"listQuery.${field.propertyName}\" placeholder=\"请输入${field.comment}\" style=\"width:200px\" clearable></el-input>\n        #end\n        <el-button type=\"primary\" @click=\"getList\" icon=\"el-icon-search\" v-waves>查询</el-button>\n        <el-button type=\"primary\" @click=\"handleCreate\" icon=\"el-icon-plus\" v-waves>添加</el-button>        \n        #end\n      </cus-filter-wraper>\n      <div class=\"table-container\">\n        <el-table v-loading=\"listLoading\" :data=\"list\" size=\"mini\" element-loading-text=\"Loading\" fit border highlight-current-row>\n	        #foreach($field in ${table.fields})\n	        #if(${field.propertyType.equals(\"Date\")})\n	        <el-table-column label=\"${field.comment}\" align=\"center\">\n	            <template slot-scope=\"scope\">\n	                <span>{{scope.row.${field.propertyName}|dateTimeFilter}}</span>\n	            </template>\n	        </el-table-column>\n	        #else\n	        <el-table-column label=\"${field.comment}\" prop=\"${field.propertyName}\" align=\"center\"></el-table-column>\n	        #end\n	        #end\n          <el-table-column align=\"center\" label=\"操作\">\n            <template slot-scope=\"scope\">\n              <el-button size=\"mini\" type=\"primary\" @click=\"handleUpdate(scope.row)\" icon=\"el-icon-edit\" plain v-waves>编辑</el-button>\n              <cus-del-btn @ok=\"handleDelete(scope.row)\"></cus-del-btn>\n            </template>\n          </el-table-column>\n        </el-table>\n        <!-- 分页 -->\n        <cus-pagination v-show=\"total>0\" :total=\"total\" :page.sync=\"listQuery.page\" :limit.sync=\"listQuery.limit\" @pagination=\"getList\"/>\n      </div>\n\n      <el-dialog :title=\"titleMap[dialogStatus]\" :visible.sync=\"dialogVisible\" width=\"40%\" @close=\"handleDialogClose\">\n        <el-form ref=\"dataForm\" :model=\"form\" :rules=\"rules\" label-width=\"100px\" class=\"demo-ruleForm\">\n        #foreach($field in ${table.fields})\n        <el-form-item label=\"${field.comment}:\" prop=\"${field.propertyName}\">\n            <el-input v-model=\"form.${field.propertyName}\"></el-input>\n        </el-form-item>\n        #end\n        </el-form>\n        <span slot=\"footer\" class=\"dialog-footer\">\n          <el-button @click=\"dialogVisible = false\" v-waves>取 消</el-button>\n          <el-button type=\"primary\" @click=\"submitForm\" v-waves>确 定</el-button>\n        </span>\n      </el-dialog>\n    </cus-wraper>\n  </div>\n</template>\n\n<script>\n import { get${entity}Page, saveOrUpdate${entity}, delete${entity} } from \"@/api/${package.moduleName}/${entityPropertyName}\";\n\nexport default {\n  name: \'${entity}\',\n  data() {\n    return {\n      dialogVisible: false,\n      list: [],\n      listLoading: true,\n      total: 0,\n      listQuery: {\n        page: 1,\n        limit: 10,\n	    #if(${queryFields})\n	    #foreach($field in ${queryFields})\n	    ${field.propertyName}:undefined,\n	    #end\n	    #end\n      },\n      input: \'\',\n      form: {\n	     #foreach($field in ${table.fields})\n	     ${field.propertyName}: undefined, //${field.comment}\n	     #end\n      },\n     dialogStatus: \"\",\n     titleMap: {\n        update: \"编辑\",\n        create: \"创建\"\n     },\n     rules: {\n         name: [\n            { required: true, message: \'请输入项目名称\', trigger: \'blur\' }\n         ]\n      }\n    }\n  },\n  created() {\n    this.getList();\n  },\n  methods: {\n    getList() {\n      this.listLoading = true;\n      get${entity}Page(this.listQuery).then(response => {\n        this.list = response.data.records;\n    	this.total = response.data.total;\n    	this.listLoading = false;\n		});\n    },\n    handleCreate() {\n        this.resetForm();\n        this.dialogStatus = \"create\";\n        this.dialogVisible = true;\n    },\n    handleUpdate(row) {\n        this.form = Object.assign({}, row);\n        this.dialogStatus = \"update\";\n        this.dialogVisible = true;\n    },\n    handleDelete(row) {\n      #foreach($field in ${table.fields})\n		#if(${field.keyFlag})\n		 let id = row.${field.propertyName};\n		#end\n	  #end\n      delete${entity}(id).then(response => {\n            if (response.code == 200) {\n            this.getList();\n            this.submitOk(response.message);\n        } else {\n            this.submitFail(response.message);\n        }\n    });\n    },\n    submitForm() {\n    this.#[[$refs]]#.dataForm.validate(valid => {\n        if (valid) {\n            saveOrUpdate${entity}(this.form).then(response => {\n                if (response.code == 200) {\n                    this.getList();\n                    this.submitOk(response.message);\n                    this.dialogVisible = false;\n                } else {\n                     this.submitFail(response.message);\n                }\n        }).catch(err => { console.log(err);  });\n            }\n        });\n    },\n    resetForm() {\n        this.form = {\n            #foreach($field in ${table.fields})\n                ${field.propertyName}: undefined, //${field.comment}\n            #end\n        };\n    },\n    // 监听dialog关闭时的处理事件\n    handleDialogClose(){\n        if(this.$refs[\'dataForm\']){\n             this.$refs[\'dataForm\'].clearValidate(); // 清除整个表单的校验\n        }\n    }\n  }\n}\n</script>\n', '2019-09-11 21:40:06', '2019-09-18 09:32:20');
INSERT INTO `t_code_project_template` VALUES (27, 1, 19, '.js', 'import request from \'@/utils/request\';\n\nexport function get${entity}Page(query) {\n    return request({\n        url: \'/api/${package.moduleName}/${entityPropertyName}/listPage\',\n        method: \'post\',\n        data: query\n    });\n}\n\nexport function saveOrUpdate${entity}(form) {\n    return request({\n        url: \'/api/${package.moduleName}/${entityPropertyName}/saveOrUpdate\',\n        method: \'post\',\n        data: form\n    });\n}\n\nexport function delete${entity}(id) {\n    return request({\n        url: \'/api/${package.moduleName}/${entityPropertyName}/delete\',\n        method: \'post\',\n        data: { \'id\': id }\n    });\n}\n\nexport function get${entity}ById(id) {\n    return request({\n        url: \'/api/${package.moduleName}/${entityPropertyName}/detail\',\n        method: \'post\',\n        data: { \'id\': id }\n    });\n}', '2019-09-11 21:40:06', '2019-09-14 22:20:44');

-- ----------------------------
-- Table structure for t_code_project_velocity_context
-- ----------------------------
DROP TABLE IF EXISTS `t_code_project_velocity_context`;
CREATE TABLE `t_code_project_velocity_context`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `velocity` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '模板数据',
  `context` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci COMMENT '内容',
  `project_id` int(11) DEFAULT NULL COMMENT '所属项目',
  `gmt_create` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 305 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '代码生成器 - 项目 - 模板数据源' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_code_project_velocity_context
-- ----------------------------
INSERT INTO `t_code_project_velocity_context` VALUES (247, 'date', '\"2019-09-18 10:51:57\"', 1, '2019-09-18 10:51:58', '2019-09-18 10:51:58');
INSERT INTO `t_code_project_velocity_context` VALUES (248, 'superControllerClassPackage', 'null', 1, '2019-09-18 10:51:58', '2019-09-18 10:51:58');
INSERT INTO `t_code_project_velocity_context` VALUES (249, 'superServiceImplClassPackage', '\"com.baomidou.mybatisplus.service.impl.ServiceImpl\"', 1, '2019-09-18 10:51:58', '2019-09-18 10:51:58');
INSERT INTO `t_code_project_velocity_context` VALUES (250, 'baseResultMap', 'true', 1, '2019-09-18 10:51:58', '2019-09-18 10:51:58');
INSERT INTO `t_code_project_velocity_context` VALUES (251, 'vue', '\"com.zhengqing.modules.system.vue\"', 1, '2019-09-18 10:51:58', '2019-09-18 10:51:58');
INSERT INTO `t_code_project_velocity_context` VALUES (252, 'service.impl', '\"com.zhengqing.modules.system.service.impl\"', 1, '2019-09-18 10:51:58', '2019-09-18 10:51:58');
INSERT INTO `t_code_project_velocity_context` VALUES (253, 'moduleName', '\"system\"', 1, '2019-09-18 10:51:58', '2019-09-18 10:51:58');
INSERT INTO `t_code_project_velocity_context` VALUES (254, 'js', '\"com.zhengqing.modules.system.js\"', 1, '2019-09-18 10:51:58', '2019-09-18 10:51:58');
INSERT INTO `t_code_project_velocity_context` VALUES (255, 'mapper', '\"com.zhengqing.modules.system.mapper\"', 1, '2019-09-18 10:51:59', '2019-09-18 10:51:59');
INSERT INTO `t_code_project_velocity_context` VALUES (256, 'superMapperClass', '\"BaseMapper\"', 1, '2019-09-18 10:51:59', '2019-09-18 10:51:59');
INSERT INTO `t_code_project_velocity_context` VALUES (257, 'mapper.xml', '\"com.zhengqing.modules.system.mapper.xml\"', 1, '2019-09-18 10:51:59', '2019-09-18 10:51:59');
INSERT INTO `t_code_project_velocity_context` VALUES (258, 'superControllerClass', 'null', 1, '2019-09-18 10:51:59', '2019-09-18 10:51:59');
INSERT INTO `t_code_project_velocity_context` VALUES (259, 'entityPropertyName', '\"log\"', 1, '2019-09-18 10:51:59', '2019-09-18 10:51:59');
INSERT INTO `t_code_project_velocity_context` VALUES (260, 'activeRecord', 'true', 1, '2019-09-18 10:51:59', '2019-09-18 10:51:59');
INSERT INTO `t_code_project_velocity_context` VALUES (261, 'superServiceClass', '\"IService\"', 1, '2019-09-18 10:51:59', '2019-09-18 10:51:59');
INSERT INTO `t_code_project_velocity_context` VALUES (262, 'api', '\"com.zhengqing.modules.system.api\"', 1, '2019-09-18 10:51:59', '2019-09-18 10:51:59');
INSERT INTO `t_code_project_velocity_context` VALUES (263, 'superServiceImplClass', '\"ServiceImpl\"', 1, '2019-09-18 10:51:59', '2019-09-18 10:51:59');
INSERT INTO `t_code_project_velocity_context` VALUES (264, 'table', '{\"comment\":\"系统管理 - 日志表\",\"commonFields\":[],\"convert\":true,\"entityName\":\"Log\",\"entityPath\":\"log\",\"fieldNames\":\"id AS id, name AS name, url AS url, ip AS ip, user_id AS userId, status AS status, execute_time AS executeTime, gmt_create AS gmtCreate, gmt_modified AS gmtModified\",\"fields\":[{\"capitalName\":\"Id\",\"columnType\":\"INTEGER\",\"comment\":\"主键ID\",\"convert\":true,\"keyFlag\":true,\"keyIdentityFlag\":true,\"name\":\"id\",\"propertyName\":\"id\",\"propertyType\":\"Integer\",\"type\":\"int(11)\"},{\"capitalName\":\"Name\",\"columnType\":\"STRING\",\"comment\":\"接口名称\",\"convert\":true,\"keyFlag\":false,\"keyIdentityFlag\":false,\"name\":\"name\",\"propertyName\":\"name\",\"propertyType\":\"String\",\"type\":\"varchar(100)\"},{\"capitalName\":\"Url\",\"columnType\":\"STRING\",\"comment\":\"接口地址\",\"convert\":true,\"keyFlag\":false,\"keyIdentityFlag\":false,\"name\":\"url\",\"propertyName\":\"url\",\"propertyType\":\"String\",\"type\":\"varchar(255)\"},{\"capitalName\":\"Ip\",\"columnType\":\"STRING\",\"comment\":\"访问人IP\",\"convert\":true,\"keyFlag\":false,\"keyIdentityFlag\":false,\"name\":\"ip\",\"propertyName\":\"ip\",\"propertyType\":\"String\",\"type\":\"varchar(30)\"},{\"capitalName\":\"UserId\",\"columnType\":\"INTEGER\",\"comment\":\"访问人ID\",\"convert\":true,\"keyFlag\":false,\"keyIdentityFlag\":false,\"name\":\"user_id\",\"propertyName\":\"userId\",\"propertyType\":\"Integer\",\"type\":\"int(11)\"},{\"capitalName\":\"Status\",\"columnType\":\"INTEGER\",\"comment\":\"1 成功 0失败\",\"convert\":true,\"keyFlag\":false,\"keyIdentityFlag\":false,\"name\":\"status\",\"propertyName\":\"status\",\"propertyType\":\"Integer\",\"type\":\"int(2)\"},{\"capitalName\":\"ExecuteTime\",\"columnType\":\"STRING\",\"comment\":\"接口执行时间\",\"convert\":true,\"keyFlag\":false,\"keyIdentityFlag\":false,\"name\":\"execute_time\",\"propertyName\":\"executeTime\",\"propertyType\":\"String\",\"type\":\"varchar(20)\"},{\"capitalName\":\"GmtCreate\",\"columnType\":\"DATE\",\"comment\":\"创建时间\",\"convert\":true,\"keyFlag\":false,\"keyIdentityFlag\":false,\"name\":\"gmt_create\",\"propertyName\":\"gmtCreate\",\"propertyType\":\"Date\",\"type\":\"datetime\"},{\"capitalName\":\"GmtModified\",\"columnType\":\"DATE\",\"comment\":\"更新时间\",\"convert\":true,\"keyFlag\":false,\"keyIdentityFlag\":false,\"name\":\"gmt_modified\",\"propertyName\":\"gmtModified\",\"propertyType\":\"Date\",\"type\":\"datetime\"}],\"importPackages\":[\"com.baomidou.mybatisplus.enums.IdType\",\"java.util.Date\",\"com.baomidou.mybatisplus.annotations.TableId\",\"com.baomidou.mybatisplus.annotations.TableField\",\"com.baomidou.mybatisplus.enums.IdType\",\"com.baomidou.mybatisplus.activerecord.Model\",\"com.baomidou.mybatisplus.annotations.TableName\",\"java.io.Serializable\"],\"name\":\"t_log\",\"packageInfo\":{\"input\":\"LogInput\",\"service\":\"LogService\",\"vue\":\"LogVue\",\"service.impl\":\"LogServiceImpl\",\"js\":\"LogJs\",\"mapper\":\"LogMapper\",\"api\":\"LogApi\",\"mapper.xml\":\"LogMapperXml\",\"entity\":\"LogEntity\"}}', 1, '2019-09-18 10:52:00', '2019-09-18 10:52:00');
INSERT INTO `t_code_project_velocity_context` VALUES (265, 'package', '{\"input\":\"com.zhengqing.modules.system.dto.input\",\"service\":\"com.zhengqing.modules.system.service\",\"vue\":\"com.zhengqing.modules.system.vue\",\"service.impl\":\"com.zhengqing.modules.system.service.impl\",\"moduleName\":\"system\",\"js\":\"com.zhengqing.modules.system.js\",\"mapper\":\"com.zhengqing.modules.system.mapper\",\"api\":\"com.zhengqing.modules.system.api\",\"mapper.xml\":\"com.zhengqing.modules.system.mapper.xml\",\"entity\":\"com.zhengqing.modules.system.entity\"}', 1, '2019-09-18 10:52:00', '2019-09-18 10:52:00');
INSERT INTO `t_code_project_velocity_context` VALUES (266, 'queryFields', '[{\"capitalName\":\"GmtCreate\",\"columnType\":\"DATE\",\"comment\":\"创建时间\",\"convert\":true,\"keyFlag\":false,\"keyIdentityFlag\":false,\"name\":\"gmt_create\",\"propertyName\":\"gmtCreate\",\"propertyType\":\"Date\",\"type\":\"datetime\"}]', 1, '2019-09-18 10:52:00', '2019-09-18 10:52:00');
INSERT INTO `t_code_project_velocity_context` VALUES (267, 'author', '\"zhengqing\"', 1, '2019-09-18 10:52:00', '2019-09-18 10:52:00');
INSERT INTO `t_code_project_velocity_context` VALUES (268, 'baseColumnList', 'false', 1, '2019-09-18 10:52:00', '2019-09-18 10:52:00');
INSERT INTO `t_code_project_velocity_context` VALUES (269, 'tableInfo', '{\"input\":\"LogInput\",\"service\":\"LogService\",\"vue\":\"LogVue\",\"service.impl\":\"LogServiceImpl\",\"js\":\"LogJs\",\"mapper\":\"LogMapper\",\"api\":\"LogApi\",\"mapper.xml\":\"LogMapperXml\",\"entity\":\"LogEntity\"}', 1, '2019-09-18 10:52:00', '2019-09-18 10:52:00');
INSERT INTO `t_code_project_velocity_context` VALUES (270, 'superMapperClassPackage', '\"com.baomidou.mybatisplus.mapper.BaseMapper\"', 1, '2019-09-18 10:52:00', '2019-09-18 10:52:00');
INSERT INTO `t_code_project_velocity_context` VALUES (271, 'input', '\"com.zhengqing.modules.system.dto.input\"', 1, '2019-09-18 10:52:00', '2019-09-18 10:52:00');
INSERT INTO `t_code_project_velocity_context` VALUES (272, 'entityBuilderModel', 'false', 1, '2019-09-18 10:52:00', '2019-09-18 10:52:00');
INSERT INTO `t_code_project_velocity_context` VALUES (273, 'superServiceClassPackage', '\"com.baomidou.mybatisplus.service.IService\"', 1, '2019-09-18 10:52:01', '2019-09-18 10:52:01');
INSERT INTO `t_code_project_velocity_context` VALUES (274, 'service', '\"com.zhengqing.modules.system.service\"', 1, '2019-09-18 10:52:01', '2019-09-18 10:52:01');
INSERT INTO `t_code_project_velocity_context` VALUES (275, 'entityColumnConstant', 'false', 1, '2019-09-18 10:52:01', '2019-09-18 10:52:01');
INSERT INTO `t_code_project_velocity_context` VALUES (276, 'enableCache', 'false', 1, '2019-09-18 10:52:01', '2019-09-18 10:52:01');
INSERT INTO `t_code_project_velocity_context` VALUES (277, 'entity', '\"Log\"', 1, '2019-09-18 10:52:01', '2019-09-18 10:52:01');
INSERT INTO `t_code_project_velocity_context` VALUES (278, 'superEntityClass', 'null', 1, '2019-09-18 10:52:01', '2019-09-18 10:52:01');

-- ----------------------------
-- Table structure for t_code_table_config
-- ----------------------------
DROP TABLE IF EXISTS `t_code_table_config`;
CREATE TABLE `t_code_table_config`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `project_id` int(11) DEFAULT NULL COMMENT '项目ID',
  `table_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '表名',
  `query_columns` varchar(1024) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '用于检索字段',
  `gmt_create` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '项目数据表配置表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_sys_log
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_log`;
CREATE TABLE `t_sys_log`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '接口名称',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '接口地址',
  `ip` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '访问人IP',
  `user_id` int(11) DEFAULT 0 COMMENT '访问人ID 0:未登录用户操作',
  `status` int(2) DEFAULT 1 COMMENT '访问状态',
  `execute_time` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '接口执行时间',
  `gmt_create` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1702 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理 - 日志表' ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for t_sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_menu`;
CREATE TABLE `t_sys_menu`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '上级资源ID',
  `url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'url',
  `resources` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资源编码',
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资源名称',
  `level` int(11) DEFAULT NULL COMMENT '资源级别',
  `sort_no` int(11) DEFAULT NULL COMMENT '排序',
  `icon` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资源图标',
  `type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '类型 menu、button',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `gmt_create` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 81 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理-权限资源表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_menu
-- ----------------------------
INSERT INTO `t_sys_menu` VALUES (1, '0', NULL, 'systemManage', '系统管理', 1, 3, 'component', 'menu', '', '2019-03-28 18:51:08', '2019-03-28 18:51:10');
INSERT INTO `t_sys_menu` VALUES (2, '1', '/system/user/listPage', 'user', '用户管理', 2, 1, 'my-user', 'menu', '', '2019-03-28 18:52:13', '2019-08-31 21:26:57');
INSERT INTO `t_sys_menu` VALUES (3, '2', NULL, 'sys:user:add', '添加', 3, 1, 'el-icon-edit', 'button', '', '2019-03-28 18:53:31', '2019-04-01 20:19:55');
INSERT INTO `t_sys_menu` VALUES (4, '2', NULL, 'sys:user:edit', '编辑', 3, 2, NULL, 'button', '', '2019-03-28 18:54:26', '2019-04-01 20:20:16');
INSERT INTO `t_sys_menu` VALUES (5, '2', '/system/user/delete', 'sys:user:delete', '删除', 3, 3, NULL, 'button', '', '2019-03-28 18:55:25', '2019-04-01 20:20:09');
INSERT INTO `t_sys_menu` VALUES (13, '0', NULL, 'codeGenerator', '代码生成器', 1, 2, 'table', 'menu', '', '2019-03-30 13:54:43', '2019-09-07 21:06:55');
INSERT INTO `t_sys_menu` VALUES (15, '13', '/code/project/listPage', 'project', '项目管理', 1, 1, 'icon-news', 'menu', '', '2019-03-30 13:58:00', '2019-09-01 15:01:58');
INSERT INTO `t_sys_menu` VALUES (16, '1', '/system/role/listPage', 'role', '角色管理', 2, 2, 'my-role', 'menu', '', '2019-03-30 14:00:03', '2019-03-30 14:20:59');
INSERT INTO `t_sys_menu` VALUES (17, '1', '/system/menu/treeMenu', 'menu', '菜单管理', 2, 3, 'my-sysmenu', 'menu', '', '2019-03-30 14:00:53', '2019-03-30 14:21:10');
INSERT INTO `t_sys_menu` VALUES (43, '16', NULL, 'sys:role:add', '添加', 3, 1, '', 'button', '', '2019-04-01 20:20:46', '2019-04-01 20:20:46');
INSERT INTO `t_sys_menu` VALUES (44, '16', NULL, 'sys:role:edit', '编辑', 3, 2, '', 'button', '', '2019-04-01 20:21:03', '2019-04-01 20:21:03');
INSERT INTO `t_sys_menu` VALUES (45, '16', NULL, 'roleSetting', '权限设置', 3, 3, '', 'button', '', '2019-04-01 20:21:24', '2019-04-01 20:21:24');
INSERT INTO `t_sys_menu` VALUES (46, '16', '/system/role/delete', 'sys:role:delete', '删除', 3, 4, '', 'button', '', '2019-04-01 20:21:55', '2019-04-01 20:21:55');
INSERT INTO `t_sys_menu` VALUES (47, '17', '', 'sys:menu:add', '添加', 3, 1, '', 'button', '', '2019-04-01 20:22:31', '2019-04-01 20:22:31');
INSERT INTO `t_sys_menu` VALUES (48, '17', NULL, 'sys:menu:addsub', '添加下级', 3, 2, '', 'button', '', '2019-04-01 20:23:00', '2019-04-01 20:23:00');
INSERT INTO `t_sys_menu` VALUES (49, '17', NULL, 'sys:menu:edit', '编辑', 3, 3, '', 'button', '', '2019-04-01 20:23:28', '2019-04-01 20:23:28');
INSERT INTO `t_sys_menu` VALUES (50, '17', '/system/menu/delete', 'sys:menu:delete', '删除', 3, 4, '', 'button', '', '2019-04-01 20:23:46', '2019-04-01 20:23:46');
INSERT INTO `t_sys_menu` VALUES (53, '13', '/code/bsTemplate/listPage', 'bs_template', '初始模板', 2, 2, 'icon-news', 'menu', NULL, '2019-09-01 15:01:42', '2019-09-01 15:02:05');
INSERT INTO `t_sys_menu` VALUES (54, '13', '/code/project_template/listPage', 'project_template', '项目模板管理', 3, 3, 'documentation', 'menu', NULL, '2019-09-01 15:03:02', '2019-09-01 15:03:02');
INSERT INTO `t_sys_menu` VALUES (55, '13', '/code/database/listPage', 'database', '数据库管理', 4, 4, 'icon-news', 'menu', NULL, '2019-09-01 15:04:09', '2019-09-01 15:11:25');
INSERT INTO `t_sys_menu` VALUES (56, '55', '/code/database/tableList', 'table', '是否显示数据库表', 3, 4, NULL, 'button', NULL, '2019-09-01 15:10:34', '2019-09-13 03:27:56');
INSERT INTO `t_sys_menu` VALUES (57, '55', '/code/database/columnList', 'column', '是否显示数据表字段信息', 3, 5, NULL, 'button', NULL, '2019-09-01 15:11:12', '2019-09-13 03:38:31');
INSERT INTO `t_sys_menu` VALUES (59, '15', NULL, 'code:project:add', '新建一个项目', 3, 1, NULL, 'button', NULL, '2019-09-12 11:17:54', '2019-09-12 11:17:54');
INSERT INTO `t_sys_menu` VALUES (60, '15', NULL, 'code:project:edit', '编辑项目', 3, 2, NULL, 'button', NULL, '2019-09-12 11:17:54', '2019-09-12 11:17:54');
INSERT INTO `t_sys_menu` VALUES (61, '15', NULL, 'code:projectPackage:showTree', '是否显示项目树形包', 3, 3, NULL, 'button', NULL, '2019-09-12 11:17:54', '2019-09-12 11:17:54');
INSERT INTO `t_sys_menu` VALUES (62, '15', NULL, 'code:project:delete', '删除项目', 3, 4, NULL, 'button', NULL, '2019-09-12 11:17:54', '2019-09-12 11:17:54');
INSERT INTO `t_sys_menu` VALUES (63, '15', NULL, 'code:projectPackage:add', '添加项目包', 2, 5, NULL, 'button', NULL, '2019-09-12 11:17:54', '2019-09-12 11:17:54');
INSERT INTO `t_sys_menu` VALUES (64, '15', NULL, 'code:projectPackage:edit', '编辑项目包', 2, 6, NULL, 'button', NULL, '2019-09-12 11:19:49', '2019-09-12 11:19:49');
INSERT INTO `t_sys_menu` VALUES (65, '15', NULL, 'code:projectPackage:delete', '删除项目包', 2, 7, NULL, 'button', NULL, '2019-09-12 11:22:18', '2019-09-12 11:22:18');
INSERT INTO `t_sys_menu` VALUES (66, '54', NULL, 'code:projectTemplate:add', '添加项目模板', 4, 1, NULL, 'button', NULL, '2019-09-12 22:24:14', '2019-09-12 22:24:14');
INSERT INTO `t_sys_menu` VALUES (67, '54', NULL, 'code:projectTemplate:delete', '删除项目模板', 4, 2, NULL, 'button', NULL, '2019-09-12 22:25:26', '2019-09-12 22:25:26');
INSERT INTO `t_sys_menu` VALUES (68, '54', NULL, 'code:projectTemplate:edit', '编辑项目模板', 4, 3, NULL, 'button', NULL, '2019-09-12 22:27:15', '2019-09-12 22:27:15');
INSERT INTO `t_sys_menu` VALUES (69, '53', NULL, 'code:bsTemplate:add', '添加初始模板', 3, 1, NULL, 'button', NULL, '2019-09-12 22:53:00', '2019-09-12 22:53:00');
INSERT INTO `t_sys_menu` VALUES (70, '53', NULL, 'code:bsTemplate:edit', '编辑初始模板', 3, 2, NULL, 'button', NULL, '2019-09-12 22:53:49', '2019-09-12 22:53:49');
INSERT INTO `t_sys_menu` VALUES (71, '53', NULL, 'code:bsTemplate:delete', '删除初始模板', 3, 3, NULL, 'button', NULL, '2019-09-12 22:54:34', '2019-09-12 22:54:34');
INSERT INTO `t_sys_menu` VALUES (72, '55', NULL, 'code:database:add', '添加数据库信息', 5, 1, NULL, 'button', NULL, '2019-09-13 03:20:45', '2019-09-13 03:21:17');
INSERT INTO `t_sys_menu` VALUES (73, '55', NULL, 'code:database:edit', '编辑数据库信息', 5, 2, NULL, 'button', NULL, '2019-09-13 03:28:25', '2019-09-13 03:28:25');
INSERT INTO `t_sys_menu` VALUES (74, '55', NULL, 'code:database:delete', '删除数据库', 5, 3, NULL, 'button', NULL, '2019-09-13 03:28:53', '2019-09-13 03:28:53');
INSERT INTO `t_sys_menu` VALUES (75, '55', NULL, 'code:column:update', '修改表字段信息', 5, 6, NULL, 'button', NULL, '2019-09-13 03:41:46', '2019-09-13 03:41:46');
INSERT INTO `t_sys_menu` VALUES (76, '55', NULL, 'generateCode', '生成代码', 5, 7, NULL, 'button', NULL, '2019-09-13 03:45:12', '2019-09-13 03:45:12');
INSERT INTO `t_sys_menu` VALUES (77, '54', NULL, 'code:projectTemplate:showVelocityContext', '是否显示参考模板数据源配置信息', 4, 4, NULL, 'button', '是否显示参考模板数据源配置信息', '2019-09-17 17:45:09', '2019-09-17 17:45:09');
INSERT INTO `t_sys_menu` VALUES (78, '54', NULL, 'code:projectTemplate:generateTemplate', '生成项目模板', 4, 5, NULL, 'button', '生成项目模板', '2019-09-17 17:46:12', '2019-09-17 17:46:12');
INSERT INTO `t_sys_menu` VALUES (79, '1', '/system/log', 'log', '系统日志', 2, 4, 'my-sysmenu', 'menu', '', '2019-03-30 14:00:53', '2019-09-18 14:21:38');
INSERT INTO `t_sys_menu` VALUES (80, '2', '/system/user/updatePersonalInfo', 'sys:user:editPersonalInfo', '修改个人信息', 3, 4, NULL, 'button', NULL, '2019-09-18 21:05:51', '2019-09-18 22:40:45');

-- ----------------------------
-- Table structure for t_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role`;
CREATE TABLE `t_sys_role`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色编码',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名称',
  `remarks` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色描述',
  `gmt_create` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理-角色表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_role
-- ----------------------------
INSERT INTO `t_sys_role` VALUES (1, 'admin', '系统管理员', '系统管理员', '2019-03-28 15:51:56', '2019-03-28 15:51:59');
INSERT INTO `t_sys_role` VALUES (2, 'visitor', '访客', '访客', '2019-03-28 20:17:04', '2019-09-09 16:32:15');

-- ----------------------------
-- Table structure for t_sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_role_menu`;
CREATE TABLE `t_sys_role_menu`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(10) DEFAULT NULL COMMENT '角色ID',
  `menu_id` int(10) DEFAULT NULL COMMENT '菜单ID',
  `gmt_create` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1578 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理 - 角色-权限资源关联表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_role_menu
-- ----------------------------
INSERT INTO `t_sys_role_menu` VALUES (1507, 1, 13, '2019-09-18 21:06:23', '2019-09-18 21:06:23');
INSERT INTO `t_sys_role_menu` VALUES (1508, 1, 15, '2019-09-18 21:06:23', '2019-09-18 21:06:23');
INSERT INTO `t_sys_role_menu` VALUES (1509, 1, 59, '2019-09-18 21:06:23', '2019-09-18 21:06:23');
INSERT INTO `t_sys_role_menu` VALUES (1510, 1, 60, '2019-09-18 21:06:23', '2019-09-18 21:06:23');
INSERT INTO `t_sys_role_menu` VALUES (1511, 1, 61, '2019-09-18 21:06:24', '2019-09-18 21:06:24');
INSERT INTO `t_sys_role_menu` VALUES (1512, 1, 62, '2019-09-18 21:06:24', '2019-09-18 21:06:24');
INSERT INTO `t_sys_role_menu` VALUES (1513, 1, 63, '2019-09-18 21:06:24', '2019-09-18 21:06:24');
INSERT INTO `t_sys_role_menu` VALUES (1514, 1, 64, '2019-09-18 21:06:24', '2019-09-18 21:06:24');
INSERT INTO `t_sys_role_menu` VALUES (1515, 1, 65, '2019-09-18 21:06:24', '2019-09-18 21:06:24');
INSERT INTO `t_sys_role_menu` VALUES (1516, 1, 53, '2019-09-18 21:06:24', '2019-09-18 21:06:24');
INSERT INTO `t_sys_role_menu` VALUES (1517, 1, 69, '2019-09-18 21:06:24', '2019-09-18 21:06:24');
INSERT INTO `t_sys_role_menu` VALUES (1518, 1, 70, '2019-09-18 21:06:24', '2019-09-18 21:06:24');
INSERT INTO `t_sys_role_menu` VALUES (1519, 1, 71, '2019-09-18 21:06:25', '2019-09-18 21:06:25');
INSERT INTO `t_sys_role_menu` VALUES (1520, 1, 54, '2019-09-18 21:06:25', '2019-09-18 21:06:25');
INSERT INTO `t_sys_role_menu` VALUES (1521, 1, 66, '2019-09-18 21:06:25', '2019-09-18 21:06:25');
INSERT INTO `t_sys_role_menu` VALUES (1522, 1, 67, '2019-09-18 21:06:25', '2019-09-18 21:06:25');
INSERT INTO `t_sys_role_menu` VALUES (1523, 1, 68, '2019-09-18 21:06:25', '2019-09-18 21:06:25');
INSERT INTO `t_sys_role_menu` VALUES (1524, 1, 77, '2019-09-18 21:06:25', '2019-09-18 21:06:25');
INSERT INTO `t_sys_role_menu` VALUES (1525, 1, 78, '2019-09-18 21:06:25', '2019-09-18 21:06:25');
INSERT INTO `t_sys_role_menu` VALUES (1526, 1, 55, '2019-09-18 21:06:25', '2019-09-18 21:06:25');
INSERT INTO `t_sys_role_menu` VALUES (1527, 1, 72, '2019-09-18 21:06:26', '2019-09-18 21:06:26');
INSERT INTO `t_sys_role_menu` VALUES (1528, 1, 73, '2019-09-18 21:06:26', '2019-09-18 21:06:26');
INSERT INTO `t_sys_role_menu` VALUES (1529, 1, 74, '2019-09-18 21:06:26', '2019-09-18 21:06:26');
INSERT INTO `t_sys_role_menu` VALUES (1530, 1, 56, '2019-09-18 21:06:26', '2019-09-18 21:06:26');
INSERT INTO `t_sys_role_menu` VALUES (1531, 1, 57, '2019-09-18 21:06:26', '2019-09-18 21:06:26');
INSERT INTO `t_sys_role_menu` VALUES (1532, 1, 75, '2019-09-18 21:06:26', '2019-09-18 21:06:26');
INSERT INTO `t_sys_role_menu` VALUES (1533, 1, 76, '2019-09-18 21:06:26', '2019-09-18 21:06:26');
INSERT INTO `t_sys_role_menu` VALUES (1534, 1, 1, '2019-09-18 21:06:26', '2019-09-18 21:06:26');
INSERT INTO `t_sys_role_menu` VALUES (1535, 1, 2, '2019-09-18 21:06:27', '2019-09-18 21:06:27');
INSERT INTO `t_sys_role_menu` VALUES (1536, 1, 3, '2019-09-18 21:06:27', '2019-09-18 21:06:27');
INSERT INTO `t_sys_role_menu` VALUES (1537, 1, 4, '2019-09-18 21:06:27', '2019-09-18 21:06:27');
INSERT INTO `t_sys_role_menu` VALUES (1538, 1, 5, '2019-09-18 21:06:27', '2019-09-18 21:06:27');
INSERT INTO `t_sys_role_menu` VALUES (1539, 1, 80, '2019-09-18 21:06:27', '2019-09-18 21:06:27');
INSERT INTO `t_sys_role_menu` VALUES (1540, 1, 16, '2019-09-18 21:06:27', '2019-09-18 21:06:27');
INSERT INTO `t_sys_role_menu` VALUES (1541, 1, 43, '2019-09-18 21:06:27', '2019-09-18 21:06:27');
INSERT INTO `t_sys_role_menu` VALUES (1542, 1, 44, '2019-09-18 21:06:27', '2019-09-18 21:06:27');
INSERT INTO `t_sys_role_menu` VALUES (1543, 1, 45, '2019-09-18 21:06:28', '2019-09-18 21:06:28');
INSERT INTO `t_sys_role_menu` VALUES (1544, 1, 46, '2019-09-18 21:06:28', '2019-09-18 21:06:28');
INSERT INTO `t_sys_role_menu` VALUES (1545, 1, 17, '2019-09-18 21:06:28', '2019-09-18 21:06:28');
INSERT INTO `t_sys_role_menu` VALUES (1546, 1, 47, '2019-09-18 21:06:28', '2019-09-18 21:06:28');
INSERT INTO `t_sys_role_menu` VALUES (1547, 1, 48, '2019-09-18 21:06:28', '2019-09-18 21:06:28');
INSERT INTO `t_sys_role_menu` VALUES (1548, 1, 49, '2019-09-18 21:06:28', '2019-09-18 21:06:28');
INSERT INTO `t_sys_role_menu` VALUES (1549, 1, 50, '2019-09-18 21:06:28', '2019-09-18 21:06:28');
INSERT INTO `t_sys_role_menu` VALUES (1550, 1, 79, '2019-09-18 21:06:28', '2019-09-18 21:06:28');
INSERT INTO `t_sys_role_menu` VALUES (1551, 2, 13, '2019-09-18 21:26:42', '2019-09-18 21:26:42');
INSERT INTO `t_sys_role_menu` VALUES (1552, 2, 15, '2019-09-18 21:26:42', '2019-09-18 21:26:42');
INSERT INTO `t_sys_role_menu` VALUES (1553, 2, 59, '2019-09-18 21:26:42', '2019-09-18 21:26:42');
INSERT INTO `t_sys_role_menu` VALUES (1554, 2, 60, '2019-09-18 21:26:42', '2019-09-18 21:26:42');
INSERT INTO `t_sys_role_menu` VALUES (1555, 2, 61, '2019-09-18 21:26:42', '2019-09-18 21:26:42');
INSERT INTO `t_sys_role_menu` VALUES (1556, 2, 62, '2019-09-18 21:26:42', '2019-09-18 21:26:42');
INSERT INTO `t_sys_role_menu` VALUES (1557, 2, 63, '2019-09-18 21:26:43', '2019-09-18 21:26:43');
INSERT INTO `t_sys_role_menu` VALUES (1558, 2, 64, '2019-09-18 21:26:43', '2019-09-18 21:26:43');
INSERT INTO `t_sys_role_menu` VALUES (1559, 2, 65, '2019-09-18 21:26:43', '2019-09-18 21:26:43');
INSERT INTO `t_sys_role_menu` VALUES (1560, 2, 53, '2019-09-18 21:26:43', '2019-09-18 21:26:43');
INSERT INTO `t_sys_role_menu` VALUES (1561, 2, 69, '2019-09-18 21:26:43', '2019-09-18 21:26:43');
INSERT INTO `t_sys_role_menu` VALUES (1562, 2, 70, '2019-09-18 21:26:43', '2019-09-18 21:26:43');
INSERT INTO `t_sys_role_menu` VALUES (1563, 2, 71, '2019-09-18 21:26:43', '2019-09-18 21:26:43');
INSERT INTO `t_sys_role_menu` VALUES (1564, 2, 54, '2019-09-18 21:26:43', '2019-09-18 21:26:43');
INSERT INTO `t_sys_role_menu` VALUES (1565, 2, 66, '2019-09-18 21:26:44', '2019-09-18 21:26:44');
INSERT INTO `t_sys_role_menu` VALUES (1566, 2, 67, '2019-09-18 21:26:44', '2019-09-18 21:26:44');
INSERT INTO `t_sys_role_menu` VALUES (1567, 2, 68, '2019-09-18 21:26:44', '2019-09-18 21:26:44');
INSERT INTO `t_sys_role_menu` VALUES (1568, 2, 77, '2019-09-18 21:26:44', '2019-09-18 21:26:44');
INSERT INTO `t_sys_role_menu` VALUES (1569, 2, 78, '2019-09-18 21:26:44', '2019-09-18 21:26:44');
INSERT INTO `t_sys_role_menu` VALUES (1570, 2, 55, '2019-09-18 21:26:44', '2019-09-18 21:26:44');
INSERT INTO `t_sys_role_menu` VALUES (1571, 2, 72, '2019-09-18 21:26:44', '2019-09-18 21:26:44');
INSERT INTO `t_sys_role_menu` VALUES (1572, 2, 73, '2019-09-18 21:26:45', '2019-09-18 21:26:45');
INSERT INTO `t_sys_role_menu` VALUES (1573, 2, 56, '2019-09-18 21:26:45', '2019-09-18 21:26:45');
INSERT INTO `t_sys_role_menu` VALUES (1574, 2, 57, '2019-09-18 21:26:45', '2019-09-18 21:26:45');
INSERT INTO `t_sys_role_menu` VALUES (1575, 2, 75, '2019-09-18 21:26:45', '2019-09-18 21:26:45');
INSERT INTO `t_sys_role_menu` VALUES (1576, 2, 76, '2019-09-18 21:26:45', '2019-09-18 21:26:45');
INSERT INTO `t_sys_role_menu` VALUES (1577, 2, 80, '2019-09-18 21:26:45', '2019-09-18 21:26:45');

-- ----------------------------
-- Table structure for t_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user`;
CREATE TABLE `t_sys_user`  (
  `id` int(64) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '账号',
  `password` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '登录密码',
  `nick_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '昵称',
  `sex` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '性别 0:男 1:女',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号码',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮箱',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
  `flag` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '状态',
  `salt` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '盐值',
  `token` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'token',
  `qq_oppen_id` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'QQ 第三方登录Oppen_ID唯一标识',
  `pwd` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '明文密码',
  `gmt_create` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 22 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理-用户基础信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_user
-- ----------------------------
INSERT INTO `t_sys_user` VALUES (1, 'admin', '0ad1be0abfc90ee593f2a68d3193afedf15fc9141382f6e1492c0629ed6263ac', '郑清', '0', '15183303003', '10086@qq.com', 'http://qzapp.qlogo.cn/qzapp/101536330/86F96F92387D69BD7659C4EC3CD6BD69/100', '1', '123', 'code-generator_token_8775cc36-f687-4db9-860a-338f0ae7d0df', '', '123456', '2019-05-05 16:09:06', '2019-09-19 00:59:47');
INSERT INTO `t_sys_user` VALUES (2, 'test', '0ad1be0abfc90ee593f2a68d3193afedf15fc9141382f6e1492c0629ed6263ac', '测试号', '0', '10000', '10000@qq.com', 'https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif', '1', '123', 'code-generator_token_5d731387-bb0e-4eaa-9415-30268281fcf2', NULL, '123456', '2019-05-05 16:15:06', '2019-09-19 01:47:19');

-- ----------------------------
-- Table structure for t_sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_sys_user_role`;
CREATE TABLE `t_sys_user_role`  (
  `id` int(10) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role_id` int(10) DEFAULT NULL COMMENT '角色ID',
  `user_id` int(10) DEFAULT NULL COMMENT '用户ID',
  `gmt_create` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 45 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统管理 - 用户角色关联表 ' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_sys_user_role
-- ----------------------------
INSERT INTO `t_sys_user_role` VALUES (12, 1, 1, '2019-08-21 10:49:41', '2019-08-21 10:49:41');
INSERT INTO `t_sys_user_role` VALUES (33, 2, 2, '2019-09-18 21:26:32', '2019-09-18 21:26:32');

SET FOREIGN_KEY_CHECKS = 1;
