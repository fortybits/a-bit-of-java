package edu.bit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.concurrent.*;

public class ThreadCreator {


    public static void main(String[] args) {
        ExecutorService executor1;
        try{
            Method method = Executors.class.getMethod("newVirtualThreadPerTaskExecutor");
            executor1 = (ExecutorService) method.invoke(null);
        }catch(NoSuchElementException e) {
            executor1 = Executors.newFixedThreadPool(10);//or similar
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }



        ThreadFactory threadFactory = Runtime.version().feature() < 21 ?
                Executors.defaultThreadFactory() : Thread.ofVirtual().name("").factory();
        try (var executor = Executors.newThreadPerTaskExecutor(threadFactory)) {
        }

    }

    public static void scheduledExecutor() {
        ScheduledExecutorService virtualExecutor = Executors.newScheduledThreadPool(0, Thread.ofVirtual().factory());
        virtualExecutor.scheduleWithFixedDelay(() -> System.out.println("A"), 0, 1000, TimeUnit.MILLISECONDS);
        virtualExecutor.scheduleWithFixedDelay(() -> System.out.println("B"), 0, 1, TimeUnit.MINUTES);
// java.util.concurrent.ScheduledThreadPoolExecutor@65b3120a[Running, pool size = 1, active threads = 0, queued tasks = 2, completed tasks = 2]

//        ScheduledExecutorService executor = Executors.newScheduledThreadPool();
//        executor.scheduleWithFixedDelay(() -> System.out.println("A"), 0, 1000,
//                TimeUnit.MILLISECONDS);
//        executor.scheduleWithFixedDelay(() -> System.out.println("B"), 0, 2000, TimeUnit.MILLISECONDS);
//// java.util.concurrent.ScheduledThreadPoolExecutor@277050dc[Running, pool size = 1, active threads = 0, queued tasks = 2, completed tasks = 2]
        System.out.println("Wait!");
    }
    public ExecutorService createExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    public boolean isVirtual() {
        return true;
    }
}
