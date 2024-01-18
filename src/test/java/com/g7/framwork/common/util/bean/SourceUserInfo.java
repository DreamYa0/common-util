package com.g7.framwork.common.util.bean;

import java.io.Serializable;

/**
 * @author dreamyao
 * @title
 * @date 2018/9/18 下午2:47
 * @since 1.0.0
 */
public class SourceUserInfo implements Serializable{

    private static final long serialVersionUID = 6384827778993431679L;

    /**
     * 用户NO
     */
    private Long userNo;

    /**
     * 资金账号
     */
    private Long accountNo;

    /**
     * 账号类型
     */
    private Integer accountType;

    public Long getUserNo() {
        return userNo;
    }

    public void setUserNo(Long userNo) {
        this.userNo = userNo;
    }

    public Long getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(Long accountNo) {
        this.accountNo = accountNo;
    }

    public Integer getAccountType() {
        return accountType;
    }

    public void setAccountType(Integer accountType) {
        this.accountType = accountType;
    }
}
