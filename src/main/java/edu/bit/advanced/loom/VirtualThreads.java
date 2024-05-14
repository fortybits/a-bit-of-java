package edu.bit.advanced.loom;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class VirtualThreads {

    // virtual threads are scheduled by a global scheduler with as many workers as there are CPU cores
    // or as explicitly set with -Djdk.defaultScheduler.parallelism=N
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000)
                    .forEach(i -> executor.submit(() -> {
                        Thread.sleep(Duration.ofMillis(1200));
                        System.out.println(Thread.currentThread().threadId() + " : " + Thread.currentThread().getName()
                                + " : " + Thread.currentThread().getThreadGroup().getName() + " : " + i);
                        System.out.println("Active Count:: " + Thread.activeCount());
                        return i;
                    }));
//            executor.shutdown();
        }  // executor.close() is called implicitly, and waits

        System.out.println("Before a virtual thread! In - " + Thread.currentThread().threadId());
        Thread start = Thread.ofVirtual().start(() -> System.out.println("Inside of a virtual thread! In - " + Thread.currentThread().threadId()));
        start.join();
        System.out.println("Active Count:: " + Thread.activeCount());

        System.out.println("After a virtual thread! In - " + Thread.currentThread().threadId());

        ThreadFactory tf = Thread.ofVirtual().factory();
        ExecutorService e = Executors.newThreadPerTaskExecutor(tf);
        Future<String> f = e.submit(() -> "Naman"); // spawns a new virtual thread
        String y = f.get(); // joins the virtual thread

        System.out.println("Active Count:: " + Thread.activeCount());
    }
}