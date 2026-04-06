package org.example.comp.database;

public interface Database<K, V> {
    V get(K key);

    void put(K key, V value);
}
