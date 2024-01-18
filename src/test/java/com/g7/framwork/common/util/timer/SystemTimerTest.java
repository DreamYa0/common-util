package com.g7.framwork.common.util.timer;

import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

/**
 * @author dreamyao
 * @title
 * @date 2021/1/5 11:29 下午
 * @since 1.0.0
 */
public class SystemTimerTest {

    @Test
    public void testAdd() {
        System.out.println(System.currentTimeMillis());
        System.out.println(TimeUnit.NANOSECONDS.toMillis(System.nanoTime()));
    }
}