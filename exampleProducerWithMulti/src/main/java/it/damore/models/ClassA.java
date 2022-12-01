package it.damore.models;

import java.util.concurrent.atomic.AtomicInteger;

public class ClassA {
    private String value;
    private AtomicInteger counter = new AtomicInteger();

    public ClassA(String v) {
        this.value = v;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public AtomicInteger getCounter() {
        return counter;
    }

    public Integer getAndIncrement() {
        return this.counter.getAndIncrement();
    }

    @Override
    public String toString() {
        return "ClassA{" +
                "value='" + value + '\'' +
                '}';
    }
}
