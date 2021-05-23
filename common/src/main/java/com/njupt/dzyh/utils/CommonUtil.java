/**
 * All rights Reserved, Designed By MiGu
 * Copyright:    Copyright(C) 2016-2020
 * Company       MiGu  Co., Ltd.
 */

package com.njupt.dzyh.utils;


import com.njupt.dzyh.utils.concasts.CommonConst;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Random;

/**
 * 项目名称: common. 包: com.migu.redstone.microservice.common.util 类名称:
 * CommonUtil.java 类描述: 创建人: wangqian 创建时间: 2017年7月29日 下午12:53:58
 */
public final class CommonUtil {
	/**
	 * CommonUtil 私有构造方法，解决checkstyle问题.
	 */
	private CommonUtil() {

	}

	/**
	 * .
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(CommonUtil.class);


	/**
	 * 类名称: isNull. 类描述: 判断对象是否为空 创建人: shenhf 创建时间: 2017年8月28日 上午11:05:45
	 *
	 * @param obj
	 *            Object
	 * @return boolean 对象不为空，返回true.反之返回false
	 */
	public static boolean isNull(final Object obj) {
		return obj == null;
	}

	/**
	 * 类名称: isNotNull. 类描述: 判断对象是否不为空 创建人: shenhf 创建时间: 2017年8月28日 上午11:06:03
	 *
	 * @param obj
	 *            Object
	 * @return boolean
	 */
	public static boolean isNotNull(final Object obj) {
		return !isNull(obj);
	}

	/**
	 * 〈给查询加上state=1的条件〉. 〈给查询加上state=1的条件〉
	 *
	 * @param obj
	 *            [查询条件对象 ]
	 */
	public static void andState(final Object obj) {
		Class c = obj.getClass();
		try {
			Method m = c.getMethod(CommonConst.ANDSTATE.METHOD_NAME, String.class);
			m.invoke(obj, CommonConst.ANDSTATE.METHOD_VAL);
		} catch (RuntimeException e) {
			LOGGER.error("Method is null,obj" + obj.toString());
		} catch (Exception e) {
			LOGGER.error("Method is null,obj" + obj.toString());
		}
	}

	/**
	 *
	  *〈生成随机数〉
	  *〈功能详细描述〉
	  * @param
	 */
	public static String getRandomCode(int number) {
		String code = "";
		Random random = new Random();
		StringBuffer bufferStr = new StringBuffer();
		bufferStr.append(code);
		for (int i = 0; i < number; i++) {
			int r = random.nextInt(10); // 每次随机出一个数字（0-9）
			bufferStr.append(String.valueOf(r));
		}
		code = bufferStr.toString();
		return code;
	}
}
