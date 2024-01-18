package com.g7.framwork.common.util.extension;

/**
 * @author dreamyao
 * @description
 * @date 2017/12/13 下午7:50
 * @since 1.0.0
 */
public class ExtensionHolder<T> {

    private volatile T value;

    public void set(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

}
