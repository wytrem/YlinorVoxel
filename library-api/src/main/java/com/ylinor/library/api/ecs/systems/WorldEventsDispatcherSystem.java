package com.ylinor.library.api.ecs.systems;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.artemis.systems.IteratingSystem;
import com.ylinor.library.api.ecs.events.entity.EntityDeletedEvent;
import com.ylinor.library.api.ecs.events.entity.EntitySpawnedEvent;

import net.mostlyoriginal.api.event.common.EventSystem;

public class WorldEventsDispatcherSystem extends IteratingSystem {

    @Wire
    private EventSystem eventSystem;
    
    public WorldEventsDispatcherSystem() {
        super(Aspect.all());
    }

    @Override
    protected void process(int entityId) {
        
    }
    
    @Override
    protected void inserted(int entityId) {
        eventSystem.dispatch(new EntitySpawnedEvent(entityId));
    }
    
    @Override
    protected void removed(int entityId) {
        eventSystem.dispatch(new EntityDeletedEvent(entityId));
    }
}
