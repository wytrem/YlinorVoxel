package com.ylinor.library.api.ecs.events.entity;

public class EntityDeletedEvent extends EntityEvent {
    public EntityDeletedEvent(int entityId) {
        super(entityId);
    }
}
