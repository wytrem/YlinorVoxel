package com.ylinor.library.api.terrain.block.state.props;

import java.util.Arrays;

public class PropertyBool extends StateProperty<Boolean> {
	protected PropertyBool(String name) {
		super(name, Arrays.asList(true, false));
	}

	@Override
	public String serialize(Boolean value) {
		return Boolean.toString(value);
	}

	@Override
	public Boolean unserialize(String serialized) {
		return Boolean.valueOf(serialized);
	}

	public static PropertyBool create(String name) {
		return new PropertyBool(name);
	}
}
