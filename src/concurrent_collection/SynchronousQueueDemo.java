package concurrent_collection;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/23 17:02
 */

import java.util.concurrent.SynchronousQueue;

public class SynchronousQueueDemo {
    static final SynchronousQueue<Integer> queue = new SynchronousQueue<Integer>();

    public static  class  PutTask implements Runnable{
        @Override
        public void run() {
            System.out.println("put thread start");
            try {
                queue.put(1);
            } catch (InterruptedException e) {
            }
            System.out.println("put thread end");
        }
    }
    public static class TakeTask implements Runnable{
        @Override
        public void run() {
            System.out.println("take thread start");
            try {
                System.out.println("take from putThread: " + queue.take());
            } catch (InterruptedException e) {
            }
            System.out.println("take thread end");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread product=new Thread(new PutTask());
        Thread consumer=new Thread(new TakeTask());
        consumer.start();
        product.start();
        product.join();
        consumer.join();
    }
}
