package edu.bit;

/**
 * pattern matching
 * https://openjdk.java.net/jeps/305
 * https://openjdk.java.net/jeps/375
 */
public class PatternMatching {

    public static void main(String[] args) {
        // Before JEP-305
        Object obj = new Object();
        String formatted = "unknown";
        if (obj instanceof Integer) {
            int i = (Integer) obj;
            formatted = String.format("int %d", i);
        } else if (obj instanceof Byte) {
            byte b = (Byte) obj;
            formatted = String.format("byte %d", b);
        } else if (obj instanceof Long) {
            long l = (Long) obj;
            formatted = String.format("long %d", l);
        } else if (obj instanceof Double) {
            double d = (Double) obj;
            formatted = String.format("double %f", d);
        } else if (obj instanceof String s) {
            formatted = String.format("String %s", s);
        }

        // After JEP-305 and 375
        String formatting = "unknown";
        formatting = obj instanceof Float f ? String.valueOf(f.byteValue()) : "";

        if (obj instanceof Integer x) {
            formatting = String.format("int %d", x);
        } else if (obj instanceof Byte x) {
            formatting = String.format("byte %d", x);
        } else if (obj instanceof Long x) {
            formatting = String.format("long %d", x);
        } else if (obj instanceof Double x) {
            formatting = String.format("double %f", x);
        } else if (obj instanceof String x) {
            formatting = String.format("String %s", x);
        }

        // further there would be a consideration to evolve pattern matching such as
//        String format  = "unknown";
//        switch (obj) {
//            case Integer i: format = String.format("int %d", i); break;
//            case Byte b:    format = String.format("byte %d", b); break;
//            case Long l:    format = String.format("long %d", l); break;
//            case Double d:  format = String.format("double %f", d); break;
//            case String s:  format = String.format("String %s", s); break;
//            default:        format = obj.toString();
//        }
    }

    class Shape {
    }

    class Rectangle extends Shape {
    }

    class Triangle extends Shape {
        int calculateArea() {
            return 1;
        }
    }

//    static void testTriangle(Shape s) {
//        switch (s) {
//            case null:
//                break;
//            case Triangle t:
//                System.out.println("Triangle :)");
//                if (t.calculateArea() > 100) {
//                    System.out.println("Oh, Large triangle!!");
//                }
//                break;
//            default:
//                System.out.println("Non-triangle");
//        }
//    }
//
//    static void testTriangle(Shape s) {
//        switch (s) {
//            case Triangle t ->
//                System.out.println("Triangle :)"); // Q. the order matters? not really an if..else per say!!
//            case Triangle t && (t.calculateArea() > 100) ->
//                System.out.println("Oh, Large triangle!!");
//            default -> System.out.println("Non-triangle");
//        }
//    }

    /**
     *
     * The compiler checks all pattern labels. It is a compile-time error if a pattern label in a
     * switch block is dominated by an earlier pattern label in that switch block.
     * (For this purpose, case null, T t is treated as if it were case T t.)
     */

    /**
     * It is also a compile-time error if a switch block has more than one match-all switch label.
     * The two match-all switch labels are default and case null, default.
     */

    /**
     * If the type of the selector expression is a sealed class (JEP 397),
     * then the type coverage check can take into account the permits clause of the sealed class
     * to determine whether a switch block is complete.
     * To defend against incompatible separate compilation, the compiler automatically adds a default label
     * whose code throws an IncompatibleClassChangeError.
     * This label will only be reached if the sealed interface is changed and the switch code is not recompiled.
     * In effect, the compiler hardens your code for you.
     */
}