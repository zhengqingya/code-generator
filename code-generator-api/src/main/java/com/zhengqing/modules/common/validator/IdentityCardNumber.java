package com.zhengqing.modules.common.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 *  <p> 自定义身份证校验 注解 </p>
 *
 * @description : 作用在Field字段上，运行时生效，触发IdentityCardNumber验证类
 *                  message 定制化的提示信息，主要是从ValidationMessages.properties里提取，也可以依据实际情况进行定制
 *                  groups 这里主要进行将validator进行分类，不同的类group中会执行不同的validator操作
 *                  payload 主要是针对bean的，使用不多。
 * @author : zhengqing
 * @date : 2019/9/9 16:43
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IdentityCardNumberValidator.class)
public @interface IdentityCardNumber {

    String message() default "身份证号码不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
