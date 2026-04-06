package org.example.comp.strategy.eviction;

import java.util.LinkedHashMap;
import java.util.Optional;

public class LRUEvictionPolicy<K> implements EvictionPolicy<K> {
    private final LinkedHashMap<K, Boolean> accessOrderMap = new LinkedHashMap<>(16, 0.75f, true);

    @Override
    public synchronized void recordRead(K key) {
        accessOrderMap.put(key, Boolean.TRUE);
    }

    @Override
    public synchronized void recordWrite(K key) {
        accessOrderMap.put(key, Boolean.TRUE);
    }

    @Override
    public synchronized void remove(K key) {
        accessOrderMap.remove(key);
    }

    @Override
    public synchronized Optional<K> evictCandidate() {
        if (accessOrderMap.isEmpty()) {
            return Optional.empty();
        }

        K oldestKey = accessOrderMap.entrySet().iterator().next().getKey();
        accessOrderMap.remove(oldestKey);
        return Optional.of(oldestKey);
    }
}
