package com.ylinor.library.api.ecs.systems;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.ylinor.library.api.ecs.events.entity.EntityDeletedEvent;
import com.ylinor.library.api.ecs.events.entity.EntitySpawnedEvent;
import com.ylinor.library.util.ecs.entity.Aspect;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.util.ecs.system.EventSystem;
import com.ylinor.library.util.ecs.system.IteratingSystem;


@Singleton
public class WorldEventsDispatcherSystem extends IteratingSystem {

    @Inject
    private EventSystem eventSystem;

    public WorldEventsDispatcherSystem() {
        super(Aspect.all());
    }

    @Override
    protected void process(Entity entityId) {

    }

    @Override
    protected void inserted(Entity entityId) {
        eventSystem.dispatch(new EntitySpawnedEvent(entityId));
    }

    @Override
    protected void removed(Entity entityId) {
        eventSystem.dispatch(new EntityDeletedEvent(entityId));
    }
}
