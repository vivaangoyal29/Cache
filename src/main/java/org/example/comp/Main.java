package org.example.comp;

import org.example.comp.config.CacheConfig;
import org.example.comp.core.DistributedCache;
import org.example.comp.database.InMemoryDatabase;
import org.example.comp.strategy.distribution.ModuloDistributionStrategy;
import org.example.comp.strategy.eviction.LRUEvictionPolicy;

public class Main {
    public static void main(String[] args) {
        InMemoryDatabase<Integer, String> database = new InMemoryDatabase<>();
        database.put(1, "db-value-1");
        database.put(2, "db-value-2");

        CacheConfig config = new CacheConfig(3, 2, true);
        DistributedCache<Integer, String> cache = DistributedCache.create(
                config,
                new ModuloDistributionStrategy<>(),
                LRUEvictionPolicy::new,
                database
        );

        System.out.println("Node count: " + cache.getNodeCount());
        System.out.println("get(1) miss then DB load -> " + cache.get(1));

        System.out.println("get(1) cache hit -> " + cache.get(1));

        cache.put(10, "manual-value-10");
        System.out.println("get(10) after put -> " + cache.get(10));

        System.out.println("key 10 goes to node index " + cache.getTargetNodeIndex(10));
        System.out.println("key 11 goes to node index " + cache.getTargetNodeIndex(11));
    }
}
