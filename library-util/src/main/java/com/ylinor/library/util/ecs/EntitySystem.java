package com.ylinor.library.util.ecs;

import java.util.ArrayList;
import java.util.List;


public abstract class EntitySystem extends BaseSystem {
    protected List<Entity> entities;
    protected Aspect aspect;
    
    public EntitySystem(Aspect.Builder builder) {
        this.entities = new ArrayList<>();
        this.aspect = builder.build();
    }

    void notifyAspectChanged(Entity entity) {
        if (entities.contains(entity) && !aspect.matches(entity)) {
            entities.remove(entity);
            removed(entity);
        }
        else if (!entities.contains(entity) && aspect.matches(entity)) {
            entities.add(entity);
            inserted(entity);
        }
    }
    
    void notifyDeleted(Entity entity) {
        if (entities.contains(entity)) {
            entities.remove(entity);
            removed(entity);
        }
    }

    protected void inserted(Entity entity) {
    }

    protected void removed(Entity entity) {
    }
}
