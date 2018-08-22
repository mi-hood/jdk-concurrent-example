package sync_control;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/21 16:31
 */


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreDemo extends Thread{
    static final Semaphore semaphore=new Semaphore(5);

    @Override
    public void run() {
         try {
             semaphore.acquireUninterruptibly();
             Thread.sleep(2000);
             System.out.println(Thread.currentThread().getId()+":ok");
             semaphore.release();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
    }

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService= Executors.newCachedThreadPool();
        for(int i=0;i<50;i++){
            executorService.submit(new SemaphoreDemo());
        }
    }
}


