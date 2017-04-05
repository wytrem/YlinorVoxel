package com.ylinor.library.api.terrain;

import org.jetbrains.annotations.NotNull;


public interface IChunkProvider {
    @NotNull
    Chunk getChunk(int x, int z);
}
