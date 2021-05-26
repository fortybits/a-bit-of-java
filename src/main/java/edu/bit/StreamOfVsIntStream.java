package edu.bit;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * https://stackoverflow.com/questions/67669583/what-is-the-difference-between-stream-of-and-intstream-range
 */
public class StreamOfVsIntStream {

    public static void main(String[] args) {
        System.out.println("#1");
        Stream.of(0, 1, 2, 3)
                .peek(System.out::println)
                .sorted()
                .findFirst();

        System.out.println("#2"); // this is an optimisation
        IntStream.range(0, 4)
                .peek(System.out::println)
                .sorted()
                .findFirst();
        IntStream.range(0, 4)
                .peek(System.out::println)
                .min();
        IntStream.range(0, 4)
                .peek(System.out::println)
                .max(); // why not use the same optimisation here?

        System.out.println("#3");
        IntStream.iterate(0, i -> i < 4, i -> i + 1)
                .peek(System.out::println)
                .sorted()
                .findFirst();

        System.out.println("#4");
        IntStream.range(0, 4)
                .parallel()
                .peek(System.out::println)
                .sorted()
                .findFirst();
    }
}
