package com.g7.framwork.common.util.bean;

import java.util.HashMap;

public enum OrderStatus {

    /**
     * 0-正常
     */
    NORMAL(0, "正常"),
    /**
     * 1-已取消
     */
    CANCELED(1, "已取消"),
    /**
     * 2-账务处理中
     */
    PROCESSING(2, "账务处理中"),;

    private final static HashMap<Integer, OrderStatus> VALUE_MAP = new HashMap<>();

    static {
        for (OrderStatus o : OrderStatus.values()) {
            VALUE_MAP.put(o.getCode(), o);
        }
    }

    private Integer code;
    private String name;

    OrderStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public static OrderStatus valueOf(Integer siteStatusCode) {
        if (siteStatusCode == null) {
            return null;
        }
        OrderStatus v = VALUE_MAP.get(siteStatusCode);
        if (v == null) {
            throw new RuntimeException("Unkonw Code: " + siteStatusCode);
        }
        return v;
    }

    public static Boolean contain(String name) {
        if (name == null) {
            return false;
        }
        for (OrderStatus typeEnum : OrderStatus.values()) {
            if (typeEnum.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static Boolean contain(Integer code) {
        return VALUE_MAP.containsKey(code);
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
