/**
 * All rights Reserved, Designed By MiGu
 * Copyright:    Copyright(C) 2016-2020
 * Company       MiGu  Co., Ltd.
*/

package com.njupt.dzyh.utils;

import com.njupt.dzyh.enums.CommonResultEm;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 项目名称:  color-common.
 * 包:        com.migu.colorcommon.entity
 * 类名称:    Result
 * 类描述:    结果对象,只关心错误的result不关注正确的resultcode，正确的resultcode各中心都不一样。
 * 创建人:    nirui
 * 创建时间:   2017年7月29日 下午12:53:58
 */
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonResult {
    /**
     * 结果代码.
     */
    private String resultCode;
    /**
     * 结果消息.
     */
    private String resultMessage;

//    private Object obj;

    public static CommonResult success(){
        return new CommonResult(CommonResultEm.SUCCESS.getEcode(),CommonResultEm.SUCCESS.getEmsg());

    }
//    public static CommonResult success(Object obj){
//        return new CommonResult(CommonResultEm.SUCCESS.getEcode(),CommonResultEm.SUCCESS.getEmsg(),obj);
//
//    }

//    public static ResponceBean error(){
//        return new ResponceBean(RespBeanEm.ERROR.getEcode(),RespBeanEm.ERROR.getEmsg(),null);
//
//    }

    public static CommonResult error(CommonResultEm respBeanEm){
        return new CommonResult(respBeanEm.getEcode(),respBeanEm.getEmsg());

    }

//    public static CommonResult error(CommonResultEm respBeanEm,Object obj){
//        return new CommonResult(respBeanEm.getEcode(),respBeanEm.getEmsg(),obj);
//
//    }

}
