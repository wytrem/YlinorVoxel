package com.ylinor.library.api.world.conception;

import com.ylinor.library.api.block.BlockPos;
import com.ylinor.library.api.world.conception.storage.StorageUtil;
import com.ylinor.library.util.math.Sizeable3D;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TIntShortHashMap;

public class Chunk implements IBlockContainer, Sizeable3D
{
    public static final short SIZE_X = 16;
    public static final short SIZE_Y = 256;
    public static final short SIZE_Z = 16;

    private TIntShortHashMap blocks = new TIntShortHashMap();
    private TIntObjectHashMap<BlockExtraData> datas = new TIntObjectHashMap<>();

    @Override
    public Block getBlock(BlockPos pos)
    {
        return new Block(pos, getBlockData(pos), getBlockType(pos));
    }

    @Override
    public Block getBlock(int x, int y, int z)
    {
        return new Block(new BlockPos(x, y, z), getBlockData(x, y, z), getBlockType(x, y, z));
    }

    @Override
    public Block getOrCreate(BlockPos pos)
    {
        return null;
    }

    @Override
    public Block getOrCreate(int x, int y, int z)
    {
        return null;
    }

    @Override
    public BlockType getBlockType(BlockPos pos)
    {
        return BlockType.REGISTRY.get(blocks.get(StorageUtil.posToInt(pos)));
    }

    @Override
    public BlockType getBlockType(int x, int y, int z)
    {
        return BlockType.REGISTRY.get(blocks.get(StorageUtil.posToInt((byte) x, (short) y, (byte) z)));
    }

    @Override
    public BlockExtraData getBlockData(BlockPos pos)
    {
        return datas.get(StorageUtil.posToInt(pos));
    }

    @Override
    public BlockExtraData getBlockData(int x, int y, int z)
    {
        return datas.get(StorageUtil.posToInt((byte) x, (short) y, (byte) z));
    }

    @Override
    public void setBlock(Block block)
    {
        setBlockType(block.getPos(), block.getType());
        setBlockData(block.getPos(), block.getData());
    }

    @Override
    public void setBlockType(BlockPos pos, BlockType type)
    {
        blocks.put(StorageUtil.posToInt(pos), type.getId());
        setBlockData(pos, null);
    }

    @Override
    public void setBlockType(int x, int y, int z, BlockType type)
    {
        blocks.put(StorageUtil.posToInt((byte) x, (byte) y, (byte) z), type.getId());
        setBlockData(x, y, z, null);
    }

    @Override
    public void setBlockData(BlockPos pos, BlockExtraData data)
    {
        datas.put(StorageUtil.posToInt(pos), data);
    }

    @Override
    public void setBlockData(int x, int y, int z, BlockExtraData data)
    {
        datas.put(StorageUtil.posToInt((byte) x, (byte) y, (byte) z), data);
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
