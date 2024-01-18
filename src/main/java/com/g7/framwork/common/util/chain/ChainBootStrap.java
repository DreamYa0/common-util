package com.g7.framwork.common.util.chain;

import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;

/**
 * 启动配置类
 * @author dreamyao
 * @date 2022-08-11
 */
public class ChainBootStrap<T> {

    private Channel channel;
    private List<Map<String,Object>> in;
    private OutboundFactory<T> outboundFactory;

    public ChainBootStrap<T> channel(Channel channel) {
        this.channel = channel;
        return this;
    }

    public ChainBootStrap<T> inboundParameter(List<Map<String,Object>> in) {
        this.in = in;
        return this;
    }

    public ChainBootStrap<T> outboundFactory(OutboundFactory<T> outboundFactory) {
        this.outboundFactory = outboundFactory;
        return this;
    }

    public ChainBootStrap<T> childHandler(String name, ChannelHandler channelHandler) {
        Assert.notNull(name, "handler name is not null");
        this.channel.pipeline().addLast(name, channelHandler);
        return this;
    }

    public T process() {
        T out = outboundFactory.newInstance();
        channel.process(in, out);
        return out;
    }
}
