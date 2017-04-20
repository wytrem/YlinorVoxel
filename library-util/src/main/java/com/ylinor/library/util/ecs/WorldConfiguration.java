package com.ylinor.library.util.ecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ylinor.library.util.ecs.system.BaseSystem;


public class WorldConfiguration {
    private List<Module> injectorModules = new ArrayList<>();
    private List<Class<? extends BaseSystem>> systemsClasses = new ArrayList<>();
    
    @SafeVarargs
    public final WorldConfiguration with(Class<? extends BaseSystem>...classes) {
        return with(0, classes);
    }
    
    @SafeVarargs
    public final WorldConfiguration with(int priority, Class<? extends BaseSystem>...classes) {
        systemsClasses.addAll(Arrays.asList(classes));
        return this;
    }
    
    public WorldConfiguration with(Module module) {
        injectorModules.add(module);
        return this;
    }

    public World build() {
        Injector injector = Guice.createInjector(injectorModules);
        World world = new World(injector);
       
        for (Class<? extends BaseSystem> clazz : systemsClasses) {
            world.setSystem(injector.getInstance(clazz));
        }
        
        world.initialize();

        return world;
    }
}
