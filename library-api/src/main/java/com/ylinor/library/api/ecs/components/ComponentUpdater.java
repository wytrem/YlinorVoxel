package com.ylinor.library.api.ecs.components;

import com.ylinor.library.util.ecs.component.Component;

public interface ComponentUpdater<C extends Component> {
    void update(C component);
    
    Class<C> getComponentClass();
}
