package edu.bit;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class ReflectionTest {

    @Test
    void testAnnotationParsingUsingReflection() throws NoSuchFieldException {
        Assertions.assertEquals("jedi", Reflection.AnnotationParsingUsingReflection.Jedi.class
                .getAnnotation(Reflection.AnnotationParsingUsingReflection.Table.class).name());
        Assertions.assertEquals("attack_type", Reflection.AnnotationParsingUsingReflection.Jedi.class.getField("attackType")
                .getAnnotation(Reflection.AnnotationParsingUsingReflection.Column.class).name());
    }

    static class Foo extends Bar {
    }

    abstract static class Bar {
    }

    interface ServiceA<S> {
        S getType();
    }

    interface ServiceB {

        @Deprecated
        Bar getType();
    }

    static class ServiceImpl implements ServiceX {
        @Override
        public Foo getType() {
            return null;
        }
    }

    interface ServiceX extends ServiceA<Foo>, ServiceB {
    }

    @Test
    void testGettingTheMostSpecificMethod() {
        ServiceImpl service = new ServiceImpl();
        for (Method method : service.getClass().getMethods()) {
            for (Class<?> anInterface : method.getDeclaringClass().getInterfaces()) {
                try {
                    Method interpretedMethod = anInterface.getMethod(method.getName(), method.getParameterTypes());
                    System.out.println("interpretedMethod = " + interpretedMethod);
                    Assertions.assertEquals(ServiceB.class, interpretedMethod.getDeclaringClass());
                    Assertions.assertEquals(Bar.class, interpretedMethod.getReturnType());
                } catch (NoSuchMethodException e) {
                }
            }
        }
    }
}