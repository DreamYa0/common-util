package com.g7.framwork.common.util.http;

/**
 * @author dreamyao
 * @title
 * @date 2019-05-09 16:26
 * @since 1.0.0
 */
@FunctionalInterface
public interface ApiCallback<T> {

    void onResponse(T response);

    default void onFailure(Throwable cause) {
    }
}