package com.g7.framwork.common.util.chain.handlers;

import com.g7.framwork.common.util.chain.ChannelHandlerAdapter;
import com.g7.framwork.common.util.chain.ChannelHandlerContext;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CheckParameterHandler extends ChannelHandlerAdapter {

    @Override
    public void channelProcess(ChannelHandlerContext ctx, List<Map<String,Object>> in, Object out) throws Exception {

        System.out.println("参数校验:开始执行");
        final String articleId = in.get(0).get(AttributeHandlerMappingEnum.ARTICLE_ID.getAttribute()).toString();
        final String title = in.get(0).get(AttributeHandlerMappingEnum.TITLE.getAttribute()).toString();
        final String content = in.get(0).get(AttributeHandlerMappingEnum.CONTENT.getAttribute()).toString();

        Objects.requireNonNull(articleId, "articleId不能为空");
        Objects.requireNonNull(title, "title不能为空");
        Objects.requireNonNull(content, "content不能为空");

        System.out.println("参数校验:校验通过,即将进入下一个Handler");
        ctx.fireChannelProcess(in, out);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause, List<Map<String,Object>> in, Object out)
            throws Exception {
        System.out.println("参数校验的异常处理逻辑:不处理直接向后传递");
        ctx.fireExceptionCaught(cause, in, out);
    }
}
