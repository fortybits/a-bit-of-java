package edu.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

class StreamsUtilityTest {

    @Test
    void testFlatMappingTwoDimensionalArrays() {
        StreamsUtility streamsUtility = new StreamsUtility();
        String[][] strArray = {{"Sample1", "Sample2"}, {"Sample3", "Sample4", "Sample5"}};
        List<String> strings = streamsUtility.flatMapTwoDimensionalArray(strArray).collect(Collectors.toList());
        Assertions.assertEquals(List.of("Sample1", "Sample2", "Sample3", "Sample4", "Sample5"), strings);
        int[][] intArr = {{1, 2, 3}, {4, 5, 6}};
        List<Integer> integers = streamsUtility.flatMapTwoDimensionalArray(intArr).collect(Collectors.toList());
        Assertions.assertEquals(List.of(1, 2, 3, 4, 5, 6), integers);
    }

    @Test
    void testGroupingAndFilteringToCount() {
        List<StreamsUtility.Coordinate> myList = List.of(
                new StreamsUtility.Coordinate("4", "6"),
                new StreamsUtility.Coordinate("4", "4"),
                new StreamsUtility.Coordinate("4", "6"),
                new StreamsUtility.Coordinate("4", "7"),
                new StreamsUtility.Coordinate("4", "6"),
                new StreamsUtility.Coordinate("4", "8"),
                new StreamsUtility.Coordinate("4", "6"),
                new StreamsUtility.Coordinate("4", "0"),
                new StreamsUtility.Coordinate("4", "9"),
                new StreamsUtility.Coordinate("4", "1"),
                new StreamsUtility.Coordinate("6", "6"),
                new StreamsUtility.Coordinate("6", "0"),
                new StreamsUtility.Coordinate("6", "4"),
                new StreamsUtility.Coordinate("6", "6"),
                new StreamsUtility.Coordinate("6", "7"),
                new StreamsUtility.Coordinate("6", "6"),
                new StreamsUtility.Coordinate("7", "11"));
        StreamsUtility streamsUtility = new StreamsUtility();
        streamsUtility.groupingAndFilteringToCount(myList);
    }
}