package com.ylinor.library.api.ecs.events.entity;

import com.ylinor.library.util.ecs.Entity;
import com.ylinor.library.util.ecs.Event;

public abstract class EntityEvent implements Event {
    public final Entity entity;

    public EntityEvent(Entity entityId) {
        this.entity = entityId;
    }
}
