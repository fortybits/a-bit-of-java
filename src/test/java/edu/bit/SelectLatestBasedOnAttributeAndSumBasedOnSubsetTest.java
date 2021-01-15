package edu.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class SelectLatestBasedOnAttributeAndSumBasedOnSubsetTest {

    @Test
    void testGroupByAndSumToConstructBarResults() {
        SelectLatestBasedOnAttributeAndSumBasedOnSubset slb = new SelectLatestBasedOnAttributeAndSumBasedOnSubset();
        List<SelectLatestBasedOnAttributeAndSumBasedOnSubset.Foo> fooList = List.of(
                new SelectLatestBasedOnAttributeAndSumBasedOnSubset.Foo(1L, "bbb", "cccc", 111, 0),
                new SelectLatestBasedOnAttributeAndSumBasedOnSubset.Foo(1L, "bbb", "cccc", 234, 1), // latest
                new SelectLatestBasedOnAttributeAndSumBasedOnSubset.Foo(1L, "bbb", "dddd", 111, 0),
                new SelectLatestBasedOnAttributeAndSumBasedOnSubset.Foo(1L, "bbb", "dddd", 112, 1),
                new SelectLatestBasedOnAttributeAndSumBasedOnSubset.Foo(1L, "bbb", "dddd", 113, 2),
                new SelectLatestBasedOnAttributeAndSumBasedOnSubset.Foo(1L, "bbb", "dddd", 114, 3), // latest
                new SelectLatestBasedOnAttributeAndSumBasedOnSubset.Foo(1L, "xxx", "cccc", 111, 0), // latest
                new SelectLatestBasedOnAttributeAndSumBasedOnSubset.Foo(2L, "xxx", "yyyy", 0, 0),
                new SelectLatestBasedOnAttributeAndSumBasedOnSubset.Foo(2L, "xxx", "yyyy", 1, 1)
        );

        Set<SelectLatestBasedOnAttributeAndSumBasedOnSubset.Result> barResults = Set.of(
                new SelectLatestBasedOnAttributeAndSumBasedOnSubset.Result(1L, "bbb", 348),
                new SelectLatestBasedOnAttributeAndSumBasedOnSubset.Result(1L, "xxx", 111),
                new SelectLatestBasedOnAttributeAndSumBasedOnSubset.Result(2L, "xxx", 1)
        );
        Assertions.assertEquals(barResults, new HashSet<>(slb.groupByAndSumToConstructBarResults(fooList)));
    }

}