package sync_control;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/22 13:55
 */

import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo implements Runnable{
    private final  CountDownLatch counter;
    private final  CountDownLatch startSinal;

    public CountDownLatchDemo( CountDownLatch startSinal,CountDownLatch counter) {
        this.counter = counter;
        this.startSinal = startSinal;
    }

    @Override
    public void run() {
        try {
            startSinal.await();
            Thread.sleep(new Random().nextInt(10)*1000);
            System.out.println("count down!");
            counter.countDown();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        final   Integer COUNT_INDEX=10;
        CountDownLatch start=new CountDownLatch(1);
        CountDownLatch count=new CountDownLatch(COUNT_INDEX);
        Runnable countDownLatchDemo=new CountDownLatchDemo(start,count);
        ExecutorService es= Executors.newFixedThreadPool(COUNT_INDEX);
        for(int i=0;i<COUNT_INDEX;i++){
            es.submit(countDownLatchDemo);
        }
        Thread.sleep(new Random().nextInt(10)*1000);
        System.out.println("start your counter..");
        start.countDown();
        count.await();
        System.out.println("fire the hole...");
        es.shutdown();
    }
}
