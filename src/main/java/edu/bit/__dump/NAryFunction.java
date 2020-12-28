package edu.bit.__dump;

@FunctionalInterface
public interface NAryFunction<T, R> {
    R apply(T... t);
}