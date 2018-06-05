package model;

public class SubType {

    public String name;

    public SubType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
