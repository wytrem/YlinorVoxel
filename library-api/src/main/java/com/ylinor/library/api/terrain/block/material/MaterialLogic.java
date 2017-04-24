package com.ylinor.library.api.terrain.block.material;

public class MaterialLogic extends Material {
    public MaterialLogic(MapColor color) {
        super(color);
    }

    public boolean isSolid() {
        return false;
    }

    public boolean blocksLight() {
        return false;
    }

    public boolean blocksMovement() {
        return false;
    }
}
