package it.damore.models;

public class ClassA {
    private String value;

    public ClassA(String v) {
        this.value = v;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ClassA{" +
                "value='" + value + '\'' +
                '}';
    }
}
