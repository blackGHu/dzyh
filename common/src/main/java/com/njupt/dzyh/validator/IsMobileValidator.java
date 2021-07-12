package com.njupt.dzyh.validator;

import com.njupt.dzyh.utils.IsMobile;
import com.njupt.dzyh.utils.validatorUtil;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 项目名称:  手机号码校验类
 *
 * 实现   ConstraintValidator<IsMobile,String>接口
 *         该接口有两个泛型  第一个为注解类
 */
public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return validatorUtil.isMobile(s);
        }else {
            // 如果非必填，则为空也可以
            if (StringUtils.isEmpty(s)) {
                return true;
            }else {
                return validatorUtil.isMobile(s);
            }
        }
    }
}