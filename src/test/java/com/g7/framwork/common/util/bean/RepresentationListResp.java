package com.g7.framwork.common.util.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 申诉单列表VO
 *
 * @author hk
 * @date 2020-02-19
 */
public class RepresentationListResp extends RepresentationResp implements Serializable {
    private static final long serialVersionUID = -7117185692400432787L;

    public List<QuestionResp> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResp> questions) {
        this.questions = questions;
    }

    /**
     * 问题列表
     */
    private List<QuestionResp> questions;
}
