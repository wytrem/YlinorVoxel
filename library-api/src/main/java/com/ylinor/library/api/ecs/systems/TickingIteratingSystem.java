package com.ylinor.library.api.ecs.systems;

import javax.inject.Inject;

import com.ylinor.library.util.ecs.entity.Aspect.Builder;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.util.ecs.system.EntitySystem;


public abstract class TickingIteratingSystem extends EntitySystem {

    @Inject
    private Timer timer;

    public TickingIteratingSystem(Builder aspect) {
        super(aspect);
    }

    /** @inheritDoc */
    @Override
    protected final void processSystem() {
        for (int j = 0; j < this.timer.elapsedTicks; ++j) {
            this.tickEntities();
        }
    }

    protected void tickEntities() {
        for (Entity entity : entities) {
            tickEntity(entity);
        }
    }

    protected abstract void tickEntity(Entity entity);

}
