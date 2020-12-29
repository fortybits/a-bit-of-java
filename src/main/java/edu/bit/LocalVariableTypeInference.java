package edu.bit;

import java.util.*;

public class LocalVariableTypeInference {

    // type inference could be helpful with the mixing if the interface implementations and their types
    public static void intersectionWithTypeInference() {
        var duck = (Quacks & Waddles) Mixing::create;
        duck.quack();
        duck.waddle();
    }

    //An alternate implementation to the intersection type
    public static <Duck extends Quacks & Waddles> void alternateIntersectionExample(Duck duck) {
        duck.quack();
        duck.waddle();
        duck.__noop__();
    }

    // the type of the generic used cannot be inferred without an additional hint
    // relevant issue https://stackoverflow.com/questions/49616434
    // discussion in detail over this with Brian https://chat.stackoverflow.com/rooms/168213/
    public static void contravarianceInGenericTypeInference() {
        distinct(List.of(1, 2, 3), Comparator.naturalOrder());
        distinctWithExplicitType(List.of(1, 2, 3), Comparator.naturalOrder());
        System.out.println("Done");
    }

    private static <T> Set<T> distinct(Collection<? extends T> list, Comparator<? super T> comparator) {
        Set<T> set = new TreeSet<>(comparator);
        set.addAll(list);
        return set;
    }

    private static <T> Set<T> distinctWithExplicitType(Collection<? extends T> list, Comparator<? super T> comparator) {
        var set = new TreeSet<T>(comparator);
        set.addAll(list);
        return set;
    }

    public static void rhsInference(String[] args) {
        var list = List.of(1, 3, 2);
//        var list = List.of(1, 3, "three");

        var anonymous = new ArrayList<>() {
        };

        ArrayList arrayList = new ArrayList<>();

        var varList = new ArrayList<>();

//        var mix = new ArrayList<>(List.of("2",3));
//        System.out.println(mix);

        var list1 = new ArrayList<>();
//        List<Number> numberList = list1;
//        System.out.println(numberList);

        ArrayList list2 = new ArrayList<>();
        List<Number> numberList1 = list2;
        System.out.println(numberList1);

    }

    // anonymous classes can be used with inference easily
    public void anonymousClassTypeInference() {
        var o = new Object() {
            void m() {
                System.out.println("m");
            }

            void n() {
                System.out.println("n");
            }
        };
        o.m();
        o.n();
    }

    // another case of inference for the local variable could be for the iniherited classes
    public void typeInferenceWithInheritance() {
        var myA = new A();
        myA.someMethod();
        myA = new B();
        myA.someMethod();
    }

    // Readability shouldn't be compromised. Keyword should be used in local scope
    public void localInferencesUseCases() {
        Integer x = 150;
        System.out.println(x);
        var list = List.of('A', 'B', 'C');
        var stream = list.stream();
        var optional = stream.max(Comparator.naturalOrder());
        System.out.println(list.getClass());
        System.out.println(stream.getClass());
        System.out.println(optional.getClass());
    }

    interface Quacks extends Mixing {
        default void quack() {
            System.out.println("Quack");
        }
    }

    interface Waddles extends Mixing {
        default void waddle() {
            System.out.println("Waddle");
        }
    }

    interface Mixing {
        static void create() {
        }

        void __noop__();
    }

    public static class A {
        public void someMethod() {
            System.out.println("A!");
        }
    }

    public static class B extends A {
        @Override
        public void someMethod() {
            System.out.println("B that extends A!");
        }
    }
}
