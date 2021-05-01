package edu.bit;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class RegistrySetUp {
    private static final ScheduledExecutorService scheduler1 = Executors.newScheduledThreadPool(1);
    private static final ScheduledExecutorService scheduler2 = Executors.newScheduledThreadPool(1);
    private static final ScheduledExecutorService updater = Executors.newScheduledThreadPool(1);

    private static Map<String, FC> registry1 = new ConcurrentHashMap<>();
    private static Map<String, FC> registry2 = new ConcurrentHashMap<>();
    private static List<FC> fcs = new ArrayList<>();

    static long calls = 0;
    static long misses1 = 0;
    static long misses2 = 0;

    record FC(String id) {
    }

    public static void main(String[] args) {
        fcs = IntStream.range(0, 1000)
                .mapToObj(i -> new FC(String.valueOf(i)))
                .collect(Collectors.toList());

        RegistrySetUp registrySetUp = new RegistrySetUp();
        registrySetUp.updateRegistry();

        long startTime = System.nanoTime();
        Random random = new Random();
        while ((System.nanoTime() - startTime) < TimeUnit.MINUTES.toNanos(7)) {
            getFC1(String.valueOf(random.nextInt(1000)));
            getFC2(String.valueOf(random.nextInt(1000)));
            calls++;
        }

        System.out.println("Calls : " + calls);
        System.out.println("Misses 1 : " + misses1);
        System.out.println("Misses 2 : " + misses2);
    }

    private void updateRegistry() {
        scheduler1.scheduleAtFixedRate(this::loadRegistry,
                1, 1, TimeUnit.MINUTES);
        scheduler2.scheduleAtFixedRate(this::loadRegistryExisting,
                1, 1, TimeUnit.MINUTES);
        updater.scheduleAtFixedRate(this::updatedFcs,
                2, 3, TimeUnit.MINUTES);
    }

    private synchronized void loadRegistryExisting() {
        List<FC> featuredCollections = fcs;
        Map<String, FC> register = new HashMap<>();
        for (FC f : featuredCollections) {
            register.put(f.id(), f);
        }
        registry1 = register;
    }

    private synchronized void loadRegistry() {
        List<FC> featuredCollections = fcs;
        for (FC f : featuredCollections) {
            registry2.putIfAbsent(f.id(), f);
        }
    }

    private void updatedFcs() {
        int currentSize = fcs.size();
        fcs.add(new FC(String.valueOf(currentSize + 1)));
    }

    public static Optional<FC> getFC1(String id) {
        if (registry1.get(id) == null) {
            System.out.println(id);
            misses1++;
        }
        return Optional.ofNullable(registry1.get(id));
    }

    public static Optional<FC> getFC2(String id) {
        if (registry2.get(id) == null) {
            System.out.println(id);
            misses2++;
        }
        return Optional.ofNullable(registry2.get(id));
    }
}
