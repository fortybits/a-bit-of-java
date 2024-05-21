package edu.bit;

import java.util.concurrent.CancellationException;
import java.util.concurrent.StructuredTaskScope;
import java.util.logging.Logger;

public class StructuredVsCoroutines {


    private static final Logger logger = Logger.getLogger(StructuredVsCoroutines.class.getName());

    public static void main(String[] args) {
        new StructuredVsCoroutines().forgettingTheBirthdayRoutine();
    }

    public void forgettingTheBirthdayRoutine() {
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // Forking the workingConsciousness task
            StructuredTaskScope.Subtask<Void> workingJob = scope.fork(() -> {
                try {
                    workingConsciousness();
                } catch (InterruptedException e) {
                    // Task was interrupted
                    logger.info("Working consciousness was interrupted");
                    Thread.currentThread().interrupt();
                }
                return null;
            });

            // Forking the task that will cancel the workingJob after a delay
            StructuredTaskScope.Subtask<Object> anotherSubTask = scope.fork(() -> {
                try {
                    Thread.sleep(2000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                scope.shutdown();
                try {
                    workingJob.get(); // Wait for the task to complete or be cancelled
                } catch (CancellationException e) {
                    // Expected exception on cancellation
                    logger.info("Task was cancelled as expected");
                }
                logger.info("I forgot the birthday! Let's go to the mall!");
                return null;
            });

            // Wait for all tasks to complete or be cancelled
            scope.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.info("Scope was interrupted");
        }
    }

    private void workingConsciousness() throws InterruptedException {
        // Simulate working task
        logger.info("Inside working consciousness!");
        Thread.sleep(2100L); // Simulating long running task
        logger.info("Working consciousness completed");
    }
}