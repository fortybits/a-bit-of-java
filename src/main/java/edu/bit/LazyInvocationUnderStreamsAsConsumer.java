package edu.bit;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LazyMethodInvocationForStream {

    record A(int id) {
        A {
            System.out.printf("Constructing with %d!\n", id);
        }
    }

    void terminateTheStreamCreatedBySupplier() {
        Supplier<List<A>> supplierOfA = () -> getAList();
        List<Integer> collectIntegers = supplierOfA.get().stream()
                .map(A::id)
                .collect(Collectors.toList());
        List<A> limitedA = supplierOfA.get().stream()
                .limit(1)
                .collect(Collectors.toList());
        // just to consume
        System.out.println(collectIntegers);
        System.out.println(limitedA);
    }

    void intermediateStreamCreatedBySupplier() {
        Supplier<List<A>> supplierOfA = () -> getAList();
        Stream.of(supplierOfA).map(Supplier::get).flatMap(Collection::stream);
        Stream.generate(this::getAList).limit(1).flatMap(Collection::stream);
    }

    List<A> getAList() {
        return List.of(new A(0), new A(1), new A(2));
    }
}
