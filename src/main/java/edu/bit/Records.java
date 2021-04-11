package edu.bit;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.lang.reflect.RecordComponent;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Records {

    public static void writeAndReadTestForSerialisation() throws IOException, ClassNotFoundException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream("persons.bin"))) {
            out.writeObject(new PersonRecord("Heinz", "Kabutz"));
            out.writeObject(new PersonClass("Heinz", "Sommerfeld"));
            out.writeObject(null);
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("persons.bin"))) {
            Person human;
            while ((human = (Person) in.readObject()) != null) {
                System.out.println(human);
            }
        }
    }

    // this particular example of moving from lombok based annotations to records is also listed at
    // https://stackoverflow.com/questions/60796961/compatibility-issues-while-converting-classes-to-records
    public void equalsImplementationDuringMigration() {
        List<City> cities = List.of(
                new City(1, "one"),
                new City(2, "two"),
                new City(3, "three"),
                new City(2, "two"));
        Map<City, Long> cityListMap = cities.stream()
                .collect(Collectors.groupingBy(Function.identity(),
                        Collectors.counting()));
        System.out.println(cityListMap);

        List<CityRecord> cityRecords = List.of(
                new CityRecord(1, "one"),
                new CityRecord(2, "two"),
                new CityRecord(3, "three"),
                new CityRecord(2, "two"));
        Map<CityRecord, Long> cityRecordListMap = cityRecords.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(cityRecordListMap);
    }

    public void recordsWithInterfaces() {
        new Summable("message").method();
    }

    // one can verify using class name with an API if a class is a record or not
    public void verifyRecordOrClass() throws ClassNotFoundException {
        Student student = new Student("naman", 30, 0.45);
        Class<?> studentCls = Class.forName("edu.bit.features.records.Student");
        System.out.println(studentCls.isRecord());

        PersonRecord person = new PersonRecord("naman", "nigam");
        Class<?> personCls = Class.forName("edu.bit.features.records.Person");
        System.out.println(personCls.isRecord());
    }

    record ConstFunction<T, R>(T t, R r) implements Function<T, R> {
        // static fields are allowed
        static String field;

        @Override
        public R apply(T t) {
            return null;
        }

        // trick here is that the name of the variable is same as the abstract methods of interfaces
        record ConstCallable<V>(V call) implements Callable<V> {
        }

        record ConstSupplier<T>(T get) implements Supplier<T> {
        }

        // what records are not meant for
        record Executor<T>(Supplier<T> supply, Consumer<T> accept) {
        }
    }

    // records could be used to implement callable
    public void usingRecordForCallablesToSubmit() throws ExecutionException, InterruptedException {
        ConstFunction.ConstCallable<String> aRecord;
        Stream.generate(new ConstFunction.ConstSupplier<>(5)).limit(2).forEach(System.out::println);
        aRecord = new ConstFunction.ConstCallable<>("record");
        System.out.println(ForkJoinPool.commonPool().submit(aRecord).get());
    }

    public void leakingViaRecords() {
        Singleton singleton = Singleton.getInstance();
        System.out.println(singleton.leak());
    }

    // the behaviour os wrapping primitive integer array within a class versus within records
    public void wrappingPrimitiveTypesWithinRecords() {
        var ints = new int[]{1, 2};

        var foo = new Foo(ints);
        System.out.println(foo); // Foo[ints=[I@6433a2]
        System.out.println(new Foo(new int[]{1, 2}).equals(new Foo(new int[]{1, 2}))); // false
        System.out.println(new Foo(ints).equals(new Foo(ints))); //true
        System.out.println(foo.equals(foo)); // true

        var bar = new Bar(ints);
        System.out.println(bar); // Bar{arr=[1, 2]}
        System.out.println(new Bar(new int[]{1, 2}).equals(new Bar(new int[]{1, 2}))); // true
        System.out.println(new Bar(ints).equals(new Bar(ints))); //true
        System.out.println(bar.equals(bar)); // true

        var integers = Arrays.asList(1, 2);
        var world = new World(integers);
        System.out.println(world); // World{ints=[1, 2]}
        System.out.println(new World(Arrays.asList(1, 2)).equals(new World(Arrays.asList(1, 2)))); // true
        System.out.println(new World(integers).equals(new World(integers))); //true
        System.out.println(world.equals(world)); // true


        var worldRecord = new WorldRecord(integers);
        System.out.println(worldRecord); // World{ints=[1, 2]}
        System.out.println(new WorldRecord(Arrays.asList(1, 2)).equals(new WorldRecord(Arrays.asList(1, 2)))); // true
        System.out.println(new WorldRecord(integers).equals(new WorldRecord(integers))); //true
        System.out.println(worldRecord.equals(worldRecord)); // true


    }

    public void circularReferenceDeSerialisation() throws IOException, ClassNotFoundException {
        One one = new One("tail");
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("records.bin"))) {
            out.writeObject(new One("one", new Two(2, new Three(3L, one))));
            out.writeObject(new Cyclic(new Cyclic(new Cyclic(new Cyclic()))));
            out.writeObject(null);
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("records.bin"))) {
            One allInOne;
            while ((allInOne = (One) in.readObject()) != null) {
                System.out.println(allInOne);
            }

            Cyclic cyclic;
            while ((cyclic = (Cyclic) in.readObject()) != null) {
                System.out.println(cyclic);
            }
        }
    }

    public void constructorValidationTestForRecords() throws IOException, ClassNotFoundException {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream("persons.bin"))) {
            out.writeObject(new PersonRecord("Heinz", "Kabutz"));
            out.writeObject(new PersonClass("Heinz", "Sommerfeld"));
            out.writeObject(null);
        }
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("persons.bin"))) {
            Person human;
            while ((human = (Person) in.readObject()) != null) {
                System.out.println(human);
            }
        }

    }

    public void writeAndReadWithOptionalParameterTest() throws IOException, ClassNotFoundException {
        record PersonRecord(String firstName, String lastName) implements Person, Serializable {
            public PersonRecord(String firstName) {
                this(firstName, null);
            }
        }
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("persons.bin"))) {
            out.writeObject(new PersonRecord("Heinz", "Kabutz"));
            out.writeObject(new PersonClass("Heinz", "Sommerfeld"));
            out.writeObject(null);
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("persons.bin"))) {
            Person human;
            while ((human = (Person) in.readObject()) != null) {
                System.out.println(human);
            }
        }
    }

    public void writeAndReadWithToStringTest() throws IOException, ClassNotFoundException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("persons.bin"))) {
            out.writeObject(new PersonRecord("Heinz", "Kabutz"));
            out.writeObject(new PersonClass("Heinz", "Sommerfeld"));
            out.writeObject(null);
        }

        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream("persons.bin"))) {
            Person human;
            while ((human = (Person) in.readObject()) != null) {
                System.out.println(human);
            }
        }
    }


    // the pattern for null values can be implemented such as the following implementations
    interface MyRecord {
        String id();

        Optional<String> value();
    }

    // records with interfaces and its implementations at its feasibility
    interface Able {
        default void method() {
            System.out.println("interface able");
        }
    }

    // serialisation samples for records follow up further
    public interface Person {
        String firstName();

        String lastName();
    }

    /**
     * Records behave like normal classes: they can be declared top level or nested, they can be generic,
     * they can implement interfaces, and they are instantiated via the new keyword.
     * The record's body may declare static methods, static fields, static initializers, constructors,
     * instance methods, and nested types.
     * The record, and the individual components in a state description, may be annotated.
     * If a record is nested, then it is implicitly static; this avoids an immediately enclosing instance
     * which would silently add state to the record.
     *
     * @param name     first name of the person
     * @param lastName last name of the person
     */
    public record PersonRecord(String name, String lastName) {
        static char gender; // can be assigned value anytime and statically keeps it further

        /**
         * The constructor may be declared without a formal parameter list (in this case, it is assumed identical
         * to the state description), and any record fields which are definitely unassigned when the constructor body
         * completes normally are implicitly initialized from their corresponding formal parameters (this.x = x) on exit.
         * This allows an explicit canonical constructor to perform only validation and normalization of its parameters,
         * and omit the obvious field initialization.
         */
        public PersonRecord {
            if (name.equalsIgnoreCase(lastName)) {
                System.out.println("same-same");
            }
        }

        void print() {
            System.out.println(name + ":" + lastName + "-" + gender);
        }
    }

    // the getters are sufficient for the implemented interfaces
    record CompositeInterface<T, V>(V call, T get) implements Callable<V>, Supplier<T> {
    }

    @Value
    public static class City {
        Integer id;
        String name;
    }

    record CityRecord(Integer id, String name) {
    }

    // Null object pattern for records as listed
    // in https://stackoverflow.com/questions/62799232/java-records-and-null-object-pattern
    public static class Id {

        public static final Id NULL_ID = new Id();

        private String id;

        public Id(String id) {
            this.id = Objects.requireNonNull(id);
        }

        private Id() {
        }
    }

    public static record IdRecord(String id) {
        public static final IdRecord NULL_ID = new IdRecord();

        public IdRecord() {
            this(null);
        }

        public IdRecord {
            Objects.requireNonNull(id);
        }

        public static void main(String[] args) {
            IdRecord.NULL_ID.id();
        }
    }

    static class MyClassDataAccess {
        Optional<String> getValue(MyClass myClass) {
            return Optional.ofNullable(myClass.value());
        }

        String readId(MyClass myClass) {
            return myClass.id();
        }

        private record MyClass(String id, String value) {
            Optional<String> getValue() {
                return Optional.ofNullable(value());
            }
        }
    }

    record MyRecordNoValue(String id) implements MyRecord {
        public Optional<String> value() {
            return Optional.empty();
        }
    }

    record MyRecordWithValue(String id, String actualValue) implements MyRecord {
        public Optional<String> value() {
            return Optional.of(actualValue);
        }
    }

    // It is a compile-time error for a record declaration to declare a record component with the name:
    // clone, finalize, getClass, hashCode, notify, notifyAll, toString, or wait (8.10.3)
    // for variable arity the compilation fails without any argument being passed to the constructor
    // https://stackoverflow.com/questions/64131753/why-is-the-variable-arity-record-component
    public record Break<R extends Record>(R record, String... notifications) {

        public Break(R record, String... notifications) {
            System.out.println("record: " + record + " and notifications: " + Arrays.toString(notifications));
            this.record = record;
            this.notifications = notifications;
        }

//    public Break(R record) {
//        System.out.println("record: " + record);
//        this.record = record;
//    }

        public Break() {
            this(null); // this works
            // actually intelliJ suggests it uses the constructor that is not compiling
        }

        public void invokeVariableAritySample() {
            new Break<>(new Break<>());
        }
    }

    public static record RecordBodyDeclaration() {

        static int valStat = 10;

        static {
            record InnerRec(@NonNull String innerText) {
                public InnerRec {
                    System.out.println(innerText);
                }

                public String innerText() {
                    return innerText;
                }

                @Override
                public boolean equals(Object obj) {
                    return false;
                }

                @Override
                public String toString() {
                    return "fundoo";
                }
            }
            new InnerRec("inner string");
            System.out.println("static block");
        }

        static class InnerClass {
            int value;
        }
    }

    record Summable(String msg) implements Able {
        @Override
        public void method() {
            System.out.println("record able");
        }
    }

    // reference of a local record can be used tto prevent leaking the information from singleton
    public static class Singleton {

        public static Singleton getInstance() {
            record Holder() {
                static final Singleton INSTANCE = new Singleton();
            }
            return Holder.INSTANCE;
        }

        public Singleton leak() {
            // try using Holder here
            System.out.println("I wanted to leak information!");
            return Singleton.getInstance();
        }
    }

    static class Bar {
        int[] arr;

        public Bar(int[] arr) {
            this.arr = arr;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Bar bar = (Bar) o;

            return Arrays.equals(arr, bar.arr);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(arr);
        }

        @Override
        public String toString() {
            return "Bar{" +
                    "arr=" + Arrays.toString(arr) +
                    '}';
        }
    }

    static record Foo(int[] ints) {

        @Override
        public String toString() {
            return "Foo{" +
                    "ints=" + Arrays.toString(ints) +
                    '}';
        }
    }

    static class World {
        List<Integer> ints;

        public World(List<Integer> ints) {
            this.ints = ints;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            World world = (World) o;

            return ints.equals(world.ints);
        }

        @Override
        public int hashCode() {
            return ints.hashCode();
        }

        @Override
        public String toString() {
            return "World{" +
                    "ints=" + ints +
                    '}';
        }
    }

    static record WorldRecord(List<Integer> ints) {
    }

    private record Cyclic(Cyclic cycle) implements Serializable {
        public Cyclic() {
            this(new Cyclic(null));
        }
    }

    @Builder
    private record One(String one, Two two) implements Serializable {
        public One(String one) {
            this(one, null);
        }
    }

    private record Two(Integer two, Three three) implements Serializable {
    }

    private record Three(Long three, One one) implements Serializable {
    }

    public record PersonRec(String firstName, String lastName) implements Person, Serializable {
        public PersonRec(String firstName) {
            this(firstName, null);
        }

        public PersonRec {
            if ("Heinz".equals(firstName))
                throw new IllegalArgumentException(
                        "\"%s\" is trademarked".formatted(firstName));
        }
    }

    static class PersonClass implements Person, Serializable {
        final String firstName;
        final String lastName;

        public PersonClass(String firstName,
                           String lastName) {
            if ("Heinz".equals(firstName))
                throw new IllegalArgumentException(
                        "\"%s\" is trademarked".formatted(firstName));
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public PersonClass(String firstName) {
            this(firstName, null);
        }

        public String firstName() {
            return firstName;
        }

        public String lastName() {
            return lastName;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass())
                return false;
            PersonClass that = (PersonClass) o;
            return Objects.equals(firstName, that.firstName)
                    && Objects.equals(lastName, that.lastName);
        }

        public int hashCode() {
            return Objects.hash(firstName, lastName);
        }

        public String toString() {
            return "HumanClass{" +
                    "firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    '}';
        }
    }

    @Value
    public class Student {
        String name;
        int age;
        double timeSpent;
    }

    // constructor parameter names for records
    // https://stackoverflow.com/questions/67038058/java-record-cannot-get-parameter-names-from-constructors
    public void constructorParametersOfRecords() {
        var recordTest2 = new RecordConstructors(1, 2, 3.0, LocalDateTime.now());
        Class<?> objectClass = recordTest2.getClass();
        Constructor<?>[] constructors = objectClass.getConstructors();
        for (Constructor<?> con : constructors) {
            System.out.println(con.getName());
            Parameter[] parameters = con.getParameters();
            for (Parameter parameter : parameters) {
                System.out.printf("param: %s\n", parameter.getName());
            }
        }
    }

    public record RecordConstructors(int id, int something, double total, LocalDateTime createdOn) {

        public RecordConstructors(int id, int something, double total) {
            this(id, something, total, LocalDateTime.now());
        }
    }
}