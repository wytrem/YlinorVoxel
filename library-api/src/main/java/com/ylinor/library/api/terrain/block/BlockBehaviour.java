package com.ylinor.library.api.terrain.block;

import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.util.ecs.Entity;
import com.ylinor.library.util.ecs.World;

public class BlockBehaviour {
	private BlockState state;

	public BlockBehaviour(BlockState state) {
		this.state = state;
	}
	
	public void onLanded(World world, Entity entityId) {
		
	}
}
