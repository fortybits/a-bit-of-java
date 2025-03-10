package edu.bit.advanced;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Flow;

public class TeeingProcessor<T> implements Flow.Processor<T, T> {
    private final List<Flow.Subscriber<? super T>> subscribers = new CopyOnWriteArrayList<>();
    private Flow.Subscription subscription;

    @Override
    public void subscribe(Flow.Subscriber<? super T> subscriber) {
        subscribers.add(subscriber);
        subscriber.onSubscribe(new Flow.Subscription() {
            @Override
            public void request(long n) {
                // Request elements from the original subscription
                if (subscription != null) {
                    subscription.request(n);
                }
            }

            @Override
            public void cancel() {
                subscribers.remove(subscriber);
            }
        });
    }

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription;
        subscription.request(Long.MAX_VALUE); // Request all items upfront
    }

    @Override
    public void onNext(T item) {
        for (Flow.Subscriber<? super T> subscriber : subscribers) {
            subscriber.onNext(item);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        for (Flow.Subscriber<? super T> subscriber : subscribers) {
            subscriber.onError(throwable);
        }
    }

    @Override
    public void onComplete() {
        for (Flow.Subscriber<? super T> subscriber : subscribers) {
            subscriber.onComplete();
        }
    }
}