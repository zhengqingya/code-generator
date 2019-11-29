### 一、前言
最近花了一个月时间完成了一套基于Spring Boot+Vue+Shiro前后端分离的代码生成器，目前项目代码已基本完成
#### 止步传统CRUD，进阶代码优化：
该项目可根据数据库字段动态生成 **controller、mapper、service、html、jsp、vue、php、.py ...** 等各种类型代码，采用 **velocity** 模板引擎在页面动态配置生成代码，前后端动态权限配置，前端权限精确到 **按钮** 级别，后端权限精确到 **uri** 上，QQ授权第三方单用户登录...等
#### 基本环境：
1. JDK 1.8
2. IDEA 2019.2
3. Redis 5.0.5
4. MySQL 5.7
5. Nginx 1.17.1
6. Docker 18.09.7
7. Node.js 10.15.3
#### 项目使用技术栈：
前端：Vue + Axios
后端：Spring Boot 、 MyBatis-Plus
缓存：Redis
权限：Shiro
.......

#### 项目部署

参考： [docker-compose部署项目到服务器](https://zhengqing.blog.csdn.net/article/details/103068220) 

### 二、代码生成器介绍
###### 线上体验地址： [http://www.zhengqingya.com:8100/code-generator/dashboard](http://www.zhengqing520.com:8100/code-generator/dashboard)
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140826_05b3d0de_1988119.png)
#### 1、登录：
测试账号：test   密码：123456  
> 由于权限问题 线上开放的权限仅只是代码生成器那一部分，需要权限控制、系统日志等功能可fork源码参考~

三方登录目前暂支持QQ授权登录，默认权限只有代码生成器模块，登录过后，30分钟内不进行操作将自动下线，同一账号在别处登录将被挤下线，点击头像在个人信息中可自行修改账号、密码、昵称
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140826_d562c271_1988119.png)
#### 2、代码生成器
###### ① 项目管理
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140826_e1ce76fd_1988119.png)
新建一个项目后，编辑项目包，这里和我们ide打开一个项目下面的包层次类似，后面生成的代码也将存放在此树包目录下，可以根据自己的需求来灵活配置建包
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140826_85ee2c97_1988119.png)
###### ② 初始模板
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140826_1fd6cdbf_1988119.png)
这里可以选择给自己的项目添加一个初始模板，在`项目模板管理`处可选择项目进行一键新增生成对应的模板
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140826_e847b34e_1988119.png)
生成规则：根据`项目管理`中的包名与`初始模板`中的模板类型名对应上即可生成，不对应的即不会处理~

###### ③ 项目模板管理
列表页面和`初始模板`列表页面类型，不同的是新增时需要选择项目哦，后面的代码生成将会依照这里配置的代码模板动态生成！
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140826_10c79735_1988119.png)
于是乎，整个代码生成的精髓就在乎这里自己项目所配置的模板哦，这里也给出了可以参考配置的模板数据
> 数据源配置信息，要在根据数据库字段生成一次代码后才拥有数据可以查看哦，不然会是空数据！

![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140826_017bb1cd_1988119.png)
ex: `${author}` 对应生成 `zhengqing`
 `${package.input}` 对应生成 `com.zhengqing.modules.system.dto.input`
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140827_2bee52d0_1988119.png)
另外就需要参考 `Velocity` 模板引擎的基本语法使用了
这里再例出一个小编配置的实体类模板吧
> **温馨小提示**：在项目的sql中保存有小编代码生成器项目中配置的项目模板以供参考哦~ 
```java
package ${package.entity};

#foreach($pkg in ${table.importPackages})
import ${pkg};
#end
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>  ${table.comment} </p>
 *
 * @author: ${author}
 * @date: ${date}
 */
#if(${table.convert})
@Data
@ApiModel(description = "${table.comment}")
@TableName("${table.name}")
#end
#if(${superEntityClass})
public class ${entity} extends ${superEntityClass}#if(${activeRecord})<${entity}>#end {
#elseif(${activeRecord})
public class ${entity} extends Model<${entity}> {
#else
public class ${entity} implements Serializable {
#end

    private static final long serialVersionUID = 1L;

#foreach($field in ${table.fields})
#if(${field.keyFlag})
#set($keyPropertyName=${field.propertyName})
#end
#if("$!field.comment" != "")
    /**
     * ${field.comment}
     */
	@ApiModelProperty(value = "${field.comment}")
#end
#if(${field.keyFlag})
	@TableId(value="${field.name}", type= IdType.AUTO)
#else
	@TableField("${field.name}")
#end
	private ${field.propertyType} ${field.propertyName};
#end

#if(${entityColumnConstant})
#foreach($field in ${table.fields})
	public static final String ${field.name.toUpperCase()} = "${field.name}";

#end
#end
#if(${activeRecord})
	@Override
	protected Serializable pkVal() {
#if(${keyPropertyName})
		return this.${keyPropertyName};
#else
		return this.id;
#end
	}

#end
}

```

###### ④ 数据库管理
1. 连接数据库
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140827_bdefc7fa_1988119.png)
这里便是配置数据库连接信息了，这里小编给出一个开放的数据库可供连接测试使用，希望读者不要乱搞哦
数据库连接地址：`www.zhengqing520.com:3306`  用户名：`hello`  密码：`root`
可以在本地连接小编给出的数据库 在里面新建表然后在页面配置连接后选择表测试生成哦~
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140827_7066f252_1988119.png)
2. 选择表
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140827_dc3245f4_1988119.png)
3. 生成代码或修改远程数据库注释信息，这里为了安全考虑未做新增字段，删除字段等功能，如有兴趣，可fork小编源码自行修改其功能
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140827_ddb30dc8_1988119.png)

#### 3、系统管理
###### ① 用户管理
这里不多说，就是一些基础信息
###### ② 角色管理
1. 将指定的角色分配给指定的用户
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140827_8a5c07c6_1988119.png)
2. 分配指定的权限给该角色
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140827_7259aa58_1988119.png)
###### ③ 菜单管理
在这里动态配置后端uri请求权限与前端按钮权限
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140827_9a73ad19_1988119.png)
###### ④ 系统日志
![在这里插入图片描述](https://images.gitee.com/uploads/images/2019/0922/140827_d5572a85_1988119.png)
### 三、总结
1. 该代码生成器，打破传统的CRUD，避免花费太多时间在重复事情上，将更多时间用于学习晋升
2. 该项目对于小白来说可学习技术也有很多，比如`前后端分离`，`跨域解决`，`三方授权登录`，`@Validated后端参数校验`，`简单的redis缓存处理`，`shiro动态权限配置`，`前端按钮级别权限控制` 等等，后端使用目前企业流行的`Spring Boot`+`MyBatis-Plus`技术栈，前端也是较为普遍 学习轻松的`vue`。对于全栈了解入门也是一个好的demo
3. 本文目前只是给出项目使用介绍流程，后续若有时间，将会给出其中某些技术栈的案例教程
案例demo代码将会存放至：[https://gitee.com/zhengqingya/java-workspace](https://gitee.com/zhengqingya/java-workspace)

###### 注：实现本代码生成器参考了网上很多前后端教程，以及小编老大`肖哥`，在这一个月时间里，白天上班，晚上回家利用空闲时间来码代码，其中也是学到了不少东西，现在也分享出来给大家，希望能够帮助到有需要的小伙伴们~

---
### 项目源码
> 觉得还可以的话，请给个❤

###### GitHub地址：[https://github.com/zhengqingya/code-generator](https://github.com/zhengqingya/code-generator)
###### 码云地址：[https://gitee.com/zhengqingya/code-generator](https://gitee.com/zhengqingya/code-generator)
