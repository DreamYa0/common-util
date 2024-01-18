package com.g7.framwork.common.util.timer;

public interface Timer {

    /**
     * 增加一个延迟任务
     * @param task 任务
     */
    void add(TimerTask task);

    /**
     * 提前内部时钟，执行在传递的超时时间内已达到到期的所有任务。
     * @param timeout 超时时间
     * @throws InterruptedException 中断异常
     */
    boolean advanceClock(long timeout) throws InterruptedException;

    /**
     * 获取待执行的任务数
     * @return 当前任务数量
     */
    int size();

    /**
     * 关闭时间轮服务，保留未执行的任务
     */
    void shutdown();
}
