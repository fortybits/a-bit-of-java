package edu.bit;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomGroupingBy {

    public static void main(String[] args) {
        List<Employee> list = Arrays.asList(
                new Employee("mahesh", "IT", 25, 1000.00),
                new Employee("rajesh", "IT", 30, 1200.00),
                new Employee("Saket", "IT", 30, 1000.00),
                new Employee("Samuel", "IT", 30, 1150.00),
                new Employee("Anil", "Eng", 29, 2000.00),
                new Employee("Sachin", "Eng", 29, 2000.00));

        List<Employee> employees = list.stream()
                .collect(Collectors.groupingBy(employee -> employee.dept + employee.salary))
                .entrySet()
                .stream()
                .filter(e -> e.getValue().size() > 1)
                .flatMap(e -> e.getValue().stream())
                .collect(Collectors.toList());

        System.out.println(employees);
    }

    static class Employee {
        private final String name;
        private final String dept;
        private final Integer age;
        private final Double salary;

        public Employee(String name, String dept, Integer age, Double salary) {
            this.name = name;
            this.dept = dept;
            this.salary = salary;
            this.age = age;
        }

        public static String uniqueKey(Employee employee) {
            return employee.dept + employee.salary;
        }
    }
}