package com.njupt.dzyh.service.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 一句话功能描述.
 * 项目名称:
 * 包:
 * 类名称:
 * 类描述:   类功能详细描述
 * 创建人:
 * 创建时间:
 */
public class test {

    public static String camel2Underline(String line) {
        if (line == null || "".equals(line)) {
            return "";

        }

        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));

        StringBuffer sb = new StringBuffer();

        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");

        Matcher matcher = pattern.matcher(line);

        while (matcher.find()) {
            String word = matcher.group();

            sb.append(word.toLowerCase());

            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String key = "goodsUseStatus";
        key = camel2Underline(key);
        System.out.println(key);
    }
}
