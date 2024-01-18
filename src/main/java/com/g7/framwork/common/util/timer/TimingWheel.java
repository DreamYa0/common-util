package com.g7.framwork.common.util.timer;

import java.util.concurrent.DelayQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 1、简言之，就是根据每个TimerTaskEntry的过期时间和当前时间轮的时间，选择一个合适的bucket(实际上就是TimerTaskList),
 * 这个桶的超时时间相同(会去余留整), 把这个TimerTaskEntry对象放进去，如果当前的bucket因超时被DelayQueue队列poll出来的话,
 * 意味着这个bucket里面的都过期了, 会调用这个bucket的flush方法, 将里面的entry都再次add一次,在这个add里因task已过期,将被立即提交执行,
 * 同时reset这个bucket的过期时间, 这样它就可以用来装入新的task了
 * 2、这个时间轮是支持层级的，就是如果当前放入的TimerTaskEntry的过期时间如果超出了当前层级时间轮的覆盖范围，
 * 那么就创建一个overflowWheel，overflowWheel的一格（tick）时间跨度为上一个时间轮的interval(相当于老时间轮的tick * wheelSize)，
 * 基本可以类比成钟表的分针和时针
 * 3、def add(timerTaskEntry: TimerTaskEntry): Boolean: 将TimerTaskEntry加入适当的TimerTaskList
 * 4、def advanceClock(timeMs: Long)::推动时间轮向前走，更新CurrentTime
 * 5、值得注意的是TimingWheel这个类不是线程安全的，也就是说add方法和advanceClock的调用方式使用者要来保证
 * @author dreamyao
 * @title 时间轮 NotThreadSafe
 * @date 2019/12/1 下午9:56
 * @since 1.0.0
 */
class TimingWheel {

    // 时间轮所能表示的时间跨度，也就是tickMs*wheelSize
    private final long interval;
    // 时间格，kafka中称为桶,每个桶是一个双向链表
    private final TimerTaskList[] buckets;
    // 一个槽所代表的时间范围，kafka的默认值的1ms
    private final long tickMs;
    // 该时间轮有多少个槽，kafka的默认值是20
    private final int wheelSize;
    // 该时间轮的开始时间
    private final long startMs;
    // 该时间轮的任务总数
    private final AtomicInteger taskCounter;
    // 是一个TimerTaskList的延迟队列。每个槽都有它一个对应的TimerTaskList，
    // TimerTaskList是一个双向链表，有一个expireTime的值，
    // 这些TimerTaskList都被加到这个延迟队列中，expireTime最小的槽会排在队列的最前面
    private final DelayQueue<TimerTaskList> queue;
    // 当前时间，也就是时间轮指针指向的时间
    private long currentTime;
    // 上层时间轮
    // 两个并发线程可以通过add（）来更新和读取overflowWheel。
    // 因此，由于JVM的双重检查锁定模式的问题，它需要volatile修饰
    private volatile TimingWheel overflowWheel;

    /**
     * @param tickMs      轮子每个格子的时间
     * @param wheelSize   每个轮子的大小
     * @param startMs     开始时间
     * @param taskCounter 任务计数器
     * @param queue       延迟队列
     */
    protected TimingWheel(long tickMs, int wheelSize, long startMs, AtomicInteger taskCounter,
                          DelayQueue<TimerTaskList> queue) {
        this.tickMs = tickMs;
        this.wheelSize = wheelSize;
        this.interval = tickMs * wheelSize;
        this.startMs = startMs;
        this.taskCounter = taskCounter;
        this.queue = queue;
        this.currentTime = startMs - (startMs % tickMs);
        buckets = new TimerTaskList[wheelSize];
        for (int i = 0; i < wheelSize; i++) {
            buckets[i] = new TimerTaskList(taskCounter);
        }
    }

    /**
     * 添加任务，返回是否成功
     * @param timerTaskEntry 任务链表项
     * @return 是否成功
     */
    protected boolean add(TimerTaskEntry timerTaskEntry) {
        long expiration = timerTaskEntry.expirationMs;
        if (timerTaskEntry.cancelled()) {
            // 如果任务在添加之前，已经被取消掉了
            return false;
        } else if (expiration < currentTime + tickMs) {
            // 如果任务已经过期了
            return false;
        } else if (expiration < currentTime + interval) {
            // 计算添加到哪个时间格
            long virtualId = expiration / tickMs;
            int index = (int) (virtualId % wheelSize);
            // 从时间轮上找到对应的时间格
            TimerTaskList bucket = buckets[index];
            // 添加任务到对应的任务列表
            bucket.add(timerTaskEntry);
            // 设置任务列表的过期时间
            if (bucket.setExpiration(virtualId * tickMs)) {
                // 将列表添加到DelayQueue
                queue.offer(bucket);
            }
            return true;
        } else {
            // 超时当前时间轮最大时间跨度
            if (overflowWheel == null) {
                // 创建父级时间轮
                addOverflowWheel();
            }
            // 添加当前任务到父级时间轮
            overflowWheel.add(timerTaskEntry);
            return true;
        }
    }

    private void addOverflowWheel() {
        synchronized (this) {
            if (overflowWheel == null) {
                overflowWheel = new TimingWheel(interval, wheelSize, startMs,
                        taskCounter, queue);
            }
        }
    }

    // 尝试提前计时
    protected void advanceClock(long timeMs) {
        // 只有过了单位时间，才会更新当前时间
        if (timeMs >= currentTime + tickMs) {
            currentTime = timeMs - (timeMs % tickMs);
            // 尝试提前设置父级时间轮的时钟
            if (overflowWheel != null) {
                overflowWheel.advanceClock(currentTime);
            }
        }
    }
}
