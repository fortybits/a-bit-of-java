package edu.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

class StreamUtilityTest {

    @Test
    void optimisingUsingPredicates() {
        StreamsUtility streamUtility = new StreamsUtility();
        List<Integer> values = Arrays.asList(1, 2, 3, 4, 5);
        Assertions.assertEquals(streamUtility.totalValues(values), streamUtility.totalValues(values, e -> true));
        Assertions.assertEquals(streamUtility.totalEvenValues(values), streamUtility.totalValues(values, e -> e % 2 == 0));
        Assertions.assertEquals(streamUtility.totalOddValues(values), streamUtility.totalValues(values, e -> e % 2 != 0));
    }
}