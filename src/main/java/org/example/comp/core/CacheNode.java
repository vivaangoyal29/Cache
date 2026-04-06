package org.example.comp.core;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.example.comp.strategy.eviction.EvictionPolicy;

public class CacheNode<K, V> {
    private final int capacity;
    private final Map<K, V> entries = new ConcurrentHashMap<>();
    private final EvictionPolicy<K> evictionPolicy;

    public CacheNode(int capacity, EvictionPolicy<K> evictionPolicy) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be greater than 0");
        }
        this.capacity = capacity;
        this.evictionPolicy = evictionPolicy;
    }

    public V get(K key) {
        V value = entries.get(key);
        if (value != null) {
            evictionPolicy.recordRead(key);
        }
        return value;
    }

    public void put(K key, V value) {
        synchronized (this) {
            boolean isNewKey = !entries.containsKey(key);
            if (isNewKey && entries.size() >= capacity) {
                Optional<K> victim = evictionPolicy.evictCandidate();
                victim.ifPresent(entries::remove);
            }

            entries.put(key, value);
            evictionPolicy.recordWrite(key);
        }
    }

    public void remove(K key) {
        entries.remove(key);
        evictionPolicy.remove(key);
    }

    public int size() {
        return entries.size();
    }
}
