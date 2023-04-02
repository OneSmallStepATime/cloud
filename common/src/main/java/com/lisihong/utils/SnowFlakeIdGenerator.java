package com.lisihong.utils;

public record SnowFlakeIdGenerator() {
    private static final long START = 1288834974657L;
    private static final byte WORKER_ID_SHIFT = 12;
    private static final byte DATACENTER_ID_SHIFT = 17;
    private static final byte TIMESTAMP_LEFT_SHIFT = 22;
    private static final short MAX_SEQUENCE_NUM = 1 << 12;
    private static short sequence = -1;
    private static final int NODE_ID = 1 << DATACENTER_ID_SHIFT | 1 << WORKER_ID_SHIFT;

    public static synchronized long getId() {
        ++sequence;
        if (sequence == MAX_SEQUENCE_NUM) {
            sequence = 0;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return System.currentTimeMillis() - START << TIMESTAMP_LEFT_SHIFT | NODE_ID;
        }
        return System.currentTimeMillis() - START << TIMESTAMP_LEFT_SHIFT | NODE_ID | sequence;
    }
}
