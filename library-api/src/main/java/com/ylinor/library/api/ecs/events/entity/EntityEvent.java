package com.ylinor.library.api.ecs.events.entity;

import net.mostlyoriginal.api.event.common.Event;

public abstract class EntityEvent implements Event {
    public final int entityId;

    public EntityEvent(int entityId) {
        this.entityId = entityId;
    }
}
