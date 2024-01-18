package com.g7.framwork.common.util.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 申诉单VO
 *
 * @author hk
 * @date 2020-02-19
 */
public class RepresentationResp implements Serializable {
    private static final long serialVersionUID = 2230089865758411764L;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 机构编码
     */
    private String orgcode;

    /**
     * 运单id
     */
    private String waybillId;

    /**
     * 关联问题类型(逗号分隔)
     */
    private String questionTypes;

    /**
     * 申述类型:0=风控异常申述，5=配送用时异常申述，10=运费超限额异常申述，15=其他
     */
    private RepresentationTypeEnum representationType;

    /**
     * 异常运单申诉状态:0=申诉中，5=申诉失败，10=申诉成功，15=待处理
     */
    private RepresentationStatusEnum representationStatus;

    /**
     * 申诉单id
     */
    private String representationId;

    /**
     * 装货时间
     */
    private Date actualShippingTime;

    /**
     * 卸货时间
     */
    private Date receiptTime;

    /**
     * 装货单
     */
    private String actualShippingImages;

    /**
     * 卸货单
     */
    private String receiptImages;

    /**
     * 其他单据
     */
    private String otherImages;

    /**
     * 申诉时间
     */
    private Date appealTime;

    /**
     * 审核时间
     */
    private Date auditTime;

    /**
     * 提交人
     */
    private String operator;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 是否可用：0=可用，1=不可用
     */
    private RepresentationAvailableEnum available;

    /**
     * 申诉描述
     */
    private String appealDesc;

    /**
     * 审核描述
     */
    private String auditDesc;

    /**
     * 联系人
     */
    private String linkman;

    /**
     * 联系人手机号
     */
    private String linkmanPhone;
    /**
     * 是否可用：0=可用，1=不可用
     */
    private RepresentationAvailableEnum preAvailable;

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtModified() {
        return gmtModified;
    }

    public void setGmtModified(Date gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getOrgcode() {
        return orgcode;
    }

    public void setOrgcode(String orgcode) {
        this.orgcode = orgcode;
    }

    public String getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(String waybillId) {
        this.waybillId = waybillId;
    }

    public String getQuestionTypes() {
        return questionTypes;
    }

    public void setQuestionTypes(String questionTypes) {
        this.questionTypes = questionTypes;
    }

    public RepresentationTypeEnum getRepresentationType() {
        return representationType;
    }

    public void setRepresentationType(RepresentationTypeEnum representationType) {
        this.representationType = representationType;
    }

    public RepresentationStatusEnum getRepresentationStatus() {
        return representationStatus;
    }

    public void setRepresentationStatus(RepresentationStatusEnum representationStatus) {
        this.representationStatus = representationStatus;
    }

    public String getRepresentationId() {
        return representationId;
    }

    public void setRepresentationId(String representationId) {
        this.representationId = representationId;
    }

    public Date getActualShippingTime() {
        return actualShippingTime;
    }

    public void setActualShippingTime(Date actualShippingTime) {
        this.actualShippingTime = actualShippingTime;
    }

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    public String getActualShippingImages() {
        return actualShippingImages;
    }

    public void setActualShippingImages(String actualShippingImages) {
        this.actualShippingImages = actualShippingImages;
    }

    public String getReceiptImages() {
        return receiptImages;
    }

    public void setReceiptImages(String receiptImages) {
        this.receiptImages = receiptImages;
    }

    public String getOtherImages() {
        return otherImages;
    }

    public void setOtherImages(String otherImages) {
        this.otherImages = otherImages;
    }

    public Date getAppealTime() {
        return appealTime;
    }

    public void setAppealTime(Date appealTime) {
        this.appealTime = appealTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public RepresentationAvailableEnum getAvailable() {
        return available;
    }

    public void setAvailable(RepresentationAvailableEnum available) {
        this.available = available;
    }

    public String getAppealDesc() {
        return appealDesc;
    }

    public void setAppealDesc(String appealDesc) {
        this.appealDesc = appealDesc;
    }

    public String getAuditDesc() {
        return auditDesc;
    }

    public void setAuditDesc(String auditDesc) {
        this.auditDesc = auditDesc;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinkmanPhone() {
        return linkmanPhone;
    }

    public void setLinkmanPhone(String linkmanPhone) {
        this.linkmanPhone = linkmanPhone;
    }

    public RepresentationAvailableEnum getPreAvailable() {
        return preAvailable;
    }

    public void setPreAvailable(RepresentationAvailableEnum preAvailable) {
        this.preAvailable = preAvailable;
    }
}
