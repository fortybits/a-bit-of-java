package edu.bit.advanced;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class FlowTeeExample {

    private static Flow.Subscriber<Integer> createSubscriber(String name) {
        return new Flow.Subscriber<>() {
            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println(name + " received: " + item);
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println(name + " error: " + throwable);
            }

            @Override
            public void onComplete() {
                System.out.println(name + " complete");
            }
        };
    }

    @Test
    void testFanningOutToSubscribers() throws InterruptedException {
        SubmissionPublisher<Integer> publisher = new SubmissionPublisher<>();
        TeeingProcessor<Integer> teeingProcessor = new TeeingProcessor<>();

        publisher.subscribe(teeingProcessor);

        // Create and add multiple subscribers
        List<Flow.Subscriber<Integer>> subscribers = List.of(createSubscriber("Subscriber 1"),
                createSubscriber("Subscriber 2"),
                createSubscriber("Subscriber N"));

        subscribers.forEach(teeingProcessor::subscribe);

        // Publish items
        publisher.submit(100);
        publisher.submit(200);
        publisher.submit(300);

        // Allow time for processing
        Thread.sleep(1000);

        publisher.close();
    }
}