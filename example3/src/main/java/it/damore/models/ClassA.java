package it.damore.models;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClassA {
    static Random r = new Random();
    private String value;
    private String buffer;
    private AtomicInteger counter = new AtomicInteger();

    public ClassA(String v) {
        this.value = v;
        this.buffer = IntStream.range(0, 10000).parallel().mapToObj(i -> String.valueOf(r.nextInt())).collect(Collectors.joining());
    }

    public String getValue() {
        return value;
    }

    public Integer getCounterAndIncrement() {
        return counter.getAndIncrement();
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ClassA{" +
                "value='" + value + '\'' +
                "counter='" + counter.get() + '\'' +
                "buffer length=" + buffer.length() +
                '}';
    }
}
