package com.g7.framwork.common.util.chain.handlers;

import com.g7.framwork.common.util.chain.ChannelHandler;
import com.g7.framwork.common.util.chain.ChannelHandlerContext;

import java.util.List;
import java.util.Map;

public class ArticleModifyContentHandler implements ChannelHandler {

    @Override
    public void channelProcess(ChannelHandlerContext ctx, List<Map<String,Object>> in, Object out) throws Exception {

        System.out.println("修改正文:进入修改正文的Handler");
        System.out.println(in.get(0).get(AttributeHandlerMappingEnum.CONTENT.getAttribute()));
        System.out.println("修改正文:执行完成,即将进入下一个Handler");
        ctx.fireChannelProcess(in, out);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause, List<Map<String,Object>> in, Object out)
            throws Exception {
        System.out.println("修改正文的异常处理逻辑:不处理直接向后传递");
        ctx.fireExceptionCaught(cause, in, out);
    }
}
