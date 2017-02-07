package com.ylinor.library.api.block;

import org.joml.Vector3f;
import org.joml.Vector3i;

import com.ylinor.library.util.Facing;
import com.ylinor.library.util.math.MathHelper;


public class BlockPos extends Vector3i {
    public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);
    private static final int NUM_X_BITS = 1 + MathHelper.calculateLogBaseTwo(MathHelper.roundUpToPowerOfTwo(30000000));
    private static final int NUM_Z_BITS = NUM_X_BITS;
    private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
    private static final int Y_SHIFT = 0 + NUM_Z_BITS;
    private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
    private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
    private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
    private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

    public BlockPos(int x, int y, int z) {
        super(x, y, z);
    }

    public BlockPos up() {
        return this.up(1);
    }

    public BlockPos up(int n) {
        return this.offset(Facing.UP, n);
    }

    public BlockPos down() {
        return this.down(1);
    }

    public BlockPos down(int n) {
        return this.offset(Facing.DOWN, n);
    }

    public BlockPos north() {
        return this.north(1);
    }

    public BlockPos north(int n) {
        return this.offset(Facing.NORTH, n);
    }

    public BlockPos south() {
        return this.south(1);
    }

    public BlockPos south(int n) {
        return this.offset(Facing.SOUTH, n);
    }

    public BlockPos west() {
        return this.west(1);
    }

    public BlockPos west(int n) {
        return this.offset(Facing.WEST, n);
    }

    public BlockPos east() {
        return this.east(1);
    }

    public BlockPos east(int n) {
        return this.offset(Facing.EAST, n);
    }

    public BlockPos offset(Facing facing) {
        return this.offset(facing, 1);
    }

    public BlockPos offset(Facing facing, int n) {
        return n == 0 ? this : set(this.x() + facing.getFrontOffsetX() * n, this.y() + facing.getFrontOffsetY() * n, this.z() + facing.getFrontOffsetZ() * n);
    }

    public long toLong() {
        return toLong(x, y, z);
    }

    public static long toLong(int x, int y, int z) {
        return ((long) x & X_MASK) << X_SHIFT | ((long) y & Y_MASK) << Y_SHIFT | ((long) z & Z_MASK) << 0;
    }

    public static BlockPos fromLong(long serialized) {
        int i = (int) (serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
        int j = (int) (serialized << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
        int k = (int) (serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
        return new BlockPos(i, j, k);
    }

    public BlockPos negative() {
        return new BlockPos(-this.x(), -this.y(), -this.z());
    }

    public Vector3f toVector() {
        return new Vector3f(this.x(), this.y(), this.z());
    }

    public Vector3f toVector(Vector3f dest) {
        dest.x = x();
        dest.y = y();
        dest.z = z();
        return dest;
    }

    public BlockPos set(int x, int y, int z) {
        this.set(x, y, z);
        return this;
    }

    public BlockPos set(BlockPos pos) {
        this.set(pos.x(), pos.y(), pos.z());
        return this;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BlockPos other = (BlockPos) obj;
        if (x != other.x)
            return false;
        if (y != other.y)
            return false;
        if (z != other.z)
            return false;
        return true;
    }

    public float getDistanceSquared(BlockPos blockPos) {
        float distanceX = x - blockPos.x();
        float distanceY = y - blockPos.y();
        float distanceZ = z - blockPos.z();
        return distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;
    }

    public boolean isInBounds() {
        return x >= -30000000 && z >= -30000000 && x < 30000000 && z < 30000000 && y >= 0 && y < 256;
    }

    public boolean isInBoundsCorrectY() {
        if (y < 0) {
            y = 0;
        }
        else if (y > 255) {
            y = 255;
        }

        return isInBounds();
    }

    public void clone15(BlockPos pos) {
        set(pos.x & 15, pos.y, pos.z & 15);
    }

    public void clone15Y(BlockPos pos) {
        set(pos.x, pos.y & 15, pos.z);
    }

    public BlockPos copy() {
        return new BlockPos(x, y, z);
    }

    public Vector3f clone(Vector3f vect0) {
        vect0.x = x;
        vect0.y = y;
        vect0.z = z;

        return vect0;
    }
}
