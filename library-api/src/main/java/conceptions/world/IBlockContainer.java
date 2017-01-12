package conceptions.world;

public interface IBlockContainer {

    Block getBlock(int x, int y, int z);

    Block getBlock(BlockPos pos);

    Block getCachedBlock(int x, int y, int z);

    Block getCachedBlock(BlockPos pos);

    BlockExtraData getData(int x, int y, int z);

    default boolean hasData(int x, int y, int z)
    {
        return getData(x, y, z) != null;
    }

    default void setType(int x, int y, int z, BlockType type)
    {
        setType(x, y, z, type, true);
    }

    void setType(int x, int y, int z, BlockType type, boolean resetData);

    void setData(int x, int y, int z, BlockExtraData data);
}
