package edu.bit.advanced.loom;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.Executors;


public class ListVT {

    public static void main(String[] args) throws InterruptedException {

        // Create and start virtual threads
        var executor = Executors.newVirtualThreadPerTaskExecutor();

        for (int i = 0; i < 10; i++) {
            executor.submit(() -> {
                try {
                    Thread.sleep(1000); // Simulate some work
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        // Pause to give virtual threads time to start
        Thread.sleep(5000);

        // Get the ThreadMXBean instance
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

        // Get all thread IDs
        long[] threadIds = threadMXBean.getAllThreadIds();

        // Get information about all threads
        ThreadInfo[] threadInfos = threadMXBean.getThreadInfo(threadIds);

        for (ThreadInfo threadInfo : threadInfos) {
            if (threadInfo != null) {
                Thread thread = findThreadById(threadInfo.getThreadId());

                if (thread != null && thread.isVirtual()) {
                    System.out.println("Virtual Thread: " + thread.getName() + " (ID: " + thread.getId() + ")");
                }
            }
        }
    }

    // Method to find thread by its ID
    private static Thread findThreadById(long threadId) {
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getId() == threadId) {
                return thread;
            }
        }
        return null;
    }
}