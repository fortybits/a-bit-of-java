package edu.bit;

import java.util.*;
import java.util.stream.Collectors;

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

    //
    public void postAndPreIncrementOperators() {
        int num1 = 7;
        int num2 = 7;

        if (num1 > num2 && num1++ > ++num2) {
            num1++;
        }

        if (++num2 > num1 || num1++ == num2++) {
            num1++;
        }

        System.out.println(num1 + ":" + num2);
    }

    public void preAndPostIncrementOpsWithStream() {
        Integer[] seq = {1, 3, 4, 1, 8, 11};
        List<Integer> seqFilteredPre = Arrays.stream(seq)
                .filter(i -> i % 2 != 0)
                .map(i -> ++i)
                .collect(Collectors.toList());
        System.out.println(seqFilteredPre);

        List<Integer> seqFilteredPost = Arrays.stream(seq)
                .filter(i -> i % 2 != 0)
                .map(i -> i++)
                .collect(Collectors.toList());
        System.out.println(seqFilteredPost);
    }

    public void priorityQSample() {
        PriorityQueue<Grade> pq = new PriorityQueue<>();
        pq.add(new Grade("Python", 2));
        pq.add(new Grade("C++", 3));
        pq.add(new Grade("JAVA", 1));
        pq.forEach(System.out::println); // way to iterate on PQ is not to use 'iterator'
    }

    static class Grade implements Comparable<Grade> {
        private String subject;
        private int grade;

        Grade(String subject, int grade) {
            this.subject = subject;
            this.grade = grade;
        }

        public int compareTo(Grade g) {
            return this.grade - g.grade;
        }

        public String toString() {
            return "sub : " + subject + " -> " + "grade : " + grade;
        }
    }

    //
    public void randomMethodInvocation() {
        //using runnable
        List<Runnable> runnableList = Arrays.asList(this::meth1, this::meth2,
                this::meth3, this::meth4, this::meth5);
        Collections.shuffle(runnableList);
        runnableList.iterator().next().run();

        // using switch
        int randomNumber = new Random().nextInt(5);
        switch (randomNumber) {
            case 0 -> meth1();
            case 1 -> meth2();
            case 2 -> meth3();
            case 3 -> meth4();
            case 4 -> meth5();
            default -> throw new IllegalArgumentException();
        }
    }

    public void meth1() {
        System.out.println("one");
    }

    public void meth2() {
        System.out.println("two");
    }

    public void meth3() {
        System.out.println("three");
    }

    public void meth4() {
        System.out.println("four");
    }

    public void meth5() {
        System.out.println("five");
    }

    // custom implementation for priority queue with only two elements
    public class PairPriorityQueue<E> {
        private final PriorityQueue<E> priorityQueue;
        private final Comparator<? super E> comparator;

        public PairPriorityQueue(final Comparator<? super E> comparator) {
            super();
            if (comparator == null) {
                throw new NullPointerException("Comparator is null.");
            }
            this.priorityQueue = new PriorityQueue<>(1, comparator);
            this.comparator = comparator;
        }

        public void add(final E e) {
            if (e == null) {
                throw new NullPointerException("e is null.");
            }
            if (priorityQueue.size() >= 2) {
                final E firstElm = priorityQueue.peek();
                if (comparator.compare(e, firstElm) < 1) {
                    return;
                } else {
                    priorityQueue.poll();
                }
            }
            priorityQueue.add(e);
        }

        public PriorityQueue<E> get() {
            return this.priorityQueue;
        }
    }

    //
    private static void printingMethodDetails() {
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().getId());
        System.out.println(Thread.currentThread().getStackTrace().length);
        StackTraceElement[] details = Thread.currentThread().getStackTrace();
        for (int i = 0; i < Thread.currentThread().getStackTrace().length; i++) {
            System.out.println(details[i].getMethodName());
            System.out.println(details[i].getClassName());
            System.out.println(details[i].getLineNumber());
            System.out.println(details[i].getFileName()); //Returns the class file name of called
        }
    }
}
