package com.ylinor.library.util.ecs;

import com.ylinor.library.util.ecs.Aspect.Builder;

public abstract class IteratingSystem extends EntitySystem {

    public IteratingSystem(Builder builder) {
        super(builder);
    }

    @Override
    protected void processSystem() {
        entities.forEach(this::process);
    }
    
    protected abstract void process(Entity entity);
}
