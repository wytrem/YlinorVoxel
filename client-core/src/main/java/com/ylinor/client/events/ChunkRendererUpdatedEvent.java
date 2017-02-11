package com.ylinor.client.events;

import com.ylinor.client.render.ChunkRenderer;
import com.ylinor.library.api.terrain.Chunk;


public class ChunkRendererUpdatedEvent implements ClientEvent {
    public final ChunkRenderer chunkRenderer;
    public final Chunk chunk;

    public ChunkRendererUpdatedEvent(ChunkRenderer chunkRenderer, Chunk chunk) {
        super();
        this.chunkRenderer = chunkRenderer;
        this.chunk = chunk;
    }
}
