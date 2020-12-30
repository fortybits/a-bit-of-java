package edu.bit;

import java.util.*;

public class Basics {
    public static class CustomStackImpl<T> {
        private StackNode<T> top;
        // Nested class for StackNode

        public CustomStackImpl() {

        }

        public T pop() {
            if (top == null) throw new EmptyStackException();
            T item = top.data;
            top = top.next;
            return item;
        }

        public void push(T item) {
            StackNode<T> t = new StackNode<T>(item);
            t.next = top;
            top = t;
        }

        public T peek() {
            if (top == null) throw new EmptyStackException();
            return top.data;
        }

        public boolean isEmpty() {
            return top == null;
        }

        private static class StackNode<T> {
            private T data;
            private StackNode<T> next;

            public StackNode(T data) {
                this.data = data;
            }
        }
    }

    public void trimVersusStripOfString() {
        String emptyString = "";
        System.out.println(emptyString.strip());
        System.out.println(emptyString.trim());
        String whiteSpace = " 123 456 ";
        System.out.println(whiteSpace.trim().length());
        System.out.println(whiteSpace.strip().length());

        System.out.println("".strip());
        System.out.println("  both  ".strip());
        System.out.println("  some com.stackoverflow.nullpointer.string with whitespace  \n \t leading character  ".strip());
        System.out.println("  some com.stackoverflow.nullpointer.string with whitespace  \n \t leading character  ".trim());
        System.out.println("trailing  ".strip());
        System.out.println("trailing  ".trim());
    }

    public void repeatStrings() {
        //UseCase1
        String name = "stackoverflow";
        name += name.repeat(2);
        System.out.println(name);

        //UseCase2
        String str = "do";
        String res = str + str.repeat(3);
        System.out.println(res);

        //UseCase3
        int i = 3;
        String ch = "0";
        String someNum = "123";
        someNum = someNum + ch.repeat(i);
        System.out.println(someNum);
    }

    public static class StackOverflowTagConsumer {

        public static void main(String[] args) {
            List<Question> currentQuestions = new ArrayList<>();

            // if some action to sort by votes
            displaySortedByVotes(currentQuestions);

            // if another action to sort by newest
            displaySortedByNewest(currentQuestions);
        }

        private static void displaySortedByVotes(List<Question> currentQuestions) {
            System.out.println(StackOverflowTag.sortByNewest(currentQuestions));
        }

        private static void displaySortedByNewest(List<Question> currentQuestions) {
            System.out.println(StackOverflowTag.sortByNewest(currentQuestions));
        }
    }

    public static interface StackOverflowTag {

        static List<Question> sortByNewest(List<Question> questions) {
            return sortBy("NEWEST", questions);
        }

        static List<Question> sortByVotes(List<Question> questions) {
            return sortBy("VOTE", questions);
        }

        //... other sortBy methods

        static List<Question> sortBy(String sortByType, List<Question> questions) {
            if (sortByType.equals("VOTE")) {
                // sort by votes
            }
            if (sortByType.equals("NEWEST")) {
                // sort using the created timestamp
            }
            return questions;
        }
    }

    public static class Question {
        int votes;
        long created;
    }
}
