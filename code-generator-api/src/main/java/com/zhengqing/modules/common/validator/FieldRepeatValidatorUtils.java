package com.zhengqing.modules.common.validator;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.zhengqing.modules.common.entity.BaseEntity;
import com.zhengqing.modules.common.exception.MyException;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.List;

/**
 *  <p> 数据库字段内容重复判断处理工具类 </p>
 *
 * @author：  zhengqing <br/>
 * @date：  2019/9/10$ 9:28$ <br/>
 * @version：  <br/>
 */
public class FieldRepeatValidatorUtils {

    /**
     * 实体类id
     */
    private static Integer id;
    /**
     * 校验字段
     */
    private static String field;
    /**
     * 校验字段值 - 字符串、数字、对象...
     */
    private static Object fieldValue;
    /**
     * 实体类对象值
     */
    private static Object object;

    /**
     * 校验数据 TODO 后期如果需要校验同个字段是否重复的话，将 `field` 做 , 或 - 分割... ；  如果id不唯一考虑传值过来判断 或 取fields第二个字段值拿id
     *
     * @param field：校验字段
     * @param object：对象数据
     * @param message：回调到前端提示消息
     * @return: boolean
     */
    public static boolean fieldRepeat(String field, Object object, String message) {
        // 使用Class类的中静态forName()方法获得与字符串对应的Class对象 ; className: 必须是接口或者类的名字
        // 静态方法forName()调用 启动类加载器 -> 加载某个类xx -> 实例化 ----> 从而达到降耦 更灵活
//        Object object = Class.forName(className).newInstance();

        FieldRepeatValidatorUtils.field = field;
        FieldRepeatValidatorUtils.object = object;
        getFieldValue();

        // ⑦ 校验字段内容是否重复
        // 工厂模式 + ar动态语法
        BaseEntity entity = (BaseEntity) object;
//        List list = entity.selectPage( new Page<>( 1,1 ), new EntityWrapper().eq( field, fieldValue ) ).getRecords();
        List list = entity.selectList( new EntityWrapper().eq( field, fieldValue ) );
        // 如果数据重复返回false -> 再返回自定义错误消息到前端
        if ( id == null ){
            if ( !CollectionUtils.isEmpty( list ) ){
                throw new MyException( message );
            }
        } else {
            if ( !CollectionUtils.isEmpty( list ) ){
                // fieldValueNew：前端输入字段值
                Object fieldValueNew = fieldValue;
                FieldRepeatValidatorUtils.object = entity.selectById( id );
                // 获取该id所在对象的校验字段值 - 旧数据
                getFieldValue();
                if ( !fieldValueNew.equals( fieldValue ) || list.size() > 1 ){
                    throw new MyException( message );
                }
            }
        }
        return true;
    }

    /**
     * 获取id、校验字段值
     */
    public static void getFieldValue(){
        // ① 获取所有的字段
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field f : fields) {
            // ② 设置对象中成员 属性private为可读
            f.setAccessible(true);
            // ③ 判断字段注解是否存在
            if ( f.isAnnotationPresent(ApiModelProperty.class) ) {
                // ④ 如果存在则获取该注解对应的字段,并判断是否与我们要校验的字段一致
                if ( f.getName().equals( field ) ){
                    try {
                        // ⑤ 如果一致则获取其属性值
                        fieldValue = f.get(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                // ⑥ 获取id值 -> 作用：判断是插入还是更新操作
                if ( "id".equals( f.getName() ) ){
                    try {
                        id = (Integer) f.get(object);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
