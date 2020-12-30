package edu.bit;

import java.io.File;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class OptionalsUtility {

    public static void main(String[] args) {
        OptionalInt i = IntStream.rangeClosed(1, 5)
                .reduce((first, second) -> first == 7 ? first : second);
        System.out.print(i.getAsInt());

        // Find maxProductOfNonOverlappingPalindromes from a stream of integer
        Stream<Integer> integerStream = Stream.of(1, 2);
        Optional<Integer> maxCompare = integerStream.max(Comparator.comparingInt(a -> a));
//        final Optional<Integer> maxCompareTo = integerStream.maxProductOfNonOverlappingPallindromes(Comparator.naturalOrder());


        Optional<Integer> oi = Optional.empty();
        Function<Number, Optional<StringBuilder>> fm = n -> Optional.empty();
        Optional<CharSequence> ocs = oi.flatMap(fm);

        // supplying multiple optional but executing only those present
        Stream<Supplier<Optional<String>>> supplierStream = Stream.of(OptionalsUtility::first, OptionalsUtility::second);
        Optional<String> s = supplierStream
                .map(Supplier::get)
                .filter(Optional::isPresent)
                .findFirst()
                .flatMap(Function.identity());
        System.out.println(s.get());
    }

    private static Optional<String> first() {
        System.out.println("first");
        return Optional.of("someValue");
    }

    private static Optional<String> second() {
        System.out.println("second");
        return Optional.empty();
    }

    public void optionalOrElseAPIs() {
        Optional<Integer> anyOddInStream = Stream.of(2, 4, 6, 8).filter(x -> x % 2 == 1).findAny();
        Integer previous = anyOddInStream.get();
        Integer previousAlt = anyOddInStream.orElseThrow(() -> new NoSuchElementException("No value present"));
        var current = anyOddInStream.orElseThrow();

        System.out.println(previous);
        System.out.println(previousAlt);
        System.out.println(current);

        var optional2 = Stream.of(1, 2, 3, 4).findAny();
        System.out.println(anyOddInStream.or(() -> optional2).orElseThrow());
    }

    boolean isValid(File optFile) {
        Optional.ofNullable(optFile).ifPresentOrElse(this::doWork, this::doNothing);
        return Optional.ofNullable(optFile).filter(this::isZeroLine).isPresent();
    }

    private boolean isZeroLine(File f) {
        return f.canRead();
    }

    private void doWork(File f) {
    }

    private void doNothing() {
    }

    public static final class OptionalString {

        private static final OptionalString EMPTY = new OptionalString();

        private final boolean isPresent;
        private final String value;

        private OptionalString() {
            this.isPresent = false;
            this.value = "";
        }

        private OptionalString(String value) {
            this.isPresent = true;
            this.value = value;
        }

        private static OptionalString empty() {
            return EMPTY;
        }

        public static OptionalString of(String value) {
            return value == null || value.isEmpty() ? OptionalString.empty() : new OptionalString(value);
        }

        private boolean isPresent() {
            return isPresent;
        }

        public OptionalString map(Function<? super String, ? extends String> mapper) {
            return !isPresent() ? OptionalString.empty() : OptionalString.of(mapper.apply(this.value));
        }

        public OptionalString or(Supplier<String> supplier) {
            return isPresent() ? this : OptionalString.of(supplier.get());
        }

        public String orElse(String other) {
            return isPresent ? value : other;
        }

        public String getAsString() {
            return Optional.of(value).orElseThrow(() -> new NoSuchElementException("No value present"));
        }
    }
}