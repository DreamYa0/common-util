package com.g7.framwork.common.util.chain;

import java.util.List;
import java.util.Map;

/**
 * 通道
 * @author dreamyao
 * @date 2022-08-11
 */
public interface Channel {

    Channel process(List<Map<String,Object>> in, Object out);

    ChannelPipeline pipeline();

    interface ChannelProcessor {
        void process(List<Map<String,Object>> in, Object out);
    }
}
