package com.g7.framwork.common.util.json;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeReference<T> implements Comparable<TypeReference<T>> {

    private final Type type;

    public TypeReference() {

        Type superClass = getClass().getGenericSuperclass();
        // sanity check, should never happen
        if (superClass instanceof Class<?>) {
            throw new IllegalArgumentException("Internal error: TypeReference constructed without " +
                    "actual type information");
        }

        type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }

    @Override
    public int compareTo(TypeReference<T> o) {
        return 0;
    }
}
