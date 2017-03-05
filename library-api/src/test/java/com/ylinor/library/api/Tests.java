package com.ylinor.library.api;

import java.util.LinkedHashMap;

import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.state.BlockStateFactory;
import com.ylinor.library.api.terrain.block.state.props.PropertyBool;
import com.ylinor.library.api.terrain.block.state.props.PropertyEnum;
import com.ylinor.library.api.terrain.block.state.props.StateProperty;
import com.ylinor.library.util.Facing;

public class Tests {
	public static void main(String[] args) {
		
		
		StateProperty<Facing> a = PropertyEnum.create("toto", Facing.class);
		StateProperty<Facing> b = PropertyEnum.create("tata", Facing.class);
		StateProperty<Boolean> c = PropertyBool.create("coucou");
		BlockStateFactory factory = new BlockStateFactory(null, new StateProperty<?>[] {a, b, c});
	
		BlockState state = factory.getOneState();
		System.out.println(state.toString());
		state = state.with(a, Facing.DOWN);
		System.out.println(state.toString());
		state = state.with(c, true);
		System.out.println(state.toString());
		state = state.with(c, false);
		System.out.println(state.toString());
		
		System.out.println(state.propertiesToString());
	}
}
