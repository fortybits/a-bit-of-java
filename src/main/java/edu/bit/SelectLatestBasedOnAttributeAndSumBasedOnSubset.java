package edu.bit;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

/**
 * Problem statement detailed under
 * https://stackoverflow.com/questions/65737983/java-8-stream-collectingandthen-get-latest-record-to-sum
 */
public class SelectLatestBasedOnAttributeAndSumBasedOnSubset {

    record Foo(Long one, String two, String three, int value, int version) {
    }

    record Result(Long one, String two, int totalValue) {
    }

    List<Result> groupByAndSumToConstructBarResults(List<Foo> fooList) {
        Map<List<Object>, Foo> groupedMaxVersion = fooList.stream()
                .collect(Collectors.toMap(foo -> Arrays.asList(foo.one(), foo.two(), foo.three()),
                        foo -> foo, BinaryOperator.maxBy(Comparator.comparing(Foo::version))));
        Map<List<Object>, Integer> resultMapping = groupedMaxVersion.entrySet().stream()
                .collect(Collectors.groupingBy(e -> Arrays.asList(e.getKey().get(0), e.getKey().get(1)),
                        Collectors.summingInt(e -> e.getValue().value())));
        return resultMapping.entrySet().stream()
                .map(e -> new Result((Long) e.getKey().get(0), (String) e.getKey().get(1), e.getValue()))
                .collect(Collectors.toList());
    }

}
