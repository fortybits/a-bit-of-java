package edu.bit.__dump;

public interface SubInt extends SuperInt {
    @Override
    default void method3() {  //  compile time error
        System.out.println("Inside SubInt");
    }
}