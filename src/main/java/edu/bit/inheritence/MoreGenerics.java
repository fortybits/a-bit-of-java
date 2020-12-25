package edu.bit.inheritence;

import java.util.HashMap;
import java.util.Map;

public class MoreGenerics {

    private <T extends Enum<T> & InterfaceA> MoreGenerics(Class<T> type) {
        Map<String, Class<T>> filter = new HashMap<>();
        filter.put("a", type);
        importSettingViaEnum(filter.get("a"));
    }

    public static void main(String[] args) {
        new MoreGenerics(EnumA.class);
    }

    private static void f(int ordinal) {
        System.out.println("f");
    }

    private <T extends Enum<T> & InterfaceA> void importSettingViaEnum(Class<? extends T> clazz) {
        for (T elem : clazz.getEnumConstants()) {
            f(elem.ordinal());
            elem.foo();
        }
    }

    public enum EnumA implements InterfaceA {
        RED();

        public Object[] foo() {
            System.out.println("g");
            return null;
        }
    }

    public enum EnumB implements InterfaceA {
        OAK();

        public Object[] foo() {
            System.out.println("g");
            return null;
        }
    }

    public interface InterfaceA {
        Object[] foo();
    }

}