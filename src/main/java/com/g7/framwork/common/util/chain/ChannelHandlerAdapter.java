package com.g7.framwork.common.util.chain;

import java.util.List;
import java.util.Map;

/**
 * 通道处理器适配器
 * @author dreamyao
 * @date 2022-08-11
 */
public abstract class ChannelHandlerAdapter implements ChannelHandler {

    @Override
    public void channelProcess(ChannelHandlerContext ctx, List<Map<String,Object>> in, Object out) throws Exception {
        ctx.fireChannelProcess(in, out);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause, List<Map<String,Object>> in, Object out)
            throws Exception {
        ctx.fireExceptionCaught(cause, in, out);
    }
}
