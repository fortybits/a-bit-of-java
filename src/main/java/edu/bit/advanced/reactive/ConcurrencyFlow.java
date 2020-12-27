package edu.bit.advanced.reactive;

import java.util.concurrent.*;
import java.util.function.Function;

public class ConcurrencyFlow {
    public static class OneShotPublisher implements Flow.Publisher<Boolean> {
        private final ExecutorService executor = ForkJoinPool.commonPool(); // daemon-based
        private boolean subscribed; // true after first subscribe

        public synchronized void subscribe(Flow.Subscriber<? super Boolean> subscriber) {
            if (subscribed)
                subscriber.onError(new IllegalStateException()); // only one allowed
            else {
                subscribed = true;
                subscriber.onSubscribe(new OneShotSubscription(subscriber, executor));
            }
        }
    }

    public static class OneShotSubscription implements Flow.Subscription {
        private final Flow.Subscriber<? super Boolean> subscriber;
        private final ExecutorService executor;
        private Future<?> future; // to allow cancellation
        private boolean completed;

        OneShotSubscription(Flow.Subscriber<? super Boolean> subscriber, ExecutorService executor) {
            this.subscriber = subscriber;
            this.executor = executor;
        }

        public synchronized void request(long n) {
            if (n != 0 && !completed) {
                completed = true;
                if (n < 0) {
                    IllegalArgumentException ex = new IllegalArgumentException();
                    executor.execute(() -> subscriber.onError(ex));
                } else {
                    future = executor.submit(() -> {
                        subscriber.onNext(Boolean.TRUE);
                        subscriber.onComplete();
                    });
                }
            }
        }

        public synchronized void cancel() {
            completed = true;
            if (future != null) future.cancel(false);
        }
    }

    public static class OneShotProcessor implements Flow.Publisher<Boolean> {
        private final ExecutorService executor = ForkJoinPool.commonPool(); // daemon-based
        private boolean subscribed; // true after first subscribe

        public synchronized void subscribe(Flow.Subscriber<? super Boolean> subscriber) {
            if (subscribed)
                subscriber.onError(new IllegalStateException()); // only one allowed
            else {
                subscribed = true;
                subscriber.onSubscribe(new OneShotSubscription(subscriber, executor));
            }
        }
    }

    public static class StringToIntegerProcessor<T, R> extends SubmissionPublisher<R> implements Flow.Processor<T, R> {

        private Function<? super T, ? extends R> function;
        private Flow.Subscription subscription;

        StringToIntegerProcessor(Function<? super T, ? extends R> function) {
            super();
            this.function = function;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(1);
        }

        @Override
        public void onNext(T item) {
            submit(function.apply(item));
            subscription.request(1);
        }

        @Override
        public void onError(Throwable t) {
            t.printStackTrace();
        }

        @Override
        public void onComplete() {
            close();
        }
    }
}