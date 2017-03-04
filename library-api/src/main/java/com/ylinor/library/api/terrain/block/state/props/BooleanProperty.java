package com.ylinor.library.api.terrain.block.state.props;

import java.util.Arrays;

public class BooleanProperty extends StateProperty<Boolean> {
	protected BooleanProperty(String name) {
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

	public static BooleanProperty create(String name) {
		return new BooleanProperty(name);
	}
}
