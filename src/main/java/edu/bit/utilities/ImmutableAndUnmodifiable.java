package edu.bit.utilities;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ImmutableAndUnmodifiable {

    record Person(String name, LocalDateTime date, boolean attend, int age, List<String> languagesSpoken,
                  List<Address> addresses, String address) {
        Person(String name) {
            this(name, null, false, 0, List.of(), List.of(), null);
        }
    }

    record Address(String city, String houseNumber) {
    }


    public static void main(String[] args) {

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
}