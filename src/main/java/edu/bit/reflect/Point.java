package edu.bit.reflect;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

class Point {

    private static final VarHandle X;

    static {
        try {
            X = MethodHandles.lookup().findVarHandle(Point.class, "x",
                    int.class);
            System.out.println(X.getVolatile(new Point()));
        } catch (ReflectiveOperationException e) {
            throw new Error(e);
        }
    }

    volatile int x, y;

    public static void main(String[] args) {

    }
}