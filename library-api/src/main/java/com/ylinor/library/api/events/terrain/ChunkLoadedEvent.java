package com.ylinor.library.api.events.terrain;

import com.ylinor.library.api.ecs.events.CancellableEvent;
import com.ylinor.library.api.terrain.Chunk;

public class ChunkLoadedEvent extends CancellableEvent {
    public final Chunk loaded;

    public ChunkLoadedEvent(Chunk loaded) {
        this.loaded = loaded;
    }
}
