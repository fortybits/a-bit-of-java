package edu.bit.__dump;


/**
 * @see <href>https://stackoverflow.com/questions/48227496</href>
 * error: reference to ok is ambiguous
 * return ok(() -> System.out.append("aaa"));
 * ^
 * both method <T#1>ok(Supplier<T#1>) in Q48227496 and method <T#2>ok(Procedure) in Q48227496 match
 * where T#1,T#2 are type-variables:
 * T#1 extends Object declared in method <T#1>ok(Supplier<T#1>)
 * T#2 extends Object declared in method <T#2>ok(Procedure)
 */
public class Q48227496_FixedInJv11 {

    //uncomment the below
//    public A<?> test() {
//        return ok(() -> System.out.append("aaa"));
//    }

    private <T> A<T> ok(java.util.function.Supplier<T> action) {
        return new A<>();
    }

    public <T> A<T> ok(T body) {
        return new A<>();
    }

    private <T> A<T> ok(Procedure action) {
        return new A<>();
    }

    public <T> A<T> ok() {
        return new A<>();
    }

    @FunctionalInterface
    public interface Procedure {
        void invoke();
    }

    // <href>https://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8195598</href>
    private class A<T> {
    }
}