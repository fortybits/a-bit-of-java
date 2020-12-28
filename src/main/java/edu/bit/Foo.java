package edu.bit;

import lombok.Getter;

@Getter
public class Foo {
    Bar bar;
    String fooVal;

    Foo(String fooVal) {
        this.fooVal = fooVal;
    }

    Foo(Bar bar) {
        this.bar = bar;
        System.out.println("Inside the RESTRO!");
    }
}
