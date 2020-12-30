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
}