package com.g7.framwork.common.util.bean;

import java.util.HashMap;

/**
 * 运单异常-状态
 *
 * @author hk
 * @date 2020-02-19
 */
public enum WaybillQuestionStatusEnum {
    /**
     * 待处理
     */
    PENDING(0, "PENDING"),
    /**
     * 处理中
     */
    PROCESSING(5, "PROCESSING"),
    /**
     * 已处理
     */
    SUCCESS(10, "SUCCESS"),
    ;

    private static final HashMap<Integer, WaybillQuestionStatusEnum> VALUE_MAP = new HashMap<>();

    static {
        for (WaybillQuestionStatusEnum o : WaybillQuestionStatusEnum.values()) {
            VALUE_MAP.put(o.getCode(), o);
        }
    }

    WaybillQuestionStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private Integer code;
    private String name;

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static WaybillQuestionStatusEnum valueOf(Integer siteStatusCode) {
        if (siteStatusCode == null) {
            return null;
        }
        WaybillQuestionStatusEnum v = VALUE_MAP.get(siteStatusCode);
        if (v == null) {
            throw new RuntimeException("Unkonw Code: " + siteStatusCode);
        }
        return v;
    }

    public static Boolean contain(String name) {
        if (name == null) {
            return false;
        }
        for (WaybillQuestionStatusEnum typeEnum : WaybillQuestionStatusEnum.values()) {
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
