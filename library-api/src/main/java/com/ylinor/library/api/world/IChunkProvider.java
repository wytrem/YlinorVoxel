package com.ylinor.library.api.world;

import com.ylinor.library.util.math.PositionableObject2D;
import org.jetbrains.annotations.NotNull;

public interface IChunkProvider
{
    @NotNull Chunk getChunk(PositionableObject2D pos);

    @NotNull Chunk getChunk(int x, int z);
}
