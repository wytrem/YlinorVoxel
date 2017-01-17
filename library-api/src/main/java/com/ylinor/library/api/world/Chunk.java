package com.ylinor.library.api.world;

import com.ylinor.library.api.block.BlockPos;
import com.ylinor.library.api.world.storage.StorageUtil;
import com.ylinor.library.util.math.Sizeable3D;
import gnu.trove.map.hash.TIntObjectHashMap;
import gnu.trove.map.hash.TIntShortHashMap;

public class Chunk implements IBlockContainer, Sizeable3D
{
    public static final short SIZE_X = 16;
    public static final short SIZE_Y = 256;
    public static final short SIZE_Z = 16;

    private short[] blocks = new short[SIZE_X * SIZE_Y * SIZE_Z];
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
        Block block = getBlock(pos);

        if (block.getData() == null)
        {
            block = block.getType().createData(block.getPos()).provide(block.getType(), block.getPos());
        }

        return block;
    }

    @Override
    public Block getOrCreate(int x, int y, int z)
    {
        Block block = getBlock(x, y, z);

        if (block.getData() == null)
        {
            block = block.getData().provide(block.getType(), block.getPos());
        }

        return block;
    }

    @Override
    public BlockType getBlockType(BlockPos pos)
    {
        return BlockType.REGISTRY.get(blocks[StorageUtil.posToInt(pos)]);
    }

    @Override
    public BlockType getBlockType(int x, int y, int z)
    {
        return BlockType.REGISTRY.get(blocks[StorageUtil.posToInt((byte) x, (short) y, (byte) z)]);
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
        blocks[StorageUtil.posToInt(pos)] =  type.getId();
        setBlockData(pos, null);
    }

    @Override
    public void setBlockType(int x, int y, int z, BlockType type)
    {
        blocks[StorageUtil.posToInt((byte) x, (byte) y, (byte) z)] = type.getId();
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
