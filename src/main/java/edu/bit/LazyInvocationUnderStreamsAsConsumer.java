package edu.bit;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LazyInvocationUnderStreamsAsConsumer {

    static final record A(int id) {
        A {
            System.out.printf("Constructing with %d!\n", id);
        }
    }


    void intermediateStreamCreatedBySupplier() {
        Supplier<List<A>> supplierOfA = this::getAList;
        Stream.of(supplierOfA).map(Supplier::get).flatMap(Collection::stream);
        Stream.generate(supplierOfA).limit(1).flatMap(Collection::stream);
    }

    void terminateTheStreamCreatedBySupplier() {
        Supplier<List<A>> supplierOfA = this::getAList;
        long countAs = Stream.of(supplierOfA).map(Supplier::get)
                .flatMap(Collection::stream)
                .filter(s -> s.id() == 0)
                .count();
        List<A> limitedA = Stream.generate(supplierOfA)
                .limit(1)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        // just to consume
        System.out.println(countAs);
        System.out.println(limitedA);
    }

    void incorrectIntermediateStreamCreatedBySupplier() {
        Supplier<List<A>> supplierOfA = this::getAList;
        supplierOfA.get().stream().map(A::id);
    }

    List<A> getAList() {
        return List.of(new A(0), new A(1), new A(2));
    }
}
