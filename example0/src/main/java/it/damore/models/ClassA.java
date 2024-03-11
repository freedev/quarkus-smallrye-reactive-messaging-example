package it.damore.models;

public class ClassA {
    private final String value;

    public ClassA(String v) {
        this.value = v;
    }

    @Override
    public String toString() {
        return "ClassA{" +
                "value='" + value + '\'' +
                '}';
    }
}
