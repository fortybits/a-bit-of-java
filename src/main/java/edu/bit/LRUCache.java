package edu.bit;

import java.util.LinkedHashSet;
import java.util.Set;

public class LRUCache {

    private final Set<Integer> cache;
    private final int capacity;

    public LRUCache(int capacity) {
        this.cache = new LinkedHashSet<>(capacity);
        this.capacity = capacity;
    }

    // This function returns false if key is not present in cache.
    // Else it moves the key to front by first removing it and then adding it, and returns true.
    public boolean get(int key) {
        if (!cache.contains(key)) {
            return false;
        }
        cache.remove(key);
        cache.add(key);
        return true;
    }

    // Refers key x with in the LRU cache
    public void refer(int key) {
        if (!get(key)) {
            put(key);
        }
    }

    public void put(int key) {
        if (cache.size() == capacity) {
            int firstKey = cache.iterator().next();
            cache.remove(firstKey);
        }
        cache.add(key);
    }
}