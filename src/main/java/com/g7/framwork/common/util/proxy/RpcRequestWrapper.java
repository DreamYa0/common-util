package com.g7.framwork.common.util.proxy;

import com.g7.framework.common.dto.Request;

/**
 * @author dreamyao
 * @title 公共入参包装器
 * @date 2019/9/30 10:44 AM
 * @since 1.0.0
 */
public final class RpcRequestWrapper {

    private RpcRequestWrapper() {

    }

    public static <T> Request<T> wrapper(T req) {
        Request<T> request = Request.create();
        request.setData(req);
        return request;
    }
}
