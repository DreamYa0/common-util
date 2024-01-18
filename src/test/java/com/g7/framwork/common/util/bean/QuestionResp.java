package com.g7.framwork.common.util.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * 运单问题VO
 *
 * @author hk
 * @date 2020-02-19
 */
public class QuestionResp implements Serializable {
    private static final long serialVersionUID = -6818187590822558526L;

    /**
     * 自增主键ID
     */
    private Long id;

    /**
     * 创建时间
     */
    private Date gmtCreate;

    /**
     * 更新时间
     */
    private Date gmtModified;

    /**
     * 问题id
     */
    private String questionId;

    /**
     * 运单id
     */
    private String waybillId;

    /**
     * 问题分类
     */
    private WaybillQuestionTypeEnum type;

    /**
     * 问题产生的操作类型
     */
    private String operateType;

    /**
     * 问题状态
     */
    private WaybillQuestionStatusEnum status;

    /**
     * 问题产生的来源
     */
    private String source;

    /**
     * 问题描述
     */
    private String describe;

    /**
     * 问题请求值 json
     */
    private String request;

    /**
     * 问题响应值 json
     */
    private String response;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getQuestionId() {
        return questionId;
    }

    public void setQuestionId(String questionId) {
        this.questionId = questionId;
    }

    public String getWaybillId() {
        return waybillId;
    }

    public void setWaybillId(String waybillId) {
        this.waybillId = waybillId;
    }

    public WaybillQuestionTypeEnum getType() {
        return type;
    }

    public void setType(WaybillQuestionTypeEnum type) {
        this.type = type;
    }

    public String getOperateType() {
        return operateType;
    }

    public void setOperateType(String operateType) {
        this.operateType = operateType;
    }

    public WaybillQuestionStatusEnum getStatus() {
        return status;
    }

    public void setStatus(WaybillQuestionStatusEnum status) {
        this.status = status;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
