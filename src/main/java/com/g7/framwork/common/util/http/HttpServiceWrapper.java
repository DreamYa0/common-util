package com.g7.framwork.common.util.http;

import com.g7.framwork.common.util.http.converter.JacksonConverterFactory;
import com.g7.framwork.common.util.http.security.AuthenticationInterceptor;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author dreamyao
 * @title 生成基于的API实现
 * @date 2019-05-09 16:26
 * @since 1.0.0
 */
public class HttpServiceWrapper {

    private static final Logger logger = LoggerFactory.getLogger(HttpServiceWrapper.class);

    public static <S> S wrapper(Class<S> serviceClass, String baseUrl) {
        return wrapper(serviceClass, null, null, baseUrl);
    }

    public static <S> S wrapper(Class<S> serviceClass,
                                String apiKey,
                                String secret,
                                String baseUrl) {

        return wrapper(serviceClass, getBuilder(), apiKey, secret, baseUrl);
    }

    public static <S> S wrapper(Class<S> serviceClass,
                                OkHttpClient.Builder builder,
                                String baseUrl) {

        return wrapper(serviceClass, builder, null, null, baseUrl);
    }

    public static <S> S wrapper(Class<S> serviceClass,
                                OkHttpClient.Builder builder,
                                String apiKey,
                                String secret,
                                String baseUrl) {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(baseUrl);
        return buildRetrofit(serviceClass, builder, apiKey, secret, retrofitBuilder);
    }

    public static <S> S wrapper(Class<S> serviceClass,
                                HttpUrl baseUrl) {

        return wrapper(serviceClass, null, null, baseUrl);
    }

    public static <S> S wrapper(Class<S> serviceClass,
                                String apiKey,
                                String secret,
                                HttpUrl baseUrl) {

        return wrapper(serviceClass, getBuilder(), apiKey, secret, baseUrl);
    }

    public static <S> S wrapper(Class<S> serviceClass,
                                OkHttpClient.Builder builder,
                                HttpUrl baseUrl) {

        return wrapper(serviceClass, builder, null, null, baseUrl);
    }

    public static <S> S wrapper(Class<S> serviceClass,
                                OkHttpClient.Builder builder,
                                String apiKey, String secret,
                                HttpUrl baseUrl) {

        Retrofit.Builder retrofitBuilder = new Retrofit.Builder().baseUrl(baseUrl);
        return buildRetrofit(serviceClass, builder, apiKey, secret, retrofitBuilder);
    }

    private static <S> S buildRetrofit(Class<S> serviceClass,
                                       OkHttpClient.Builder builder,
                                       String apiKey,
                                       String secret,
                                       Retrofit.Builder retrofitBuilder) {

        retrofitBuilder.addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create());

        if (StringUtils.isEmpty(apiKey) || StringUtils.isEmpty(secret)) {
            retrofitBuilder.client(builder.build());
        } else {
            // `adaptClient`将使用自己的拦截器，但与'prant'客户端共享线程池等
            AuthenticationInterceptor interceptor = new AuthenticationInterceptor(apiKey, secret);
            OkHttpClient adaptedClient = builder.addInterceptor(interceptor).build();
            retrofitBuilder.client(adaptedClient);
        }

        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(serviceClass);
    }

    /**
     * 执行REST调用并阻止，直到收到响应
     */
    public static <T> T sync(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("Http request failed.", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 异步REST调用
     * @param call
     * @param apiCallback
     * @param <T>
     */
    public static <T> void async(Call<T> call, ApiCallback<T> apiCallback) {
        call.enqueue(new CallbackAdapter<>(apiCallback));
    }

    private static class InstanceHolder {

        // 单列模式
        public static final OkHttpClient.Builder SHARED_CLIENT_BUILDER = new OkHttpClient.Builder()
                // 调度策略,默认最大并发数默认为 64，但个域名最大请求数 默认为 5 个
                .dispatcher(new Dispatcher())
                //
                .pingInterval(20, TimeUnit.SECONDS)
                // 连接池,默认5个空闲连接，连接保活5分钟
                .connectionPool(new ConnectionPool())
                // 设置连接超时
                .connectTimeout(15, TimeUnit.SECONDS)
                // 设置读超时
                .readTimeout(20, TimeUnit.SECONDS)
                // 设置写超时
                .writeTimeout(20, TimeUnit.SECONDS)
                // 整个调用时期的超时时间，包括解析DNS、链接、写入请求体、服务端处理、以及读取响应结果
                .callTimeout(120, TimeUnit.SECONDS)
                // 将此设置为 false 以避免在这样做具有破坏性时重试请求。 在这种情况下，调用应用程序应自行恢复连接故障。
                .retryOnConnectionFailure(true);
    }

    /**
     * 返回默认的OkHttpClient实例
     */
    public static OkHttpClient.Builder getBuilder() {
        return InstanceHolder.SHARED_CLIENT_BUILDER;
    }

    /**
     * 获取调度器
     * @param maxRequests        请求最大并发数
     * @param maxRequestsPerHost 单个域名的最大并发请求数
     * @return 调度器
     */
    public static Dispatcher getDispatcher(int maxRequests, int maxRequestsPerHost) {
        Dispatcher dispatcher = new Dispatcher();
        dispatcher.setMaxRequests(maxRequests);
        dispatcher.setMaxRequestsPerHost(maxRequestsPerHost);
        return dispatcher;
    }

    public static class HeaderConnectionInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request()
                    .newBuilder()
                    // 每个请求在完成时关闭连接
                    .addHeader("Connection", "close")
                    .build();
            return chain.proceed(request);
        }
    }
}