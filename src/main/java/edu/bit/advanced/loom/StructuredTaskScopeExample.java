package edu.bit.advanced.loom;

import java.time.Duration;
import java.time.Instant;
import java.util.Random;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask;
import java.util.function.Supplier;

public class StructuredTaskScopeExample {
    public static void main(String[] args) {
        Instant start = Instant.now();
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<String>()) {
            Subtask<String> task1 = scope.fork(Weather::getTempFromA);
            Subtask<String> task2 = scope.fork(Weather::getTempFromB);
            Subtask<String> task3 = scope.fork(Weather::getTempFromC);
            scope.join();
            System.out.println(STR. """
                    task1: \{ task1.state() }: result : \{ task1.state() == Subtask.State.SUCCESS ? task1.get() : "Not Available" }
                    task2: \{ task2.state() }: result : \{ task2.state() == Subtask.State.SUCCESS ? task2.get() : "Not Available" }
                    task3: \{ task3.state() }: result : \{ task3.state() == Subtask.State.SUCCESS ? task3.get() : "Not Available" }
                    """ );

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        System.out.println(STR. "AnySuccess : \{ Duration.between(start, Instant.now()).toMillis() }ms" );
        System.out.println("==================");
        Instant start1 = Instant.now();
        try (var scope1 = new StructuredTaskScope.ShutdownOnFailure()) {
            Subtask<String> task1 = scope1.fork(Weather::getTempFromA);
            Subtask<String> task2 = scope1.fork(Weather::getTempFromB);
            Subtask<String> task3 = scope1.fork(Weather::getTempFromC);
            scope1.join();
            scope1.throwIfFailed(RuntimeException::new);
            System.out.println(STR. """
                    task1: \{ task1.state() }: result: \{ task1.get() }
                    task2: \{ task2.state() }: result: \{ task2.get() }
                    task3: \{ task3.state() }: result: \{ task3.get() }
                    """ );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(STR. "AllSuccess : \{ Duration.between(start1, Instant.now()).toMillis() }ms" );
    }

    static class Weather {
        static Random random = new Random();

        public static String getTempFromA() {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return STR. "Temp from A: Temp = \{ random.nextInt(0, 100) }" ;
        }

        public static String getTempFromB() {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return STR. "Temp from B: Temp = \{ random.nextInt(0, 100) }" ;
        }

        public static String getTempFromC() {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return STR. "Temp from C: Temp = \{ random.nextInt(0, 100) }" ;
        }


    }
}