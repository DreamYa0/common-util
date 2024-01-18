package com.g7.framwork.common.util.chain;

import java.util.List;
import java.util.Map;

/**
 * 抽象通道
 * @author dreamyao
 * @date 2022-08-11
 */
public abstract class AbstractChannel implements Channel {

    private final DefaultChannelPipeline pipeline;
    private final ChannelProcessor processor = new DefaultChannelProcessor();

    protected AbstractChannel() {
        pipeline = newChannelPipeline();
    }

    protected DefaultChannelPipeline newChannelPipeline() {
        return new DefaultChannelPipeline(this);
    }

    @Override
    public ChannelPipeline pipeline() {
        return pipeline;
    }

    public Channel process(List<Map<String,Object>> inWrapper, Object outWrapper) {
        processor.process(inWrapper, outWrapper);
        return this;
    }

    private class DefaultChannelProcessor implements ChannelProcessor {

        @Override
        public void process(List<Map<String,Object>> inWrapper, Object outWrapper) {
            pipeline.process(inWrapper, outWrapper);
        }
    }
}
