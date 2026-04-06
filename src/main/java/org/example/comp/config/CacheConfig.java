package org.example.comp.config;

public class CacheConfig {
    private final int nodeCount;
    private final int nodeCapacity;
    private final boolean writeThrough;

    public CacheConfig(int nodeCount, int nodeCapacity, boolean writeThrough) {
        if (nodeCount <= 0) {
            throw new IllegalArgumentException("nodeCount must be greater than 0");
        }
        if (nodeCapacity <= 0) {
            throw new IllegalArgumentException("nodeCapacity must be greater than 0");
        }

        this.nodeCount = nodeCount;
        this.nodeCapacity = nodeCapacity;
        this.writeThrough = writeThrough;
    }

    public int getNodeCount() {
        return nodeCount;
    }

    public int getNodeCapacity() {
        return nodeCapacity;
    }

    public boolean isWriteThrough() {
        return writeThrough;
    }
}
