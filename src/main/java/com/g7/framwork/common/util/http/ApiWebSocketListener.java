package com.g7.framwork.common.util.http;

import com.g7.framework.framwork.exception.BusinessException;
import com.g7.framework.framwork.exception.meta.CommonErrorCode;
import com.g7.framwork.common.util.json.JsonUtils;
import com.g7.framwork.common.util.json.TypeReference;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * public Closeable onCandlestickEvent(String symbols, CandlestickInterval interval, ApiCallback<CandlestickEvent> callback) {
 * final String channel = Arrays.stream(symbols.split(","))
 * .map(String::trim)
 * .map(s -> String.format("%s@kline_%s", s, interval.getIntervalId()))
 * .collect(Collectors.joining("/"));
 * return createNewWebSocket(channel, new ApiWebSocketListener<>(callback, CandlestickEvent.class));
 * }
 * @author dreamyao
 * @title
 * @date 2019-05-09 16:26
 * @since 1.0.0
 */
public class ApiWebSocketListener<T> extends WebSocketListener {

    private static final Logger logger = LoggerFactory.getLogger(ApiWebSocketListener.class);
    private ApiCallback<T> apiCallback;
    private Class<T> eventClass;
    private TypeReference<T> eventTypeReference;
    private boolean closing = false;

    public ApiWebSocketListener(ApiCallback<T> apiCallback, Class<T> eventClass) {
        this.apiCallback = apiCallback;
        this.eventClass = eventClass;
    }

    public ApiWebSocketListener(ApiCallback<T> apiCallback, TypeReference<T> eventTypeReference) {
        this.apiCallback = apiCallback;
        this.eventTypeReference = eventTypeReference;
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        try {
            T event;
            if (eventClass == null) {
                event = JsonUtils.fromJson(text, eventTypeReference.getType());
            } else {
                event = JsonUtils.fromJson(text, eventClass);
            }
            apiCallback.onResponse(event);
        } catch (Exception e) {
            logger.error("WebSocket request failed.", e);
            throw new BusinessException(CommonErrorCode.HTTP_REQUEST_ERROR);
        }
    }

    @Override
    public void onClosing(final WebSocket webSocket, final int code, final String reason) {
        closing = true;
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        if (!closing) {
            apiCallback.onFailure(t);
        }
    }
}