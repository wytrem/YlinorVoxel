package conceptions.world;

import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TShortObjectHashMap;

/**
 *
 */
public class Chunk implements IBlockContainer {

    private TShortObjectMap<Block> blockCache;
    private TShortObjectMap<BlockExtraData> blockDatas = new TShortObjectHashMap<>(64);
    private short[][][] blockTypes;

    public static final int CHUNK_SIZE_X = 16;
    public static final int CHUNK_SIZE_Y = 256;
    public static final int CHUNK_SIZE_Z = 16;

    private World world;


    public Chunk(World worldIn) {
        world = worldIn;
        blockCache = new TShortObjectHashMap<>(64);
        blockDatas = new TShortObjectHashMap<>();
        blockTypes = new short[CHUNK_SIZE_X][CHUNK_SIZE_Y][CHUNK_SIZE_Z];
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        short pos = posInChunkToShort(x, y, z);

        Block block = getCachedBlock(x, y, z);

        if (block == null) {
            BlockExtraData data = getData(x, y, z);

            if (data == null) {
                block = new Block(new BlockPos(x, y, z), world.getBlockType(typeAt(x, y, z)), null);
            } else {
                block = data.provide(new BlockPos(x, y, z), world.getBlockType(typeAt(x, y, z)), world);
            }

            cacheBlock(pos, block);
        }

        return block;
    }

    private short typeAt(int x, int y, int z) {
        return blockTypes[x][y][z];
    }

    @Override
    public Block getBlock(BlockPos pos) {
        return getBlock(pos.x(), pos.y(), pos.z());
    }

    @Override
    public Block getCachedBlock(int x, int y, int z) {
        return blockCache.get(posInChunkToShort(x, y, z));
    }

    @Override
    public Block getCachedBlock(BlockPos pos) {
        return getCachedBlock(pos.x(), pos.y(), pos.z());
    }

    @Override
    public BlockExtraData getData(int x, int y, int z) {
        return blockDatas.get(posInChunkToShort(x, y, z));
    }

    @Override
    public boolean hasData(int x, int y, int z) {
        return false;
    }

    @Override
    public void setType(int x, int y, int z, BlockType type, boolean resetData) {
        Block block = getCachedBlock(x, y, z);

        if (block == null) {
            synchronized (blockTypes) {
                if (resetData) {
                    synchronized (blockDatas) {
                        blockDatas.remove(posInChunkToShort(x, y, z));
                    }
                }

                blockTypes[x][y][z] = type.getId();
            }
        } else {
            synchronized (block) {
                synchronized (blockTypes) {
                    short pos = posInChunkToShort(x, y, z);

                    if (resetData) {
                        synchronized (blockDatas) {
                            blockDatas.remove(pos);
                        }
                    }

                    uncacheBlock(pos);

                    blockTypes[x][y][z] = type.getId();
                }
            }
        }
    }

    private void uncacheBlock(short pos) {
        synchronized (blockCache) {
            blockCache.remove(pos);
        }
    }

    private void cacheBlock(short pos, Block block) {
        synchronized (blockCache) {
            blockCache.put(pos, block);
        }
    }

    @Override
    public void setData(int x, int y, int z, BlockExtraData data) {
        Block block = getCachedBlock(x, y, z);

        if (block == null) {
            synchronized (blockDatas) {
                blockDatas.remove(posInChunkToShort(x, y, z));
            }
        } else {
            synchronized (block) {
                short pos = posInChunkToShort(x, y, z);

                synchronized (blockDatas) {
                    blockDatas.remove(pos);
                }

                uncacheBlock(pos);
            }
        }
    }

    private static final int NUM_X_BITS = 4;
    private static final int NUM_Z_BITS = NUM_X_BITS;
    private static final int NUM_Y_BITS = 16 - NUM_X_BITS - NUM_Z_BITS;
    private static final int Y_SHIFT = 0 + NUM_Z_BITS;
    private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
    private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
    private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
    private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

    public static short posInChunkToShort(int x, int y, int z) {
        return (short) (((long) x & X_MASK) << X_SHIFT | ((long) y & Y_MASK) << Y_SHIFT | ((long) z & Z_MASK) << 0);
    }
}
