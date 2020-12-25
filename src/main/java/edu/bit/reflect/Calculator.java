package edu.bit.reflect;

public interface Calculator {
    default int methodA(int a, int b) {
        return a - b;
    }
}
