package com.g7.framwork.common.util.http;

import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @author dreamyao
 * @title
 * @date 2019-05-09 16:26
 * @since 1.0.0
 */
public class Constants {

    /**
     * 用于API-KEY身份验证的HTTP标头。
     */
    public static final String API_KEY_HEADER = "X-MBX-APIKEY";

    /**
     * Decorator表示端点需要API密钥
     */
    public static final String ENDPOINT_SECURITY_TYPE_APIKEY = "APIKEY";
    public static final String ENDPOINT_SECURITY_TYPE_APIKEY_HEADER = ENDPOINT_SECURITY_TYPE_APIKEY + ": #";

    /**
     * 装饰器指示端点需要签名
     */
    public static final String ENDPOINT_SECURITY_TYPE_SIGNED = "SIGNED";
    public static final String ENDPOINT_SECURITY_TYPE_SIGNED_HEADER = ENDPOINT_SECURITY_TYPE_SIGNED + ": #";

    /**
     * 默认接收窗口
     */
    public static final long DEFAULT_RECEIVING_WINDOW = 6_000_000L;

    /**
     * Default ToStringStyle used by toString methods.
     * Override this to change the output format of the overridden toString methods.
     * - Example ToStringStyle.JSON_STYLE
     */
    public static ToStringStyle TO_STRING_BUILDER_STYLE = ToStringStyle.SHORT_PREFIX_STYLE;
}
