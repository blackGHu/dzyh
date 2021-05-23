/**
 * All rights Reserved, Designed By MiGu
 * Copyright: Copyright(C) 2016-2020
 * Company MiGu Co., Ltd.
 */

package com.njupt.dzyh.exception;

/**
 * 类作用：自定义系统异常类.
 * 项目名称:color-common
 * 包: com.njupt.dzyh.exception
 * 类名称: SystemException
 * 类描述: 自定义系统异常类
 */

public class SystemException extends BaseException {
/**
 * 系统异常.
 * @param resultCode 结果编码
 * @param resultDesc 结果描述
 */
    public SystemException(final String resultCode, final String resultDesc) {
        super(resultCode, resultDesc);
    }
/**
 * 系统异常.
 * @param resultCode 结果编码
 */
    public SystemException(String resultCode) {
        super(resultCode);
    }
}

