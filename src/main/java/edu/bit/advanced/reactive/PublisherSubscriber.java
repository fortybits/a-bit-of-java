package edu.bit.advanced.reactive;


import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class PublisherSubscriber {

    public static void offer() throws InterruptedException {
        //Create Publisher for expected items Strings
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
        //Register Subscriber
        publisher.subscribe(new TestSubscriber<>());
        publisher.subscribe(new TestSubscriber<>());
        publisher.subscribe(new TestSubscriber<>());
        publisher.offer("item", (subscriber, value) -> false);
        Thread.sleep(500);
    }

    public void testReactiveStreams() {
        //Create Publisher
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();

        //Register Subscriber
        TestSubscriber<Integer> subscriber = new TestSubscriber<>();
        publisher.subscribe(subscriber);

        assert (publisher.hasSubscribers());

        //Publish items
        System.out.println("Publishing Items...");

        List.of(1, 2, 3, 4, 5).forEach(i -> {
            publisher.submit(i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // blah
            }
        });
        assert (5 == subscriber.getLastItem());

        publisher.close();
    }

    public static class TestSubscriber<Integer> implements Flow.Subscriber<Integer> {

        private int lastItem;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            System.out.println("Subscribed");
            lastItem = 0;
        }

        @Override
        public void onNext(Integer item) {

            System.out.println("Received : " + item);
            lastItem += 1; // expect increment by 1
    //        assertTrue(lastItem == item);
        }

        @Override
        public void onError(Throwable throwable) {
            // nothing for the moment
        }

        @Override
        public void onComplete() {
            System.out.println("Completed");
        }

        public int getLastItem() {
            return lastItem;
        }
    }
}