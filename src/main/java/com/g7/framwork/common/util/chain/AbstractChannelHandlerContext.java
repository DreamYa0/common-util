package com.g7.framwork.common.util.chain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * 抽象通道处理器上下问
 * @author dreamyao
 * @date 2022-08-11
 */
public abstract class AbstractChannelHandlerContext implements ChannelHandlerContext {

    private static final Logger logger = LoggerFactory.getLogger(AbstractChannelHandlerContext.class);

    protected volatile AbstractChannelHandlerContext next;
    protected volatile AbstractChannelHandlerContext prev;
    private final DefaultChannelPipeline pipeline;
    private final String name;
    private final Class<? extends ChannelHandler> handlerClass;

    AbstractChannelHandlerContext(DefaultChannelPipeline pipeline,
                                  String name,
                                  Class<? extends ChannelHandler> handlerClass) {
        this.name = name;
        this.pipeline = pipeline;
        this.handlerClass = handlerClass;
    }

    @Override
    public Channel channel() {
        return pipeline.channel();
    }

    @Override
    public ChannelPipeline pipeline() {
        return pipeline;
    }

    @Override
    public ChannelHandlerContext fireExceptionCaught(Throwable cause, List<Map<String,Object>> in, Object out) {
        invokeExceptionCaught(this.next, cause, in, out);
        return this;
    }

    @Override
    public ChannelHandlerContext fireChannelProcess(List<Map<String,Object>> in, Object out) {
        invokeChannelProcess(this.next, in, out);
        return this;
    }


    private void invokeExceptionCaught(final Throwable cause, List<Map<String,Object>> in, Object out) {
        try {
            handler().exceptionCaught(this, cause, in, out);
        } catch (Throwable error) {
            logger.error("exception handle throw exception", error);
        }
    }

    private void invokeChannelProcess(List<Map<String,Object>> in, Object out) {
        try {
            handler().channelProcess(this, in, out);
        } catch (Throwable throwable) {
            invokeExceptionCaught(throwable, in, out);
        }
    }

    protected static void invokeExceptionCaught(final AbstractChannelHandlerContext next,
                                      final Throwable cause,
                                      List<Map<String,Object>> in,
                                      Object out) {
        next.invokeExceptionCaught(cause, in, out);
    }

    static void invokeChannelProcess(final AbstractChannelHandlerContext next,
                                     List<Map<String,Object>> in,
                                     Object out) {
        next.invokeChannelProcess(in, out);
    }

    @Override
    public ChannelHandlerContext process(List<Map<String,Object>> in, Object out) {
        try {
            handler().channelProcess(this, in, out);
        } catch (Throwable t) {
            invokeExceptionCaught(t, in, out);
        }
        return this;
    }
}
