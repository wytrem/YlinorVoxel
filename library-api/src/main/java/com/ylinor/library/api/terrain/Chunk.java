package com.ylinor.library.api.terrain;

import com.ylinor.library.api.terrain.block.Block;
import com.ylinor.library.api.terrain.block.state.BlockState;
import com.ylinor.library.api.terrain.block.type.BlockType;
import com.ylinor.library.util.math.BlockPos;
import com.ylinor.library.util.math.Sizeable3D;

import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TShortObjectHashMap;


public class Chunk implements IBlockContainer, Sizeable3D {
    public static final short SIZE_X = 16;
    public static final short SIZE_Y = 256;
    public static final short SIZE_Z = 16;

    private TShortObjectMap<Block> blockCache = new TShortObjectHashMap<>(64);
    private short[][][] blocks;
    private byte[][][] states;
    private Terrain world;
    public final int x, z;
    public final long id;
    public boolean needsRenderUpdate;

    public Chunk(Terrain world, int chunkX, int chunkZ) {
        this.world = world;
        this.x = chunkX;
        this.z = chunkZ;
        this.id = chunkXZ2Int(chunkX, chunkZ);
        blocks = new short[SIZE_X][SIZE_Y][SIZE_Z];
        states = new byte[SIZE_X][SIZE_Y][SIZE_Z];
        needsRenderUpdate = true;
    }

    public Terrain getWorld() {
        return world;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        return _blockAt(posInChunkToShort(x, y, z));
    }

    private void _uncacheBlock(short pos) {
        Block block = _blockAt(pos);

        if (block != null) {
            synchronized (block) {
                synchronized (blockCache) {
                    blockCache.remove(pos);
                }
            }
        }
        else {
            synchronized (blockCache) {
                blockCache.remove(pos);
            }
        }
    }

    private void _cacheBlock(short pos, Block block) {
        Block previousBlock = _blockAt(pos);

        if (previousBlock != null) {
            synchronized (previousBlock) {
                synchronized (blockCache) {
                    blockCache.put(pos, block);
                }
            }
        }
        else {
            synchronized (blockCache) {
                blockCache.put(pos, block);
            }
        }
    }

    private Block _blockAt(short pos) {
        synchronized (blockCache) {
            return blockCache.get(pos);
        }
    }

    private short _typeAt(int x, int y, int z) {
        if (y >= 256 || y < 0 || x > 15 || x < 0 || z > 15 || z < 0) {
            return 0;
        }
        synchronized (blocks) {
            return blocks[x][y][z];
        }
    }

    private void _setTypeAt(int x, int y, int z, short type) {
        synchronized (blocks) {
            blocks[x][y][z] = type;
        }
    }

    private void _setState(int x, int y, int z, BlockState data) {
        synchronized (states) {
            states[x][y][z] = (byte) world.getBlockType(_typeAt(x, y, z))
                                          .getMetaFromState(data);
        }
    }

    private BlockState _stateAt(int x, int y, int z) {
        if (y >= 256 || y < 0 || x > 15 || x < 0 || z > 15 || z < 0) {
            return BlockType.air.getDefaultState();
        }

        synchronized (states) {
            return world.getBlockType(_typeAt(x, y, z))
                        .getStateFromMeta(states[x][y][z]);
        }
    }

    private BlockPos _newBlockPos(int x, int y, int z) {
        return new BlockPos(x << 4 + x, y, z << 4 + z);
    }

    @Override
    public Block getOrCreate(int x, int y, int z) {
        short pos = posInChunkToShort(x, y, z);
        Block block = _blockAt(pos);
        // if (block == null) {
        // BlockExtraData data = _dataAt(pos);
        // if (data == null) {
        // block = new Block(_newBlockPos(x, y, z), null,
        // world.getBlockType(_typeAt(x, y, z)));
        // }
        // else {
        // block = data.provide(world.getBlockType(_typeAt(x, y, z)),
        // _newBlockPos(x, y, z), world);
        // }
        // _cacheBlock(pos, block);
        // }
        return block;
    }

    @Override
    public BlockType getBlockType(int x, int y, int z) {
        return world.getBlockType(_typeAt(x, y, z));
    }

    @Override
    public BlockState getBlockState(int x, int y, int z) {
        return _stateAt(x, y, z);
    }

    @Override
    public void setBlock(Block block) {
        setBlockType(block.getPos(), block.getType());
        setBlockState(block.getPos(), block.getData());
        _cacheBlock(posInChunkToShort(block.getPos()), block);
    }

    @Override
    public void setBlockType(int x, int y, int z, BlockType type) {
        short pos = posInChunkToShort(x, y, z);
        _uncacheBlock(pos);
        _setTypeAt(x, y, z, type.getId());
        _setState(x, y, z, type.getDefaultState());
    }

    @Override
    public void setBlockState(int x, int y, int z, BlockState data) {
        short pos = posInChunkToShort(x, y, z);
        _uncacheBlock(pos);
        _setState(x, y, z, data);
    }

    @Override
    public int getSizeX() {
        return SIZE_X;
    }

    @Override
    public int getSizeY() {
        return SIZE_Y;
    }

    @Override
    public int getSizeZ() {
        return SIZE_Z;
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

    public static int xFromShort(long serialized) {
        return (int) (serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
    }

    public static int yFromShort(short serialized) {
        return (int) (serialized << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
    }

    public static int zFromShort(short serialized) {
        return (int) (serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
    }

    public static short posInChunkToShort(BlockPos worldRelative) {
        return posInChunkToShort(worldRelative.x & 15, worldRelative.y, worldRelative.z & 15);
    }

    /**
     * converts a chunk coordinate pair to a long (suitable for hashing)
     */
    public static long chunkXZ2Int(int chunkX, int chunkZ) {
        return chunkX & 4294967295L | (chunkZ & 4294967295L) << 32;
    }
}
