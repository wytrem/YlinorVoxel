package conceptions.world;

/**
 *
 */
public class BlockPos {

    private int x, y, z;

    public BlockPos(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    int x()
    {
        return x;
    }

    int y()
    {
        return y;
    }

    int z()
    {
        return z;
    }
}
