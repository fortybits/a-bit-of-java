package edu.bit;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

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
                Executors.defaultThreadFactory() : Thread.ofVirtual().factory();
        try (var executor = Executors.newThreadPerTaskExecutor(threadFactory)) {

        }

    }

    public ExecutorService createExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    public boolean isVirtual() {
        return true;
    }
}
