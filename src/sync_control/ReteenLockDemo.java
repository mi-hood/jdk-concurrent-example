package sync_control;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/20 17:16
 */

import java.util.concurrent.locks.ReentrantLock;

public class ReteenLockDemo {
    public static ReentrantLock lockA = new ReentrantLock();
    public static ReentrantLock lockB = new ReentrantLock();
    public static int i = 0;


    public static class SimpleLock implements Runnable {
        @Override
        public void run() {
            try {
                lockA.lock();
                lockA.lock();
                int count = 0;
                Thread.sleep(1000);
                while (count < 10000) {
                    i++;
                    count++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lockA.unlock();
                lockA.unlock();
            }
        }
    }

    public static class LockInteruptDemo implements Runnable {
        int i;

        public LockInteruptDemo(int i) {
            this.i = i;
        }

        @Override
        public void run() {

            try {
                if (i == 1) {
                    lockA.lockInterruptibly();
                    Thread.sleep(1000);
                    lockB.lockInterruptibly();
                } else {
                    lockB.lockInterruptibly();
                    Thread.sleep(1000);
                    lockA.lockInterruptibly();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lockA.isHeldByCurrentThread()) {
                    lockA.unlock();
                }
                if (lockB.isHeldByCurrentThread()) {
                    lockB.unlock();
                }
                System.out.println(Thread.currentThread().getId() + " : " + i + " exit");
            }
        }
    }


    public static void main(String[] args) {
        int me = 1;
        //reentanlock
        if (me == 1)
            try {
                Thread t1 = new Thread(new SimpleLock());
                Thread t2 = new Thread(new SimpleLock());
                t1.start();
                t2.start();
                t1.join();
                t2.join();
                System.out.println(i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        //lockinterupt
        if (me == 2)
            try {
                Thread t1 = new Thread(new LockInteruptDemo(1));
                Thread t2 = new Thread(new LockInteruptDemo(2));
                t1.start();
                t2.start();
                Thread.sleep(3000);
                t1.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }




    }
}
