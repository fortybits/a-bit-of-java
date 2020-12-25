package edu.bit.features.records;

public class LocalExample {
    public static void main(String[] args) {
        record Range() {
            Range {
            }
        }
        new Range();
    }
}