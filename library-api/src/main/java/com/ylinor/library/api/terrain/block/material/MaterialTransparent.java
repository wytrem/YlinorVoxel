package com.ylinor.library.api.terrain.block.material;

public class MaterialTransparent extends Material {
    public MaterialTransparent(MapColor color) {
        super(color);
        this.setReplaceable();
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
