package concurrent_collection;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/23 9:23
 */


import java.util.*;

public class SyncWrapDemo implements Runnable {
    static List<Integer> list;
    static Map<String, Object> map;
    static int STEP = 2;
    static int type = 1;
    int start;

    public SyncWrapDemo(int start) {
        this.start = start;
    }

    @Override
    public void run() {
        if (type == 1)
            for (int i = start; i <= 100000; i += STEP) {
                list.add(i);
            }
        if (type == 2)
            for (int i = start; i <= 100000; i += STEP) {
                map.put(Integer.toString(i), Integer.toHexString(i));
            }
    }

    public static void main(String[] args) throws InterruptedException {

        list = new ArrayList<>();
        map = new HashMap<>();
//        list = Collections.synchronizedList(list);
//        map = Collections.synchronizedMap(map);
        for (int i = 1; i <= STEP; i++) {
            Thread t = new Thread(new SyncWrapDemo(i));
            t.start();
        }
        while (Thread.activeCount() > 2) ;
        if (type == 1)
            System.out.println(list.size());
        if (type == 2)
            System.out.println(map.size());
    }

}
