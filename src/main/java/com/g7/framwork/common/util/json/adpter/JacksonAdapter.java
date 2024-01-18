package com.g7.framwork.common.util.json.adpter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JacksonAdapter implements JsonAdapter {

    @Override
    @SuppressWarnings("unchecked")
    public List<Object> fromJson(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, ArrayList.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(String json, Type type) {
        TypeReference<T> typeReference = new TypeReference<T>() {
            @Override
            public Type getType() {
                return type;
            }
        };
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toJson(Object src) {
        try {
            return OBJECT_MAPPER.writeValueAsString(src);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    @Override
    public <T> T fromJson(Map<String, Object> map, Class<T> clazz) {
        return OBJECT_MAPPER.convertValue(map, clazz);
    }

    public ObjectMapper objectMapper() {
        return OBJECT_MAPPER;
    }

    public static final ObjectMapper OBJECT_MAPPER = JsonMapper.builder()
            // 反序列化时使用对象属性的顺序
            .configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true)
            // 在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            // 允许解析的内容或不中使用了Java / C ++风格的注释行（“/” +“*”和“//”品种）
            .configure(JsonParser.Feature.ALLOW_COMMENTS, true)
            // 允许使用未引用的字段名
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            // 允许使用单引号（撇号，字符“\”“）的引用字符串（名称和字符串值）
            .configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true)
            // 允许反序列Json字符串中属性名没有双引号
            .configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true)
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            // 允许解析能识别JSON串里的注释符
            .configure(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true)
            // 允许浮点类型 或 数字类型都可以接受NaN值
            .configure(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS, true)
            // 解决JSON字符串的age值是"0"为开头的数字，objectMapper默认是不能解析的
            .configure(JsonReadFeature.ALLOW_LEADING_ZEROS_FOR_NUMBERS, true)
            // 允许Json反序列化可以解析单引号包住的属性名称和字符串值
            .configure(JsonReadFeature.ALLOW_SINGLE_QUOTES, true)
            // 反序列Json字符串中包含制控制字符
            .configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS, true)
            .build()
            .findAndRegisterModules()
            // 关闭空对象不让序列化功能
            .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
            // 禁止时间格式序列化为时间戳
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            // 时间格式
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

    // 线程安全的单列模式
    private static class InstanceHolder {
        public final static JacksonAdapter JACKSON_ADAPTER = new JacksonAdapter();
    }

    public static JsonAdapter getJsonAdapter() {
        return InstanceHolder.JACKSON_ADAPTER;
    }
}
