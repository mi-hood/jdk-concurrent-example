package concurrent_collection;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/23 14:22
 */

import java.util.*;
import java.util.concurrent.*;

public class ConcurrentQueue {

    //compareAndSwap UNSAFE method
   ConcurrentLinkedQueue concurrentLinkedQueue=new ConcurrentLinkedQueue();
   SynchronousQueue synchronousQueue=new SynchronousQueue();
   Exchanger exchanger=new Exchanger();
   ConcurrentSkipListMap concurrentSkipListMap=new ConcurrentSkipListMap();
   //COW reentranLock
   CopyOnWriteArrayList cowList=new CopyOnWriteArrayList();
   CopyOnWriteArraySet cowSet=new CopyOnWriteArraySet();
   //condition  write&read split
    ArrayBlockingQueue arrayBlockingQueue=new ArrayBlockingQueue(20);
    LinkedBlockingQueue linkedBlockingQueue=new LinkedBlockingQueue();
    public static void main(String[] args) {
    }
}
