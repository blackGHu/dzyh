package com.njupt.dzyh.utils;

import com.njupt.dzyh.validator.IsMobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {IsMobileValidator.class})// 定义校验的类，规则
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
//@Repeatable(IsMobile.List.class)
public @interface IsMobile {

    // 默认必填
    boolean required() default true;

    String message() default "用户手机号码格式错误";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };


}
