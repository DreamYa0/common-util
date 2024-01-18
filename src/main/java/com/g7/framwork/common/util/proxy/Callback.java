package com.g7.framwork.common.util.proxy;

import com.g7.framework.framwork.exception.BusinessException;
import com.g7.framework.framwork.exception.meta.CommonErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author dreamyao
 * @title
 * @date 2022/1/25 11:35 上午
 * @since 1.0.0
 */
@FunctionalInterface
public interface Callback<T> {

     Logger logger = LoggerFactory.getLogger(Callback.class);

    void result(T result);

    /**
     * caught exception.
     *
     * @param exception
     */
    default void caught(Throwable exception) {
        logger.error("Rpc call exception, exception is ", exception);
        throw new BusinessException(CommonErrorCode.RPC_CALL_EXCEPTION);
    }
}
