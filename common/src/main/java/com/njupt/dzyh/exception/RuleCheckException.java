/**
 * All rights Reserved, Designed By MiGu
 * Copyright:    Copyright(C) 2016-2020
 * Company       MiGu  Co., Ltd.
*/

package com.njupt.dzyh.exception;


/**
 * 类作用：自定义业务异常类.
 * 项目名称:color-common
 * 包: com.migu.colorcommon.exception
 * 类名称: BusinessException
 * 类描述: 自定义业务异常类
 * 创建人: nirui
 * 创建时间: 2017/8/02 17:17
 */

public class RuleCheckException extends BusinessException {
    /**
     * 业务异常.
     * @param resultCode 结果代码
     * @param resultDesc 结果描述
     */
    public RuleCheckException(final String resultCode, final String resultDesc) {
        super(resultCode, resultDesc);
    }
    /**
     * 业务异常.
     * @param resultCode 结果代码
     */
    public RuleCheckException(final String resultCode) {
        super(resultCode);
    }

}
