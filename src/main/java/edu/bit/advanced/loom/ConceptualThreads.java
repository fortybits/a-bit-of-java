package edu.bit.advanced.loom;

import java.time.Instant;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

public class ConceptualThreads {

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

    public static void main(String[] args) {
        ElePosRepo elePosRepo = null;
        FaultRepo faultRepo = null;
        ArdInfRepo ardInfRepo = null;

        Integer unitId = 1;
        try (var scope = new CustomTaskScope()) {
            StructuredTaskScope.Subtask<Optional<Boolean>> task1 = scope.fork(() -> elePosRepo.findInspectionByUnitId(unitId));
            scope.fork(() -> elePosRepo.findEventOfFireByUnitId(unitId));
            scope.fork(() -> faultRepo.findGeneralErrorByUnitId(unitId));
            scope.fork(() -> ardInfRepo.findArdModeByUnitId(unitId));
            scope.fork(() -> ardInfRepo.findArdPowerStatusByUnitId(unitId));
            // Once a task return true, close all remaining tasks,and return the  signal:"true" to main task;
            scope.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    interface ArdInfRepo {

        Optional<Boolean> findArdPowerStatusByUnitId(Integer unitId);

        Optional<Boolean> findArdModeByUnitId(Integer unitId);
    }

    interface FaultRepo {
        Optional<Boolean> findGeneralErrorByUnitId(Integer unitId);
    }

    interface ElePosRepo {
        Optional<Boolean> findInspectionByUnitId(Integer unitId);

        Optional<Boolean> findEventOfFireByUnitId(Integer unitId);
    }


    static class CustomTaskScope extends StructuredTaskScope<Optional<Boolean>> {
        @Override
        protected void handleComplete(Subtask<? extends Optional<Boolean>> subtask) {
            super.handleComplete(subtask);
            if (subtask.state() == Subtask.State.SUCCESS &&
                    (subtask.get().isPresent() && Boolean.TRUE.equals(subtask.get().get()))) {
                super.shutdown();
            }
        }

        @Override
        public <U extends Optional<Boolean>> Subtask<U> fork(Callable<? extends U> task) {
            return super.fork(task);
        }

        @Override
        public StructuredTaskScope<Optional<Boolean>> join() throws InterruptedException {
            super.join();
            return this;
        }

        @Override
        public StructuredTaskScope<Optional<Boolean>> joinUntil(Instant deadline)
                throws InterruptedException, TimeoutException {
            super.joinUntil(deadline);
            return this;
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