package edu.bit;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FunctionUtility {

    public void compositeFunctions() {
        Function<Integer, Integer> add = x -> x + 2;
        Function<Integer, Integer> sub = x -> x - 2;
        Function<Integer, Integer> div = x -> x / 2;
        Function<Integer, Integer> mul = x -> x * 2;
        Function<Integer, Integer> function = add.andThen(sub).andThen(div).andThen(mul);
        System.out.println(function.apply(2));
    }

    public static void main(String[] args) {
        BinaryOperator<Integer> foo = (a, b) -> a * a + b * b;
        BiFunction<Integer, Integer, Integer> bar = (a, b) -> a * a + b * b;
        System.out.println(foo.apply(2, 3));
        System.out.println(bar.apply(2, 3));

        // biFunction on custom object
        BiFunction<String, Integer, XYZProfile> xyzProfileBiFunction = (string, integer) -> {
            XYZProfile xyzProfile = new XYZProfile(string, integer);
            // modify xyZProfile
            System.out.println(xyzProfile);
            return xyzProfile;
        };
        XYZProfile xyzProfile1 = xyzProfileBiFunction.apply("com/stackoverflow/nullpointer", 0);
        XYZProfile xyzProfile2 = xyzProfileBiFunction.apply("holger", 1);
        System.out.println(xyzProfile1 + "" + xyzProfile2);

        // function chain
        Function<String, Integer> x = Integer::valueOf;
        Function<Integer, Double> y = Integer::doubleValue;
        Function<Double, String> z = String::valueOf;
        String f = x.andThen(y).andThen(z).apply("1");

        // identity for unary operator t -> t
        UnaryOperator<Object> defaultParser2 = UnaryOperator.identity();

        // predicate definition
        Predicate<String> nonEmptyStringPredicate = s -> !s.isEmpty();
        System.out.println(nonEmptyStringPredicate.test("any"));


        Functional<String, Number> functional = (string) -> {
        };
        functional.method("com.stackoverflow.nullpointer.string", (string) -> 1);

        List<String> list = Arrays.asList("Abcd", "Abcd");
        Map<String, Integer> map = list.stream()
                .collect(Collectors.toMap(Function.identity(), String::length, binaryOperatorToChooseAny()));
        System.out.println(map.size());


        BiFunction<Integer, Integer, Integer> biFunction = Integer::compare;
        System.out.println(biFunction.apply(10, 60));
        BinaryOperator<Integer> binaryOperator = Integer::sum;
        System.out.println(binaryOperator.apply(10, 60));
        IntBinaryOperator intBinaryOperator = Integer::sum;
        System.out.println(intBinaryOperator.applyAsInt(10, 60));
    }

    public static <T, U, V> Consumer<T> bind2and3(ThreeConsumer<? super T, U, V> c, U arg2, V arg3) {
        return (arg1) -> c.accept(arg1, arg2, arg3);
    }

    static <T> boolean validateAllConditions(T object, Predicate<T>... predicates) {
        Predicate<T> init = t -> true;
        Arrays.stream(predicates).forEach(init::and);
        return init.test(object);
    }

    private static <T> BinaryOperator<T> binaryOperatorToChooseAny() {
        ThreadLocalRandom.current().nextBoolean(); // suggested by Holger
        //        return Math.random() < 0.5 ? ((x, y) -> x) : ((x, y) -> y);
        return (a, b) -> Objects.equals(a, b) ? a : b;
    }

    interface ThreeConsumer<T, U, V> {
        void accept(T t, U u, V v);
    }

    record XYZProfile(String name, Integer code) {
    }

    @FunctionalInterface
    public interface Functional<E, T extends Number> {

        void perform(E e);

        default void method(E e, T t) {
        }

        default void method(E e, Function<E, T> function) {
        }
    }

    public static class ComplexFunctions {

        private static <A, B, C> Function<A, C> compose(final Function<? super B, ? extends C> g, final Function<? super A, ? extends B> f) {
            return new FunctionComposition<A, B, C>(g, f);
        }

        private static <A, B, C, D> Function<A, D> compose(final Function<? super C, ? extends D> f1,
                                                           final Function<? super B, ? extends C> f2, final Function<? super A, ? extends B> f3) {
            return new FunctionComposition<>(f1, compose(f2, f3));
        }

        private static <A, B, C, D, E> Function<A, E> compose(final Function<? super D, ? extends E> f4,
                                                              final Function<? super C, ? extends D> f3, final Function<? super B, ? extends C> f2,
                                                              final Function<? super A, ? extends B> f1) {
            return compose(f4, compose(f3, f2, f1));
        }

        public static <A, B, C, D, E, F> Function<A, F> compose(final Function<? super E, ? extends F> f5,
                                                                final Function<? super D, ? extends E> f4, final Function<? super C, ? extends D> f3,
                                                                final Function<? super B, ? extends C> f2, final Function<? super A, ? extends B> f1) {
            return compose(f5, compose(f4, f3, f2, f1));
        }

        public static class FunctionComposition<A, B, C> implements Function<A, C>, Serializable {

            private static final long serialVersionUID = 1;

            private final Function<? super B, ? extends C> g;
            private final Function<? super A, ? extends B> f;

            FunctionComposition(Function<? super B, ? extends C> g, Function<? super A, ? extends B> f) {
                this.g = Objects.requireNonNull(g);
                this.f = Objects.requireNonNull(f);
            }

            @Override
            public C apply(A a) {
                return g.apply(f.apply(a));
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof FunctionComposition) {
                    FunctionComposition<?, ?, ?> that = (FunctionComposition<?, ?, ?>) obj;
                    return f.equals(that.f) && g.equals(that.g);
                }
                return false;
            }

            @Override
            public int hashCode() {
                return Objects.hash(getClass(), f, g);
            }

            @Override
            public String toString() {
                return g + "(" + f + ")";
            }
        }
    }


    // the problem statement of exposing an API to clients to call with chained consumers with guaranteed order
    // of processing as detailed in https://stackoverflow.com/questions/59883961
    public interface ConsumerChaining<T> {

        Consumer<T> start();

        Consumer<T> performDailyAggregates();

        Consumer<T> performLastNDaysAggregates();

        Consumer<T> repopulateScores();

        Consumer<T> updateDataStore();

        private void performAllTasks(T data) {
            start().andThen(performDailyAggregates())
                    .andThen(performLastNDaysAggregates())
                    .andThen(repopulateScores())
                    .andThen(updateDataStore())
                    .accept(data);
        }

        default Consumer<T> NOOP() {
            return t -> {
            };
        }

        private void performAllTasks(Stream<Consumer<T>> consumerList, T data) {
            consumerList.reduce(NOOP(), Consumer::andThen).accept(data);
        }

        private void performAllTasks(List<Consumer<T>> consumerList, T data) {
            consumerList.stream().reduce(NOOP(), Consumer::andThen).accept(data);
        }
    }

    // references for equivalent of System.out::print as discussed in
    // https://stackoverflow.com/questions/58920215/why-functional-interface-initialize-like-singleton
    // and also relevant https://stackoverflow.com/questions/28023364/what-is-the-equivalent-lambda-expression
    public void comparingConsumersFormedByMethodReferenceAndLambda() {
        Consumer<String> consumerA = asLambdaPrintStringConsumer();
        Consumer<String> consumerB = asLambdaPrintStringConsumer();


        Consumer<String> consumerA2 = s -> System.out.println(s);
        Consumer<String> consumerB2 = s -> System.out.println(s);


        Consumer<String> consumerA3 = asMethodRefPrintStringConsumer();
        Consumer<String> consumerB3 = asMethodRefPrintStringConsumer();

        Consumer<String> consumerA4 = asMethodRefFromStaticMethodStringConsumer();
        Consumer<String> consumerB4 = asMethodRefFromStaticMethodStringConsumer();


        System.out.println(consumerA.equals(consumerB));
        System.out.println(consumerA2.equals(consumerB2));
        System.out.println(consumerA3.equals(consumerB3));
        System.out.println(consumerA4.equals(consumerB4));
    }


    public static Consumer<String> asMethodRefFromStaticMethodStringConsumer() {
        return FunctionUtility::print;
    }

    public static void print(String string) {
        System.out.println(string);
    }

    public static Consumer<String> asLambdaPrintStringConsumer() {
        return x -> System.out.println(x);
    }

    public static Consumer<String> asMethodRefPrintStringConsumer() {
        return System.out::println;
    }

    // transform a string based on a custom function defined
    private static final String MARKER = "| ";

    private String stripMargin(String string) {
        return string.lines().map(String::strip)
                .map(s -> s.startsWith(MARKER) ? s.substring(MARKER.length()) : s)
                .collect(Collectors.joining("\n", "", "\n"));
    }

    public void transformUsingCustomisedMargin() {
        String stripped = """
                    | The content of
                    | the string
                """;
        System.out.print((String) stripped.transform(this::stripMargin));
        //    Output:
        //    The content of
        //    the string
    }
}