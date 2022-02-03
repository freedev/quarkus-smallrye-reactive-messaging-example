package it.damore.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomThreadPoolProducer {
    public static ExecutorService getPoolWithName(String poolName) {
        AtomicInteger counter = new AtomicInteger();
        return Executors.newFixedThreadPool(2, r -> new Thread(r, poolName + "-" + counter.getAndIncrement()));
    }
}
