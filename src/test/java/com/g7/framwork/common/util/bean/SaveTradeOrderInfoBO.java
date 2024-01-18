package com.g7.framwork.common.util.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author dreamyao
 * @title
 * @date 2018/9/17 下午4:34
 * @since 1.0.0
 */
public class SaveTradeOrderInfoBO implements Serializable {

    private static final long serialVersionUID = 5722579639789706001L;

    /**
     * 订单号
     */
    private Long orderNo;

    private SourceUserInfo userInfo;

    /**
     * 委托单等级
     */
    private Integer level;

    /**
     * 交易货币代码
     */
    private String symbol;

    /**
     * 标的币种
     */
    private String productCode;

    /**
     * 计价币种
     */
    private String currencyCode;

    /**
     * 标的币种的美元价格
     */
    private BigDecimal productUsdPrice;

    /**
     * 计价币种的美元价格
     */
    private BigDecimal currencyUsdPrice;

    /**
     * 1-申卖单 2-申买单
     */
    private Integer action;

    /**
     * action枚举名称
     */
    private String actionName;

    /**
     * 1-限价单 2-市价单
     */
    private Integer orderType;

    /**
     * LMT-限价单 MKT-市价单
     */
    private String orderTypeName;

    /**
     * 委托限价
     */
    private BigDecimal priceLimit;

    /**
     * 成交均价
     */
    private BigDecimal priceAverage;

    /**
     * 限价委托数量
     */
    private BigDecimal quantity;

    /**
     * 限价剩余数量
     */
    private BigDecimal quantityRemaining;

    /**
     * 市价委托金额
     */
    private BigDecimal amount;

    /**
     * 市价委托剩余金额
     */
    private BigDecimal amountRemaining;

    /**
     * 收费币种
     */
    private String feeCurrencyCode;

    /**
     * 手续费金额
     */
    private BigDecimal fee;

    /**
     * 手续费率
     */
    private BigDecimal feeRate;

    /**
     * 委托单状态 0-正常 1-已撤销
     */
    private Integer status;

    /**
     * 撮合进度 0-未处理 1-无匹配 2-部分成交 3-全部成交
     */
    private Integer state;

    /**
     * 来源客户端（0-web，1-app）
     */
    private Integer fromClientType;

    private Date utcCreate;

    private Date utcUpdate;

    public Long getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    public SourceUserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(SourceUserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public BigDecimal getProductUsdPrice() {
        return productUsdPrice;
    }

    public void setProductUsdPrice(BigDecimal productUsdPrice) {
        this.productUsdPrice = productUsdPrice;
    }

    public BigDecimal getCurrencyUsdPrice() {
        return currencyUsdPrice;
    }

    public void setCurrencyUsdPrice(BigDecimal currencyUsdPrice) {
        this.currencyUsdPrice = currencyUsdPrice;
    }

    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getOrderTypeName() {
        return orderTypeName;
    }

    public void setOrderTypeName(String orderTypeName) {
        this.orderTypeName = orderTypeName;
    }

    public BigDecimal getPriceLimit() {
        return priceLimit;
    }

    public void setPriceLimit(BigDecimal priceLimit) {
        this.priceLimit = priceLimit;
    }

    public BigDecimal getPriceAverage() {
        return priceAverage;
    }

    public void setPriceAverage(BigDecimal priceAverage) {
        this.priceAverage = priceAverage;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getQuantityRemaining() {
        return quantityRemaining;
    }

    public void setQuantityRemaining(BigDecimal quantityRemaining) {
        this.quantityRemaining = quantityRemaining;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountRemaining() {
        return amountRemaining;
    }

    public void setAmountRemaining(BigDecimal amountRemaining) {
        this.amountRemaining = amountRemaining;
    }

    public String getFeeCurrencyCode() {
        return feeCurrencyCode;
    }

    public void setFeeCurrencyCode(String feeCurrencyCode) {
        this.feeCurrencyCode = feeCurrencyCode;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(BigDecimal feeRate) {
        this.feeRate = feeRate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getFromClientType() {
        return fromClientType;
    }

    public void setFromClientType(Integer fromClientType) {
        this.fromClientType = fromClientType;
    }

    public Date getUtcCreate() {
        return utcCreate;
    }

    public void setUtcCreate(Date utcCreate) {
        this.utcCreate = utcCreate;
    }

    public Date getUtcUpdate() {
        return utcUpdate;
    }

    public void setUtcUpdate(Date utcUpdate) {
        this.utcUpdate = utcUpdate;
    }
}
