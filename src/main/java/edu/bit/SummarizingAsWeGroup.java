package edu.bit;

import java.util.*;
import java.util.stream.Collectors;

public class saaa {
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

        List<Result> listResult = listSqlResult.stream()
                .collect(Collectors.groupingBy(SqlResult::getKey1,
                        Collectors.groupingBy(SqlResult::getKey2)))
                .values().stream()
                .map(e -> new Result(
                        e.get(0).get(0).getKey1(),
                        e.get(0).get(0).getKey2(),
                        e.get(0).stream().mapToDouble(SqlResult::getVal).average().getAsDouble(),
                        (long) e.get(0).size()))
                .collect(Collectors.toList());

        Map<List<String>, DoubleSummaryStatistics> groupedStatistics = listSqlResult.stream()
                .collect(Collectors.groupingBy(sr -> Arrays.asList(sr.getKey1(), sr.getKey2()),
                        Collectors.summarizingDouble(SqlResult::getVal)));

        List<Result> results = groupedStatistics.entrySet().stream()
                .map(e -> new Result(e.getKey().get(0), e.getKey().get(1),
                        e.getValue().getAverage(), e.getValue().getCount()))
                .collect(Collectors.toList());
    }

    static class SqlResult {
        private String key1;
        private String key2;
        private int val;

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
        private String key1;
        private String key2;
        private Double avgVal;
        private Long count;

        public Result(String key1, String key2, Double avgVal, Long count) {
            this.key1 = key1;
            this.key2 = key2;
            this.avgVal = avgVal;
            this.count = count;
        }

        public String getKey1() {
            return key1;
        }

        public String getKey2() {
            return key2;
        }

        public Double getAvgVal() {
            return avgVal;
        }

        public Long getCount() {
            return count;
        }
    }
