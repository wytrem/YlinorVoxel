package com.ylinor.library.api.terrain.block;

import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.type.BlockType;
import com.ylinor.library.util.math.BlockPos;


public class Block {
    private BlockPos pos;
    private BlockState data;
    private BlockType type;

    public Block(BlockPos pos, BlockType type) {
        this.pos = pos;
        this.type = type;
    }

    public Block(BlockPos pos, BlockState data, BlockType type) {
        this.pos = pos;
        this.data = data;
        this.type = type;
    }

    public void setPos(BlockPos pos) {
        this.pos = pos;
    }

    public void setData(BlockState data) {
        this.data = data;
    }

    public BlockPos getPos() {
        return pos;
    }

    public BlockState getData() {
        return data;
    }

    public BlockType getType() {
        return type;
    }
}
