package com.zhengqing.test;

import com.zhengqing.modules.common.annotation.UIColumn;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> 此生成器可测试用于生成临时单个文件，如生成整套可配置放开部分注释使用 </p>
 *
 * @author：  zhengqing <br/>
 * @date：  2019/6/24$ 15:44$ <br/>
 * @version：  <br/>
 */
public class MyEasyCodeTest {

    /**
     * 项目当前路径
     */
    private String projectPath = System.getProperty("user.dir");


    /**
     * 准备路径的拼接 【注：也是代码生成后所在的位置】
     */
    private String[] paths ={
            "/document/code/",
            "/document/code/",
            /*
            "src/main/webapp/static/js/model/",
            "src/main/webapp/WEB-INF/views/domain/",
            "src/main/java/com/zhengqing/demo/web/controller/",
            "src/main/java/com/zhengqing/demo/service/",
            "src/main/java/com/zhengqing/demo/service/impl/",
            "src/main/java/com/zhengqing/demo/query/"
            */
    };

    /**
     * 准备所需要生成代码对应的模板名称
     */
    private String[] tempNames = {
            "domain.js",
            "domain.vue"
            /* "index.jsp",
             "DomainController.java",
             "IDomainService.java",
             "DomainServiceImpl.java",
             "DomainQuery.java"*/
    };

    /**
     * TODO 准备要生成的Domain(有可能同时生成多个)
     */
    private String[] domains = {"Demo"};


    /**
     * 生成相应的文件
     */
    @Test
    public void testCreate() throws Exception{
        System.out.println("代码生成所在路径 ======================> " + System.getProperty("user.dir") + paths[0]);
        //创建模板应用上下文
        VelocityContext context = new VelocityContext();//Domain  domain   vos
        //一.遍历所有的Domain
        for (int i = 0; i < domains.length; i++) {
            //1.1拿到大写的domain
            String domainBig = domains[i];
            //1.2拿到小写的domain
            String domainSmall = domainBig.substring(0,1).toLowerCase() + domainBig.substring(1);
            System.out.println(domainBig);
            System.out.println(domainSmall);
            //1.3设置上下文的替换名称
            context.put("Domain",domainBig);
            context.put("domain",domainSmall);
            //vos  数组或者集合    {title:   filed:}
            List<FieldVo> vos = scanDomain(domainBig);
            context.put("vos", vos);

            //二.遍历所有的路径
            for (int j = 0; j < paths.length; j++) {
                //2.1拿到相应的路径
                String path =paths[j];
                //2.2拿到相应的模板名称
                String tempName = tempNames[j];
                //2.3拼接回我们需要的位置文件
                String realPath = (projectPath+path + tempName).replaceAll("Domain",domainBig).replaceAll("domain",domainSmall);

                //三.准备相应文件与模板进行组合
                //3.1准备相应的文件(要生成的文件)
                File file = new File(realPath);
                //  如果父文件不存在，我们创建一个
                File parentFile = file.getParentFile();
                if(!parentFile.exists()){
                    parentFile.mkdirs();
                }
                //3.2拿到相应的模板(需要设置好编码)
                Template template = Velocity.getTemplate("document/template/"+tempName,"UTF-8");
                FileWriter writer = new FileWriter(file);
                template.merge(context, writer);
                writer.close();
            }
        }
    }

    private List<FieldVo> scanDomain(String domainBig) {
        List<FieldVo> list = new ArrayList<>();
        try {
            //Class对象
            String className = "com.zhengqing.domain."+domainBig;
            Class c = Class.forName(className);
            //获取所有的字段
            Field[] fields = c.getDeclaredFields();
            for (Field field : fields) {
                //扫描上面的注解UIColumn
                if(field.isAnnotationPresent(UIColumn.class)){
                    //有UIColumn注解
                    UIColumn iViewColumn = field.getAnnotation(UIColumn.class);
                    String title = iViewColumn.title();
                    //拿到字段的名称
                    String name = field.getName();
                    FieldVo fv = new FieldVo(title,name);
                    list.add(fv);
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

}

