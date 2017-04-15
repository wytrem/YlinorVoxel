package com.ylinor.library.util.ecs;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;


public class World {
    private static final Logger logger = LoggerFactory.getLogger(World.class);
    
    private TIntObjectMap<Entity> entities = new TIntObjectHashMap<>();
    private List<BaseSystem> systems = new ArrayList<>();

    public float delta;

    private int findFirstEmptyId() {
        for (int i = 1; i < entities.size() + 1; i++) {
            if (!entities.containsKey(i)) {
                return i;
            }
        }

        // This should never happen.
        return 0;
    }

    public void setSystem(BaseSystem sys) {
        systems.add(sys);
        sys.setWorld(this);
    }
    
    public void tick() {
        systems.forEach(BaseSystem::process);
    }

    public Entity create() {
        return create(findFirstEmptyId());
    }

    public Entity create(int entityId) {
        Entity entity = null;
        if (!entities.containsKey(entityId)) {
            entity = new Entity(entityId, this);
            entities.put(entityId, entity);
        }
        else {
            logger.warn("Attempted to override entity {}.", entityId);
            return null;
        }

        notifyCreated(entityId);
        return entity;
    }

    public boolean delete(int entityId) {
        if (!entities.containsKey(entityId)) {
            return false;
        }
        
        notifyDeleted(entityId);
        entities.remove(entityId);
        return true;
    }

    private void notifyCreated(int entityId) {
        notifyAspectChanged(entityId);
    }

    private void notifyDeleted(int entityId) {
        systems.stream().filter(EntitySystem.class::isInstance).map(EntitySystem.class::cast).forEach(s -> s.notifyDeleted(entities.get(entityId)));
    }

    void notifyAspectChanged(int entityId) {
        systems.stream().filter(EntitySystem.class::isInstance).map(EntitySystem.class::cast).forEach(s -> s.notifyAspectChanged(entities.get(entityId)));
    }
}
