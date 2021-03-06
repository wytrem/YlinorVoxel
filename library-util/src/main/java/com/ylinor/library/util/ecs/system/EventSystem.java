package com.ylinor.library.util.ecs.system;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.common.eventbus.EventBus;
import com.ylinor.library.util.ecs.Event;


@Singleton
public class EventSystem extends BaseSystem {

    @Inject
    private EventBus eventBus;

    @Override
    public void initialize() {
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
