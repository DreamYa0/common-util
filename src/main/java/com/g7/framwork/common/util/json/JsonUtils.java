package com.g7.framwork.common.util.json;

import com.g7.framwork.common.util.json.adpter.JacksonAdapter;
import com.g7.framwork.common.util.json.adpter.JsonAdapter;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private final static JsonAdapter ADAPTER = JacksonAdapter.getJsonAdapter();

    /**
     * 解析对象为Json字符串
     * @param src 要转换的对象
     * @return 返回对象的json字符串
     */
    public static String toJson(Object src) {
        return ADAPTER.toJson(src);
    }

    /**
     * 解析json字符串到 List<Object>
     * @param json 要解析的json字符串
     * @return 返回List
     */
    public static List<Object> fromJson(String json) {
        return ADAPTER.fromJson(json);
    }

    /**
     * 按指定的Type解析json字符串到List<T> 或 Map<String,T>
     * @param json 要解析的json字符串
     * @param type {@link TypeReference} new TypeReference<List<User>>() { }.getType()
     *             或 new TypeReference<Map<String,T>() { }.getType()
     * @return 返回List
     */
    public static <T> T fromJson(String json, final Type type) {
        return ADAPTER.fromJson(json, type);
    }

    /**
     * 解析json字符串到指定类型的对象
     * @param json  要解析的json字符串
     * @param clazz 类对象class
     * @param <T>   泛型参数类型
     * @return 返回解析后的对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return ADAPTER.fromJson(json, clazz);
    }

    /**
     * 从Map转换到对象
     * @param map   Map对象
     * @param clazz 与Map可兼容的对象类型
     * @param <T>   泛型参数类型
     * @return 返回Map转换得到的对象
     */
    public static <T> T fromJson(Map<String, Object> map, Class<T> clazz) {
        return ADAPTER.fromJson(map, clazz);
    }
}
