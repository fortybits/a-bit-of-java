package edu.bit;

import java.io.Serializable;
import java.lang.reflect.Modifier;

public class Sealed {

    public static void main(Shape shape) {
        switch (shape) {
            case Rectangle r -> System.out.println(r);
            case Square sq -> System.out.println(sq);
            default -> System.out.println("Def");
        }
    }

    public static boolean isExplicitlyNonSealed(Class<?> clazz) {
        boolean isClassSealed = clazz.isSealed();
        boolean isSuperSealed = clazz.getSuperclass() != null && clazz.getSuperclass().isSealed();
        boolean isAnyInterfaceSealed = false;
        for (Class<?> interfaceClass : clazz.getInterfaces()) {
            if (interfaceClass.isSealed()) {
                isAnyInterfaceSealed = true;
                break;
            }
        }
        return (!isClassSealed && (isSuperSealed || isAnyInterfaceSealed)) && !Modifier.isFinal(clazz.getModifiers());
    }

    public static void main(String[] args) {
        System.out.println("isExplicitlyNonSealed(NonSealedClass.class) = " + isExplicitlyNonSealed(NonSealedClass.class));
        System.out.println("isExplicitlyNonSealed(SealedClass.class) = " + isExplicitlyNonSealed(SealedClass.class));
        System.out.println("isExplicitlyNonSealed(FinalClass.class) = " + isExplicitlyNonSealed(FinalClass.class));
        System.out.println("isExplicitlyNonSealed(OrdinaryClass.class) " + isExplicitlyNonSealed(OrdinaryClass.class));
        System.out.println("isExplicitlyNonSealed(FinalSealedClass.class) = " + isExplicitlyNonSealed(FinalSealedClass.class));
        System.out.println("isExplicitlyNonSealed(SubSealedClass.class) = " + isExplicitlyNonSealed(SubSealedClass.class));
        System.out.println("isExplicitlyNonSealed(SubSubClass.class) = " + isExplicitlyNonSealed(SubSubClass.class));
        System.out.println("isExplicitlyNonSealed(ImplementingClass.class) = " + isExplicitlyNonSealed(ImplementingClass.class));
        System.out.println("isExplicitlyNonSealed(Something.class) = " + isExplicitlyNonSealed(Something.class));
        System.out.println("isExplicitlyNonSealed(ConstantExpr.class) = " + isExplicitlyNonSealed(ConstantExpr.class));
    }

    public void sealedExperiments() {
        Integral integral = new Integral();
        integral.bar();
        integral.foo();

    }

    sealed interface Shape permits Rectangle, Square {
    }

    public sealed interface Int permits Some {
        void foo();

        void bar();
    }


    sealed interface Node {
        record ConstNode(int i) implements Node {
        }

        record NegNode(Node n) implements Node {
        }

        record AddNode(Node left, Node right) implements Node {
        }

        record MultiNode(Node left, Node right) implements Node {
        }
    }

    sealed interface Expr
            permits ConstantExpr {
        int eval();
    }

    sealed interface SealedInterface permits ImplementingClass {
    }

    record Rectangle() implements Shape {
    }

    record Square() implements Shape {
    }
//    int eval(Node n) {
//        return switch (n) {
//            case ConstNode( int i) -> i;
//            case NegNode(var node) -> -eval(node);
//            case AddNode(var left, var right) -> eval(left) + eval(right);
//            case MultiNode(var left, var right) -> eval(left) * eval(right);
//                // no default needed, Node is sealed and we covered all the cases
//        };
//    }

    static abstract sealed class Some implements Int permits Integral {
        public void foo() {
            System.out.println("sealed some");
        }
    }

    static non-sealed class Integral extends Some {

        @Override
        public void bar() {
            System.out.println("bar from non-sealed class");
        }

        @Override
        public void foo() {
            System.out.println("foo from non-sealed class");
        }

    }

    record ConstantExpr(int i) implements Expr {
        public int eval() {
            return i();
        }
    }

    public sealed class A permits B {
    }

    public final class B extends A implements Serializable {
    }

    sealed class SealedClass permits NonSealedClass, FinalSealedClass, SubSealedClass {
    }

    non-sealed class NonSealedClass extends SealedClass {
    }

    final class FinalSealedClass extends SealedClass {
    }

    sealed class SubSealedClass extends SealedClass permits SubSubClass {
    }

    non-sealed class SubSubClass extends SubSealedClass {
    }

    class Something extends SubSubClass {
    }

    final class FinalClass {
    }

    class OrdinaryClass {
    }

    non-sealed class ImplementingClass implements SealedInterface {
    }
}