package sync_control;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/20 16:32
 */

public class WaitNotifyDemo {
    static final Object scObject = new Object();


    public static class T1 extends Thread {
        @Override
        public void run() {
            synchronized (scObject) {
                System.out.println(this.getClass().getName()+" method get the lockA");
                try {
                    scObject.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(this.getClass().getName()+" release lockA");
            }
        }
    }

    public static class T2 extends Thread {
        @Override
        public void run() {
            synchronized (scObject) {
                System.out.println(this.getClass().getName()+" get the lockA");
                scObject.notify();
                System.out.println(this.getClass().getName()+" release lockA");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        T2 donotify=new T2();
        T1 dowait=new T1();
        dowait.start();
        donotify.start();
    }
}
