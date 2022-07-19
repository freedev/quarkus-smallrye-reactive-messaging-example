package it.damore.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadPoolProducer {
    static AtomicInteger counter = new AtomicInteger();
    public static ExecutorService getPoolWithName(String poolName) {
        return getPoolWithName(poolName, 4);
    }
    public static ExecutorService getPoolWithName(String poolName, int size) {
        return Executors.newFixedThreadPool(size, r -> new Thread(r, poolName + "-" + counter.getAndIncrement()));
    }
}
