package com.njupt.dzyh.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 类作用：JSON转换工具类. 项目名称: campaign-service 包: com.migu.redstone.microservice.common.util 类名称: JsonUtil.java 类描述: JSON转换工具类
 * 创建人: nirui 创建时间: 2016/7/26 11:46
 */
public class JsonUtil {
    /**
     * mapper.
     */
    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * stringToObject. 〈一句话功能简述〉 〈功能详细描述〉
     *
     * @param source
     *            1
     * @param tClass
     *            1
     * @param <T>
     *            source,tClass
     * @return 1
     */
    public static <T> T stringToObject(String source, Class<T> tClass) {
        try {
            if (source == null) {
                return null;
            }
            return MAPPER.readValue(source, tClass);
        } catch (Exception e) {
            return null;
        }

    }

    /**
     * objectToString. 〈一句话功能简述〉 〈功能详细描述〉
     *
     * @param object
     *            object
     * @return 1
     */
    public static String objectToString(Object object) {
        try {
            if (object != null) {
                return MAPPER.writeValueAsString(object);
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 〈string转json〉 〈功能详细描述〉
     *
     * @param jsonStr
     *            [参数1说明]
     * @return 返回类型说明
     * @throws Exception
     *             [违例说明]
     */
    public static JSONObject stringToJson(String jsonStr) {

        if (StringUtils.isNotBlank(jsonStr)) {
            return JSONObject.fromObject(jsonStr);
        } else {
            return null;
        }
    }

    /**
     * 〈获取〉 〈功能详细描述〉
     *
     * @param eventMsg
     *            [消息]
     * @param keys
     *            [keys数组，按顺序]
     * @return 返回类型说明
     */
    public static String getValueFromJson(String eventMsg, String... keys) {
        if (keys == null || eventMsg == null) {
            return null;
        }

        try {
            JSONObject json = JSONObject.fromObject(eventMsg);
            return getValueFromJson(json, keys);
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }

    /**
     * 〈获取〉 〈功能详细描述〉
     *
     * @param json
     *            [JSONObject对象]
     * @param keys
     *            [keys数组，按顺序]
     * @return 返回类型说明
     */
    public static String getValueFromJson(JSONObject json, String... keys) {
        if (keys == null || json == null) {
            return null;
        }
        try {
            int max = keys.length - 1;
            for (int i = 0; i <= max; i++) {
                if (json == null) {
                    return null;
                }
                if (i == max) {
                    return json.getString(keys[i]);
                }
                json = json.getJSONObject(keys[i]);
            }
            return null;
        } catch (Exception e) {
            // e.printStackTrace();
            return null;
        }
    }

    /**
     * <isJsonObject>. <判断这个参数是不是Json对象>
     * 
     * @param param
     *            [param]
     * @return [返回校验结果]
     * @author jianghao
     */
    public static boolean isJsonObject(Object param) {
        try {
            if (!(param instanceof String) && stringToJson(objectToString(param)) != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static String setJsonObjectByKeys(String json, List<String> exceptKeys, Object value) {

        JSONObject jsonObject = JSONObject.fromObject(json);
        return setJsonObjectByKeys(jsonObject, exceptKeys, value).toString();
    }

    /**
     * 设置json字符串属性的值
     */
    public static JSONObject setJsonObjectByKeys(JSONObject jsonObject, List<String> exceptKeys, Object value) {

        JSONObject jo = new JSONObject();
        for (Iterator<String> it = jsonObject.keys(); it.hasNext();) {

            String key = it.next();
            String exceptKey = exceptKeys.get(0);
            if (StringUtils.equals(key, exceptKey)) {
                if (jsonObject.get(key) instanceof JSONArray) {
                    if (StringUtils.equals(exceptKey, exceptKeys.get(exceptKeys.size() - 1))) {
                        jo.put(exceptKey, value);
                    } else {
                        JSONArray ja = new JSONArray();
                        List<String> newL = exceptKeys.subList(1, exceptKeys.size());
                        for (int i = 0; i < jsonObject.getJSONArray(key).size(); i++) {
                            ja.add(setJsonObjectByKeys(jsonObject.getJSONArray(key).getJSONObject(i), newL, value));
                        }
                        jo.put(key, ja);
                    }
                } else {
                    // 判断是否为最后一个key，如果不是则需要继续递归
                    if (StringUtils.equals(exceptKey, exceptKeys.get(exceptKeys.size() - 1))) {
                        jo.put(exceptKey, value);
                    } else {
                        // 数组去除比较过的
                        List<String> newL = exceptKeys.subList(1, exceptKeys.size());
                        jo.put(key, setJsonObjectByKeys(jsonObject.getJSONObject(key), newL, value));
                    }
                }

            } else {
                jo.put(key, jsonObject.get(key));
            }
        }
        return jo;
    }

    /**
     * <isJsonStr>. <判断这个参数是不是Json字符串>
     * 
     * @param param
     *            [param]
     * @return [返回校验结果]
     * @author jianghao
     */
    public static boolean isJsonStr(Object param) {
        try {
            if (param instanceof String && stringToJson(String.valueOf(param)) != null) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 将JsonArray转换成List<Map<String,String>>对象
     * @param jsonArray
     * @return List<Map<String, String>>
     */

    public static List<Map<String, String>> convertFromJsonArrayToList(JSONArray jsonArray) {
        List<Map<String, String>> mapList = new ArrayList<>();
        try {
            if (jsonArray == null || jsonArray.isEmpty()) {
                return null;
            } else {
                for (int i = 0; i < jsonArray.size(); i++) {
                    if (jsonArray.get(i) instanceof JSONObject) {
                        mapList.add((Map<String, String>) jsonArray.get(i));
                    }
                }
                return mapList;
            }
        } catch(Exception e) {
            return null;
        }
    }

}

