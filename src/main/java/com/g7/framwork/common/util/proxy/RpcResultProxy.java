package com.g7.framwork.common.util.proxy;

import com.alibaba.dubbo.remoting.exchange.ResponseCallback;
import com.alibaba.dubbo.rpc.RpcContext;
import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.dubbo.rpc.RpcResult;
import com.alibaba.dubbo.rpc.protocol.dubbo.FutureAdapter;
import com.g7.framework.common.dto.BaseResult;
import com.g7.framework.common.dto.Result;
import com.g7.framework.framwork.exception.BusinessException;
import com.g7.framework.framwork.exception.meta.CodeMeta;
import com.g7.framework.framwork.exception.meta.CommonErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author dreamyao
 * @title
 * @date 2018/11/27 5:07 PM
 * @since 1.0.0
 */
public final class RpcResultProxy {

    private static final Logger logger = LoggerFactory.getLogger(RpcResultProxy.class);

    private RpcResultProxy() {

    }

    /**
     * dubbo接口异步调用 需要使用 @Reference(async = true)
     * @param executor dubbo接口调用
     * @param callback 监听接口结果回调
     * @param <T>      dubbo接口返回值 新项目接口返回值为 Result<T> 中的 T 老项目就是对应接口的返回值
     */
    @SuppressWarnings("unchecked")
    public <T> void asyncProxy(Executor executor, Callback<T> callback) {

        executor.execute();

        ((FutureAdapter<?>) RpcContext.getContext().getFuture()).getFuture().setCallback(new ResponseCallback() {
            @Override
            public void done(Object response) {
                RpcResult rpcResult = (RpcResult) response;
                final Object obj = rpcResult.getValue();
                if (obj instanceof Result) {
                    Result<T> result = (Result<T>) obj;
                    callback.result(proxy(result));
                } else {
                    callback.result((T) obj);
                }
            }

            @Override
            public void caught(Throwable exception) {
                callback.caught(exception);
            }
        });
    }

    /**
     * dubbo接口异步调用 需要使用 @Reference(async = true)
     * @param executor dubbo接口调用
     * @param <T>      dubbo接口返回值 新项目接口返回值为 Result<T> 老项目就是对应接口的返回值
     */
    public <T> FutureAdapter<T> asyncProxy(Executor executor) {
        executor.execute();
        return (FutureAdapter<T>) RpcContext.getContext().getFuture();
    }

    /**
     * dubbo接口异步调用 需要使用 @Reference 不需要指定 async = true
     * @param supplier dubbo接口调用
     * @param <T>      dubbo接口返回值 新项目接口返回值为 Result<T> 老项目就是对应接口的返回值
     */
    public <T> Future<T> asyncProxy(Supplier<T> supplier) {
        return RpcContext.getContext().asyncCall(supplier::get);
    }

    public static <R> R proxy(Result<R> result) {

        if (Objects.isNull(result)) {
            logger.error("rpc result is null.");
            throw new BusinessException(CommonErrorCode.RPC_CALL_EXCEPTION);
        }

        Function<Result<R>, R> function = resultObj -> {

            if (Boolean.FALSE.equals(resultObj.isSuccess())) {

                String code = result.getCode();
                String description = result.getDescription();
                isError(code, description);

                throw new BusinessException(CodeMeta.builder().code(code).msgZhCN(description).build());
            }
            return resultObj.getData();

        };
        return function.apply(result);
    }

    public static <R> R proxy(Result<R> result, Function<Result<R>, R> function) {
        return function.apply(result);
    }

    /**
     * 三方接口调用代理类
     * @param supplier 生产者
     * @param isCatch  是否捕获异常 true 表示捕获异常不抛出 不传或传入false 表示要抛出异常
     * @param <R>      对应返回结果的类型
     * @return 对应返回结果
     */
    public static <R> R proxy(Supplier<Result<R>> supplier, final boolean... isCatch) {

        if (isCatch.length > 0) {

            boolean needCatch = isCatch[0];

            if (needCatch) {

                try {

                    Result<R> result = supplier.get();
                    if (Boolean.FALSE.equals(result.isSuccess())) {

                        String code = result.getCode();
                        String description = result.getDescription();
                        isError(code, description);
                    }

                    // 如果 isSuccess 为false 这里返回的就是 null 否则返回 R
                    return result.getData();

                } catch (RpcException rpc) {
                    // dubbo 调用异常
                    recordRpcException(rpc);
                } catch (Exception e) {

                    logger.error("Unknown exception, exception is ", e);
                }

                return null;
            }
        }

        return proxy(proxyThrow(supplier));
    }

    public static <R> Result<R> proxyThrow(Supplier<Result<R>> supplier) {

        Result<R> result;

        try {

            result = supplier.get();

        } catch (RpcException rpc) {

            // dubbo 调用异常
            if (rpc.isTimeout()) {
                logger.error("Rpc call timeout exception , exception is ", rpc);
                throw new BusinessException(CommonErrorCode.BUSY_SERVICE);
            } else if (rpc.isNetwork()) {
                logger.error("Rpc network exception , exception is ", rpc);
                throw new BusinessException(CommonErrorCode.NETWORK_CONNECT_FAILED);
            } else if (rpc.isSerialization()) {
                logger.error("Rpc call serialization exception , exception is ", rpc);
                throw new BusinessException(CommonErrorCode.SERIALIZATION_EXCEPTION);
            } else if (rpc.isForbidded()) {
                logger.error("Rpc call forbidden exception , exception is ", rpc);
                throw new BusinessException(CommonErrorCode.FORBIDDEN_EXCEPTION);
            } else {
                logger.error("Rpc call exception , exception is ", rpc);
                throw new BusinessException(CommonErrorCode.RPC_CALL_EXCEPTION);
            }
        } catch (Exception e) {
            logger.error("Rpc call exception, exception is ", e);
            throw new BusinessException(CommonErrorCode.RPC_CALL_EXCEPTION);
        }

        return result;
    }

    /**
     * 三方接口调用代理类（当三方接口调用失败时执行自定义回调操作）
     * @param supplier 生产者
     * @param callback 生产者
     * @param <R>      类型
     * @return 对应返回结果
     */
    public static <R> R proxy(Supplier<Result<R>> supplier, Supplier<R> callback) {

        try {

            Result<R> result = supplier.get();
            if (Boolean.FALSE.equals(result.isSuccess())) {

                String code = result.getCode();
                String description = result.getDescription();
                isError(code, description);

                // 执行回调
                try {
                    return callback.get();
                } catch (Exception e) {
                    logger.error("Run call back failed.", e);
                }
            }

            // 如果 isSuccess 为false 这里返回的就是 null 否则返回 R
            return result.getData();

        } catch (RpcException rpc) {
            // dubbo 调用异常
            recordRpcException(rpc);
        } catch (Exception e) {
            logger.error("Unknown exception, exception is ", e);
        }

        return null;
    }

    /**
     * 三方接口调用代理类（当三方接口调用失败时执行自定义回调操作）
     * @param supplier 生产者
     * @param callback 生产者
     * @param <R>      类型
     * @return 对应返回结果
     */
    public static <R> R proxy(Supplier<Result<R>> supplier, Function<BaseResult, R> callback) {

        try {

            Result<R> result = supplier.get();
            if (Boolean.FALSE.equals(result.isSuccess())) {

                String code = result.getCode();
                String description = result.getDescription();
                isError(code, description);

                // 执行回调
                try {
                    return callback.apply(result);
                } catch (Exception e) {
                    logger.error("Run call back failed.", e);
                }
            }

            // 如果 isSuccess 为false 这里返回的就是 null 否则返回 R
            return result.getData();

        } catch (RpcException rpc) {
            // dubbo 调用异常
            recordRpcException(rpc);
        } catch (Exception e) {
            logger.error("Unknown exception, exception is ", e);
        }

        return null;
    }

    public static <R> R proxy4Old(Supplier<R> supplier, final boolean... isCatch) {

        if (isCatch.length > 0) {

            boolean needCatch = isCatch[0];

            if (needCatch) {

                try {

                    return supplier.get();

                } catch (RpcException rpc) {
                    // dubbo 调用异常
                    recordRpcException(rpc);
                } catch (Exception e) {
                    logger.error("Unknown exception, exception is ", e);
                }

                return null;
            }
        }

        return proxyThrow4Old(supplier);
    }

    public static <R> R proxyThrow4Old(Supplier<R> supplier) {

        try {

            return supplier.get();

        } catch (RpcException rpc) {

            // dubbo 调用异常
            if (rpc.isTimeout()) {
                logger.error("Rpc call timeout exception , exception is ", rpc);
                throw new BusinessException(CommonErrorCode.BUSY_SERVICE);
            }

            if (rpc.isNetwork()) {
                logger.error("Rpc network exception , exception is ", rpc);
                throw new BusinessException(CommonErrorCode.NETWORK_CONNECT_FAILED);
            }

            if (rpc.isSerialization()) {
                logger.error("Rpc call serialization exception , exception is ", rpc);
                throw new BusinessException(CommonErrorCode.SERIALIZATION_EXCEPTION);
            }

            if (rpc.isForbidded()) {
                logger.error("Rpc call forbidden exception , exception is ", rpc);
                throw new BusinessException(CommonErrorCode.FORBIDDEN_EXCEPTION);
            }

            logger.error("Rpc call exception , exception is ", rpc);
            throw new BusinessException(CommonErrorCode.RPC_CALL_EXCEPTION);

        } catch (Exception e) {
            logger.error("Rpc call exception , exception is ", e);
            throw new BusinessException(buildCodeMeta(e.getMessage()));
        }
    }

    private static CodeMeta buildCodeMeta(String message) {

        if (Boolean.FALSE.equals(StringUtils.isEmpty(message)) && message.contains("[_") && message.contains("_]")) {

            int begin = message.indexOf("[_") + 2;
            int end = message.indexOf("_]");

            String errorMessage = message.substring(begin, end);
            String[] split = errorMessage.split(":");

            String code = split[0];
            if (split.length == 2) {
                return new CodeMeta(code, "ERROR", split[1]);
            }

            if (split.length > 2) {
                StringBuilder builder = new StringBuilder();
                for (int i = 1; i < split.length; i++) {
                    builder.append(split[i]);
                }
                return new CodeMeta(code, "ERROR", builder.toString());
            }

            return new CodeMeta(CommonErrorCode.REMOTE_SERVICE.getCode(), errorMessage);
        }

        return CommonErrorCode.REMOTE_SERVICE.setMsgZhCN(message);
    }

    private static void recordRpcException(RpcException rpc) {
        // dubbo 调用异常
        if (rpc.isTimeout()) {
            logger.error("Rpc call timeout exception , exception is ", rpc);
        } else if (rpc.isNetwork()) {
            logger.error("Rpc network exception , exception is ", rpc);
        } else if (rpc.isSerialization()) {
            logger.error("Rpc call serialization exception , exception is ", rpc);
        } else if (rpc.isForbidded()) {
            logger.error("Rpc call forbidden exception , exception is ", rpc);
        } else {
            logger.error("Rpc call biz exception , exception is ", rpc);
        }
    }

    private static void isError(String code, String description) {
        if (!StringUtils.isEmpty(code)) {
            int codeInt = Integer.parseInt(code);
            // 错误码2000 到 3000 需要告警
            if (2000 <= codeInt && codeInt <= 3000) {
                logger.error("Rpc return result failed , code is [{}] , description is [{}]", code, description);
            } else {
                logger.info("Rpc return result failed , code is [{}] , description is [{}]", code, description);
            }
        }
    }
}
