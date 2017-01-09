package com.ylinor.library.util;

public enum Facing {
    DOWN(0, 1, 0, -1, 0),
    UP(1, 0, 0, 1, 0),
    NORTH(2, 3, 0, 0, -1),
    SOUTH(3, 2, 0, 0, 1),
    EAST(4, 5, -1, 0, 0),
    WEST(5, 4, 1, 0, 0);

    /** List of all values in EnumFacing. Order is D-U-N-S-E-W. */
    private static final Facing[] faceList = new Facing[6];

    static {
        Facing[] var0 = values();
        for (Facing var3 : var0) {
            faceList[var3.order_a] = var3;
        }
    }

    /** Face order for D-U-N-S-E-W. */
    private final int order_a;

    private final int frontOffsetX;

    private final int frontOffsetY;

    private final int frontOffsetZ;

    Facing(int par3, int par4, int par5, int par6, int par7) {
        order_a = par3;
        frontOffsetX = par5;
        frontOffsetY = par6;
        frontOffsetZ = par7;
    }

    /**
     * Returns a offset that addresses the block in front of this facing.
     */
    public int getFrontOffsetX() {
        return frontOffsetX;
    }

    public int getFrontOffsetY() {
        return frontOffsetY;
    }

    /**
     * Returns a offset that addresses the block in front of this facing.
     */
    public int getFrontOffsetZ() {
        return frontOffsetZ;
    }

    /**
     * Returns the facing that represents the block in front of it.
     */
    public static Facing getFront(int par0) {
        return faceList[par0 % faceList.length];
    }
}
