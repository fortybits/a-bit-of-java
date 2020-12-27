package edu.bit.advanced.reactive;

import java.util.Arrays;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.function.Function;

/**
 * Oe of the application for the publisher and subscriber pattern as detailed at
 * https://community.oracle.com/docs/DOC-1006738 is the transformation of data streams.
 */
public class TransformDataStreamUsingProcessor {

    public static void main(String[] args) {
        //Create Publisher
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        //Create Processor and Subscriber
        MyFilterProcessor<String, String> filterProcessor = new MyFilterProcessor<>(s -> String.valueOf(s.equals("x")));

        MyTransformProcessor<String, Integer> transformProcessor = new MyTransformProcessor<>(Integer::parseInt);

        MySubscriber<Integer> subscriber = new MySubscriber<>();

        //Chain Processor and Subscriber
        publisher.subscribe(filterProcessor);
        filterProcessor.subscribe(transformProcessor);
        transformProcessor.subscribe(subscriber);

        System.out.println("Publishing Items...");
        String[] items = {"1", "x", "2", "x", "3", "x"};
        Arrays.stream(items).forEach(publisher::submit);
        publisher.close();
    }

    /**
     * The Processor
     * A component that acts as both a Subscriber and Publisher.
     * The processor sits between the Publisher and Subscriber, and transforms one stream to another.
     * There could be one or more processor chained together, and the result of the final processor in the chain, is processed by the Subscriber.
     *
     * @param <T> the subscribed item type
     * @param <R> the published item type
     */
    public static class MyTransformProcessor<T, R> extends SubmissionPublisher<R> implements Flow.Processor<T, R> {

        private final Function<? super T, ? extends R> function;
        private Flow.Subscription subscription;

        public MyTransformProcessor(Function<? super T, ? extends R> function) {
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

    public static class MyFilterProcessor<T, R> extends SubmissionPublisher<R> implements Flow.Processor<T, R> {

        private final Function<? super T, ? extends R> function;
        private Flow.Subscription subscription;

        MyFilterProcessor(Function<? super T, ? extends R> function) {
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