package com.g7.framwork.common.util.http;

import com.g7.framework.framwork.exception.BusinessException;
import com.g7.framework.framwork.exception.meta.CommonErrorCode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author dreamyao
 * @title 一个适配器/包装器，它将Callback从Retrofit转换为暴露给客户端的ApiCallback。
 * @date 2019-05-09 16:26
 * @since 1.0.0
 */
public class CallbackAdapter<T> implements Callback<T> {

    private final ApiCallback<T> apiCallback;

    public CallbackAdapter(ApiCallback<T> apiCallback) {
        this.apiCallback = apiCallback;
    }

    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            apiCallback.onResponse(response.body());
        } else {
            if (response.code() == 504) {
                // 当API成功发送消息但未在超时期限内获得响应时，将使用HTTP 504返回代码
                // 重要的是不要将此视为失败;执行状态为UNKNOWN，本来可以成功
                return;
            }
            onFailure(call, new BusinessException(CommonErrorCode.HTTP_REQUEST_ERROR));
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable throwable) {
        apiCallback.onFailure(throwable);
    }
}
