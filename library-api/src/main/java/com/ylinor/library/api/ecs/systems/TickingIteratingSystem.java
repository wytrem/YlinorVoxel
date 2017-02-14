package com.ylinor.library.api.ecs.systems;

import com.artemis.Aspect.Builder;
import com.artemis.BaseEntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.utils.IntBag;


public abstract class TickingIteratingSystem extends BaseEntitySystem {

    @Wire
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
        IntBag actives = subscription.getEntities();
        int[] ids = actives.getData();
        for (int i = 0, s = actives.size(); s > i; i++) {
            tickEntity(ids[i]);
        }
    }

    protected abstract void tickEntity(int i);

}
