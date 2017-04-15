package com.ylinor.library.util.ecs.system;

import javax.inject.Singleton;

import com.ylinor.library.util.ecs.Event;


@Singleton
public class EventSystem extends BaseSystem {

    @Override
    protected void processSystem() {
    }

    public void dispatch(Event obj) {

    }

    public void dispatch(Class<? extends Event> clazz) {
        dispatch(world.injector.getInstance(clazz));
    }
}
