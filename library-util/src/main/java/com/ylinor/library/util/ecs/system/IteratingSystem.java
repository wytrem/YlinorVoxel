package com.ylinor.library.util.ecs.system;

import com.ylinor.library.util.ecs.entity.Aspect.Builder;
import com.ylinor.library.util.ecs.entity.Entity;

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
