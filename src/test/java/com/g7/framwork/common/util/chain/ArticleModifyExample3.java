package com.g7.framwork.common.util.chain;

import com.beust.jcommander.internal.Lists;
import com.g7.framwork.common.util.bean.EnhanceBeanUtils;
import com.g7.framwork.common.util.chain.handlers.ArticleModifyContentHandler;
import com.g7.framwork.common.util.chain.handlers.ArticleModifyTitleHandler;
import com.g7.framwork.common.util.chain.handlers.CheckParameterHandler;
import com.g7.framwork.common.util.chain.handlers.ExceptionHandler;

public class ArticleModifyExample3 {

    public static void main(String[] args) {
        //入参
        ArticleTitleModifyCmd dto = new ArticleTitleModifyCmd();
        dto.setArticleId("articleId_001");
        dto.setTitle("articleId_001_title");
        dto.setContent("articleId_001_content");
        //创建引导类
        ChainBootStrap<Result> bootStrap = new ChainBootStrap<>();

        Result result = bootStrap
                // 入参 对象转 Map
                .inboundParameter(Lists.newArrayList(EnhanceBeanUtils.object2Map(dto)))
                // 出参工厂
                .outboundFactory(new ResultFactory())
                // 自定义channel
                .channel(new ArticleModifyChannel())
                // 第一个handler
                .childHandler("checkParameter", new CheckParameterHandler())
                // 第二个handler
                .childHandler("modifyTitle", new ArticleModifyTitleHandler())
                // 第三个handler
                .childHandler("modifyContent", new ArticleModifyContentHandler())
                // 异常处理handler
                .childHandler("exception", new ExceptionHandler())
                // 执行
                .process();
        // result为执行结果
        System.out.println("result code " + result.getCode() + " msg " + result.getMsg());
    }
}
