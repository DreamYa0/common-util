package com.g7.framwork.common.util.bean;

import java.util.HashMap;

/**
 * @author dreamyao
 * @title 申诉类型：0=风控异常申诉，5=配送用时异常申诉，10=运费总价异常申诉
 * @date 2020-01-07 15:17
 * @since 1.0.0
 */
public enum RepresentationTypeEnum {

    /**
     * 风控异常申诉
     */
    RISK_CONTROL_ANOMALIES(0, "RISK_CONTROL_ANOMALIES"),
    /**
     * 配送用时异常申诉
     */
    ANOMALOUS_REPRESENTATION_DURING_DELIVERY(5, "ANOMALOUS_REPRESENTATION_DURING_DELIVERY"),
    /**
     * 运费总价异常申诉
     */
    ABNORMAL_REPRESENTATION_OF_TOTAL_FREIGHT_PRICE(10, "ABNORMAL_REPRESENTATION_OF_TOTAL_FREIGHT_PRICE"),

    /**
     * 发车异常
     */
    ABNORMAL_DEPARTURE(15, "ABNORMAL_DEPARTURE"),

    /**
     * 运单冲突
     */
    WAYBILL_EXCEPTION(20, "WAYBILL_EXCEPTION"),
    ;

    private Integer code;
    private String name;

    RepresentationTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private final static HashMap<Integer, RepresentationTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (RepresentationTypeEnum o : RepresentationTypeEnum.values()) {
            VALUE_MAP.put(o.getCode(), o);
        }
    }

    public static RepresentationTypeEnum valueOf(Integer siteStatusCode) {
        if (siteStatusCode == null) {
            return null;
        }
        RepresentationTypeEnum v = VALUE_MAP.get(siteStatusCode);
        if (v == null) {
            throw new RuntimeException("Unkonw Code: " + siteStatusCode);
        }
        return v;
    }

    public static Boolean contain(String name) {
        if (name == null) {
            return false;
        }
        for (RepresentationTypeEnum typeEnum : RepresentationTypeEnum.values()) {
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
