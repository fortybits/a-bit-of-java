package edu.bit;

import java.util.List;
import java.util.stream.IntStream;

// https://stackoverflow.com/questions/67707196/remove-first-n-occurrence-of-an-element-from-a-list
public class RemoveFirstNOccurrenceOfAnElement {

    public void updateListRemovingNOccurrenceOfM(List<Integer> input, int element, int occurrence) {
        input.removeIf(i -> i == element); // removes all

        // works
        for (int i = 0; i < occurrence; i++) {
            input.remove(Integer.valueOf(element));
        }

        // better? the overall complexity of the solution can be reduced from O(n^2_ to O(n)
        int NthIndex = indexOf(input, element, occurrence);
        input.subList(0, NthIndex + 1).removeIf(x -> x == element);
    }

    int indexOf(List<Integer> myList, int M, int N) {
        return IntStream.range(0, myList.size())
                .filter(ix -> myList.get(ix) == M)
                .limit(N)
                .reduce((a, b) -> b)
                .orElse(-1);
    }
}
