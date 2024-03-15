package it.damore.models;

public class ClassB {
    public String value;

    public ClassB(String v) {
        this.value = v;
    }

    @Override
    public String toString() {
        return "ClassB{" +
                "value='" + value + '\'' +
                '}';
    }
}
