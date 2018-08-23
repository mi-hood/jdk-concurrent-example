package concurrent_collection;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/23 20:22
 */

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CowListDemo {
    static int step=100000;
    public static class CollectionActionInsert implements Runnable {
        List<String> map;
        int start;
        public CollectionActionInsert(List<String> map, int start) {
            this.map = map;
            this.start=start;
        }


        @Override
        public void run()   {
            for(int j=0;j<100000;start++,j++){
                map.add(Integer.toString(start));
            }
        }
    }


    public static class CollectionActionSearch implements Runnable{
        List<String> map;
        public CollectionActionSearch( List<String> map ) {
            this.map = map;
        }
        @Override
        public void run() {
            for(int i=1;i<100000;i++){
                map.get(new Random().nextInt(100000));
            }
        }
    }


    public static void main(String[] args) {
        //CopyOnWriteArrayList:  1 0 0  / 5  104  0 /...  写入速度过慢
        //syncList:              1 0 0 / 5  0   0 / 10  0  0
        final int type=2;
        final int THREAD_NUM=2;
        CopyOnWriteArrayList<String> cowList=new CopyOnWriteArrayList<>();
        List<String> syncList= Collections.synchronizedList(new ArrayList<String>());
        ExecutorService executorService= Executors.newFixedThreadPool(THREAD_NUM);

        //insert
        long start=System.currentTimeMillis();
        for(int i=0;i<THREAD_NUM;i++) {
            if(type==1) {
                executorService.submit(new CollectionActionInsert(cowList, i*step));
            }
            if(type==2) {
                 executorService.submit(new CollectionActionInsert(syncList, i*step));
            }
        }
        executorService.shutdown();
        while(!executorService.isTerminated());
        long end=System.currentTimeMillis();
        System.out.println("insert consume "+(end-start)/1000+"s");

        //search
        executorService=Executors.newFixedThreadPool(THREAD_NUM);
        start=System.currentTimeMillis();
        for(int i=0;i<THREAD_NUM;i++) {
            if(type==1) {
                 executorService.submit(new CollectionActionSearch(cowList ));
            }
            if(type==2) {
                 executorService.submit(new CollectionActionSearch(syncList ));
            }
        }
        executorService.shutdown();
        while(!executorService.isTerminated());
        end=System.currentTimeMillis();
        System.out.println("search consume "+(end-start)/1000+"s");
    }

}
