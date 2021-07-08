/**
 * All rights Reserved, Designed By MiGu
 * Copyright:    Copyright(C) 2016-2020
 * Company       MiGu  Co., Ltd.
*/

package com.njupt.dzyh.exception;

import com.njupt.dzyh.utils.CommonResult;
import lombok.Data;

/**
 * 类作用：自定义异常基类.
 * 项目名称:color-common
 * 包: com.migu.colorcommon.exception
 * 类名称: BaseException
 * 类描述: 自定义异常基类
 * 创建人: nirui
 * 创建时间: 2017/8/02 17:17
 */

@Data
public class BaseException extends Exception {
    /**
     * 共同的结果.
     */
    private CommonResult result;
    /**
     * 基本异常.
     * @param resultCode 结果代码
     * @param resultDesc 结果描述
     */
    public BaseException(final String resultCode, final String resultDesc) {
        result = new CommonResult(resultCode, resultDesc,null);
    }
    /**
     * 基本异常.
     * @param resultCode 结果代码
     */
    public BaseException(final String resultCode) {
        result = new CommonResult(resultCode, null,null);
    }
}
