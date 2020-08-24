package me.ayushdev.runesenchant;

public class Placeholder {

    private final String name;
    private final Object data;

    public Placeholder(String name, Object data) {
        this.name = name;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public Object getData() {
        return data;
    }

}
