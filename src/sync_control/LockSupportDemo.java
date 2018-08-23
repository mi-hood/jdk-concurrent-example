package sync_control;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/22 17:57
 */

import java.util.concurrent.locks.LockSupport;

public class LockSupportDemo extends Thread{
    static Object o=new Object();

    public LockSupportDemo(String name) {
        super(name);
    }

    @Override
    public void run() {
        synchronized (o){
            System.out.println("in "+getName());
//            LockSupport.park();
            Thread.currentThread().suspend();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1=new LockSupportDemo("one");
        Thread t2=new LockSupportDemo("two");
        t1.start();
        Thread.sleep(500);
        t2.start();

        t1.resume();
//        uncomment to make resume validate
//        Thread.sleep(1000);
        t2.resume();
//        LockSupport.unpark(t1);
//        LockSupport.unpark(t2);
        t1.join();
        t2.join();
    }
}