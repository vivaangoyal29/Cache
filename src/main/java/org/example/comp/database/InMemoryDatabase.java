package org.example.comp.database;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryDatabase<K, V> implements Database<K, V> {
    private final Map<K, V> storage = new ConcurrentHashMap<>();

    @Override
    public V get(K key) {
        return storage.get(key);
    }

    @Override
    public void put(K key, V value) {
        storage.put(key, value);
    }
}
