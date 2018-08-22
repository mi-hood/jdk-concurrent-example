package sync_control;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/21 18:20
 */

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockDemo {
    private static ReentrantLock lock=new ReentrantLock();
    private static ReadWriteLock readWriteLock=new ReentrantReadWriteLock();
    private static Lock readLock=readWriteLock.readLock();
    private static Lock writeLock=readWriteLock.writeLock();
    private int value;
    public Object handleRead(Lock lock){
        try {
            lock.lock();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return value;
    }

    public void handleWrite(Lock lock,int v){
        try {
            lock.lock();
            value=v;
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReadWriteLockDemo readWriteLockDemo=new ReadWriteLockDemo();
        Runnable readTask=new Runnable() {
            @Override
            public void run() {
//                readWriteLockDemo.handleRead(readLock);
                readWriteLockDemo.handleRead(lock);
            }
        };

        Runnable writeTasl=new Runnable() {
            @Override
            public void run() {
//                readWriteLockDemo.handleWrite(writeLock,new Random().nextInt());
                readWriteLockDemo.handleWrite(lock,new Random().nextInt());
            }
        };
        long start=System.currentTimeMillis();

        for(int i=0;i<20;i++){
            Thread t=new Thread(readTask);
            t.start();
        }

        for(int i=0;i<5;i++){
            Thread t=new Thread(writeTasl);
            t.start();
        }
        while(Thread.activeCount()>2){
            System.out.println(Thread.activeCount()+" thread is working...");
            Thread.sleep(1000);
        }
        System.out.println("task last "+(System.currentTimeMillis()-start)/1000+"s");
    }
}
