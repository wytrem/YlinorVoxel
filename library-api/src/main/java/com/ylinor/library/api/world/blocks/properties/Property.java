package com.ylinor.library.api.world.blocks.properties;

/**
 *
 */
public abstract class Property<T> {
    public abstract String serialize(T value);

    public abstract T unserialize(String serialized);
}
