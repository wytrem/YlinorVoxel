package com.ylinor.library.api.terrain;

import org.jetbrains.annotations.NotNull;

import com.ylinor.library.util.math.PositionableObject2D;


public interface IChunkProvider {
    @NotNull
    Chunk getChunk(PositionableObject2D pos);

    @NotNull
    Chunk getChunk(int x, int z);
}
