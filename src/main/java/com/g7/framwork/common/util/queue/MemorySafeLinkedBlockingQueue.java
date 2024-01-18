package com.g7.framwork.common.util.queue;

import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author dreamyao
 * @title 内存安全的阻塞队列
 * @date 2022/6/7 11:11 AM
 * @since 1.0.0
 */
public class MemorySafeLinkedBlockingQueue<E> extends LinkedBlockingQueue<E> {

    private static final long serialVersionUID = 8032578371739960142L;

    // JVM默认可用内存最小不小于256M
    public static int THE_256_MB = 256 * 1024 * 1024;
    // JVM最大可用内存
    private int maxFreeMemory;

    public MemorySafeLinkedBlockingQueue() {
        this(THE_256_MB);
    }

    public MemorySafeLinkedBlockingQueue(final int maxFreeMemory) {
        super(Integer.MAX_VALUE);
        this.maxFreeMemory = maxFreeMemory;
    }

    public MemorySafeLinkedBlockingQueue(final Collection<? extends E> c,
                                         final int maxFreeMemory) {
        super(c);
        this.maxFreeMemory = maxFreeMemory;
    }

    public void setMaxFreeMemory(final int maxFreeMemory) {
        // 设置最大可用内存
        this.maxFreeMemory = maxFreeMemory;
    }

    public int getMaxFreeMemory() {
        // 获取最大可用内存。
        return maxFreeMemory;
    }

    public boolean hasRemainedMemory() {
        // 确定是否有剩余的可用内存。
        return MemoryLimitCalculator.maxAvailable() > maxFreeMemory;
    }

    @Override
    public void put(final E e) throws InterruptedException {
        if (hasRemainedMemory()) {
            super.put(e);
        }
    }

    @Override
    public boolean offer(final E e, final long timeout, final TimeUnit unit) throws InterruptedException {
        return hasRemainedMemory() && super.offer(e, timeout, unit);
    }

    @Override
    public boolean offer(final E e) {
        return hasRemainedMemory() && super.offer(e);
    }

    public static class MemoryLimitCalculator {

        // JVM最大可用内存
        private static volatile long maxAvailable;
        private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor();

        static {
            // 加载此类时立即刷新以防止maxAvailable为0
            refresh();
            // 每50ms检查一次以提高性能
            SCHEDULER.scheduleWithFixedDelay(MemoryLimitCalculator::refresh, 50, 50,
                    TimeUnit.MILLISECONDS);
            Runtime.getRuntime().addShutdownHook(new Thread(SCHEDULER::shutdown));
        }

        private static void refresh() {
            // Runtime#freeMemory equals MemoryUsage#getMax() - MemoryUsage#getUsed()
            maxAvailable = Runtime.getRuntime().freeMemory();
        }

        public static long maxAvailable() {
            // 获取当前JVM的最大可用内存。
            return maxAvailable;
        }

        public static long calculate(final float percentage) {
            // 以当前JVM的最大可用内存占结果的百分比作为限制。
            if (percentage <= 0 || percentage > 1) {
                throw new IllegalArgumentException();
            }
            // 可用内存
            return (long) (maxAvailable() * percentage);
        }

        public static long defaultLimit() {
            // 默认情况下，它占用当前JVM最大可用内存的80%。
            return (long) (maxAvailable() * 0.8);
        }
    }
}
