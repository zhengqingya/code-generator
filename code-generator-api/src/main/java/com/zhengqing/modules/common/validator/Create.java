package com.zhengqing.modules.common.validator;

import javax.validation.groups.Default;

/**
 *  <p> 使用groups的校验 </p>
 *
 * @description : 同一个对象要复用,比如UserDTO在更新时候要校验userId,在保存的时候不需要校验userId,在两种情况下都要校验username,那就用上groups了
 * 在需要校验的地方@Validated声明校验组 ` update(@RequestBody @Validated(Update.class) UserDTO userDTO) `
 * 在DTO中的字段上定义好groups = {}的分组类型     `  @NotNull(message = "用户id不能为空", groups = Update.class)       或  groups = {Create.class, Update.class}
 *                                              private Long userId; `
 *  【注】注意:在声明分组的时候尽量加上 extend javax.validation.groups.Default 否则,在你声明@Validated(Update.class)的时候,就会出现你在默认没添加groups = {}的时候的校验组@Email(message = "邮箱格式不对"),会不去校验,因为默认的校验组是groups = {Default.class}.
 * @author : zhengqing
 * @date : 2019/9/9 16:51
 */
public interface Create extends Default {
}
