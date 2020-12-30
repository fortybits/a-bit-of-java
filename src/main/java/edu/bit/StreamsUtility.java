package edu.bit;

import edu.bit.__dump.SimpleKeySupplier;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.*;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

public class StreamsUtility {

    public static void calculateAverageValuePerGroup() {
        record Item(String groupName, Double value) {
        }
        List<Item> items = Arrays.asList(
                new Item("A", 1.0),
                new Item("A", 1.0),
                new Item("B", 1.0)
        );
        double result = items.stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.groupingBy(Item::groupName, Collectors.summingDouble(Item::value)),
                        map -> map.values().stream().mapToDouble(Double::doubleValue).sum() / map.size()));
        System.out.println(result);

        double res = items.stream()
                .collect(Collectors.groupingBy(Item::groupName, Collectors.averagingDouble(Item::value)))
                .values().stream().mapToDouble(v -> v).average().orElse(Double.NaN);
        System.out.println(res);

        long distinct = items.stream().map(Item::groupName).distinct().count();
        double sums = items.stream().mapToDouble(Item::value).sum();
        System.out.println(sums / distinct);

    }

    public static void main(String[] args) {
        System.out.println(findMaxOrMin(List.of(2, 3, 4, 56, 90)));
        System.out.println(filterStringBasedOnCondition(List.of("abc", "", "bc", "efg", "abcd", "", "jkl")));
        System.out.println(convertIntArrayToSet(new int[]{1, 2, 3}));
        System.out.println(listToConsecutiveSubLists(List.of(1, 2, 3, 4, 5, 6, 7), 3));
        System.out.println(Stream.of(1, 3, 5).filter(distinctByKey(Integer::doubleValue)).collect(Collectors.toList()));
        System.out.println(isAnagram(List.of("cat", "cta", "act", "atc", "tac", "tca").toArray(new String[0])));
        System.out.println(mostFrequentStream(List.of("cat", "cat", "act", "atc", "act", "act")));
        System.out.println(sumOfListBigDecimal(List.of(BigDecimal.ONE, BigDecimal.TEN, BigDecimal.ZERO)));
        System.out.println(getDeduplicateList(List.of(List.of(0, 1), List.of(1, 2), List.of(3, 2), List.of(2, 3))));
        System.out.println(sameAttributeFilteredFurtherUsingRecentDate(
                List.of(new EmployeeContract(1L, Date.from(Instant.now())),
                        new EmployeeContract(1L, Date.from(Instant.now())),
                        new EmployeeContract(2L, Date.from(Instant.now())),
                        new EmployeeContract(3L, Date.from(Instant.now())))));
        System.out.println(countryWithMostNumberOfStudents(List.of(
                new Student("Jan", 13, Country.POLAND, 92, Collections.emptyList()),
                new Student("Anna", 15, Country.POLAND, 95, Collections.emptyList()),
                new Student("Helga", 14, Country.GERMANY, 93, Collections.emptyList()),
                new Student("Leon", 14, Country.GERMANY, 97, Collections.emptyList()),
                new Student("Chris", 15, Country.GERMANY, 97, Collections.emptyList()),
                new Student("Michael", 14, Country.UK, 90, Collections.emptyList()),
                new Student("Tim", 15, Country.UK, 91, Collections.emptyList()),
                new Student("George", 14, Country.UK, 98, Collections.emptyList()
                ))));
        Arrays.stream(charToString(new char[][]{{'a', 'b', 'c'},
                {'d', 'e', 'f'},
                {'g', 'h', 'i'}
        })).forEach(System.out::println);
        System.out.println(maxEntryFromAListBasedOnCount(
                List.of(1, 1, 2, 3, 5, 8, 13, 21, 21, 61, 98, 15, 25, 41, 67, 55, 89, 89, 89, 89)));
        System.out.println(sizeOfMaximumSizeSet(Map.of(1, Set.of(1, 2, 3), 4, Set.of(2, 3, 5), 5, Set.of(1, 2, 3, 4, 5))));
        System.out.println(removeFormerDuplicates(List.of("interface", "list", "Primitive", "class", "primitive", "List", "Interface", "lIst", "Primitive")));
        System.out.println(Arrays.toString(sortArrayBasedOnCustomComparison(new int[]{3, 30, 34, 5, 9})));
        printWhileCollecting(List.of(
                new Book(new Author("overflow", 100)),
                new Book(new Author("stack", 80)),
                new Book(new Author("com/stackoverflow/nullpointer", 49))));
        System.out.println(getSum(List.of(new MyDTO(12), new MyDTO(15), new MyDTO(1))));
        System.out.println(getMinimumSizeList(List.of(List.of(0, 2, 3), List.of(0, 2, 3, 4))).size());
        System.out.println(Stream.of().anyMatch(a -> true)); // empty stream anyMatch
        System.out.println(splitStringIntoMap(List.of("10-A", "10-B", "11-C", "11-A")));
        updateAListOfMapOfStrings();
        System.out.println(groupByListAttributeInListOfObject(List.of(new CarShop("ford", 25000, Set.of("black", "white", "red")),
                new CarShop("audi", 30000, Set.of("blue", "white")),
                new CarShop("bmw", 35000, Set.of("black")),
                new CarShop("mersedes", 45000, Set.of("blue", "white", "yellow")))));
    }

    private static Integer findMaxOrMin(List<Integer> list) {
//        return list.stream().min(Comparator.comparingInt(a -> a)).orElse(Integer.MAX_VALUE);
        return list.stream().max(Comparator.comparingInt(a -> a)).orElse(Integer.MIN_VALUE);
    }

    private static void stateFullVsStatelessStream() {
        // stateless
        List.of("One", "Two", "Three").stream()
                .peek(System.out::println)
                .takeWhile(s -> true)
                .forEach(System.out::println);
        // stateful
        List.of("One", "Two", "Three").stream()
                .peek(System.out::println)
                .sorted()
                .forEach(System.out::println);
    }

    private static List<Integer> differenceOfEqualSizeList(List<Integer> a, List<Integer> b) {
        return IntStream.range(0, a.size())
                .mapToObj(i -> a.get(i) - b.get(i))
                .collect(Collectors.toList());
    }

    private static List<String> filterStringBasedOnCondition(List<String> strings) {
        return strings.stream()
                .filter(Predicate.not(String::isEmpty))
                .collect(Collectors.toList());
    }

    private static Set<Integer> convertIntArrayToSet(int[] input) {
        return Arrays.stream(input).boxed().collect(Collectors.toSet());
    }

    private static List<List<Integer>> listToConsecutiveSubLists(List<Integer> list, int subListSize) {
        return IntStream.rangeClosed(0, list.size() - subListSize)
                .mapToObj(i -> list.subList(i, i + subListSize))
                .collect(Collectors.toList());
    }

    private static int indexOfTheListWithTheMinSize(List<List<Integer>> listOLists) {
//        int minIndex1 = IntStream.range(0, listOLists.size()).boxed()
//                .min(Comparator.comparingInt(i -> listOLists.get(i).size()))
//                .orElse(-1);
        return listOLists.indexOf(Collections.min(listOLists, Comparator.comparingInt(List::size)));
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
        // previously
        // Map<Object,Boolean> seen = new ConcurrentHashMap<>();
        // return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }

    @SuppressWarnings("unchecked")
    static <T> Stream<T> reverse(Stream<T> input) {
        Object[] temp = input.toArray();
        return (Stream<T>) IntStream.range(0, temp.length).mapToObj(i -> temp[temp.length - i - 1]);
    }

    private static boolean isAnagram(String[] list) {
        return Stream.of(list)
                .map(String::toCharArray)
                .peek(Arrays::sort)
                .map(String::valueOf)
                .distinct()
                .count() == 1;
    }

    private static String mostFrequentStream(List<String> elements) {
        Map<String, Long> ordered = elements.stream()
                .collect(Collectors.groupingBy(a -> a, Collectors.counting()));
        return ordered.entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private static BigDecimal sumOfListBigDecimal(List<BigDecimal> input) {
        return input.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static List<String> readLinesOfFile(String pathToFile) {
        // read a file into a list of words with certain conditions
        try (var lines = Files.lines(Path.of(pathToFile))) {
            return lines.map(String::toLowerCase) // step 1
                    .flatMap(str -> Arrays.stream(str.split("[^a-z]+")) // step 2
                            .filter(Predicate.not(String::isEmpty)).sorted() // step 3
                            .distinct())    // step 4
                    .collect(Collectors.toList()); // step 5
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ArrayList<>();
        }
    }

    private static List<List<Integer>> getDeduplicateList(List<List<Integer>> lists) {
        return lists.stream()
                .map(HashSet::new)
                .distinct()
                .map(ArrayList::new)
                .collect(Collectors.toList());
    }

    private static List<String> fetchItemsWithSpecificCount(List<String> list) {
//        Map<String, Long> result2 = list.stream()
//                .collect(Collectors.groupingBy(Function.identity(),
//                        Collectors.counting()));
//        result2.entrySet().removeIf(v -> v.getValue() == 1);

//        List<String> recurringItems = list.stream()
//                .filter(item -> list.lastIndexOf(item) != list.indexOf(item))
//                .collect(Collectors.toList());

        return list.stream()
                .filter(x -> list.stream().filter(x::equals).count() >= 2)
                .distinct()
                .collect(toList());
    }

    private static List<EmployeeContract> sameAttributeFilteredFurtherUsingRecentDate(List<EmployeeContract> contract) {
//        List<EmployeeContract> finalContract = contract.stream()
//                .collect(Collectors.toMap(EmployeeContract::getId,
//                        EmployeeContract::getConsultedOn, (a, b) -> a.after(b) ? a : b))
//                .entrySet().stream()
//                .map(a -> new EmployeeContract(a.getKey(), a.getValue()))
//                .collect(Collectors.toList());

        return new ArrayList<>(contract.stream()
                .collect(Collectors.toMap(EmployeeContract::id, Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparing(EmployeeContract::date))))
                .values());
    }

    private static Country countryWithMostNumberOfStudents(List<Student> students) {
        Map<Country, Long> numberOfStudentsByCountry = students.stream()
                .collect(Collectors.groupingBy(Student::country, Collectors.counting()));

        Map.Entry<Country, Long> mostFrequentEntry = numberOfStudentsByCountry.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
        System.out.println(mostFrequentEntry);

        return numberOfStudentsByCountry.entrySet()
                .stream()
                .max((Map.Entry.comparingByValue()))
                .map(Map.Entry::getKey)
                .orElse(Country.POLAND);
    }

    private static Map.Entry<Integer, Long> maxEntryFromAListBasedOnCount(List<Integer> list) {
        return list.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue))
                .orElseThrow(IllegalArgumentException::new);
    }

    private static int sizeOfMaximumSizeSet(Map<Integer, Set<Integer>> adj) {
        return adj.values()
                .stream()
                .max(Comparator.comparingInt(Set::size))
                .map(Set::size)
                .orElse(0);
    }

    private static List<Person> filterDistinctElementsOfAList(List<Person> people) {
//        Set<Student> finalListOfStudents = students.stream()
//                .map(x -> nameToStudentMap.merge(x.getConsultedBy(), x, (a, b) -> a.getAge() > b.getAge() ? a : b))
//                .collect(Collectors.toSet());
        return new ArrayList<>(people.stream()
                .collect(Collectors.toMap(a -> a.name().toLowerCase(),
                        Function.identity(),
                        (person, person2) -> person.date().isAfter(person2.date()) ? person : person2))
                .values());
    }

    private static String[] charToString(char[][] array) {
        return Arrays.stream(array) //forms a Stream<char[]> (all your rows)
                .map(String::new) // maps the char[] to String forming Stream<String>
                .toArray(String[]::new); // converts the stream to array String[]
    }

    private static List<String> removeFormerDuplicates(List<String> strings) {
        return new ArrayList<>(strings.stream()
                .collect(Collectors.toMap(String::toLowerCase, Function.identity()))
                .values());
    }

    private static int[] sortArrayBasedOnCustomComparison(int[] arr) {
        return Arrays.stream(arr)
                .boxed()
                .sorted(Comparator.comparingDouble(
                        (Integer x) -> x / (Math.pow(10, x.toString().length() - 1))).reversed())
                .mapToInt(i -> i)
                .toArray();
    }

    private static void printWhileCollecting(List<Book> library) {
        List<String> lastNames = library.stream()
                .map(Book::author)
                .filter(author -> author.age() >= 50)
                .map(Author::lastName)
                .limit(10)
                .peek(System.out::println)
                .collect(Collectors.toList());
    }

    // convertBinaryArrayToInt(List.of(0, 0, 0, 1)
    private static int convertBinaryArrayToInt(List<Integer> binary) {
        return binary.stream()
                .reduce((x, y) -> x * 2 + y)
                .orElse(0);
    }

    private static Double getSum(List<MyDTO> myDTOList) {
        return myDTOList.stream().filter(Objects::nonNull).mapToDouble(MyDTO::amount).sum();
    }

    private static Node createNodeIfValueIsOneOrTwo(ArrayList<Integer> list) {
        return list.stream()
                .filter(l -> l == 1 || l == 2)
                .findAny()
                .map(Node::new)
                .orElse(null);
    }

    private static Map<Long, List<Long>> swapValuesToKeyInAMap(Map<Long, List<Long>> skuMap) {
//        Map<Long, List<Long>> actMap = new HashMap<>();
//        skuMap.forEach((k, v) -> v.forEach(val -> actMap.computeIfAbsent(val, key -> new ArrayList<>()).add(val)));
        return skuMap.entrySet().stream()
                .flatMap(e -> e.getValue().stream().map(v -> new AbstractMap.SimpleEntry<>(e.getKey(), v)))
                .collect(Collectors.groupingBy(Map.Entry::getValue, Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
    }

    private static List<Integer> getMinimumSizeList(List<List<Integer>> lists) {
        return lists.stream()
                .min(Comparator.comparingInt(List::size))
                .orElse(new ArrayList<>());
    }

    private static String convertTwoDimensionalBoardToString(int[][] board) {
        StringJoiner stringJoiner = new StringJoiner("\n");
        Arrays.stream(board).forEach(aBOARD -> {
            StringJoiner joiner = new StringJoiner(",", "[", "]");
            Arrays.stream(aBOARD).mapToObj(String::valueOf).forEach(joiner::add);
            stringJoiner.add(joiner.toString());
        });
        return stringJoiner.toString();
    }

    private static Map<String, List<String>> splitStringIntoMap(List<String> pendingCpcList) {
        return pendingCpcList.stream().map(rec -> rec.split("-"))
                .collect(Collectors.groupingBy(a -> a[0], Collectors.mapping(a -> a[1], Collectors.toList())));
    }

    private static Map<String, List<
            Person>> groupPeopleByLanguage(List<Person> people) {
        // group by language since each person knows a List<String> languages
        return people.stream()
                .flatMap(p -> p.languagesSpoken()
                        .stream()
                        .map(l -> new AbstractMap.SimpleEntry<>(l, p)))
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue,
                                Collectors.toList())));
    }

    private static void updateAListOfMapOfStrings() {
        // alternatively without object creation and using Map<String, String>
        List<Map<String, String>> mapListOne = List.of(Map.of("partyId", "1",
                "accountId", "1",
                "sourceSystem", "1"),
                Map.of("partyId", "2",
                        "accountId", "2",
                        "sourceSystem", "2"));

        List<Map<String, String>> mapListTwo = List.of(Map.of("partyId", "3",
                "accountId", "3",
                "sourceSystem", "3"),
                Map.of("partyId", "1",
                        "accountId", "2",
                        "sourceSystem", "2"));

        List<Map<String, String>> resultMap = Stream.concat(mapListOne.stream(), mapListTwo.stream())
                .collect(Collectors.groupingBy(m -> m.get("partyId"), Collectors.toList()))
                .entrySet().stream()
                .map(e -> e.getValue().get(0))
                .collect(Collectors.toList());
        System.out.println(resultMap);
    }

    private static Map<String, List<CarShop>> groupByListAttributeInListOfObject(List<CarShop> carShops) {
        return carShops.stream()
                .flatMap(e -> e.colors().stream()
                        .map(v -> new AbstractMap.SimpleEntry<>(v, e)))
                .collect(Collectors.groupingBy(Map.Entry::getKey,
                        Collectors.mapping(Map.Entry::getValue, Collectors.toList())));
    }

    /**
     * @apiNote The {@code filtering()} collectors are most useful when used in a
     * multi-level reduction, such as downstream of a {@code groupingBy} or
     * {@code partitioningBy}.  For example, given a stream of
     * {@code Employee}, to accumulate the employees in each department that have a
     * salary above a certain threshold:
     * <pre>{@code
     * Map<Department, Set<Employee>> wellPaidEmployeesByDepartment
     *   = employees.stream().collect(
     *     groupingBy(Employee::getDepartment,
     *                filtering(e -> e.getSalary() > 2000,
     *                          toSet())));
     */
    private static Map<Department, Set<Employee>> filteringWhileGrouping(List<Employee> employees) {
        return employees.stream()
                .collect(Collectors.groupingBy(Employee::department,
                        Collectors.filtering(e -> e.salary() > 2000, Collectors.toSet())));
    }

    public static <T> Predicate<T> chainPredicates(Function<T, Predicate<T>> mapFn, T[] args) {
        return Arrays.stream(args).map(mapFn).reduce(a -> true, Predicate::or);
    }

    private static <T> Stream<T> zipped(Stream<T> first, Stream<T> second) {
        Iterator<T> iterator1 = first.iterator();
        Iterator<T> iterator2 = second.iterator();
        List<T> elements = new LinkedList<>();
        while (iterator1.hasNext() || iterator2.hasNext()) {
            elements.add(iterator1.next());
            elements.add(iterator2.next());
        }
        return elements.stream();
    }

    private static List<String> cartesianProduct() {
        List<String> names = Arrays.asList("Superman", "Batman", "Wonder Woman");
        List<String> likes = Arrays.asList("good1", "good2", "good3");
        List<String> dislikes = Arrays.asList("bad1", "bad2", "bad3");

        Stream<Supplier<Stream<String>>> streamOfSuppliers = List.of(names, likes, dislikes)
                .stream()
                .map(list -> list::stream);
        Supplier<Stream<String>> supplierOfStream = streamOfSuppliers
                .reduce(() -> Stream.of(""),
                        (s1, s2) -> () -> s1.get()
                                .flatMap(a -> s2.get().map(b -> a + b)));
        List<String> statements = supplierOfStream.get().collect(toList());

        System.out.println(statements);
        return statements;
    }

    // https://stackoverflow.com/questions/63106815
    private static List<List<String>> getTopN(int n, List<String> values) {
        Map<String, Long> valueCountMap = values.stream()
                .collect(Collectors.groupingBy(x -> x, Collectors.counting()));

        TreeMap<Long, List<String>> rankedEntries = valueCountMap.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        TreeMap::new, Collectors.mapping(Map.Entry::getKey,
                                Collectors.toList())));

        return rankedEntries.descendingMap().entrySet().stream()
                .limit(n)
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private List<Stake> filteredStakeForDistinctCustomer(List<Stake> stakes, int maxBetOfferId) {
        return stakes.stream()
                .filter(s -> s.betOfferI() == maxBetOfferId) // maxProductOfNonOverlappingPallindromes betOfferId
                .filter(StreamsUtility.distinctByKey(Stake::customerId)) // distinct customer Id
                .limit(20) // limit to 20
                .collect(toList());
    }

    /**
     * @apiNote The {@code flatMapping()} collectors are most useful when used in a
     * multi-level reduction, such as downstream of a {@code groupingBy} or
     * {@code partitioningBy}.  For example, given a stream of
     * {@code Order}, to accumulate the set of line items for each customer:
     * <pre>{@code
     * Map<String, Set<LineItem>> itemsByCustomerName
     *   = orders.stream().collect(
     *     groupingBy(Order::getCustomerName,
     *                flatMapping(order -> order.getLineItems().stream(),
     *                            toSet())));
     */
    private Map<String, Set<LineItem>> flatMappingWhileGroupingItemsByCustomerName(List<Order> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::customerName,
                        Collectors.flatMapping(order -> order.lineItems().stream(), Collectors.toSet())));
    }

    private void consumeObjectsProvidedBySupplier() {
        Consumer<String> consumer = (String key) -> System.out.println("key=" + key);
        Supplier<String> keyGen = new SimpleKeySupplier("keyPrefix", 4);
        Stream.generate(keyGen).takeWhile(Objects::nonNull).forEach(consumer);
    }

    public IntStream convertOneDimensionalArray(int[] val) {
        return Stream.of(val)
                .flatMapToInt(Arrays::stream);
    }

    public IntStream convertTwoDimensionalArray(int[][] val) {
        return Stream.of(val)
                .flatMapToInt(Arrays::stream);
    }

    public Map<String, Integer> getBestPrices(List<PriceGroup> priceGroups, List<Price> prices) {
        Map<String, Integer> minPrice = prices.stream()
                .collect(Collectors.groupingBy(Price::getPriceName,
                        Collectors.reducing(0, Price::getPrice,
                                BinaryOperator.minBy(Comparator.naturalOrder()))));
        return priceGroups.stream()
                .collect(Collectors.toMap(PriceGroup::getPriceGroup,
                        priceGroup ->
                                minPrice.getOrDefault(priceGroup.getPriceName(), 10000000)));
    }

    public enum Country {POLAND, UK, GERMANY}

    record EmployeeContract(Long id, Date date) {
    }

    record Student(String name, int age, Country country, int score, List<Subject> subjects) {
    }

    record Subject(String name, Integer marks, boolean optional) {
    }

    record Author(String lastName, int age) {
    }

    record Book(Author author) {
    }

    record CarShop(String carName, int cost, Set<String> colors) {
    }

    record MyDTO(int amount) {
    }

    record Person(String name, LocalDateTime date, boolean attend, int age, List<String> languagesSpoken,
                  List<Address> addresses, String address) {
    }

    record Address(String city, String houseNumber) {
    }

    record Employee(int id, int salary, List<Employee> subordinates, Department department, String gender) {
    }

    record Department() {
    }

    record Stake(int customerId, int betOfferI, int stake) {
    }

    record LineItem() {
    }

    record Order(String customerName, List<LineItem> lineItems) {
    }

    record Node(int degree) {
    }

    @Getter
    public class PriceGroup {
        String priceName;
        String priceGroup;
    }

    @Getter
    public class Price {
        String priceName;
        Integer price;
    }

    public void dropWhileVersusTakeWhile() {
        Stream.of("a", "b", "c", "de", "f", "g", "h")
                .peek(System.out::println)
                .takeWhile(s -> s.length() <= 1)
                .collect(Collectors.toList()).forEach(System.out::println);

        Stream.of("a", "b", "c", "de", "f", "g", "h")
                .peek(s -> System.out.print(s + ", "))
                .dropWhile(s -> s.length() <= 1)
                .collect(Collectors.toList()).forEach(System.out::println);

        Stream.of("a", "b", "c", "de", "f", "g", "h")
                .dropWhile(s -> {
                    System.out.println("dropWhile: " + s);
                    return s.length() <= 1;
                })
                .peek(s -> System.out.println("collecting " + s))
                .collect(Collectors.toList()).forEach(System.out::println);
    }

    public static void groupByAndSort(String[] args) {
        List<String> items = Arrays.asList("apple", "apple", "banana", "apple", "orange", "banana", "papaya");

        // 1.1== >Group by a List and display the total count of it
        Map<String, Long> result =
                items.stream().sorted().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println("RESULT : " + result);

        // 1.2 Add sorting
        Map<String, Long> finalMap = new LinkedHashMap<>();
        result.entrySet().stream().sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEachOrdered(e -> finalMap.put(e.getKey(), e.getValue()));
        System.out.println("FINAL RESULT : " + finalMap);
    }

    /**
     * Like {@code DoubleSummaryStatistics}, {@code IntSummaryStatistics}, and
     * {@code LongSummaryStatistics}, but for {@link BigDecimal}.
     * The code has been a referred from the following stackoverflow source
     * https://stackoverflow.com/questions/51645432/bigdecimal-summary-statistics
     */
    public static class BigDecimalSummaryStatistics implements Consumer<BigDecimal> {

        private BigDecimal sum = BigDecimal.ZERO, min, max;
        private long count;

        public static Collector<BigDecimal, ?, BigDecimalSummaryStatistics> statistics() {
            return Collector.of(BigDecimalSummaryStatistics::new,
                    BigDecimalSummaryStatistics::accept, BigDecimalSummaryStatistics::merge);
        }

        public void accept(BigDecimal t) {
            if (count == 0) {
                Objects.requireNonNull(t);
                count = 1;
                sum = t;
                min = t;
                max = t;
            } else {
                sum = sum.add(t);
                if (min.compareTo(t) > 0) min = t;
                if (max.compareTo(t) < 0) max = t;
                count++;
            }
        }

        private BigDecimalSummaryStatistics merge(BigDecimalSummaryStatistics s) {
            if (s.count > 0) {
                if (count == 0) {
                    count = s.count;
                    sum = s.sum;
                    min = s.min;
                    max = s.max;
                } else {
                    sum = sum.add(s.sum);
                    if (min.compareTo(s.min) > 0) min = s.min;
                    if (max.compareTo(s.max) < 0) max = s.max;
                    count += s.count;
                }
            }
            return this;
        }

        public long getCount() {
            return count;
        }

        public BigDecimal getSum() {
            return sum;
        }

        public BigDecimal getAverage(MathContext mc) {
            return count < 2 ? sum : sum.divide(BigDecimal.valueOf(count), mc);
        }

        public BigDecimal getMin() {
            return min;
        }

        public BigDecimal getMax() {
            return max;
        }

        @Override
        public String toString() {
            return count == 0 ? "empty" : (count + " elements between " + min + " and " + max + ", sum=" + sum);
        }
    }

    // pattern to simplify code using Predicate
    int totalValues(List<Integer> numbers) {
        int total = 0;
        for (int e : numbers) {
            total += e;
        }
        return total;
    }

    int totalEvenValues(List<Integer> numbers) {
        int total = 0;
        for (int e : numbers) {
            if (e % 2 == 0) total += e;
        }
        return total;
    }

    int totalOddValues(List<Integer> numbers) {
        int total = 0;
        for (int e : numbers) {
            if (e % 2 != 0) total += e;
        }
        return total;
    }


    // use of predicate could simplify all these use cases into a common method
    // the strategy pattern is simplified further by using lambdas, this is covered in a video
    // at https://www.youtube.com/watch?v=WN9kgdSVhDo&t=982s&ab_channel=Devoxx
    int totalValues(List<Integer> numbers, Predicate<Integer> selector) {
        return numbers.stream()
                .mapToInt(e -> e)
                .filter(selector::test)
                .sum();
    }

    // optimised
    int optimisedTotalValues(Collection<Integer> numbers, Predicate<Integer> selector) {
        return numbers.stream()
                .filter(selector)
                .reduce(0, Integer::sum);
    }


    // a new API introduced since Java-11 to transform patterns into matching predicates
    public void asMatchPredicateWithPatterns() {
        var languages = List.of("c#", "java", "python", "scala");
        var p = Pattern.compile("[a-z]{4}");

        for (String lang : languages) {
            if (p.matcher(lang).matches()) {
                System.out.println(lang);
            }
        }

        languages.stream()
                .filter(s -> p.matcher(s).matches())
                .forEach(System.out::println);

        languages.stream()
                .filter(Predicate.not(p.asMatchPredicate())) //here
                .forEach(System.out::println);
    }

    private static List<User> userListWithSameEmailAndMergeLists(List<User> users) {
        return new ArrayList<>(users.stream()
                .collect(Collectors.toMap(User::email, Function.identity(), (user1, user2) -> {
                    List<Integer> l1 = user1.lists();
                    List<Integer> l2 = user2.lists();
                    List<Integer> merge = IntStream.range(0, l1.size())
                            .mapToObj(i -> (l1.get(i) == 0 && l2.get(i) == 0) ? 0 : 1)
                            .collect(Collectors.toList());
                    return new User(user1.email(), merge);
                })).values());
    }

    private static Map<Integer, List<Integer>> collectGroupingByAndMapping(List<ProductCatalogue> productCatalogueList) {
        return productCatalogueList.stream()
                .collect(Collectors.groupingBy(ProductCatalogue::pId,
                        Collectors.mapping(ProductCatalogue::cId, Collectors.toList())));

    }

    private static List<Stake> stakesHighestPerCustomerForParticularStakeLimited(List<Stake> stakes, int maxBetOfferId) {
        return stakes.stream()
                .filter(x -> x.betOfferI() == maxBetOfferId) // retains only objects where their offer is if equal to the supplied offerId
                .collect(toMap(Stake::customerId,    // customer ids do not repeat as you've mentioned.
                        Function.identity(),
                        BinaryOperator.maxBy(Comparator.comparingInt(Stake::stake))))  //   gets the highest stake values customer wise.
                .values()
                .stream()
                .collect(groupingBy(Stake::stake)) // particular stake
                .values()
                .stream()
                .flatMap(x -> x.stream().limit(20)) //  and limited to 20 customers for a particular stake.
                .collect(Collectors.toList());
    }

    record User(String name, String id, String email, List<Integer> lists, int age) {
        User(String email, List<Integer> lists) {
            this(null, null, email, lists, 0);
        }
    }

    record ProductCatalogue(Integer pId, Integer cId) {
    }

    public void collectorInference() {
        List<BlogPost> posts = new ArrayList<>();
        Function<? super BlogPost, ? extends BlogPostType> classifier = BlogPost::getType;
        Map<BlogPostType, List<BlogPost>> postsPerType = posts.stream()
                .collect(groupingBy(classifier));
    }

    private static class BlogPostType {
    }

    private static class BlogPost {
        BlogPostType type;

        public BlogPostType getType() {
            return type;
        }
    }

    public void collectAtOnceUsingStreamConcat() throws ParseException {
        Info info1 = new Info(1L, getDateFromStr("2018-02-02T10:00:00"), 3L);
        Info info2 = new Info(2L, getDateFromStr("2018-02-02T12:00:00"), 3L);
        Info info3 = new Info(3L, getDateFromStr("2018-02-05T12:00:00"), 6L);
        Info info4 = new Info(4L, getDateFromStr("2018-02-05T10:00:00"), 6L);
        List<Info> listInfo = List.of(info1, info2, info3, info4);
        Date date = getDateFromStr("2018-02-03T10:10:10");


        BiFunction<Info, Info, Info> remapping = (i1, i2) -> i1.date().getTime() > i2.date().getTime() ? i1 : i2;
        // filter 1: less date - group by maxProductOfNonOverlappingPallindromes date by groupId
        Map<Long, Info> map = new HashMap<>();
        List<Info> listMoreByDate = new ArrayList<>();
        for (Info info : listInfo) {
            if (info.date().getTime() < date.getTime()) {
                map.merge(info.groupId(), info, remapping);
            } else {
                listMoreByDate.add(info);
            }
        }
        List<Info> listResult = new ArrayList<>(map.values());
        listResult.addAll(listMoreByDate);


        // holger solved it
        List<Info> listResult2 = Stream.concat(
                listInfo.stream()
                        .filter(info -> info.date().getTime() < date.getTime())
                        .collect(toMap(Info::groupId, Function.identity(),
                                BinaryOperator.maxBy(Comparator.comparing(Info::date))))
                        .values().stream(),
                listInfo.stream()
                        .filter(info -> info.date().getTime() >= date.getTime()))
                .collect(Collectors.toList());

        System.out.println("result: " + listResult);
    }

    private static Date getDateFromStr(String dateStr) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateStr);
    }

    private record Info(Long id, Date date, Long groupId) {
    }

    public void collectorToUnmodifiableList() {
        var result = Stream.of(1, 2, 3, 4, null, 5)
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        Collections::unmodifiableList));
        System.out.println(result);

        var result2 = Stream.of(1, 2, 3, 4)
                .collect(Collectors.toUnmodifiableList());
        System.out.println(result2);
    }

    //
    public long compareCharacterDifferencesBetweenStrings(String str1, String str2) {
        return IntStream.range(0, str1.length())
                .filter(i -> str1.charAt(i) != str2.charAt(i))
                .count();
    }

    // common use case for grouping
    public static void groupByStartAndEndDateToMerge() {
        List<ReleaseTime> ungroupedAvailability = List.of();
        Collection<ReleaseTime> mergedRegionsCollection = ungroupedAvailability.stream()
                .collect(Collectors.toMap(t -> Arrays.asList(t.getStartDate(), t.getEndDate()),
                        Function.identity(), ReleaseTime::mergeRegions))
                .values();
    }

    @Getter
    @AllArgsConstructor
    class ReleaseTime {
        private Date startDate;
        private Date endDate;
        private List<String> regions;

        ReleaseTime mergeRegions(ReleaseTime that) {
            return new ReleaseTime(this.startDate, this.endDate,
                    Stream.concat(this.getRegions().stream(), that.getRegions().stream())
                            .collect(Collectors.toList()));
        }
    }


    //
    public void compositePredicates() {
        Stream<Integer> stream = Stream.of(5, 7, 9, 11, 13, 14, 21, 28, 35, 42, 49, 56, 63, 70, 71);
        IntPredicate p0 = n -> n > 10;
        IntPredicate p1 = n -> n % 2 != 0;
        IntPredicate p2 = StreamsUtility::isPrime;
        System.out.println(matchAll(stream, p0, p1, p2));
        // should get [11, 13, 71]
    }

    private static boolean isPrime(Integer n) {
        return IntStream.range(2, n) // note  division by zero possible in your attempt
                .noneMatch(i -> n % i == 0);
    }

    private List<Integer> matchAll(Stream<Integer> input, IntPredicate... conditions) {
        IntPredicate compositePredicate =
                Arrays.stream(conditions)
                        .reduce(IntPredicate::and)
                        .orElse(p -> true);
        return input.mapToInt(i -> i)
                .filter(compositePredicate)
                .boxed()
                .collect(Collectors.toList());
    }

    // detailed in https://stackoverflow.com/questions/59154995/cleaning-a-list-of-data-in-java8/59156527#59156527
    public <T> List<T> cleanDataInPlaceWithMappingFunction(List<T> data, List<Function<T, T>> cleanOps) {
        return data.stream().map((str) -> {
            T cleanData = str;
            for (Function<T, T> function : cleanOps) {
                cleanData = function.apply(cleanData);
            }
            return cleanData;
        }).collect(Collectors.toList());
    }

    public <T> List<T> cleanDataByHolger(List<T> data, List<Function<T, T>> cleanOps) {
        cleanOps.stream()
                .reduce(Function::andThen)
                .ifPresent(f -> data.replaceAll(f::apply));
        return data;
    }

    public <T> List<T> cleanDataByHolgerOptimised(List<T> data, List<UnaryOperator<T>> cleanOps) {
        cleanOps.stream()
                .reduce((f1, f2) -> t -> f2.apply(f1.apply(t)))
                .ifPresent(data::replaceAll);
        return data;
    }


    //
    List<String> generatingRandomListOfWords() {
        int listSize = 10;
        int maxWordSize = 10;
        int[] letters = IntStream.range('A', 'Z').toArray();
        return IntStream.range(0, listSize)
                .mapToObj(ix -> ThreadLocalRandom.current()
                        .ints(ThreadLocalRandom.current().nextInt(1, maxWordSize), 0, letters.length)
                        .map(i -> letters[i])
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString())
                .collect(Collectors.toList());
    }


    // collect to a treemap or a sorted map while using streams
    public SortedMap<String, Long> skill_nApplicants(Map<String, Skill> skillMap) {
        return skillMap.values().stream()
                .collect(Collectors.toMap(Skill::name, Skill::numApplicants,
                        (a, b) -> a, TreeMap::new));
    }

    public record Skill(String name, Long numApplicants) {
    }


    // conversion of a list or stream to a byte[] e.g. type widening to int and conversion to ByteArrayOutputStream
    public static byte[] toByteArray(IntStream stream) {
        return stream.collect(ByteArrayOutputStream::new, (baos, i) -> baos.write((byte) i),
                (baos1, baos2) -> baos1.write(baos2.toByteArray(), 0, baos2.size()))
                .toByteArray();
    }

    public static byte[] toByteArrayFunctional(List<Byte> listByte) {
        return listByte.stream().collect(ByteArrayOutputStream::new, ByteArrayOutputStream::write,
                (baos1, baos2) -> baos1.writeBytes(baos2.toByteArray()))
                .toByteArray();
    }

    public static byte[] toByteArrayIterative(List<Byte> listByte) {
        byte[] arrayBytes = new byte[listByte.size()];
        IntStream.range(0, listByte.size()).forEach(i -> arrayBytes[i] = listByte.get(i));
        return arrayBytes;
    }

    // custom comparator for a problem detailed in
    // https://stackoverflow.com/questions/60914762/ignore-zero-values-at-sorted-in-lambda
    public static <T> Comparator<T> zerosLast(ToIntFunction<? super T> keyExtractor) {
        return (o1, o2) -> {
            if (keyExtractor.applyAsInt(o1) == 0) {
                return keyExtractor.applyAsInt(o2) == 0 ? 0 : 1;
            } else {
                return keyExtractor.applyAsInt(o2) == 0 ? -1 :
                        Integer.compare(keyExtractor.applyAsInt(o1),
                                keyExtractor.applyAsInt(o2));
            }
        };
    }

    // using multiple mappers to joining the output as a string from one object with varied attributes
    public String extractSimilarAttributesFromEntityToCombinedString(SomeClass shipment) {
        return Optional.ofNullable(shipment)
                .map(SomeClass::bill)
                .map(bill -> extractAttributes(bill, Bill::numberString, Bill::prefixString))
                .orElse(null);
    }

    @SafeVarargs
    private String extractAttributes(Bill entity, Function<Bill, String>... mappers) {
        List<String> attributes = Arrays.stream(mappers)
                .map(function -> function.apply(entity))
                .collect(Collectors.toList());
        return attributes.stream().anyMatch(s -> s == null || s.isEmpty()) ?
                null : String.join("-", attributes);
    }

    record SomeClass(Bill bill) {
    }

    record Bill(String prefixString, String numberString) {
    }

    // flatMapping two dimensional arrays of various types

    <T> Stream<T> flatMapTwoDimensionalArray(T[][] array) {
        return Arrays.stream(array).flatMap(Arrays::stream);
    }

    Stream<Integer> flatMapTwoDimensionalArray(int[][] array) {
        return Arrays.stream(array).flatMap(arr -> Arrays.stream(arr).boxed());
    }

    Stream<Double> flatMapTwoDimensionalArray(double[][] array) {
        return Arrays.stream(array).flatMap(arr -> Arrays.stream(arr).boxed());
    }

    Stream<Long> flatMapTwoDimensionalArray(long[][] array) {
        return Arrays.stream(array).flatMap(arr -> Arrays.stream(arr).boxed());
    }
}