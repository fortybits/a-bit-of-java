package edu.bit;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RegistrySetUp {

    static class Document {
        String id;

        Document(String id) {
            this.id = id;
        }
    }

    private static final ScheduledExecutorService loadFirstDocuments = Executors.newScheduledThreadPool(1);
    private static final ScheduledExecutorService loadSecondDocuments = Executors.newScheduledThreadPool(1);
    private static final ScheduledExecutorService updateDocuments = Executors.newScheduledThreadPool(1);

    private static Map<String, Document> firstRegistry = new ConcurrentHashMap<>();
    private static Map<String, Document> secondRegistry = new ConcurrentHashMap<>();
    private static List<Document> documents = new ArrayList<>();

    public static final int TOTAL_DOCUMENTS = 1000;
    static long calls;
    static long firstMiss;
    static long secondMiss;


    public static void main(String[] args) {
        documents = IntStream.range(0, TOTAL_DOCUMENTS)
                .mapToObj(i -> new Document(String.valueOf(i)))
                .collect(Collectors.toList());
        refreshFirstDocuments();
        refreshSecondDocuments();

        updateDocuments.scheduleAtFixedRate(RegistrySetUp::updatedDocuments,
                2, 3, TimeUnit.MINUTES);
        loadFirstDocuments.scheduleAtFixedRate(RegistrySetUp::refreshSecondDocuments,
                1, 1, TimeUnit.MINUTES);
        loadSecondDocuments.scheduleAtFixedRate(RegistrySetUp::refreshFirstDocuments,
                1, 1, TimeUnit.MINUTES);

        performRoutineCalls();
        System.out.println("Calls : " + calls);
        System.out.println("Misses 1 : " + firstMiss);
        System.out.println("Misses 2 : " + secondMiss);
    }

    private static void performRoutineCalls() {
        long startTime = System.nanoTime();
        var random = new Random();
        while ((System.nanoTime() - startTime) < TimeUnit.MINUTES.toNanos(7)) {
            lookUpFirst(String.valueOf(random.nextInt(TOTAL_DOCUMENTS)));
            lookUpSecond(String.valueOf(random.nextInt(TOTAL_DOCUMENTS)));
            calls++;
        }
    }

    private static synchronized void refreshFirstDocuments() {
        List<Document> featuredCollections = documents;
        Map<String, Document> register = new HashMap<>();
        for (Document f : featuredCollections) {
            register.put(f.id, f);
        }
        firstRegistry = register;
    }

    private static void refreshSecondDocuments() {
        List<Document> featuredCollections = documents;
        for (Document f : featuredCollections) {
            secondRegistry.putIfAbsent(f.id, f);
        }
    }

    private static void updatedDocuments() {
        int currentSize = documents.size();
        documents.add(new Document(String.valueOf(currentSize + 1)));
    }

    public static Optional<Document> lookUpFirst(String id) {
        if (firstRegistry.get(id) == null) {
            System.out.println("First " + id);
            firstMiss++;
        }
        return Optional.ofNullable(firstRegistry.get(id));
    }

    public static Optional<Document> lookUpSecond(String id) {
        if (secondRegistry.get(id) == null) {
            System.out.println("Second " + id);
            secondMiss++;
        }
        return Optional.ofNullable(secondRegistry.get(id));
    }
}
