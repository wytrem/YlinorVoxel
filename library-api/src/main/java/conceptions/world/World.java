package conceptions.world;

/**
 *
 */
public class World implements IBlockContainer {
    private WorldStorage storage;

    @Override
    public Block getBlock(int x, int y, int z) {
        return storage.getBlock(x, y, z);
    }

    @Override
    public Block getBlock(BlockPos pos) {
        return storage.getBlock(pos);
    }

    @Override
    public Block getCachedBlock(int x, int y, int z) {
        return storage.getCachedBlock(x, y, z);
    }

    @Override
    public Block getCachedBlock(BlockPos pos) {
        return storage.getCachedBlock(pos);
    }

    @Override
    public BlockExtraData getData(int x, int y, int z) {
        return storage.getData(x, y, z);
    }

    @Override
    public boolean hasData(int x, int y, int z) {
        return storage.hasData(x, y, z);
    }

    @Override
    public void setType(int x, int y, int z, BlockType type) {
        storage.setType(x, y, z, type);
    }

    @Override
    public void setType(int x, int y, int z, BlockType type, boolean resetData) {
        storage.setType(x, y, z, type, resetData);
    }

    @Override
    public void setData(int x, int y, int z, BlockExtraData data) {
        storage.setData(x, y, z, data);
    }

    public BlockType getBlockType(short id)
    {
        return null;
    }
}
