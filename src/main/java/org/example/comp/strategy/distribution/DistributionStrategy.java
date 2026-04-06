package org.example.comp.strategy.distribution;

public interface DistributionStrategy<K> {
    int selectNodeIndex(K key, int numberOfNodes);
}
