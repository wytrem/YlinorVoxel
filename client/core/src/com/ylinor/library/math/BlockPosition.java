package com.ylinor.library.math;

import com.badlogic.gdx.math.Vector3;
import com.ylinor.library.Facing;

public class BlockPosition extends Position3D
{
    public static final BlockPosition ORIGIN = new BlockPosition(0, 0, 0);

    private static final int NUM_X_BITS = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
    private static final int NUM_Z_BITS = NUM_X_BITS;
    private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
    private static final int Y_SHIFT = 0 + NUM_Z_BITS;
    private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
    private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
    private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
    private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

    public BlockPosition(int x, int y, int z)
    {
        super(x, y, z);
    }

    public BlockPosition up()
    {
        return this.up(1);
    }

    public BlockPosition up(int n)
    {
        return this.offset(Facing.UP, n);
    }

    public BlockPosition down()
    {
        return this.down(1);
    }

    public BlockPosition down(int n)
    {
        return this.offset(Facing.DOWN, n);
    }

    public BlockPosition north()
    {
        return this.north(1);
    }

    public BlockPosition north(int n)
    {
        return this.offset(Facing.NORTH, n);
    }

    public BlockPosition south()
    {
        return this.south(1);
    }

    public BlockPosition south(int n)
    {
        return this.offset(Facing.SOUTH, n);
    }

    public BlockPosition west()
    {
        return this.west(1);
    }

    public BlockPosition west(int n)
    {
        return this.offset(Facing.WEST, n);
    }

    public BlockPosition east()
    {
        return this.east(1);
    }

    public BlockPosition east(int n)
    {
        return this.offset(Facing.EAST, n);
    }

    public BlockPosition offset(Facing facing)
    {
        return this.offset(facing, 1);
    }

    public BlockPosition offset(Facing facing, int n)
    {
        return n == 0 ? this : set(this.getX() + facing.getFrontOffsetX() * n, this.getY() + facing.getFrontOffsetY() * n, this.getZ() + facing.getFrontOffsetZ() * n);
    }

    public long toLong()
    {
        return ((long) this.getX() & X_MASK) << X_SHIFT | ((long) this.getY() & Y_MASK) << Y_SHIFT | ((long) this.getZ() & Z_MASK) << 0;
    }

    public static BlockPosition fromLong(long serialized)
    {
        int i = (int) (serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
        int j = (int) (serialized << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
        int k = (int) (serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
        return new BlockPosition(i, j, k);
    }

    public BlockPosition negative()
    {
        return new BlockPosition(-this.getX(), -this.getY(), -this.getZ());
    }

    public Vector3 toVector()
    {
        return new Vector3(this.getX(), this.getY(), this.getZ());
    }

    public Vector3 toVector(Vector3 dest)
    {
        dest.x = getX();
        dest.y = getY();
        dest.z = getZ();
        return dest;
    }

    public BlockPosition set(int x, int y, int z)
    {
        this.setX(x);
        this.setY(y);
        this.setZ(z);
        return this;
    }

    public BlockPosition set(BlockPosition pos)
    {
        this.set(pos.getX(), pos.getY(), pos.getZ());
        return this;
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;

        result = prime * result + getX();
        result = prime * result + getY();
        result = prime * result + getZ();

        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BlockPosition other = (BlockPosition) obj;
        if (getX() != other.getX())
            return false;
        if (getY() != other.getY())
            return false;
        if (getZ() != other.getZ())
            return false;
        return true;
    }

    public float getDistanceSquared(BlockPosition BlockPosition)
    {
        float distanceX = getX() - BlockPosition.getX();
        float distanceY = getY() - BlockPosition.getY();
        float distanceZ = getZ() - BlockPosition.getZ();

        return distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
    }

    public void clone15(BlockPosition pos)
    {
        set(pos.getX() & 15, pos.getY(), pos.getZ() & 15);
    }

    public void clone15Y(BlockPosition pos)
    {
        set(pos.getX(), pos.getY() & 15, pos.getZ());
    }

    public BlockPosition copy()
    {
        return new BlockPosition(getX(), getY(), getZ());
    }

    @Override
    public String toString()
    {
        return "BlockPosition [x=" + getX() + ", y=" + getY() + ", z=" + getZ() + "]";
    }

    public Vector3 clone(Vector3 vect0)
    {
        vect0.x = getX();
        vect0.y = getY();
        vect0.z = getZ();

        return vect0;
    }
}
