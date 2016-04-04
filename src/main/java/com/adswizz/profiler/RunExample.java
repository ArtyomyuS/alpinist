package com.adswizz.profiler;


public class RunExample {

    public RunExample() {

    }

    public void doSleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

    private void doTask() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
    }

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