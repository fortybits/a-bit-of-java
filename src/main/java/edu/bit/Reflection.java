package edu.bit;

import sun.misc.Unsafe;

import java.lang.annotation.*;
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

    // the UNSAFE
    public static class CompareAndSwap {

        static Unsafe UNSAFE = getUnsafe();

        public static void main(String[] args) throws NoSuchFieldException, SecurityException {

            Holder h = new Holder(33);
            Class<?> holderClass = Holder.class;
            long valueOffset = UNSAFE.objectFieldOffset(holderClass.getDeclaredField("value"));
            int result = 0;
            for (int i = 0; i < 30_000; ++i) {
                result = strong(h, valueOffset);
            }
            System.out.println(result);

        }

        private static int strong(Holder h, long offset) {
            int sum = 0;
            for (int i = 33; i < 11_000; ++i) {
                boolean result = UNSAFE.compareAndSwapInt(h, offset, i, i + 1);
                if (!result) {
                    sum++;
                }
            }
            return sum;
        }

        private static Unsafe getUnsafe() {
            try {
                Field f = Unsafe.class.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                return (Unsafe) f.get(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        record Holder(int value) {
        }
    }

    // Inner annotations and their purpose
    // https://stackoverflow.com/questions/65614890/what-is-the-purpose-of-having-annotation-tmesis-inside-java-qualified-types
    static class My {
        static class Builder {
            public My build() {
                return new My();
            }
        }
    }

    @Target({ElementType.METHOD, ElementType.TYPE_USE})
    public @interface NotNull {
    }

    @Target({ElementType.METHOD, ElementType.TYPE_USE})
    public @interface Other {
    }

    public static @NotNull @Other My.@NotNull @Other Builder createBuilder() {
        return new My.Builder();
    }
}