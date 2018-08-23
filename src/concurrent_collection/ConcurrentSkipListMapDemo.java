package concurrent_collection;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/23 19:01
 */

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

public class ConcurrentSkipListMapDemo {
    static int step=1000000;
    public static class CollectionActionInsert implements Callable<Integer>{
        Map<String,String> map;
        int start;
        public CollectionActionInsert(Map<String, String> map, int start) {
            this.map = map;
            this.start=start;
        }


        @Override
        public Integer call() throws Exception {
            for(int j=0;j<1000000;start++,j++){
                map.put(Integer.toString(start),Integer.toHexString(start));
            }
            return start;
        }
    }


    public static class CollectionActionSearch implements Callable<Integer>{
        Map<String,String> map;
        public CollectionActionSearch(Map<String, String> map) {
            this.map = map;
        }
        @Override
        public Integer call() throws Exception {
           for(int i=1;i<1000000;i++){
               map.get(Integer.toString(new Random().nextInt(1000000)));
           }
            return 0;
        }
    }

    public static void main(String[] args) {
        //1 :syncHashMap            20:27 49 / 10:14 16 / 5:9 4 / 4:9 3 / 1:2 2
        //2 :concurrentSkipListMap  20:46 90 / 10:14 9 / 5:5 3  / 4:4 2 / 1:0 1
        //3:concurrentHashMap       20:46 93 / 10:16 1 / 5:5 1  / 4:6 0 / 1:1 0
        final int type=3;
        final int THREAD_NUM=1;
        ConcurrentSkipListMap<String,String> skipListMap=new ConcurrentSkipListMap<>();
        Map<String,String> syncMap= Collections.synchronizedMap(new HashMap<>());
        ConcurrentHashMap<String,String> concurrentHashMap=new ConcurrentHashMap();
        ExecutorService executorService=Executors.newFixedThreadPool(THREAD_NUM);

        //insert
        long start=System.currentTimeMillis();
        for(int i=0;i<THREAD_NUM;i++) {
            Future<Integer> res;
            if(type==1) {
                res = executorService.submit(new CollectionActionInsert(skipListMap, i*step));
            }
            if(type==2) {
                res = executorService.submit(new CollectionActionInsert(syncMap, i*step));
            }
            if(type==3){
                res = executorService.submit(new CollectionActionInsert(concurrentHashMap, i*step));
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
            Future<Integer> res;
            if(type==1) {
                res = executorService.submit(new CollectionActionSearch(skipListMap ));
            }
            if(type==2) {
                res = executorService.submit(new CollectionActionSearch(syncMap ));
            }
            if(type==3) {
                res = executorService.submit(new CollectionActionSearch(concurrentHashMap ));
            }
        }
        executorService.shutdown();
        while(!executorService.isTerminated());
        end=System.currentTimeMillis();
        System.out.println("search consume "+(end-start)/1000+"s");
    }
}
