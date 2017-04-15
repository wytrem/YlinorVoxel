package com.ylinor.library.util.ecs;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import com.ylinor.library.util.spring.Assert;

@SuppressWarnings("unchecked")
public final class Entity {
    private Map<Class<? extends Component>, Component> components = new HashMap<>();
    private int entityId;
    private transient World world;

    public Entity(int entityId, World world) {
        this.entityId = entityId;
        this.world = world;
    }
    
    public <C extends Component> Entity set(Class<C> clazz) {
        set(world.injector.getInstance(clazz));
        return this;
    }

    public <C extends Component> Entity set(C component) {
        Assert.notNull(component, "Component cannot be null, use unset to remove a component from this entity.");
        components.put(component.getClass(), component);
        world.notifyAspectChanged(entityId);
        return this;
    }
    
    @Nullable
    public <C extends Component> C unset(Class<C> clazz) {
        C result = (C) components.remove(clazz);
        world.notifyAspectChanged(entityId);
        return result;
    }

    public <C extends Component> boolean has(Class<C> clazz) {
        return components.containsKey(clazz);
    }

    @Nullable
    public <C extends Component> C get(Class<C> clazz) {
        return (C) components.get(clazz);
    }
    
    public Set<Class<? extends Component>> componentsTypes() {
        return components.keySet();
    }

    public void delete() {
        world.delete(entityId);
    }
    
    public int getEntityId() {
        return entityId;
    }
    
    @Override
    public String toString() {
        return "Entity [components={" + components.keySet().stream().map(Class::getSimpleName).collect(Collectors.joining(";")) + "}, entityId=" + entityId + "]";
    }
}
