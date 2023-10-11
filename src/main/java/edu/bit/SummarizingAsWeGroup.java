package edu.bit;

import java.util.*;
import java.util.stream.Collectors;

public class SummarizingAsWeGroup {
    public static void main(String[] args) {
        List<SqlResult> listSqlResult = new ArrayList<>();
        listSqlResult.add(new SqlResult("a1", "b1", 123));
        listSqlResult.add(new SqlResult("a1", "b1", 10));
        listSqlResult.add(new SqlResult("a1", "b1", 23));
        listSqlResult.add(new SqlResult("a1", "b2", 3));
        listSqlResult.add(new SqlResult("a1", "b2", 73));
        listSqlResult.add(new SqlResult("a1", "b2", 15));
        listSqlResult.add(new SqlResult("a2", "b1", 43));
        listSqlResult.add(new SqlResult("a2", "b1", 19));
        listSqlResult.add(new SqlResult("a2", "b1", 15));
        listSqlResult.add(new SqlResult("a2", "b2", 38));
        listSqlResult.add(new SqlResult("a2", "b2", 73));
        listSqlResult.add(new SqlResult("a2", "b2", 15));

        Map<List<String>, DoubleSummaryStatistics> groupedStatistics = listSqlResult.stream()
                .collect(Collectors.groupingBy(sr -> Arrays.asList(sr.getKey1(), sr.getKey2()),
                        Collectors.summarizingDouble(SqlResult::getVal)));

        List<Result> results = groupedStatistics.entrySet().stream()
                .map(e -> new Result(e.getKey().get(0), e.getKey().get(1),
                        e.getValue().getAverage(), e.getValue().getCount()))
                .collect(Collectors.toList());
        System.out.println(results);

        List<Student> students = Arrays.asList(
                new Student("Arun", "ECE", new SubjectMarks(12, 34, 56)),
                new Student("JIM", "ECE", new SubjectMarks(20, 40, 56)),
                new Student("ROSE", "CSE", new SubjectMarks(30, 34, 45)),
                new Student("Mary", "CSE", new SubjectMarks(40, 34, 23))
        );
        summarise(students);
    }

    static void summarise(List<Student> stList) {
        Map<String, LinkedHashMap<String, Double>> branchStudentsSortedOnMarks = stList.stream()
                .sorted(Comparator.comparingDouble(s -> s.getSubject().getAverageMarks()))
                .collect(Collectors.groupingBy(Student::getBranch, LinkedHashMap::new,
                        Collectors.collectingAndThen(
                                Collectors.toMap(Student::getFirstName,
                                        s -> s.getSubject().getAverageMarks(), Double::max),
                                m -> m.entrySet().stream()
                                        .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                                        .collect(Collectors.toMap(Map.Entry::getKey,
                                                Map.Entry::getValue, (a, b) -> b, LinkedHashMap::new))
                        )));
        System.out.println(branchStudentsSortedOnMarks);
    }

    static class SqlResult {
        private final String key1;
        private final String key2;
        private final int val;

        public SqlResult(String key1, String key2, int val) {
            this.key1 = key1;
            this.key2 = key2;
            this.val = val;
        }

        public String getKey1() {
            return key1;
        }

        public String getKey2() {
            return key2;
        }

        public int getVal() {
            return val;
        }
    }

    static class Result {
        private final String key1;
        private final String key2;
        private final Double avgVal;
        private final Long count;

        public Result(String key1, String key2, Double avgVal, Long count) {
            this.key1 = key1;
            this.key2 = key2;
            this.avgVal = avgVal;
            this.count = count;
        }
    }

    static class Student {
        private final String firstName;
        private final String branch;
        private final SubjectMarks subject;

        public Student(String firstName, String branch, SubjectMarks subject) {
            this.firstName = firstName;
            this.branch = branch;
            this.subject = subject;
        }

        public SubjectMarks getSubject() {
            return subject;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getBranch() {
            return branch;
        }
    }

    static class SubjectMarks {
        int maths, biology, computers;

        public SubjectMarks(int maths, int biology, int computers) {
            this.maths = maths;
            this.biology = biology;
            this.computers = computers;

        }

        public int getMaths() {
            return maths;
        }

        public int getBiology() {
            return biology;
        }

        public int getComputers() {
            return computers;
        }

        public double getAverageMarks() {
            return (getBiology() + getMaths() + getComputers()) / 3;
        }
    }
}