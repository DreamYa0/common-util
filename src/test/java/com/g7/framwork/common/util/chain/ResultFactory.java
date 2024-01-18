package com.g7.framwork.common.util.chain;

/**
 * 出参工厂
 */
public class ResultFactory implements OutboundFactory<Result> {

    @Override
    public Result newInstance() {
        Result result = new Result();
        result.setCode(0);
        result.setMsg("ok");
        return result;
    }
}
