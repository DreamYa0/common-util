package com.g7.framwork.common.util.chain.handlers;

import org.testng.collections.Lists;

import java.util.List;

/**
 * @author dreamyao
 * @title 属性名称与处理器对应关系
 * @date 2022/8/18 5:08 PM
 * @since 1.0.0
 */
public enum AttributeHandlerMappingEnum {

    // content属性
    CONTENT("content", Lists.newArrayList(ArticleModifyContentHandler.class,
            CheckParameterHandler.class)),
    // title
    TITLE("title", Lists.newArrayList(ArticleModifyTitleHandler.class,
            CheckParameterHandler.class)),
    // articleId
    ARTICLE_ID("articleId", Lists.newArrayList(CheckParameterHandler.class)),
    ;

    private final String attribute;

    AttributeHandlerMappingEnum(String attribute, List<Class<?>> handlers) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }
}
