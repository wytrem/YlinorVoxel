package com.ylinor.library.api.ecs.systems;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;

public abstract class TickingSystem extends BaseSystem {
    
    @Wire
    private Timer timer;
    
    @Override
    protected final void processSystem() {
        for (int i = 0; i < timer.elapsedTicks; i++) {
            tick();
        }
    }
    
    protected abstract void tick();
}
