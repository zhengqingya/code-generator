package com.zhengqing.test;

import java.util.Collections;
import java.util.LinkedList;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhengqing.modules.code.entity.CodeProjectVelocityContext;
import com.zhengqing.modules.code.entity.ProjectPackage;
import com.zhengqing.modules.shiro.utils.MD5Util;
import com.zhengqing.modules.shiro.utils.SHA256Util;
import com.zhengqing.modules.system.entity.User;
import com.zhengqing.modules.system.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {

    @Autowired
    UserMapper userMapper;

    @Test
    public void testCRUD() throws Exception {
        User user = new User();
        // 分页查询 10 条姓名为‘xx’、性别为男，且年龄在18至50之间的用户记录
        List<User> userList = user.selectPage(
                new Page<User>(1, 10),
                new EntityWrapper<User>().eq("nick_name", "郑清")
                        .eq("sex", 0)
//                        .between("age", "18", "50")
        ).getRecords();
        userList.forEach(System.out::println);
    }

    /**
     * 对数据库密码进行加密操作，加密10次，MD5算法，盐值：数据库中获取
     */
    @Test
    public void testMD5() throws Exception {
        List<User> list = userMapper.selectList(null);
        list.forEach(e -> {
            String password = MD5Util.createMD5Str(e.getPassword(), e.getSalt());
            System.out.println(password);
            e.setPassword(MD5Util.createMD5Str(e.getPassword(), e.getSalt()));
//            userMapper.insert(e);
        });
    }

    /**
     * SHA-256加密
     */
    @Test
    public void testSHA256() throws Exception {
        List<User> list = userMapper.selectList(null);
        list.forEach(e -> {
            String password = SHA256Util.sha256(e.getPwd(), e.getSalt());
            System.out.println(password);
            e.setPassword(password);
            userMapper.updateById(e);
        });
    }

    // -----------------------------------------------------------

    @Test
    public void testGetPackagePath() throws Exception {
        List<String> list = new LinkedList<>();
        String name = getRecursionPackage(7, list);
        // 反转list数据
        Collections.reverse(list);
        String str = String.join(".", list);
        System.out.println(str);
        System.out.println(name);
    }

    public String getRecursionPackage(Integer projectPackageId, List<String> packagePathList) {
        ProjectPackage p = new ProjectPackage().selectById(projectPackageId);
        packagePathList.add(p.getName());
        // 如果不是父包则递归
        if (p.getParentId() != 0) {
            getRecursionPackage(p.getParentId(), packagePathList);
        }
        return p.getName();
    }

    // -------------------------------------------------------------

    @Test
    public void testInsertOrUpdate() throws Exception{
        CodeProjectVelocityContext velocityContext = new CodeProjectVelocityContext();
        velocityContext.setId(5);
        velocityContext.setProjectId( 1 );
        velocityContext.setVelocity( "(String) key" );
        velocityContext.setContext( "valuefsdafsadfsaStr" );
        velocityContext.insertOrUpdate();
    }

}
