package com.ylinor.library.api.world.blocks.properties;

/**
 *
 */
public class EnumProperty<E extends Enum<E>> extends FiniteSetProperty<E> {

    private Class<E> enumClass;

    @Override
    public String serialize(E value) {
        return value.name().toLowerCase();
    }

    @Override
    public E unserialize(String serialized) {

        for (E value : enumClass.getEnumConstants()) {
            if (value.name().equalsIgnoreCase(serialized)) {
                return value;
            }
        }
        return null;
    }

    @Override
    public E[] values() {
        return enumClass.getEnumConstants();
    }
}
