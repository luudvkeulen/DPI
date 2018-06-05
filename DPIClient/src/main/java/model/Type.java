package model;

import java.util.ArrayList;
import java.util.List;

public class Type {
    public String name;
    public List<SubType> subTypes = new ArrayList<>();
    
    public Type(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
