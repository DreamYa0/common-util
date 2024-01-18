package com.g7.framwork.common.util.timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SystemTimer implements Timer {

    private static final Logger logger = LoggerFactory.getLogger(SystemTimer.class);
    private final ExecutorService taskExecutor;
    private final ExecutorService bossExecutor = Executors.newFixedThreadPool(1);
    // 用于存储桶的延迟队列
    private final DelayQueue<TimerTaskList> delayQueue = new DelayQueue<>();
    // 任务计数
    private final AtomicInteger taskCounter = new AtomicInteger();
    // 时间轮，环形数组
    private final TimingWheel timingWheel;
    // 勾选时用于保护数据结构的锁
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock readLock = readWriteLock.readLock();
    private final Lock writeLock = readWriteLock.writeLock();
    private final CountDownLatch shutdown = new CountDownLatch(1);
    // 时间轮推进线程中断重启计数
    private final AtomicInteger retry = new AtomicInteger();

    /**
     * 创建时间轮
     * @param tickMs    轮子每个格子的时间
     * @param wheelSize 时间轮大小
     * @param interval  轮询间隔(此参数已经不再需要，改为可变参数目的是兼容之前的用法)
     */
    public SystemTimer(long tickMs, int wheelSize, long... interval) {
        timingWheel = new TimingWheel(tickMs, wheelSize,
                TimeUnit.NANOSECONDS.toMillis(System.nanoTime()), taskCounter,
                delayQueue);
        // 获取cpu核数
        final int cpuCount = Runtime.getRuntime().availableProcessors();
        taskExecutor = Executors.newFixedThreadPool(2 * cpuCount + 1);
        bossExecutor.submit(() -> {
            Thread.setDefaultUncaughtExceptionHandler((t, e) -> {
                logger.error("timing wheel boss thread interrupted exception", e);
                if (retry.incrementAndGet() <= 10) {
                    // 当线程异常中断时，从新拉起boss线程以便继续推进时间轮
                    doAdvanceClock();
                }
            });
            doAdvanceClock();
        });
    }

    private void doAdvanceClock() {
        while (shutdown.getCount() != 0) {
            try {
                advanceClock(200L);
            } catch (InterruptedException e) {
                logger.error("Boss executor interrupted exception", e);
            }
        }
    }

    @Override
    public void add(TimerTask task) {
        readLock.lock();
        try {
            addTimerTaskEntry(new TimerTaskEntry(task,
                    task.getDelayTime() + TimeUnit.NANOSECONDS.toMillis(System.nanoTime())));
        } finally {
            readLock.unlock();
        }
    }

    private void addTimerTaskEntry(TimerTaskEntry timerTaskEntry) {
        if (!timingWheel.add(timerTaskEntry)) {
            // 如果是因为任务过期，导致添加失败，那么将任务丢到线程池执行
            if (!timerTaskEntry.cancelled()) {
                taskExecutor.submit(timerTaskEntry.getTimerTask());
            }
        }
    }

    /**
     * 如果存储桶已过期，则提前时钟。如果在调用时没有任何过期的存储桶，则等待超时直到放弃。
     * @param timeout 超时时间
     * @throws InterruptedException 中断异常
     */
    @Override
    public boolean advanceClock(long timeout) throws InterruptedException {
        // DelayQueue查找过期的任务槽
        TimerTaskList bucket = delayQueue.poll(timeout, TimeUnit.MILLISECONDS);
        if (bucket != null) {
            writeLock.lock();
            try {
                while (bucket != null) {
                    // 时间推进
                    // 调用时间轮的advanceClock方法，更新该时间轮的时间
                    timingWheel.advanceClock(bucket.getExpiration());
                    // 对任务列表依次执行reinsert操作
                    bucket.flush(this::addTimerTaskEntry);
                    // 继续查看是否还有超时的任务列表
                    bucket = delayQueue.poll();
                }
            } finally {
                writeLock.unlock();
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int size() {
        return taskCounter.get();
    }

    @Override
    public void shutdown() {
        // 首先关闭任务执行线程池
        taskExecutor.shutdown();
        // 其次中断时间轮时间推进线程
        shutdown.countDown();
        // 最后关闭时间轮时间推进线程池
        bossExecutor.shutdown();
        logger.info("timing wheel is shutdown.");
    }
}
