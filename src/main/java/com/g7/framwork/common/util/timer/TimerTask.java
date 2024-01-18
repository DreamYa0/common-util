package com.g7.framwork.common.util.timer;

/**
 * 需要放在时间轮里执行的任务都要继承这个TimerTask
 * 每个TimerTask必须和一个TimerTaskEntry绑定，实现上放到时间轮里的是TimerTaskEntry
 * @author dreamyao
 * @title 抽象延迟任务
 * @date 2019/12/1 下午9:56
 * @since 1.0.0
 */
public abstract class TimerTask implements Runnable {

    private final long delayTime;
    private TimerTaskEntry timerTaskEntry;

    public TimerTask(long delayTime) {
        this.delayTime = delayTime;
    }

    // 取消当前的Task, 实际是解除在当前 TimerTaskEntry 上的绑定
    public synchronized void cancel() {
        if (timerTaskEntry != null) {
            timerTaskEntry.remove();
        }
        timerTaskEntry = null;
    }

    protected TimerTaskEntry getTimerTaskEntry() {
        return timerTaskEntry;
    }

    protected synchronized void setTimerTaskEntry(TimerTaskEntry entry) {
        // 如果此timerTask已由现有的计时器任务条目保存，我们将首先删除此类条目。
        if (timerTaskEntry != null && timerTaskEntry != entry) {
            timerTaskEntry.remove();
        }
        timerTaskEntry = entry;
    }

    protected long getDelayTime() {
        return delayTime;
    }
}
