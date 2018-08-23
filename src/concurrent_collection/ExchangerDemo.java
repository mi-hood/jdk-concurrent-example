package concurrent_collection;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/23 18:40
 */

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangerDemo {

    public static class Trader implements Runnable{
        String name;
        String stuff;
        Exchanger trade;
        public Trader(String name, String stuff, Exchanger tradeFair) {
            this.name=name;
            this.stuff = stuff;
            this.trade=tradeFair;
        }


        @Override
        public void run() {
            try {
                System.out.println(name+":I have a "+stuff);
                Thread.sleep(1000);
                String stuff_get=trade.exchange(stuff).toString();
                System.out.println(name+":I get a "+stuff_get);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final int THREAD_NUM=10;
        final List<String> stuffList= Arrays.asList("apple","pear","apricot","peach","grape","banana","pineapple","plum","watermelon","orange","lemon");
        Exchanger tradeFair=new Exchanger();
        ExecutorService executorService= Executors.newFixedThreadPool(THREAD_NUM);
        for(int i=0;i<THREAD_NUM;i++){
            executorService.submit(new Trader(String.valueOf((char)(i+'A')),stuffList.get(i),tradeFair));
        }
        executorService.shutdown();
        while(!executorService.isTerminated());
    }
}
