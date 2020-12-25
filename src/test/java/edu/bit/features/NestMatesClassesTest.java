package edu.bit.features;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class NestMatesClassesTest {

    @Test
    void testAClassIsItselfANestMate() {
        Assertions.assertTrue(NestMates.Entity.class.isNestmateOf(NestMates.Entity.class));
    }

    @Test
    void testInnerClassIsANestMateOfEnclosingClass() {
        Assertions.assertTrue(NestMates.Entity.class.isNestmateOf(NestMates.Entity.class));
        Assertions.assertTrue(NestMates.Entity.class.isNestmateOf(NestMates.Entity.InnerEntity.class));
        Assertions.assertTrue(NestMates.Entity.class.isNestmateOf(NestMates.Entity.AnotherInnerEntity.class));
    }

    @Test
    void testInnerClassesOfSameClassAreNestMates() {
        Assertions.assertTrue(NestMates.Entity.InnerEntity.class.isNestmateOf(NestMates.Entity.AnotherInnerEntity.class));
    }
}