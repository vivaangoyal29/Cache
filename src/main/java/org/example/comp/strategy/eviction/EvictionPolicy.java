package org.example.comp.strategy.eviction;

import java.util.Optional;

public interface EvictionPolicy<K> {
    void recordRead(K key);

    void recordWrite(K key);

    void remove(K key);

    Optional<K> evictCandidate();
}
