package edu.bit;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class Objects {

    record Address(String city, String houseNumber) {
    }

    record Author(String lastName, int age) {
    }

    record Book(Author author) {
    }

    record CarShop(String carName, int cost, Set<String> colors) {
    }

    record Employee(int id, int salary, List<Employee> subordinates, Department department, String gender) {
    }

    record Department() {
    }

    record Person(String name, LocalDateTime date, boolean attend, int age, List<String> languagesSpoken,
                  List<Address> addresses, String address) {
    }

    record EmployeeContract(Long id, Date date) {
    }

    record LineItem() {
    }

    record MyDTO(int amount) {
    }

    record Node(int degree) {
    }

    record Order(String customerName, List<LineItem> lineItems) {
    }

    record ProductCatalogue(Integer pId, Integer cId) {
    }

    record Stake(int customerId, int betOfferI, int stake) {
    }

    record Student(String name, int age, Country country, int score, List<Subject> subjects) {
    }

    public enum Country {POLAND, UK, GERMANY}


    record Subject(String name, Integer marks, boolean optional) {
    }

    record XYZProfile(String name, Integer code) {
    }

    record User(String name, String id, String email, List<Integer> lists, int age) {
    }
}