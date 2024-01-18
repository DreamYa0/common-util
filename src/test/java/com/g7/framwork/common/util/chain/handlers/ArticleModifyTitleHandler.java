package com.g7.framwork.common.util.chain.handlers;

import com.g7.framwork.common.util.chain.ChannelHandler;
import com.g7.framwork.common.util.chain.ChannelHandlerContext;

import java.util.List;
import java.util.Map;

public class ArticleModifyTitleHandler implements ChannelHandler {

    @Override
    public void channelProcess(ChannelHandlerContext ctx, List<Map<String,Object>> in, Object out) throws Exception {

        System.out.println("修改标题:进入修改标题的Handler");
        String title = in.get(0).get(AttributeHandlerMappingEnum.TITLE.getAttribute()).toString();
        //此处的异常用于模拟执行过程中出现异常的场景，正常情况下注释掉
        throw new RuntimeException("修改title发生异常");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause, List<Map<String,Object>> in, Object out)
            throws Exception {
        System.out.println("修改标题的异常处理逻辑:不处理直接向后传递");
        ctx.fireExceptionCaught(cause, in, out);
    }
}
