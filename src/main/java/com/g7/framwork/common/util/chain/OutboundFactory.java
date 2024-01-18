package com.g7.framwork.common.util.chain;

/**
 * 出参工厂
 */
public interface OutboundFactory<T> {

    T newInstance();
}
