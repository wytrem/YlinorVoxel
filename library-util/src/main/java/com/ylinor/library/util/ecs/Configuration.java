package com.ylinor.library.util.ecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;


public class Configuration {
    private List<Module> injectorModules = new ArrayList<>();
    private List<Class<? extends BaseSystem>> systemsClasses = new ArrayList<>();
    
    @SafeVarargs
    public final Configuration with(Class<? extends BaseSystem>...classes) {
        return with(0, classes);
    }
    
    @SafeVarargs
    public final Configuration with(int priority, Class<? extends BaseSystem>...classes) {
        systemsClasses.addAll(Arrays.asList(classes));
        return this;
    }
    
    public Configuration with(Module module) {
        injectorModules.add(module);
        return this;
    }

    public World build() {
        World world = new World();

        Injector injector = Guice.createInjector(injectorModules);

        for (Class<? extends BaseSystem> clazz : systemsClasses) {
            world.setSystem(injector.getInstance(clazz));
        }

        return world;
    }
}
