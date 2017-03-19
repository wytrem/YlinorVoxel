package com.ylinor.library.api.terrain.block;

import com.ylinor.library.api.terrain.block.state.BlockState;

public class BlockBehaviour {
	private BlockState state;

	public BlockBehaviour(BlockState state) {
		this.state = state;
	}
	
	public void onLanded(int entityId) {
		
	}
}
