package edu.bit;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReflectionTest {

    @Test
    void testAnnotationParsingUsingReflection() throws NoSuchFieldException {
        Assertions.assertEquals("jedi", Reflection.AnnotationParsingUsingReflection.Jedi.class
                .getAnnotation(Reflection.AnnotationParsingUsingReflection.Table.class).name());
        Assertions.assertEquals("attack_type", Reflection.AnnotationParsingUsingReflection.Jedi.class.getField("attackType")
                .getAnnotation(Reflection.AnnotationParsingUsingReflection.Column.class).name());
    }
}