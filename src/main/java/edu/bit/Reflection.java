package edu.bit;

import sun.misc.Unsafe;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;

public class Reflection {
    public static class AnnotatedElementImpl implements AnnotatedElement {
        @Override
        public boolean isAnnotationPresent(Class<? extends Annotation> annotationClass) {
            return false;
        }

        @Override
        public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
            return null;
        }

        @Override
        public Annotation[] getAnnotations() {
            return new Annotation[0];
        }

        @Override
        public <T extends Annotation> T[] getAnnotationsByType(Class<T> annotationClass) {
            return null;
        }

        @Override
        public <T extends Annotation> T getDeclaredAnnotation(Class<T> annotationClass) {
            return null;
        }

        @Override
        public <T extends Annotation> T[] getDeclaredAnnotationsByType(Class<T> annotationClass) {
            return null;
        }

        @Override
        public Annotation[] getDeclaredAnnotations() {
            return new Annotation[0];
        }
    }

    public static class AnnotationParsingUsingReflection {
        @Retention(RetentionPolicy.RUNTIME)
        @interface Table {
            String name();
        }

        @Retention(RetentionPolicy.RUNTIME)
        @interface Column {
            String name();
        }

        @Table(name = "jedi")
        static class Jedi {
            @Column(name = "attack_type")
            public String attackType; // not accessible unless public

            public String getAttackType() {
                return attackType;
            }
        }
    }

    // the particular use cases of accessing a JVM argument to toggle its value makes use of Unsafe
    public void disableWarningAccessingJVMArgs() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            Unsafe u = (Unsafe) theUnsafe.get(null);

            Class cls = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field logger = cls.getDeclaredField("logger");
            u.putObjectVolatile(cls, u.staticFieldOffset(logger), null);
        } catch (Exception e) {
            // ignore
        }
    }


    public interface Calculator {
        default int methodA(int a, int b) {
            return a - b;
        }
    }

    public static class CalculatorInvocation implements InvocationHandler {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return MethodHandles.lookup()
                    .in(method.getDeclaringClass())
                    .unreflectSpecial(method, method.getDeclaringClass())
                    .bindTo(this)
                    .invokeWithArguments();
        }

        public void invokeCalculationMethods() {
            InvocationHandler invocationHandler = new CalculatorInvocation();
            Calculator c = (Calculator) Proxy.newProxyInstance(Calculator.class.getClassLoader(),
                    new Class[]{Calculator.class}, invocationHandler);
            System.out.println(c.methodA(1, 3));
        }
    }
}