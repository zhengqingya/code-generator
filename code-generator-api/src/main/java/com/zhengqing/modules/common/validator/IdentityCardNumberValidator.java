package com.zhengqing.modules.common.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 *  <p> 自定义Validator注解实现类 </p>
 *
 * @description : 这个是真正进行验证的逻辑代码
 *          技巧01：必须实现ConstraintValidator接口
 *          技巧02：实现了ConstraintValidator接口后即使不进行Bean配置，spring也会将这个类进行Bean管理
 *          技巧03：可以在实现了ConstraintValidator接口的类中依赖注入其它Bean
 *          技巧04：实现了ConstraintValidator接口后必须重写 initialize 和 isValid 这两个方法；
 *                   initialize方法主要来进行初始化，通常用来获取自定义注解的属性值；
 *                   isValid 方法主要进行校验逻辑，返回true表示校验通过，返回false表示校验失败，通常根据注解属性值和实体类属性值进行校验判断
 * @author : zhengqing
 * @date : 2019/9/9 16:46
 */
public class IdentityCardNumberValidator implements ConstraintValidator<IdentityCardNumber, Object> {

    @Override
    public void initialize(IdentityCardNumber identityCardNumber) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return IdCardValidatorUtils.isValidate18Idcard(o.toString());
    }
    
}
