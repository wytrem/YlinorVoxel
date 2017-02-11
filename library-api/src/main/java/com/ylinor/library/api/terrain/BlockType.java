package com.ylinor.library.api.terrain;

import com.ylinor.library.util.math.BlockPos;

import gnu.trove.map.hash.TShortObjectHashMap;


public class BlockType {
    public static final TShortObjectHashMap<BlockType> REGISTRY = new TShortObjectHashMap<>();
    public static final BlockType air = new BlockType(0);
    public static final BlockType stone = new BlockType(1);
    public static final BlockType dirt = new BlockType(2);
    public static final BlockType wood = new BlockType(3).setTextureId(4);
    public static final BlockType stoneBricks = new BlockType(4).setTextureId(5);
    public static final BlockType bricks = new BlockType(5).setTextureId(7);
    public static final BlockType sand = new BlockType(6).setTextureId(18);
    public static final BlockType gravel = new BlockType(7).setTextureId(19);
    public static final BlockType sponge = new BlockType(8).setTextureId(48);
    public static final BlockType ice = new BlockType(9).setTextureId(67);

    private final short id;
    private int textureId;

    public BlockType(int id) {
        this.id = (short) id;
        textureId = id;
        REGISTRY.put(this.id, this);
    }

    public BlockType setTextureId(int texture) {
        this.textureId = texture;
        return this;
    }

    public int getTextureId() {
        return textureId;
    }

    public boolean isOpaque() {
        return id != 0;
    }

    public BlockExtraData createData(BlockPos pos, Terrain world) {
        return null;
    }

    public short getId() {
        return id;
    }

    public boolean isCollidable() {
        return this != air;
    }
}
