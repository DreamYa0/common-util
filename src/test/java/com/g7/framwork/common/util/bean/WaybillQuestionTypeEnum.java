package com.g7.framwork.common.util.bean;

import java.util.HashMap;

/**
 * @author hebiao
 * @version :WaybillQuestionTypeEnum.java, v0.1 2020-02-19 14:10 hebiao
 */
public enum WaybillQuestionTypeEnum {
    /**
     * 运费总价超限
     */
    TOTAL_FREIGHT_EXCEEDS_LIMIT(0, "运费总价超限"),
    /**
     * 吨公里单价超限
     */
    FREIGHT_UNIT_PRICE_IS_OVER_THE_LIMIT(1, "吨公里单价超限"),

    /**
     * 货物重量超限
     */
    THE_CARGO_IS_OVER_WEIGHT(2, "货物重量超限"),

    /**
     * 运单冲突
     */
    WAYBILL_CONFLICT(3, "运单冲突"),

    /**
     * 发车异常
     */
    GRID_ABNORMAL(4, "发车异常"),

    /**
     * 到达异常
     */
    REACH_ABNORMAL(5, "到达异常"),

    /**
     * 配送用时不合理
     */
    DELIVERY_TIME_IS_NOT_REASONABLE(6, "配送用时不合理"),

    /**
     * 司机当月运费超限
     */
    DRIVER_MONTHLY_FREIGHT_EXCEEDS_THE_LIMIT(7, "司机当月运费超限"),

    /**
     * 车辆当月运费超限
     */
    VEHICLE_MONTHLY_FREIGHT_EXCEEDS_THE_LIMIT(8, "车辆当月运费超限"),

    /**
     * 装卸货操作异常
     */
    LOADING_OR_UNLOADING_OPERATE_ABNORMAL(9, "装卸货操作异常"),

    /**
     * 平台抽检
     */
    PLATFORM_SPOT_CHECK(10, "平台抽检"),

    /**
     * 到达作废
     */
    ARRIVAL_VOID(11, "缺少卸货时间"),

    /**
     * 对外接口 编辑金额
     */
    EDIT_PRICE(12, "运单金额过大") ,

    /***
     * 运单可疑
     */
    WAYBILL_SUSPIC(13, "运单可疑"),

    /**
     * 在途时间过长
     */
    ON_PASSAGE_TIME(14, "在途时间超长"),


    ;

    private Integer code;
    private String name;

    WaybillQuestionTypeEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private final static HashMap<Integer, WaybillQuestionTypeEnum> VALUE_MAP = new HashMap<>();

    static {
        for (WaybillQuestionTypeEnum o : WaybillQuestionTypeEnum.values()) {
            VALUE_MAP.put(o.getCode(), o);
        }
    }

    public static WaybillQuestionTypeEnum valueOf(Integer siteStatusCode) {
        if (siteStatusCode == null) {
            return null;
        }
        WaybillQuestionTypeEnum v = VALUE_MAP.get(siteStatusCode);
        if (v == null) {
            throw new RuntimeException("Unkonw Code: " + siteStatusCode);
        }
        return v;
    }

    public static Boolean contain(String name) {
        if (name == null) {
            return false;
        }
        for (WaybillQuestionTypeEnum typeEnum : WaybillQuestionTypeEnum.values()) {
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
