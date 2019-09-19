package com.zhengqing.modules.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *  <p> FieldRepeatValidator注解接口实现类 </p>
 *
 * @description :
 *        技巧01：必须实现ConstraintValidator接口
 * 　　　　技巧02：实现了ConstraintValidator接口后即使不进行Bean配置，spring也会将这个类进行Bean管理
 * 　　　　技巧03：可以在实现了ConstraintValidator接口的类中依赖注入其它Bean
 * 　　　　技巧04：实现了ConstraintValidator接口后必须重写 initialize 和 isValid 这两个方法；
 *              initialize 方法主要来进行初始化，通常用来获取自定义注解的属性值；
 *              isValid 方法主要进行校验逻辑，返回true表示校验通过，返回false表示校验失败，通常根据注解属性值和实体类属性值进行校验判断 [Object:校验字段的属性值]
 * @author : zhengqing
 * @date : 2019/9/10 9:22
 */
public class FieldRepeatValidatorClass implements ConstraintValidator<FieldRepeatValidator, Object> {

    private String field;
    private String message;

    @Override
    public void initialize(FieldRepeatValidator fieldRepeatValidator) {
        this.field = fieldRepeatValidator.field();
        this.message = fieldRepeatValidator.message();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return FieldRepeatValidatorUtils.fieldRepeat(field, o, message);
    }

}
