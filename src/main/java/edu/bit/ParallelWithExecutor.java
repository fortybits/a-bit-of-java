package edu.bit;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class ParallelWithExecutor {

    public static void main(String[] args) {
        final ExecutorService worker = Executors.newFixedThreadPool(8);
        List<String> tables = Stream.of("User", "Comments", "Tags", "Product", "Question", "Answer",
                        "Company", "Achievement", "Magic")
                .sorted(String::compareTo)
                .toList();
        System.out.println(tables);

        tables.stream()
                .parallel()
                .forEach(table -> worker.execute(new Work(table)));


        System.out.println("===========================================");

        tables.stream()
                .forEach(table -> worker.execute(new Work(table)));
    }


    static class Work implements Runnable {

        final String table;

        public Work(String table) {
            this.table = table;
        }

        @Override
        public void run() {
            String information = table + ":: Using -> " + Thread.currentThread().getName();
            System.out.println("Started Processing | " + information);
            try {
                boolean nextBoolean = new Random().nextBoolean();
                if (nextBoolean) {
                    System.out.println("Sleeping for 5s inside - " + information);
                    Thread.sleep(5000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Compltd Processing | " + information);
        }
    }
}