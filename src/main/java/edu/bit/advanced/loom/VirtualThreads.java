package edu.bit.advanced.loom;

import java.time.Duration;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class VirtualThreads {

    // virtual threads are scheduled by a global scheduler with as many workers as there are CPU cores
    // or as explicitly set with -Djdk.defaultScheduler.parallelism=N
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            IntStream.range(0, 10_000).forEach(i -> {
                executor.submit(() -> {
                    Thread.sleep(Duration.ofSeconds(1));
                    return i;
                });
            });
        }  // executor.close() is called implicitly, and waits

        System.out.println("Before a virtual thread! In - " + Thread.currentThread().threadId());
        Thread.ofVirtual().start(() -> System.out.println("Inside of a virtual thread! In - " + Thread.currentThread().threadId()));
        System.out.println("After a virtual thread! In - " + Thread.currentThread().threadId());

        ThreadFactory tf = Thread.ofVirtual().factory();
        ExecutorService e = Executors.newThreadPerTaskExecutor(tf);
        Future<String> f = e.submit(() -> "Naman"); // spawns a new virtual thread
        String y = f.get(); // joins the virtual thread
    }
}