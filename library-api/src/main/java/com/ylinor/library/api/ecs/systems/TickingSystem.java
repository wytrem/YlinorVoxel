package com.ylinor.library.api.ecs.systems;

import javax.inject.Inject;

import com.ylinor.library.util.ecs.system.BaseSystem;


public abstract class TickingSystem extends BaseSystem {

    @Inject
    private Timer timer;

    @Override
    protected final void processSystem() {
        for (int i = 0; i < timer.elapsedTicks; i++) {
            tick();
        }
    }

    protected abstract void tick();
}
