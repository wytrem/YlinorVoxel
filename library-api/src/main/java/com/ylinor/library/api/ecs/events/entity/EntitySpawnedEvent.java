package com.ylinor.library.api.ecs.events.entity;

import com.ylinor.library.util.ecs.entity.Entity;


public class EntitySpawnedEvent extends EntityEvent {
    public EntitySpawnedEvent(Entity entity) {
        super(entity);
    }
}
