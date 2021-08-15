package com.njupt.dzyh.utils;

import java.util.UUID;

/**
 * 一句话功能描述.
 * 项目名称:
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:
 * 创建时间:
 */
public class UUIDGenerator {
    /**
     * 获取32位UUID字符串
     * @return
     */
    public static String getUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取32位UUID大写字符串
     * @return
     */
    public static String getUpperCaseUUID(){
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public static void main(String[] args) {
        System.out.println(UUIDGenerator.getUUID());
    }

}
