package sync_control;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/21 14:54
 */


import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConditionDemo extends Thread{
      final Lock lock = new ReentrantLock();
      final Condition condition  = lock.newCondition();

    @Override
    public void run() {
        try {
            lock.lock();
            condition.await();
            System.out.println("thread go on");
        }catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ConditionDemo conditionDemo=new ConditionDemo();
        conditionDemo.start();
        Thread.sleep(1000);
        conditionDemo.lock.lock();
        conditionDemo.condition.signal();
        conditionDemo.lock.unlock();
    }
}
