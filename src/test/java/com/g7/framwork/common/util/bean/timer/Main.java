package com.g7.framwork.common.util.bean.timer;


import com.g7.framwork.common.util.timer.SystemTimer;
import com.g7.framwork.common.util.timer.Timer;
import com.g7.framwork.common.util.timer.TimerTask;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        Timer timer = new SystemTimer(1, 60);
        for (int i = 0; i < 100; i++) {
            timer.add(new DemoTimerTask(10, System.nanoTime()));
        }

    }

    private static class DemoTimerTask extends TimerTask {

        private String name;
        private final long time;

        private DemoTimerTask(long delayTime, long time) {
            super(delayTime);
            this.time = time;
            this.name = delayTime + "";

        }

        @Override
        public void run() {
            System.out.println(name + " 执行了" + " 执行时间：" + (TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - time)) + "ms");
        }
    }
}
