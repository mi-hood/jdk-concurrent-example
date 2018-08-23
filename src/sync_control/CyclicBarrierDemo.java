package sync_control;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/22 14:18
 */

import java.util.Random;
import java.util.concurrent.*;

public class CyclicBarrierDemo {
    public static class Chef implements Runnable {
        private final CyclicBarrier cyclicBarrier;
        private final String chefName;

        public Chef(CyclicBarrier cyclicBarrier, String chefName) {
            this.cyclicBarrier = cyclicBarrier;
            this.chefName = chefName;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(new Random().nextInt(10)*1000);
                System.out.println("Chef"+chefName+":stuff prepared");
                cyclicBarrier.await();


                Thread.sleep(new Random().nextInt(10)*1000);
                System.out.println("Chef"+chefName+":food cooked");
                cyclicBarrier.await();

                Thread.sleep(new Random().nextInt(10)*1000);
                System.out.println("Chef"+chefName+":meal decorating");
                cyclicBarrier.await();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    public static class BarrierWork implements Runnable {
        int todo;

        public BarrierWork(int i) {
            this.todo = i;
        }

        @Override
        public void run() {
            try {
                if (todo == 1) {
                    Thread.sleep(new Random().nextInt(10) * 1000);
                    System.out.println("Head Chef:stuff prepared.");
                    todo++;
                } else if (todo == 2) {
                    Thread.sleep(new Random().nextInt(10) * 1000);
                    System.out.println("Head Chef:food cooked.");
                    todo++;
                }else  if(todo==3){
                    Thread.sleep(new Random().nextInt(10) * 1000);
                    System.out.println("Head Chef:meal decorated.");
                    todo++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static class MyExecutorService  {
        public static ExecutorService myThreadPool(int nThreads) {
            return new ThreadPoolExecutor(nThreads, nThreads+5,
                    20L, TimeUnit.SECONDS,
                    new ArrayBlockingQueue<>(10),Executors.defaultThreadFactory());
        }
    }
    public static void main(String[] args) throws InterruptedException {
        final int Chef_NUM=10;
        ExecutorService es=MyExecutorService.myThreadPool(Chef_NUM);
        CyclicBarrier cyclicBarrier=new CyclicBarrier(Chef_NUM,new BarrierWork(1));
        for(int i=0;i<Chef_NUM;i++){
            es.submit(new Chef(cyclicBarrier,String.valueOf( (char) (i+'A'))));
        }
        es.shutdown();
        while(!es.isTerminated());
        System.out.println("just eat!");
    }


}
