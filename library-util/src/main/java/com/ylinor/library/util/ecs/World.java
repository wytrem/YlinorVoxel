package com.ylinor.library.util.ecs;

import java.util.ArrayList;
import java.util.List;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;


public class World {
    private TIntObjectMap<Entity> entities = new TIntObjectHashMap<>();
    private List<System> systems = new ArrayList<>();

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

    public void setSystem(System sys) {
        systems.add(sys);
    }
    
    public void tick() {
        systems.forEach(System::process);
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

        notifyCreated(entityId);
        return entity;
    }

    public boolean delete(int entityId) {
        if (!entities.containsKey(entityId)) {
            return false;
        }

        entities.remove(entityId);
        notifyDeleted(entityId);
        return true;
    }

    private void notifyCreated(int entityId) {
        notifyAspectChanged(entityId);
    }

    private void notifyDeleted(int entityId) {
        systems.stream().filter(s -> s instanceof EntitySystem).map(EntitySystem.class::cast).forEach(s -> s.notifyDeleted(entities.get(entityId)));
    }

    void notifyAspectChanged(int entityId) {
        systems.stream().filter(s -> s instanceof EntitySystem).map(EntitySystem.class::cast).forEach(s -> s.notifyAspectChanged(entities.get(entityId)));
    }
}
