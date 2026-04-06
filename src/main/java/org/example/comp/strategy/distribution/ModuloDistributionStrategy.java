package org.example.comp.strategy.distribution;

public class ModuloDistributionStrategy<K> implements DistributionStrategy<K> {
    @Override
    public int selectNodeIndex(K key, int numberOfNodes) {
        if (numberOfNodes <= 0) {
            throw new IllegalArgumentException("numberOfNodes must be greater than 0");
        }

        int hash = key == null ? 0 : key.hashCode();
        return Math.floorMod(hash, numberOfNodes);
    }
}
