package edu.bit;

import java.lang.reflect.Field;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;

/**
 * These are the changes brought with Java-11 for providing Class a pattern for the nest based access control
 * The JEP highlighting this is documented at https://openjdk.java.net/jeps/181
 */
public class NestMates {

    // These classes effectively are nest-mates of each other for existing within the same package
    record Entity(String name) {
        public record InnerEntity(String detail) {
        }

        public record AnotherInnerEntity(String quality) {
        }
    }

    public static class BreakIteratorSample {

        public void breakIteratorWithAccessToNestMate() throws Exception {
            String text = "This is a test";
            List<String> words = new ArrayList<>();
            BreakIterator breakIterator = BreakIterator.getWordInstance();

            breakIterator.setText(text);

            int lastIndex = breakIterator.first();

            while (BreakIterator.DONE != lastIndex) {
                int firstIndex = lastIndex;
                lastIndex = breakIterator.next();
                if (lastIndex != BreakIterator.DONE) {
                    String t = text.substring(firstIndex, lastIndex);
                    words.add(t);
                }
            }
            System.out.println(words.toString());

            new NestOne().f();
        }

        public static class NestOne {

            private int varNestOne;

            public void f() throws Exception {
                final NestTwo nestTwo = new NestTwo();
                // this is ok
                nestTwo.varNest2 = 2;

                // this is not ok
                final Field declaredField = NestTwo.class.getDeclaredField("varNest2");
                declaredField.setInt(nestTwo, 2);
            }
        }

        public static class NestTwo {
            private int varNest2;
        }
    }
}