package edu.bit;

import java.util.Collection;

public class MapReferenceIntegerToPrimitiveLongInStream {

    public void sumNumbers(Collection<Integer> numbers) {
        long result1 = numbers.stream()
                .mapToLong(val -> val)
                .sum();
//        0: aload_1
//        1: invokeinterface #7,  1            // InterfaceMethod java/util/List.stream:()Ljava/util/stream/Stream;
//        6: invokedynamic #13,  0             // InvokeDynamic #0:applyAsLong:()Ljava/util/function/ToLongFunction;
//        11: invokeinterface #17,  2           // InterfaceMethod Stream.mapToLong:(Ljava/util/function/ToLongFunction;)Ljava/util/stream/LongStream;
//        16: invokeinterface #23,  1           // InterfaceMethod java/util/stream/LongStream.sum:()J
//        21: lstore_2
        System.out.println(result1);

        long result2 = numbers.stream()
                .mapToLong(Integer::longValue)
                .sum();
//        0: aload_1
//        1: invokeinterface #7,  1            // InterfaceMethod java/util/List.stream:()Ljava/util/stream/Stream;
//        6: invokedynamic #41,  0             // InvokeDynamic #1:applyAsLong:()Ljava/util/function/ToLongFunction;
//        11: invokeinterface #17,  2           // InterfaceMethod java/util/stream/Stream.mapToLong:(Ljava/util/function/ToLongFunction;)Ljava/util/stream/LongStream;
//        16: invokeinterface #23,  1           // InterfaceMethod java/util/stream/LongStream.sum:()J
//        21: lstore_2
        System.out.println(result2);
    }

//       0: aload_1
//       1: invokevirtual #42                 // Method java/lang/Integer.intValue:()I
//       4: i2l
//       5: lreturn
    long convertMe(Integer integer) {
        return integer;
    }

//       0: aload_1
//       1: invokevirtual #48                 // Method java/lang/Integer.longValue:()J
//       4: lreturn
    long convertMeToo(Integer integer) {
        return integer.longValue();
    }
}