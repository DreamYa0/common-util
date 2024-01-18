package com.g7.framwork.common.util.chain;

import java.util.List;
import java.util.Map;

/**
 * 通道处理器上下文
 * @author dreamyao
 * @date 2022-08-11
 */
public interface ChannelHandlerContext {

    Channel channel();

    ChannelHandler handler();

    ChannelPipeline pipeline();

    ChannelHandlerContext process(List<Map<String,Object>> in, Object out);

    ChannelHandlerContext fireExceptionCaught(Throwable cause, List<Map<String,Object>> in, Object out);

    ChannelHandlerContext fireChannelProcess(List<Map<String,Object>> in, Object out);
}
