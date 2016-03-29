package com.adswizz.profiler;

import java.util.Random;

public class RunExample {
    private Random random = new Random();

    public RunExample() {

    }

    @Measured
    public void doSleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    @Measured
    private void doTask() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    @Measured
    public void doWork() {
        for (int i = 0; i < 5; i++) {
            doTask();
        }
    }

    public static void main(String[] args) {
        RunExample test = new RunExample();
        while (true) {
            test.doWork();
            test.doSleep();
        }
    }
}