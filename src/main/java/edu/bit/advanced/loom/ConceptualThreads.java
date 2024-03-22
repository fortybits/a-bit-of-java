package edu.bit.advanced.loom;

import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class ConceptualThreads {

    public static void main(String[] args) {

    }

    class RequestScope<T> extends StructuredTaskScope<T> implements SomeFrameworkCallbackApi {
        private final Queue<T> subtasks = new LinkedTransferQueue<>();

        @Override
        protected void handleComplete(Subtask<? extends T> subtask) {

        }

        @Override
        public RequestScope<T> join() throws InterruptedException {
            super.join();
            return this;
        }

        public Stream<T> completedSuccessfully() {
            super.ensureOwnerAndJoined();
            return subtasks.stream();
        }

        @Override
        public void beforeRequest(RequestHeaders req, ConfigInfo info) {

        }

        @Override
        public void afterRequest() {

        }
    }

    // custom implementation of Structure Task Scope
    class ShutdownOnPrimaryFailure<T> extends StructuredTaskScope<T> {
        private final AtomicReference<Throwable> failure = new AtomicReference<>();
        private Subtask<?> primary;

        public <U extends T> Subtask<U> forkPrimary(Callable<? extends U> task) {
            ensureOwnerAndJoined();
            Subtask<U> forked = super.fork(task);
            primary = forked;
            return forked;
        }

        @Override
        protected void handleComplete(Subtask<? extends T> subtask) {
            super.handleComplete(subtask);
            if (subtask.state() == Subtask.State.FAILED) {
                if (subtask == primary) {
                    failure.set(subtask.exception());
                    shutdown();
                } else failure.compareAndSet(null, subtask.exception());
            }
        }

        @Override
        public ShutdownOnPrimaryFailure<T> join() throws InterruptedException {
            super.join();
            primary = null;
            return this;
        }

        @Override
        public ShutdownOnPrimaryFailure<T> joinUntil(Instant deadline)
                throws InterruptedException, TimeoutException {

            super.joinUntil(deadline);
            primary = null;
            return this;
        }

        public void throwIfFailed() throws ExecutionException {
            ensureOwnerAndJoined();
            Throwable t = failure.get();
            if (t != null) throw new ExecutionException(t);
        }
    }
}
