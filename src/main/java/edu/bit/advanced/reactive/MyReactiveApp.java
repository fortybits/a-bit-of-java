package edu.bit.advanced.reactive;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyReactiveApp {

    public static void main(String[] args) throws InterruptedException {

        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        CustomSubscriber subs = new CustomSubscriber();
        publisher.subscribe(subs);

        List<String> strs = getStrs();
        System.out.println("Publishing Items to Subscriber");
        strs.forEach(publisher::submit);

        /*while (strs.size() != subs.getCounter()) {
            Thread.sleep(10);
        }*/
        //publisher.close();
        System.out.println("Exiting the app");
    }

    private static List<String> getStrs() {
        return Stream.iterate(1, i -> i < 100, i -> i++)
                .map(i -> "name" + i)
                .limit(100)
                .collect(Collectors.toList());
    }

    public static class CustomSubscriber implements Flow.Subscriber<String> {

        private Flow.Subscription subscription;

        private int counter = 0;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            subscription.request(100);

        }

        @Override
        public void onNext(String item) {
            System.out.println(this.getClass().getSimpleName() + " item " + item);
            //subscription.request(1);
            counter++;

        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println(this.getClass().getName() + " an error occured " + throwable);

        }

        @Override
        public void onComplete() {
            System.out.println("activity completed");

        }

        public int getCounter() {
            return counter;
        }

    }
}
