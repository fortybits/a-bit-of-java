package edu.bit.advanced;

import lombok.SneakyThrows;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Concurrency {

    public <T> CompletableFuture<List<T>> convertListOfFutureToSingleFutureWithList(List<CompletableFuture<T>> com) {
        return CompletableFuture.allOf(com.toArray(new CompletableFuture[0]))
                .thenApply(v -> com.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList())
                );
    }

    public void countDownLatchImplementation() throws InterruptedException {
        // Let us create task that is going to wait for four threads before it starts
        CountDownLatch latch = new CountDownLatch(4);

        // Let us create four workers now threads and start them.
        Worker first = new Worker(1000, latch, "WORKER-1");
        Worker second = new Worker(2000, latch, "WORKER-2");
        Worker third = new Worker(3000, latch, "WORKER-3");
        Worker fourth = new Worker(4000, latch, "WORKER-4");
        first.start();
        second.start();
        third.start();
        fourth.start();

        // The convertNumbersToName task waits for four threads
        latch.await();

        // Main thread has started
        System.out.println(Thread.currentThread().getName() + " has finished");
    }

    public void joinCompletableFutures() {
        List<Integer> sleepTimes = Arrays.asList(1, 2, 3, 4, 5, 6);
        System.out.println("WITH SEPARATE STREAMS FOR FUTURE AND JOIN");
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        long start = System.currentTimeMillis();
        List<CompletableFuture<Integer>> futures = sleepTimes.stream()
                .map(sleepTime -> CompletableFuture.supplyAsync(() -> sleepTask(sleepTime), executorService)
                        .exceptionally(ex -> -1))
                .collect(Collectors.toList());
        executorService.shutdown();
        List<Integer> result = futures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        long finish = System.currentTimeMillis();
        System.out.printf("done in %d milliseconds.%n", finish - start);
        System.out.println(result);

        System.out.println("WITH SAME STREAM FOR FUTURE AND JOIN");
        ExecutorService executorService2 = Executors.newFixedThreadPool(6);
        start = System.currentTimeMillis();
        List<Integer> results = sleepTimes.stream()
                .map(sleepTime -> CompletableFuture.supplyAsync(() -> sleepTask(sleepTime), executorService2)
                        .exceptionally(ex -> -1))
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        executorService2.shutdown();
        finish = System.currentTimeMillis();
        System.out.printf("done in %d milliseconds.%n", finish - start);
        System.out.println(results);
    }

    private Integer sleepTask(int number) {
        System.out.printf("Task with sleep time %d%n", number);
        try {
            TimeUnit.SECONDS.sleep(number);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return -1;
        }
        return number;
    }

    // https://stackoverflow.com/questions/58791345/
    void shutdownExecServiceForAnyTaskFailure() throws InterruptedException {
        Runnable task1 = () -> {
        };
        Runnable task2 = () -> {
        };

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        ScheduledFuture<?> task1schedule =
                scheduledExecutorService.scheduleAtFixedRate(task1, 1, 60, TimeUnit.SECONDS);
        ScheduledFuture<?> task2schedule =
                scheduledExecutorService.scheduleAtFixedRate(task2, 1, 60, TimeUnit.SECONDS);
        scheduledExecutorService.awaitTermination(1, TimeUnit.MINUTES);
        if (task1schedule.isCancelled() || task2schedule.isCancelled())
            scheduledExecutorService.shutdown();
    }

    public void thenAcceptInsteadOfThenApply() {
        CompletableFuture.supplyAsync(() -> null).thenApply(result -> {
            System.out.println("SMS sended " + result);
            return null;
        });
    }


    public void ruAsyncVersusForEachExecution(String[] args) {
        List<Runnable> tasks = new ArrayList<>();

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        tasks.forEach(executorService::execute); // Each task sends an email to an user
        executorService.shutdown(); // Reclaim all the resources

        // After some research I've found a new way, using Java 8 CompletableFuture.runAsync(...) method.
        ExecutorService executor = Executors.newSingleThreadExecutor();
        tasks.forEach(task -> CompletableFuture.runAsync(task, executor));
        executor.shutdown(); // Reclaim all resources
    }

    // A class to represent threads for which the convertNumbersToName thread waits.
    public class Worker extends Thread {
        private int delay;
        private CountDownLatch latch;

        public Worker(int delay, CountDownLatch latch, String name) {
            super(name);
            this.delay = delay;
            this.latch = latch;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(delay);
                latch.countDown();
                System.out.println(Thread.currentThread().getName()
                        + " finished");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static class PhaserSample implements Runnable {

        private Phaser phaser;

        private PhaserSample(Phaser phaser) {
            this.phaser = phaser;
            this.phaser.register();  // Question
        }

        public static void main(String[] args) {
            Phaser phaser = new Phaser();
            PhaserSample task = new PhaserSample(phaser);
            Thread t1 = new Thread(task, "t1");
            Thread t2 = new Thread(task, "t2");
            Thread t3 = new Thread(task, "t3");
            t1.start();
            t2.start();
            t3.start();
        }

        @SneakyThrows
        @Override
        public void run() {
            //         this.phaser.register();  // Question
            phaserDetails("After register");
            for (int i = 0; i < 2; i++) {
                TimeUnit.SECONDS.sleep(2);
                phaserDetails("Before await" + i + ":");
                this.phaser.arriveAndAwaitAdvance();
                phaserDetails("After advance" + i + ":");
            }
        }

        private void phaserDetails(String msg) {
            System.out.printf("%s: %s, registered=%s, arrived=%s, unarrived=%s, phase=%s.", msg,
                    Thread.currentThread().getName(), this.phaser.getRegisteredParties(),
                    this.phaser.getArrivedParties(), this.phaser.getUnarrivedParties(), this.phaser.getPhase());
        }
    }

    public static class Semaphores {

        public static void main(String[] args) {
            givenLoginQueueWhenReachLimitThenBlocked();
        }

        private static void givenLoginQueueWhenReachLimitThenBlocked() {
            int slots = 10;
            ExecutorService executorService = Executors.newFixedThreadPool(slots);
            LoginQueueUsingSemaphore loginQueue = new LoginQueueUsingSemaphore(slots);
            IntStream.range(0, slots).forEach(user -> executorService.execute(loginQueue::tryLogin));
            //        executorService.shutdown();
            assert 0 == loginQueue.availableSlots();
            assert !loginQueue.tryLogin();
        }


        static class LoginQueueUsingSemaphore {

            private Semaphore semaphore;

            public LoginQueueUsingSemaphore(int slotLimit) {
                semaphore = new Semaphore(slotLimit);
            }

            boolean tryLogin() {
                return semaphore.tryAcquire();
            }

            void logout() {
                semaphore.release();
            }

            int availableSlots() {
                return semaphore.availablePermits();
            }

        }
    }

    public static class ExecutorServiceScheduling {

        public static void main(String[] args) {
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
            scheduledExecutorService.scheduleAtFixedRate(keepReading(), 1, 5, TimeUnit.SECONDS);
            scheduledExecutorService.scheduleAtFixedRate(keepWriting(), 1, 5, TimeUnit.SECONDS);
            new BeeperControl().beepForAnHour();
        }

        private static Runnable keepReading() {
            return () -> {
                Thread thread = Thread.currentThread();
                System.out.println("Hi! Printer here." + " :: " +
                        thread.getName() + " == " + thread.getId() + " :: " +
                        System.currentTimeMillis());
            };
        }

        private static Runnable keepWriting() {
            return () -> {
                Thread thread = Thread.currentThread();
                System.out.println("Hi! Writer here." + " :: " +
                        thread.getName() + " == " + thread.getId() + " :: " +
                        System.currentTimeMillis());
            };
        }

        // to beep every ten seconds for two minutes
        static class BeeperControl {
            private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

            public void beepForAnHour() {
                Runnable beeper = () -> System.out.println("beep");
                ScheduledFuture<?> beeperHandle =
                        scheduler.scheduleAtFixedRate(beeper, 10, 10, TimeUnit.SECONDS);
                Runnable canceller = () -> beeperHandle.cancel(false);
                scheduler.schedule(canceller, 2, TimeUnit.MINUTES);
            }
        }
    }

    public static class FlowSample {

        public record News(String headline, LocalDate date) {
            public static News create(String headline) {
                return new News(headline, LocalDate.now());
            }
        }

        public static void main(String[] args) {
            try (SubmissionPublisher<News> newsPublisher = new SubmissionPublisher()) {

                NewsSubscriber newsSubscriber = new NewsSubscriber();
                newsPublisher.subscribe(newsSubscriber);

                List.of(News.create("Important news"), News.create("Some other news"),
                        News.create("And news, news, news")).forEach(newsPublisher::submit);

                while (newsPublisher.hasSubscribers()) {
                    // wait
                }
                System.out.println("no more news subscribers left, closing publisher..");
            }
        }

        static class NewsSubscriber implements Flow.Subscriber<News> {

            private static final int MAX_NEWS = 3;
            private Flow.Subscription subscription;
            private int newsReceived = 0;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                System.out.printf("new subscription %s\n", subscription);
                this.subscription = subscription;
                subscription.request(1);
            }

            @Override
            public void onNext(News item) {
                System.out.printf("news received: %s (%s)\n", item.headline(), item.date());
                newsReceived++;
                if (newsReceived >= MAX_NEWS) {
                    System.out.printf("%d news received (max: %d), cancelling subscription.%n", newsReceived, MAX_NEWS);
                    subscription.cancel();
                    return;
                }
                subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.printf("error occurred fetching news: %s\n", throwable.getMessage());
                throwable.printStackTrace(System.err);
            }

            @Override
            public void onComplete() {
                System.out.println("fetching news completed");
            }
        }
    }
}