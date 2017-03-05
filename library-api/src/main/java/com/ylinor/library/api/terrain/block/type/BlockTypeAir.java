package com.ylinor.library.api.terrain.block.type;

import com.ylinor.library.api.terrain.block.material.Material;

public class BlockTypeAir extends BlockType {
	protected BlockTypeAir(int id) {
		super(id, Material.AIR);
	}
}
