package edu.bit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InheritanceTest {

    @Test
    void basicInheritanceWithEqualInstancesCreation() {
        String kind = "kind";
        String appearance = "appearance";
        Inheritance.Animal animal = new Inheritance.AmericanRobin(kind, appearance);
        Inheritance.Bird bird = new Inheritance.AmericanRobin(kind, appearance);
        Inheritance.AmericanRobin americanRobin = new Inheritance.AmericanRobin(kind, appearance);
        Assertions.assertEquals(animal, bird);
        Assertions.assertEquals(bird, americanRobin);
        Assertions.assertEquals(bird, animal);
    }

}