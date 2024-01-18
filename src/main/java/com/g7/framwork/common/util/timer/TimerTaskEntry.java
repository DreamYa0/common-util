package com.g7.framwork.common.util.timer;

/**
 * 作用：绑定一个TimerTask对象，然后被加入到一个TimerTaskList中
 * 它是TimerTaskList这个双向列表 中的元素，因此有如下三个成员
 * @author dreamyao
 * @title 任务链表项（ThreadSafe）
 * @date 2019/12/1 下午9:56
 * @since 1.0.0
 */
class TimerTaskEntry implements Comparable<TimerTaskEntry>{

    TimerTask timerTask;
    long expirationMs;
    volatile TimerTaskList list;
    TimerTaskEntry next;
    TimerTaskEntry prev;

    TimerTaskEntry(TimerTask timerTask, long expirationMs) {
        this.timerTask = timerTask;
        this.expirationMs = expirationMs;
        // 如果此timerTask已由现有的计时器任务条目保存，
        // setTimerTaskEntry将其删除。
        if (timerTask != null) {
            timerTask.setTimerTaskEntry(this);
        }
    }

    protected boolean cancelled() {
        return timerTask.getTimerTaskEntry() != this;
    }

    // 实际上就是把自己从当前所在TimerTaskList上摘掉, 为什么要使用一个while(...)来作，源码里是这样解释的
    // 如果在另一个线程将条目从任务条目列表移到另一个时调用remove，则可能由于list值的更改而无法删除条目。
    // 因此，我们重试直到列表变为空。 在极少数情况下，该线程会看到null并退出循环，但是另一个线程稍后会将条目插入到另一个列表中
    protected void remove() {
        TimerTaskList currentList = list;
        // 如果另一个线程将当前节点从一个链表移动到另一个链表，由于 list 会被修改成新链表的引用
        // 所以 remove 会失败，因此在这里用 currentList 暂存之前链表的引用，这样就避免锁住整个 list
        // 单线程: list.remove(this) 会将 this.list 置为 null, 移除后退出循环；
        // 多线程: 如果 this.list 被其它线程修改指向了新的链表, 那么循环会继续, 将该节点从新链表移除
        // 一个罕见场景: 线程 B 将该节点从链表 A 移除加入链表 B, 但是在修改节点的 list 之前, 线程 A
        // 就移除成功了, 获取的 list 为 null, 退出循环, 之后线程 B 将节点加入链表
        while (currentList != null) {
            // 如果在另一个线程将条目从任务条目列表移到另一个时调用了remove，
            // 由于list值的更改，这可能无法删除该条目。因此，我们重试直到列表变为空。
            // 在极少数情况下，该线程会看到null并退出循环，但是另一个线程稍后会将条目插入到另一个列表中。
            currentList.remove(this);
            currentList = list;
        }
    }

    protected TimerTask getTimerTask() {
        return timerTask;
    }

    protected void setTimerTask(TimerTask timerTask) {
        this.timerTask = timerTask;
    }

    @Override
    public int compareTo(TimerTaskEntry that) {
        return Long.compare(expirationMs, that.expirationMs);
    }
}
