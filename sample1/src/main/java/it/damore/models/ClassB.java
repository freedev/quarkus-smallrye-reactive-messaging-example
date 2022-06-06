package it.damore.models;

public class ClassB {
    private String value;

    public ClassB(String v) {
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
        return "ClassB{" +
                "value='" + value + '\'' +
                '}';
    }
}
