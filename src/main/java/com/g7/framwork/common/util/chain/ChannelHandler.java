package com.g7.framwork.common.util.chain;

import java.util.List;
import java.util.Map;

/**
 * 通道处理器
 * @author dreamyao
 * @date 2022-08-11
 */
public interface ChannelHandler {

    void channelProcess(ChannelHandlerContext ctx, List<Map<String,Object>> in, Object out) throws Exception;

    void exceptionCaught(ChannelHandlerContext ctx, Throwable cause, List<Map<String,Object>> in, Object out)
            throws Exception;
}
