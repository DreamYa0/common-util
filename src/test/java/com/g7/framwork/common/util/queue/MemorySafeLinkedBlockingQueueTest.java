package com.g7.framwork.common.util.queue;

import net.bytebuddy.agent.ByteBuddyAgent;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.instrument.Instrumentation;

/**
 * @author dreamyao
 * @title
 * @date 2022/6/7 11:26 AM
 * @since 1.0.0
 */
public class MemorySafeLinkedBlockingQueueTest {

    @Test
    public void test() throws Exception {
        ByteBuddyAgent.install();
        final Instrumentation instrumentation = ByteBuddyAgent.getInstrumentation();
        final long objectSize = instrumentation.getObjectSize((Runnable) () -> {});
        int maxFreeMemory = (int) MemorySafeLinkedBlockingQueue.MemoryLimitCalculator.maxAvailable();
        MemorySafeLinkedBlockingQueue<Runnable> queue = new MemorySafeLinkedBlockingQueue<>(maxFreeMemory);
        // 所有内存都是为JVM保留的，因此它将在此处失败
        Assert.assertFalse(queue.offer(() -> {}));

        // maxFreeMemory-objectSize Byte memory is reserved for the JVM, so this will succeed
        queue.setMaxFreeMemory((int) (MemorySafeLinkedBlockingQueue.MemoryLimitCalculator.maxAvailable() - objectSize));
        Assert.assertTrue(queue.offer(() -> {}));
    }
}
