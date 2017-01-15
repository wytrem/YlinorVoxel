package com.ylinor.library.api.world.conception;

import com.ylinor.library.api.block.BlockPos;
import com.ylinor.library.util.math.Sizeable3D;
import gnu.trove.map.hash.TLongShortHashMap;
import java.util.HashMap;

public class Chunk implements IBlockContainer, Sizeable3D
{
    public static final short SIZE_X = 16;
    public static final short SIZE_Y = 256;
    public static final short SIZE_Z = 16;

    private TLongShortHashMap blocks = new TLongShortHashMap();
    private HashMap<Long, BlockExtraData> datas = new HashMap<>();

    @Override
    public Block getBlock(BlockPos pos)
    {
        return null;
    }

    @Override
    public Block getBlock(int x, int y, int z)
    {
        return null;
    }

    @Override
    public BlockType getBlockType(BlockPos pos)
    {
        return null;
    }

    @Override
    public BlockType getBlockType(int x, int y, int z)
    {
        return null;
    }

    @Override
    public BlockExtraData getBlockData(BlockPos pos)
    {
        return null;
    }

    @Override
    public BlockExtraData getBlockData(int x, int y, int z)
    {
        return null;
    }

    @Override
    public void setBlock(BlockPos pos, Block block)
    {

    }

    @Override
    public void setBlock(int x, int y, int z, Block block)
    {

    }

    @Override
    public void setBlockData(BlockPos pos, BlockExtraData data)
    {

    }

    @Override
    public void setBlockData(int x, int y, int z, BlockExtraData data)
    {

    }

    @Override
    public int getSizeX()
    {
        return SIZE_X;
    }

    @Override
    public int getSizeY()
    {
        return SIZE_Y;
    }

    @Override
    public int getSizeZ()
    {
        return SIZE_Z;
    }
}
