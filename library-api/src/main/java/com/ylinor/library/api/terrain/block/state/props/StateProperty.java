package com.ylinor.library.api.terrain.block.state.props;

import java.util.Collection;

/**
 *
 */
public abstract class StateProperty<T> {
	protected String name;
	protected Collection<T> possibleValues;

	public StateProperty(String name, Collection<T> possibleValues) {
		this.name = name;
		this.possibleValues = possibleValues;
	}

	public String name() {
		return name;
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
}
