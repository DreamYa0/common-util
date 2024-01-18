package com.g7.framwork.common.util.chain;

import java.util.List;
import java.util.Map;

/**
 * 通道流水线
 * @author dreamyao
 * @date 2022-08-11
 */
public interface ChannelPipeline {

    /**
     * 处理器
     * @param in  入参
     * @param out 出参
     * @return 管道流
     */
    ChannelPipeline process(List<Map<String,Object>> in, Object out);

    /**
     * 添加处理器
     * @param name    处理器名称
     * @param handler 处理器
     * @return 管道流
     */
    ChannelPipeline addLast(String name, ChannelHandler handler);

    /**
     * 管道
     * @return 管道
     */
    Channel channel();

    /**
     * 传递异常到下一个处理器
     * @param cause 异常
     * @param in    入参
     * @param out   出参
     * @return 管道流
     */
    ChannelPipeline fireExceptionCaught(Throwable cause, List<Map<String,Object>> in, Object out);

    /**
     * 传递到下一个处理器
     * @param in  入参
     * @param out 出参
     * @return 管道流
     */
    ChannelPipeline fireChannelProcess(List<Map<String,Object>> in, Object out);

    /**
     * 头处理器上下文
     * @return 处理器上下文
     */
    ChannelHandlerContext head();

    /**
     * 尾处理器上下文
     * @return 处理器上下文
     */
    ChannelHandlerContext tail();
}
