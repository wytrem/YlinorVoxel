package com.ylinor.library.api.ecs.events.entity;

import com.ylinor.library.util.ecs.entity.Entity;

public class EntityDeletedEvent extends EntityEvent {
    public EntityDeletedEvent(Entity entity) {
        super(entity);
    }
}
