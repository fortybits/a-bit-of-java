package edu.bit.__dump;

public interface Calculator {
    default int methodA(int a, int b) {
        return a - b;
    }
}
