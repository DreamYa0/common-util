package com.g7.framwork.common.util.bean;

import java.util.HashMap;

/**
 * 申诉单可用状态
 *
 * @author hk
 * @date 2020-02-19
 */
public enum RepresentationAvailableEnum {

    /**
     * 可用
     */
    AVAILABLE(0, "AVAILABLE"),
    /**
     * 不可用
     */
    UNAVAILABLE(1, "UNAVAILABLE"),

    ;

    private Integer code;
    private String name;

    RepresentationAvailableEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private static final HashMap<Integer, RepresentationAvailableEnum> VALUE_MAP = new HashMap<>();

    static {
        for (RepresentationAvailableEnum o : RepresentationAvailableEnum.values()) {
            VALUE_MAP.put(o.getCode(), o);
        }
    }

    public static RepresentationAvailableEnum valueOf(Integer siteStatusCode) {
        if (siteStatusCode == null) {
            return null;
        }
        RepresentationAvailableEnum v = VALUE_MAP.get(siteStatusCode);
        if (v == null) {
            throw new RuntimeException("Unkonw Code: " + siteStatusCode);
        }
        return v;
    }

    public static Boolean contain(String name) {
        if (name == null) {
            return false;
        }
        for (RepresentationAvailableEnum typeEnum : RepresentationAvailableEnum.values()) {
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
