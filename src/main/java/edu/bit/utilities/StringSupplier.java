package edu.bit.utilities;

@FunctionalInterface
public interface StringSupplier {


    /**
     * Gets a result.
     *
     * @return a result
     */
    String getAsString();
}