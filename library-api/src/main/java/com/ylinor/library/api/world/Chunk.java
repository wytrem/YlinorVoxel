package com.ylinor.library.api.world;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;

import com.ylinor.library.api.block.BlockPos;
import com.ylinor.library.util.math.Sizeable3D;
import com.ylinor.library.util.spring.Assert;

import gnu.trove.iterator.TShortObjectIterator;
import gnu.trove.map.TShortObjectMap;
import gnu.trove.map.hash.TShortObjectHashMap;


/**
 * A world chunk.
 * 
 * @author Litarvam
 * @author wytrem
 * @since 1.0.0
 */
public class Chunk implements Sizeable3D {
    public static final int CHUNK_SIZE_X = 16;
    public static final int CHUNK_SIZE_Y = 256;
    public static final int CHUNK_SIZE_Z = 16;

    @NotNull
    private World world;

    @NotNull
    private Vector2i position;

    @NotNull
    private short[][][] blockIds;

    private TShortObjectMap<Block> blocks;

    public Chunk(@NotNull World world, @NotNull Vector2i position) throws IllegalArgumentException {
        Assert.notNull(world, "world cannot be null");
        Assert.notNull(position, "position cannot be null");

        this.world = world;
        this.position = position;
        this.blockIds = new short[CHUNK_SIZE_X][CHUNK_SIZE_Y][CHUNK_SIZE_Z];
        this.blocks = new TShortObjectHashMap<>(64);
    }

    public void setBlock(int x, int y, int z, @Nullable Block block) {
        short pos = posInChunkToShort(x, y, z);
        Block previous = blocks.get(pos);

        if (previous != null) {
            synchronized (previous) {
                if (block != null) {
                    blocks.put(pos, block);
                }
                else {
                    blocks.remove(pos);
                }
            }
        }
        else {
            if (block != null) {
                blocks.put(pos, block);
            }
            else {
                blocks.remove(pos);
            }
        }

        if (block != null) {
            blockIds[x][y][z] = (short) block.getTypeId();
        }
        else {
            blockIds[x][y][z] = (short) 0;
        }
    }

    public void setBlockId(int x, int y, int z, short id) {
        short pos = posInChunkToShort(x, y, z);
        Block previous = blocks.get(id);

        if (previous != null) {
            synchronized (previous) {
                blocks.remove(pos);
            }
        }

        blockIds[x][y][z] = id;
    }

    public short getBlockId(int x, int y, int z) {
        return blockIds[x][y][z];
    }

    @NotNull
    public Block getBlock(int x, int y, int z) {
        short pos = posInChunkToShort(x, y, z);
        Block block = blocks.get(pos);

        if (block == null) {
            block = new Block(getBlockId(x, y, z), new BlockPos(x() * CHUNK_SIZE_X + x, y, z() * CHUNK_SIZE_Z + z));
            blocks.put(pos, block);
        }

        return block;
    }

    @NotNull
    public World getWorld() {
        return world;
    }

    public int x() {
        return position.x();
    }

    public int z() {
        return position.y();
    }

    @Override
    public int getSizeX() {
        return CHUNK_SIZE_X;
    }

    @Override
    public int getSizeY() {
        return CHUNK_SIZE_Y;
    }

    @Override
    public int getSizeZ() {
        return CHUNK_SIZE_Z;
    }

    public void clearBlocksCache() {
        TShortObjectIterator<Block> iterator = blocks.iterator();

        while (iterator.hasNext()) {
            if (iterator.value().canBeUncached()) {
                iterator.remove();
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
