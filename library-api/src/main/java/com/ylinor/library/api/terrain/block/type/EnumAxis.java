package com.ylinor.library.api.terrain.block.type;

public enum EnumAxis {
    X("x"),
    Y("y"),
    Z("z"),
    NONE("none");

    private final String name;

    private EnumAxis(String name) {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }
}