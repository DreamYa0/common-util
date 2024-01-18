package com.g7.framwork.common.util.timer;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * TimerTaskList作为时间轮上的一个bucket, 是一个有头指针的双向链表
 * @author dreamyao
 * @title 延迟任务链表(线程安全)ThreadSafe
 * @date 2019/12/1 下午9:56
 * @since 1.0.0
 */
class TimerTaskList implements Delayed {

    // TimerTaskList使用虚拟根条目形成双向链接的循环列表
    // root.next指向头部
    // root.prev指向尾部
    // root节点不存储任何任务的空节点
    private final TimerTaskEntry root = new TimerTaskEntry(null, -1L);
    // 每个TimerTaskList都是时间轮上的一个bucket,自然也要关联一个过期时间
    private final AtomicLong expiration = new AtomicLong(-1L);
    // 当前链表任务总数
    private final AtomicInteger taskCounter;

    protected TimerTaskList(AtomicInteger taskCounter) {
        // 初始化设置前驱节点为空节点
        root.prev = root;
        // 初始化设置后继节点为空节点
        root.next = root;
        // 初始化任务数
        this.taskCounter = taskCounter;
    }

    public synchronized void foreach(Consumer<TimerTask> foreach) {
        TimerTaskEntry entry = root.next;
        while (!entry.equals(root)) {
            TimerTaskEntry next = entry.next;
            if (!entry.cancelled()) {
                foreach.accept(entry.timerTask);
            }
            entry = next;
        }
    }

    // 将计时器任务条目添加到此列表
    protected void add(TimerTaskEntry timerTaskEntry) {
        boolean done = false;
        while (!done) {
            // 如果计时器任务条目已经在其他列表中，则将其删除
            // 我们在下面的同步块之外执行此操作以避免死锁。
            // 我们可以重试，直到timerTaskEntry.list为空。
            timerTaskEntry.remove();
            // 保护多线程 add
            synchronized (this) {
                // 保护单个节点的 add
                synchronized (timerTaskEntry) {
                    // 确认当前节点已经成功被 remove
                    if (timerTaskEntry.list == null) {
                        // 将计时器任务条目放在列表的末尾。（root.prev指向尾部条目）
                        TimerTaskEntry tail = root.prev;
                        timerTaskEntry.next = root;
                        timerTaskEntry.prev = tail;
                        timerTaskEntry.list = this;
                        tail.next = timerTaskEntry;
                        root.prev = timerTaskEntry;
                        taskCounter.incrementAndGet();
                        done = true;
                    }
                }
            }
        }
    }

    // 设置存储桶的到期时间
    // 如果更改了到期时间，则返回true
    protected boolean setExpiration(long expirationMs) {
        return expiration.getAndSet(expirationMs) != expirationMs;
    }

    // 获取存储桶的到期时间
    protected long getExpiration() {
        return expiration.get();
    }

    protected void remove(TimerTaskEntry timerTaskEntry) {
        // 保护多线程 remove
        synchronized (this) {
            // 保护单个节点的 remove
            synchronized (timerTaskEntry) {
                // 确认当前节点还在当前链表上才移除
                if (timerTaskEntry.list.equals(this)) {
                    timerTaskEntry.next.prev = timerTaskEntry.prev;
                    timerTaskEntry.prev.next = timerTaskEntry.next;
                    timerTaskEntry.next = null;
                    timerTaskEntry.prev = null;
                    timerTaskEntry.list = null;
                    taskCounter.decrementAndGet();
                }
            }
        }
    }

    // 在链表的每个元素上应用给定的函数,并清空整个链表, 同时超时时间也设置为-1
    protected synchronized void flush(Consumer<TimerTaskEntry> flush) {
        // 遍历链表，执行延迟任务
        TimerTaskEntry head = root.next;
        while (!head.equals(root)) {
            remove(head);
            flush.accept(head);
            head = root.next;
        }
        expiration.set(-1L);
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(
                Math.max(expiration.get() - TimeUnit.NANOSECONDS.toMillis(System.nanoTime()),0),
                TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        if (o instanceof TimerTaskList) {
            return Long.compare(expiration.get(), ((TimerTaskList) o).getExpiration());
        }
        return 0;
    }
}
