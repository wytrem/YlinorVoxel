package com.ylinor.library.util.ecs.system;

import javax.inject.Singleton;

import com.google.common.eventbus.EventBus;
import com.ylinor.library.util.ecs.Event;

@Singleton
public class EventSystem extends BaseSystem {
    
    private EventBus eventBus;
    
    @Override
    public void initialize() {
        eventBus = new EventBus();
        
        for (BaseSystem system : world.getSystems()) {
            eventBus.register(system);
        }
    }

    @Override
    protected void processSystem() {
        
    }

    public void dispatch(Event obj) {
        eventBus.post(obj);
    }

    public void dispatch(Class<? extends Event> clazz) {
        dispatch(world.injector.getInstance(clazz));
    }
}
