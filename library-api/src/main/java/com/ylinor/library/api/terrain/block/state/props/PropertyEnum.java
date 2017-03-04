package com.ylinor.library.api.terrain.block.state.props;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 *
 */
public class PropertyEnum<E extends Enum<E>> extends StateProperty<E> {

	private Class<E> enumClass;

	protected PropertyEnum(String name, Class<E> clazz, Collection<E> values) {
		super(name, values);
		this.enumClass = clazz;
	}

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
	
	public static final <E extends Enum<E>> PropertyEnum<E> create(String name, Class<E> clazz) {
		return new PropertyEnum<E>(name, clazz, Arrays.stream(clazz.getEnumConstants()).collect(Collectors.toList()));
	}
	
	public static final <E extends Enum<E>> PropertyEnum<E> create(String name, Class<E> clazz, Predicate<E> predicate) {
		return new PropertyEnum<E>(name, clazz, Arrays.stream(clazz.getEnumConstants()).filter(predicate).collect(Collectors.toList()));
	}
}
