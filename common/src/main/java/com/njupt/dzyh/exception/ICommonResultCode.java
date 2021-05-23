/**
 * All rights Reserved, Designed By MiGu
 * Copyright:    Copyright(C) 2016-2020
 * Company       MiGu  Co., Ltd.
*/

package com.njupt.dzyh.exception;

/**
 * 错误码类.
 * 项目名称:  咪咕营销
 * 包:        com.migu.redstone.microservice.common.constants
 * 类名称:    IResultCode.java
 * 类描述:    本类是一个错误码类，定义了业务异常情况下的错误码
 * 创建人:    liaolei@asiainfo.com
 * 创建时间:     2017/7/27 10:47
 */
public interface ICommonResultCode {

    /**
     * TODO系统异常到时候要给一个编号的范围.
     */
    class SystemResultCode {
        /**
         * 系统结果代码.
         */
        private SystemResultCode() {
         }
        /**
        * 集群编号未获取到.
        */
        public static final String CLUSTER_ID_ERROR = "1000000001";
        /**
         * 序列化出错.
         */
        public static final String SERIALIZE_ERROR = "1000000002";
        /**
         * 反序列化出错 .
         */
        public static final String DESERIALIZE_ERROR = "1000000003";
        /**
         * 入参校验出错.
         */
        public static final String PARAM_BIND_ERROR = "1000000004";
        /**
         * TODO 这里面需要定义一个公共的返回码.
         */
        /**
         * 其他未知错误.
         */
        public static final String UNKNOW_ERROR = "1000599999";

    }

}

