package conceptions.world;

/**
 *
 */
public class Block {
    private final BlockPos pos;
    private final BlockType type;
    private final BlockExtraData extraData;

    public Block(BlockPos pos, BlockType typeIn, BlockExtraData extraData) {
        this.pos = pos;
        this.type = typeIn;
        this.extraData = extraData;
    }


}
