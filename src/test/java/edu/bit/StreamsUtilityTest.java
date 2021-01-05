package edu.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
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

    @Test
    void testSearchWordsInComments() {
        StreamsUtility streamsUtility = new StreamsUtility();
        streamsUtility.searchWordsInComments(Arrays.asList("Oranges", "Figs", "Mangoes", "Apples"),
                Arrays.asList("Apples are better than Oranges", "I love Mangoes and Oranges",
                        "I don't know like Figs. Mangoes are my favorites", "I love Mangoes and Apples"));

        streamsUtility.searchWordsInComments(Arrays.asList("Oranges", "Figs", "Mangoes", "Apples"),
                Arrays.asList("Apples are better than Oranges", "I love Mangoes and Oranges",
                        "I don't know like Figs. Mangoes are my favorites", "I love Mangoes and Apples"));
    }

    @Test
    void testSortingArrayWithEvenValuesIntact() {
        StreamsUtility streamsUtility = new StreamsUtility();
        Integer[] input = new Integer[]{3, 4, 5, 2, 1, 6, 9, 8, 7, 0};
        Integer[] res = new Integer[]{1, 4, 3, 2, 5, 6, 7, 8, 9, 0};
        Integer[] out = streamsUtility.sortArrayWithEvensIntact(input);
        Assertions.assertArrayEquals(out, res);
    }

    @Test
    void testCollectionToArrayVersusStreamToArray() {
        StreamsUtility streamsUtility = new StreamsUtility();
        streamsUtility.collectionToArrayVersusStreamToArray();
    }
}