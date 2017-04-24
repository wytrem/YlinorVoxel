package com.ylinor.library.util.ecs.system;

import java.util.ArrayList;
import java.util.List;

import com.ylinor.library.util.ecs.entity.Aspect;
import com.ylinor.library.util.ecs.entity.Entity;


public abstract class EntitySystem extends BaseSystem {
    protected List<Entity> entities;
    protected Aspect aspect;

    public EntitySystem(Aspect.Builder builder) {
        this.entities = new ArrayList<>();
        this.aspect = builder.build();
    }

    public void notifyAspectChanged(Entity entity) {
        if (entities.contains(entity) && !aspect.matches(entity)) {
            entities.remove(entity);
            removed(entity);
        }
        else if (!entities.contains(entity) && aspect.matches(entity)) {
            entities.add(entity);
            inserted(entity);
        }
    }

    public void notifyDeleted(Entity entity) {
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
