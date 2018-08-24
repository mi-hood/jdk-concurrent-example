package concurrent_collection;

/*
 *  mail: liubing@andlinks.com
 *  author: 刘兵
 *  version: 2018/8/24 9:18
 */

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class UnsafeDemo {
    private  Unsafe unsafe;
    private volatile int testValue;
    private static long valueOffset;

    public UnsafeDemo(int initialValue,Unsafe unsafe) {
        this.testValue = initialValue;
        this.unsafe=unsafe;
        try {
            valueOffset = unsafe.objectFieldOffset
                    (UnsafeDemo.class.getDeclaredField("testValue"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    /**
     * get an unsafe instance by fields
     *
     * @return
     */
    public static Unsafe getUnsafeByExtractFields() throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafe = null;
        theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafe.setAccessible(true);
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);
        return unsafe;
    }

    /**
     * get an unsafe instance by invoke a privite constructor
     *
     * @return
     */
    public static Unsafe getUnsafeByInvokeConstructor() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<Unsafe> unsafeConstructor = null;
        unsafeConstructor = Unsafe.class.getDeclaredConstructor();
        unsafeConstructor.setAccessible(true);
        Unsafe unsafe = unsafeConstructor.newInstance();
        return unsafe;
    }


    public int getTestValue() {
        return testValue;
    }


    public Integer getAndDegement() {
        return unsafe.getAndAddInt(this, valueOffset, -1);
    }

    public static void main(String[] args) throws InterruptedException, NoSuchFieldException {


        try {
            Integer THREAD_NUM = 5;
            Unsafe unsafe = UnsafeDemo.getUnsafeByInvokeConstructor();
            UnsafeDemo unsafeDemo=new UnsafeDemo(100,unsafe);

            System.out.println("before delete:"+unsafeDemo.getTestValue());
            ExecutorService executorService= Executors.newFixedThreadPool(THREAD_NUM);
            for(int i=0;i<THREAD_NUM;i++){
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        unsafeDemo.getAndDegement();
                    }
                });
            }
            executorService.shutdown();
            executorService.awaitTermination(1, TimeUnit.MINUTES);
            System.out.println("after delete:"+unsafeDemo.getTestValue());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }
}
