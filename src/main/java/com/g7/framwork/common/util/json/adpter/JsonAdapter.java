package com.g7.framwork.common.util.json.adpter;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public interface JsonAdapter {

    List<Object> fromJson(String json);

    <T> T fromJson(String json, final Type type);

    String toJson(Object src);

    <T> T fromJson(String json, Class<T> clazz);

    <T> T fromJson(Map<String, Object> map, Class<T> clazz);
}
