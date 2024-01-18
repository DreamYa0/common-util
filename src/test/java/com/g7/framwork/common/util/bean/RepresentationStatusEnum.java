package com.g7.framwork.common.util.bean;

import java.util.HashMap;

/**
 * @author dreamyao
 * @title 异常运单申诉状态：0=申诉中，5=申诉失败，10=申诉成功，15=待处理
 * @date 2020-01-07 15:19
 * @since 1.0.0
 */
public enum RepresentationStatusEnum {

    /**
     * 申诉中
     */
    REPRESENTATION(0, "REPRESENTATION"),
    /**
     * 申诉失败
     */
    REPRESENTATION_FAILED(5, "REPRESENTATION_FAILED"),
    /**
     * 申诉成功
     */
    REPRESENTATION_SUCCESS(10, "REPRESENTATION_SUCCESS"),
    /**
     * 待处理
     */
    PENDING(15, "PENDING"),

    ;

    private Integer code;
    private String name;

    RepresentationStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private final static HashMap<Integer, RepresentationStatusEnum> VALUE_MAP = new HashMap<>();

    static {
        for (RepresentationStatusEnum o : RepresentationStatusEnum.values()) {
            VALUE_MAP.put(o.getCode(), o);
        }
    }

    public static RepresentationStatusEnum valueOf(Integer siteStatusCode) {
        if (siteStatusCode == null) {
            return null;
        }
        RepresentationStatusEnum v = VALUE_MAP.get(siteStatusCode);
        if (v == null) {
            throw new RuntimeException("Unkonw Code: " + siteStatusCode);
        }
        return v;
    }

    public static Boolean contain(String name) {
        if (name == null) {
            return false;
        }
        for (RepresentationStatusEnum typeEnum : RepresentationStatusEnum.values()) {
            if (typeEnum.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static Boolean contain(Integer code) {
        return VALUE_MAP.containsKey(code);
    }
}
