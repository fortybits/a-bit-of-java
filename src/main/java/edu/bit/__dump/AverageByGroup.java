package edu.bit.__dump;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AverageByGroup {
    public static void calculateAverageValuePerGroup() {
        record Item(String groupName, Double value) {
        }
        List<Item> items = Arrays.asList(
                new Item("A", 1.0),
                new Item("A", 1.0),
                new Item("B", 1.0)
        );
        double result = items.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(Item::groupName, Collectors.summingDouble(Item::value)),
                        map -> map.values().stream().mapToDouble(Double::doubleValue).sum() / map.size()));
        System.out.println(result);

        double res = items.stream()
                .collect(Collectors.groupingBy(Item::groupName, Collectors.averagingDouble(Item::value)))
                .values().stream().mapToDouble(v -> v).average().orElse(Double.NaN);
        System.out.println(res);

        long distinct = items.stream().map(Item::groupName).distinct().count();
        double sums = items.stream().mapToDouble(Item::value).sum();
        System.out.println(sums / distinct);

    }


}