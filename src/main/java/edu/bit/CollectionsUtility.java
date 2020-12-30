package edu.bit;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectionsUtility {

    // With the introduction of the List.of APIs the confusion as to how do they differ from the existing
    // Arrays.asList APIs could be sought here. The discussions most relevant to it could be found at
    // https://stackoverflow.com/questions/46579074/what-is-the-difference-between-list-of-and-arrays-aslist
    public void asListVersusListOfAPIs() {
        List<Integer> list = Arrays.asList(1, 2, null);
        list.set(1, 10); // OK

        List<Integer> anotherList = List.of(1, 2, 3);
        anotherList.set(1, 10); // Fails

        // Arrays.asList allows null elements while List.of doesn't:
        List<Integer> asList = Arrays.asList(1, 2, null); // OK
        List<Integer> ofList = List.of(1, 2, null); // Fails

        // Arrays.asList returns a view of the passed array, so the changes to the array will be reflected in the list too. For List.of this is not true:
        Integer[] array = {1, 2, 3};
        List<Integer> listAsArray = Arrays.asList(array);
        array[1] = 10;
        System.out.println(listAsArray); // Prints [1, 10, 3]

        Integer[] anotherArray = {1, 2, 3};
        List<Integer> anotherListAsArray = List.of(anotherArray);
        anotherArray[1] = 10;
        System.out.println(anotherListAsArray); // Prints [1, 2, 3]

        // contains method behaves differently with nulls:
        List<Integer> listContains = Arrays.asList(1, 2, 3);
        listContains.contains(null); // Return false

        List<Integer> anotherListContains = List.of(1, 2, 3);
        anotherListContains.contains(null); // Throws NullPointerException
    }

    private static <T> Set<T> distinct(Collection<? extends T> list, Comparator<? super T> comparator) {
        Set<T> set = new TreeSet<>(comparator);
        set.addAll(list);
        return set;
    }

    public static boolean isAllEmptyOrNull(Collection... collectionList) {
        return Arrays.stream(collectionList).allMatch(Collection::isEmpty);
    }

    public static boolean isAllEmptyOrNull(Map... maps) {
        return Arrays.stream(maps).allMatch(Map::isEmpty);
    }


    public static boolean collectionIsNullOrEmpty(Stream<Collection> collectionStream) {
        return collectionStream.anyMatch(item -> item == null || item.isEmpty());
    }

    void iterateInReverse() {
        List<Integer> list = new ArrayList<>();
        // Generate an iterator. Start just after the last element.
        ListIterator li = list.listIterator(list.size());
        // Iterate in reverse.
        while (li.hasPrevious()) {
            System.out.println(li.previous());
        }
    }

    // there is a subtle difference between unmodifiable APIs introduced lately to
    // what the immutable have existed until now
    public void immutableVersusUnmodifiable() {
        List<Person> persons = Stream.of(new Person("stackoverflow")).collect(Collectors.toList());
        List<Person> unmodifiableList = List.copyOf(persons);
        List<Person> immutableList = persons.stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), Collections::unmodifiableList));
        System.out.println(persons + "" + persons.size());
        System.out.println(unmodifiableList + "" + unmodifiableList.size());
        System.out.println(immutableList + "" + immutableList.size());

        persons.add(new Person("com/stackoverflow/nullpointer"));
        System.out.println(persons + "" + persons.size());
        System.out.println(unmodifiableList + "" + unmodifiableList.size());
        System.out.println(immutableList + "" + immutableList.size());

        immutableList.add(new Person("nmn"));
        unmodifiableList.add(new Person("nmn"));
    }

    record Person(String name, LocalDateTime date, boolean attend, int age, List<String> languagesSpoken,
                  List<Address> addresses, String address) {
        Person(String name) {
            this(name, null, false, 0, List.of(), List.of(), null);
        }
    }

    record Address(String city, String houseNumber) {
    }
}