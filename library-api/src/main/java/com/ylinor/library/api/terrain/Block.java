package com.ylinor.library.api.terrain;

import com.ylinor.library.api.block.BlockPos;

public class Block
{
    private BlockPos pos;
    private BlockExtraData data;
    private BlockType type;

    public Block(BlockPos pos, BlockType type)
    {
        this.pos = pos;
        this.type = type;
    }

    public Block(BlockPos pos, BlockExtraData data, BlockType type)
    {
        this.pos = pos;
        this.data = data;
        this.type = type;
    }

    public void setPos(BlockPos pos)
    {
        this.pos = pos;
    }

    public void setData(BlockExtraData data)
    {
        this.data = data;
    }

    public BlockPos getPos()
    {
        return pos;
    }

    public BlockExtraData getData()
    {
        return data;
    }

    public BlockType getType()
    {
        return type;
    }
}
