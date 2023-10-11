package edu.bit;

import java.io.Serializable;

public class Sealed {

    public void sealedExperiments() {
        Integral integral = new Integral();
        integral.bar();
        integral.foo();
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

    public sealed class A permits B {
    }

    public final class B extends A implements Serializable {
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

}