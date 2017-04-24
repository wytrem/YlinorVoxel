package com.ylinor.library.api.terrain.block.state.props;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


public abstract class StateProperty<T> implements Comparable<StateProperty<?>> {
    protected String name;
    protected Collection<T> possibleValues;

    public StateProperty(String name, Collection<T> possibleValues) {
        this.name = name;
        this.possibleValues = possibleValues;
    }

    public String name() {
        return name;
    }

    @SuppressWarnings("unchecked")
    public String equality(Object value) {
        return name() + "=" + serialize((T) value);
    }

    @SuppressWarnings("unchecked")
    public String serializeObject(Object value) {
        return serialize((T) value);
    }

    public abstract String serialize(T value);

    public abstract T unserialize(String serialized);

    public Collection<T> possibleValues() {
        return possibleValues;
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public int compareTo(StateProperty<?> o) {
        return o.name().compareTo(name);
    }

    public static String sort(String equalities) {
        return Arrays.stream(equalities.split(","))
                     .sorted()
                     .collect(Collectors.joining(","));
    }
}
