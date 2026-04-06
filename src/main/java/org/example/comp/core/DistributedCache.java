package org.example.comp.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.example.comp.config.CacheConfig;
import org.example.comp.database.Database;
import org.example.comp.strategy.distribution.DistributionStrategy;
import org.example.comp.strategy.eviction.EvictionPolicy;

public class DistributedCache<K, V> {
    private final List<CacheNode<K, V>> nodes;
    private final DistributionStrategy<K> distributionStrategy;
    private final Database<K, V> database;
    private final boolean writeThrough;

    private DistributedCache(
            List<CacheNode<K, V>> nodes,
            DistributionStrategy<K> distributionStrategy,
            Database<K, V> database,
            boolean writeThrough
    ) {
        this.nodes = List.copyOf(nodes);
        this.distributionStrategy = distributionStrategy;
        this.database = database;
        this.writeThrough = writeThrough;
    }

    public static <K, V> DistributedCache<K, V> create(
            CacheConfig config,
            DistributionStrategy<K> distributionStrategy,
            Supplier<EvictionPolicy<K>> evictionPolicyFactory,
            Database<K, V> database
    ) {
        List<CacheNode<K, V>> nodes = new ArrayList<>(config.getNodeCount());
        for (int i = 0; i < config.getNodeCount(); i++) {
            nodes.add(new CacheNode<>(config.getNodeCapacity(), evictionPolicyFactory.get()));
        }

        return new DistributedCache<>(nodes, distributionStrategy, database, config.isWriteThrough());
    }

    public V get(K key) {
        CacheNode<K, V> node = resolveNode(key);
        V cachedValue = node.get(key);
        if (cachedValue != null) {
            return cachedValue;
        }

        V dbValue = database.get(key);
        if (dbValue != null) {
            node.put(key, dbValue);
        }

        return dbValue;
    }

    public void put(K key, V value) {
        CacheNode<K, V> node = resolveNode(key);
        node.put(key, value);

        if (writeThrough) {
            database.put(key, value);
        }
    }

    public int getNodeCount() {
        return nodes.size();
    }

    public int getTargetNodeIndex(K key) {
        return distributionStrategy.selectNodeIndex(key, nodes.size());
    }

    private CacheNode<K, V> resolveNode(K key) {
        int index = distributionStrategy.selectNodeIndex(key, nodes.size());
        return nodes.get(index);
    }
}
