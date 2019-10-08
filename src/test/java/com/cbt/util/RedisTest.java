package com.cbt.util;


import org.apache.poi.util.SystemOutLogger;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RedisTest {

    @Test
    public void test1() throws InterruptedException {
        for (int i = 0; i < 1000; i++) {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Redis.hset("test", "field", "value");
                }
            };
            thread.start();
        }
        while(true){
            System.out.println("aaaa");
        }
    }


    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < 10000; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Redis.hset("test", "field", "value");
                }
            });
        }
        executorService.shutdown();
        while (!executorService.awaitTermination(10, TimeUnit.MINUTES)) {
            System.out.println("子线程执行中......");
        }
    }
}
