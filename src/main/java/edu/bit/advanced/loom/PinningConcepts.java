package edu.bit.advanced.loom;

import java.time.LocalDateTime;
import java.util.concurrent.*;

public class PinningConcepts {

    private static final Object lock = new Object();
    private static final int MAX_TASKS = 10;
    private static final int MAX_CONCURRENT_TASKS = 5;
    private static final Semaphore semaphore = new Semaphore(MAX_CONCURRENT_TASKS);


    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        // avoid managing the virtual thread pools
        try (var executor = Executors.newScheduledThreadPool(5, Thread.ofVirtual().factory())) {
            for (int taskId = 0; taskId < MAX_TASKS; taskId++) {
                final int taskIdentifier = taskId;
                printInfo(taskId, "Submitting the task to executor: ");
                Future<?> submit = executor.submit(() -> pinningTask(taskIdentifier));
                submit.get();
                printInfo(taskId, "Computation completed: ");
            }
        }

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int taskId = 0; taskId < MAX_TASKS; taskId++) {
                printInfo(taskId, "Submitting the task to executor via semaphore: ");
                final int taskIdentifier = taskId;
                executor.submit(() -> lockViaSemaphore(taskIdentifier));
                printInfo(taskId, "Computation completed via semaphore: ");

            }
        }
    }

    private static void pinningTask(int taskId) {
        printInfo(taskId, "Virtual thread started: ");
        synchronized (lock) {
            printInfo(taskId, "Entering synchronized block: ");
            try {
                Thread.sleep(2000);  // This will pin the carrier thread
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            printInfo(taskId, "Exiting synchronized block: ");
        }
        printInfo(taskId, "Virtual thread completed: ");
    }

    static void lockViaSemaphore(int taskId) {
        try {
            semaphore.acquire();
            pinningTask(taskId);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }

    private static void printInfo(int taskId, String message) {
        System.out.println(String.format("[%s]", LocalDateTime.now()) + "\t" + String.format("[%s]", taskId) +
                "\t" + String.format("[%s]", Thread.currentThread()) + "\t" + message);
    }

}