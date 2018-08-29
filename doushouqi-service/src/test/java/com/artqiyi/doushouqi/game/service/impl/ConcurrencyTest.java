package com.artqiyi.doushouqi.game.service.impl;

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrencyTest {
    private static final AtomicInteger ato = new AtomicInteger(0);
    private int a = 0;
    private int b = 0;
    public void test(String s){

        synchronized (s){
            System.out.println(ato.incrementAndGet());
            if ("aa".equals(s)){
                a++;
                System.out.println("a="+a);
            }
            if ("dd".equals(s)){
                b++;
                System.out.println("b="+b);
            }
        }
    }

    public static void main(String[] args) {
        CountDownLatch latch = new CountDownLatch(100);
        ExecutorService executorService = Executors.newCachedThreadPool();
        ConcurrencyTest test = new ConcurrencyTest();
        String[] ss = {"aa","dd"};
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            executorService.execute(()->{
                latch.countDown();
                try {
                    String s = ss[r.nextInt(2)];
                    latch.await();
                    test.test(s);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("amax="+test.a);
        System.out.println("bmax="+test.b);
    }
}
