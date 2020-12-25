package edu.bit.functional;

@FunctionalInterface
public interface NAryFunction<T, R> {
    R apply(T... t);
}