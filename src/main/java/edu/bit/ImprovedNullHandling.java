package edu.bit;

/**
 * The improvised null handling should provide the details of the actual parameter in the
 * chain of accessors causing the null pointer exception. The document that addresses the details is :
 * JEP 358: Helpful NullPointerExceptions https://openjdk.java.net/jeps/358
 * <p>
 * Make use of -XX:+ShowCodeDetailsInExceptionMessages to make JVM analyse the program to determine
 * which reference was null and then provides the details as part of NullPointerException.getMessage()
 */
public class ImprovedNullHandling {

    record NPE(Integer stat, Reason reason) {
    }

    record Reason(String detail) {
    }
}