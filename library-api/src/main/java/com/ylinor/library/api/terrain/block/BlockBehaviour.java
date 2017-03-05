package com.ylinor.library.api.terrain.block;

import com.artemis.World;
import com.ylinor.library.api.terrain.block.state.BlockState;

public class BlockBehaviour {
	private BlockState state;

	public BlockBehaviour(BlockState state) {
		this.state = state;
	}
	
	public void onLanded(World world, int entityId) {
		
	}
}
