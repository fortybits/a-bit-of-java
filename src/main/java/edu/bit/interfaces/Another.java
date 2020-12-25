package edu.bit.interfaces;

public interface Another<T> {

    default T someMethod() {
        return null;
    }
}
