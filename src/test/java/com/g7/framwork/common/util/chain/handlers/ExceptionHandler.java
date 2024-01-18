package com.g7.framwork.common.util.chain.handlers;

import com.g7.framwork.common.util.chain.ChannelHandlerAdapter;
import com.g7.framwork.common.util.chain.ChannelHandlerContext;
import com.g7.framwork.common.util.chain.Result;

import java.util.List;
import java.util.Map;

public class ExceptionHandler extends ChannelHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause, List<Map<String,Object>> in, Object out)
            throws Exception {

        System.out.println("异常处理器中的异常处理逻辑");
        Result re = (Result) out;
        re.setCode(500);
        re.setMsg("系统异常");
    }
}
