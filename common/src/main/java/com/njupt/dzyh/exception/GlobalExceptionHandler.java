/**
 * All rights Reserved, Designed By MiGu
 * Copyright:    Copyright(C) 2016-2020
 * Company       MiGu  Co., Ltd.
*/

package com.njupt.dzyh.exception;


import com.njupt.dzyh.enums.CommonResultEm;
import com.njupt.dzyh.utils.CommonResponse;
import com.njupt.dzyh.utils.CommonResult;
import com.njupt.dzyh.utils.MessageUtil;
import com.njupt.dzyh.utils.ReflectUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * 类作用：统一异常处理类.
 * 项目名称:color-common
 * 包: com.migu.colorcommon.exception
 * 类名称: GlobalExceptionHandler
 * 类描述: 统一异常处理类
 * 创建人: nirui
 * 创建时间: 2017/8/17 17:17
 */

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 处理绑定异常.
     * @param bindException 绑定异常
     * @return 返回错误信息
     * @throws Exception [违例说明]
     */
    @ExceptionHandler(BindException.class)
    public @ResponseBody
    CommonResponse handleBindException(final BindException bindException) throws Exception {
        /**
         * 按照规范：打印异常日志.
         */
//        bindException.printStackTrace();
        LOGGER.error("handleBindException="+bindException.getMessage(), bindException);
       // System.out.println(bindException);

        CommonResponse response = new CommonResponse();
        FieldError fieldError = bindException.getFieldError();
        //构造出错信息
        StringBuffer sb = new StringBuffer("入参字段值:");
        sb.append(fieldError.getRejectedValue()).append("与入参字段:")
        .append(fieldError.getField()).append("的类型不匹配,请处理!");
        CommonResult result = new CommonResult(ICommonResultCode
              .SystemResultCode.PARAM_BIND_ERROR, sb.toString(),null);
        response.setResult(result);
        LOGGER.error("handleBindException:{}",response);
        return response;
    }

    /**
     * 处理Http消息不可读的异常.
     * @param notReadableException 不可读异常
     * @return 返回错误信息
     * @throws Exception [违例说明]
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public @ResponseBody CommonResponse
    handleHttpMessageNotReadableException(
            final HttpMessageNotReadableException notReadableException)
                    throws Exception {
        //按照规范：打印异常日志
//        notReadableException.printStackTrace();
        LOGGER.error("handleHttpMessageNotReadableException=" + notReadableException.getMessage(),
                notReadableException);

        CommonResponse response = new CommonResponse();
        Throwable throwable = notReadableException.getCause();
        String filedValue = null;
        //String filedName = null;
        try {
            filedValue = (String) ReflectUtil.getValue(throwable, "_value");
        } catch (Exception e) {
            LOGGER.error("handleHttpMessageNotReadableException-exception=" + e.getMessage(), e);
        }
        /*JsonMappingException jsonMappingException = (
         * JsonMappingException)throwable;
        List path = (List) ReflectUtil.getValue(jsonMappingException,"_path");
        if(path!=null && path.size()>0){
            filedName = (String)ReflectUtil.getValue(path.get(0),"_fieldName");
        }*/
        //构造出错信息
        StringBuffer sb = null;
        if (filedValue != null) {
            sb = new StringBuffer("入参字段值:")
                    .append(filedValue).append("与入参的类型不匹配,请处理!");
        } else {
            //这种情况下捕获不到参数值，以为没有“”自动分割不了
            sb = new StringBuffer("入参字段值格式转换错误,请检查重新输入");
        }

        CommonResult result = new CommonResult(ICommonResultCode
                .SystemResultCode.PARAM_BIND_ERROR, sb.toString(),null);
        response.setResult(result);
        LOGGER.error("handleHttpMessageNotReadableException:{}",response);
        return response;
    }

    /**'
     * 处理异常.
     * @param throwable 可抛出
     * @return 返回错误信息
     * @throws Exception [违例说明]
     */
    @ExceptionHandler(Throwable.class)
    public @ResponseBody CommonResponse
    handleException(final Throwable throwable, HttpServletResponse resp) throws Exception {
        //按照规范：打印异常日志
//        throwable.printStackTrace();
        LOGGER.error("handleException={}", throwable.getMessage());

        //System.out.println(throwable.);
        CommonResponse response = new CommonResponse();
        CommonResult result = null;



        if (throwable instanceof SystemException) {
            result = ((BaseException) throwable).getResult();
        } else if (throwable instanceof BusinessException) {
            result = ((BaseException) throwable).getResult();
            if (result != null && result.getResultCode() != null) {
                String message = MessageUtil
                        .resultCode2Message(result.getResultCode());
                if (StringUtils.isNotBlank(message)) {
                    result.setResultMessage(message);
                }
            }
        } else if (throwable instanceof MethodArgumentNotValidException) {
            boolean flag = true;
            StringBuffer bf = new StringBuffer();
            BindingResult bindingResult = ((MethodArgumentNotValidException)
                    throwable).getBindingResult();
            List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrorList) {
                if (flag) {
                    bf.append(fieldError.getDefaultMessage());
                    flag = false;
                } else {
                    bf.append(";").append(fieldError.getDefaultMessage());
                }
            }
            result =  new CommonResult(ICommonResultCode
                    .SystemResultCode.PARAM_BIND_ERROR, bf.toString(),null);
        } else {
            //未识别的错误
            result = new CommonResult(ICommonResultCode
                    .SystemResultCode.UNKNOW_ERROR, "其他未知错误！",null);
        }
        response.setResult(result);

        LOGGER.error("handleException:{}",response);

        String msg = throwable.toString();
        if(msg.indexOf("org.apache.shiro.authz.UnauthenticatedException")>-1){
            LOGGER.info("未登录");
            result.setResultCode("600");
            result.setResultMessage("请先登录");
            result.setObj("请先登录");
            response.setResult(result);
            resp.setStatus(600);
        }


        return response;
    }
}

