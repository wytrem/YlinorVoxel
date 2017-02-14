package com.ylinor.client.render.model.block;

import com.ylinor.client.renderlib.buffers.VertexBuffer;
import com.ylinor.library.api.world.Chunk;
import com.ylinor.library.api.world.IBlockContainer;
import com.ylinor.library.util.TempVars;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class BasicBlockModel extends BlockModel {
    public BasicBlockModel()
    {
        super();
    }

    public BasicBlockModel(@NotNull List<Cube> cubes)
    {
        super(cubes);
    }

    public BasicBlockModel(@Nullable String name, @NotNull List<Cube> cubes)
    {
        super(name, cubes);
    }

    @Override
    public void render(VertexBuffer vertexBuffer, IBlockContainer neighbours, int x, int y, int z) {
        TempVars tempVars = TempVars.get();

        cubes.get(0).initRendering(tempVars);

        if (y == Chunk.SIZE_Y - 1 || !neighbours.getBlockType(x, y + 1, z)
                                                .isOpaque()) {
            cubes.get(0).createTop(vertexBuffer, tempVars);
        }

        if (y == 0 || !neighbours.getBlockType(x, y - 1, z).isOpaque()) {
            cubes.get(0).createBottom(vertexBuffer, tempVars);
        }

        if (!neighbours.getBlockType(x + 1, y, z).isOpaque()) {
            cubes.get(0).createRight(vertexBuffer, tempVars);
        }

        if (!neighbours.getBlockType(x - 1, y, z).isOpaque()) {
            cubes.get(0).createLeft(vertexBuffer, tempVars);
        }

        if (!neighbours.getBlockType(x, y, z + 1).isOpaque()) {
            cubes.get(0).createBack(vertexBuffer, tempVars);
        }

        if (!neighbours.getBlockType(x, y, z - 1).isOpaque()) {
            cubes.get(0).createFront(vertexBuffer, tempVars);
        }

        tempVars.release();
    }
}
