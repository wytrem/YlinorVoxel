package com.ylinor.library.util.ecs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.ylinor.library.util.Pair;
import com.ylinor.library.util.ecs.system.BaseSystem;


public class WorldConfiguration {
    private List<Module> injectorModules = new ArrayList<>();
    private List<Pair<Integer, Class<? extends BaseSystem>>> systemsClasses = new ArrayList<>();
    
    @SafeVarargs
    public final WorldConfiguration with(Class<? extends BaseSystem>...classes) {
        return with(0, classes);
    }
    
    @SafeVarargs
    public final WorldConfiguration with(int priority, Class<? extends BaseSystem>...classes) {
        for (Class<? extends BaseSystem> clazz : classes) {
            systemsClasses.add(Pair.of(Integer.valueOf(priority), clazz));
        }
        return this;
    }
    
    public WorldConfiguration with(Module module) {
        injectorModules.add(module);
        return this;
    }

    public World build() {
        Injector injector = Guice.createInjector(injectorModules);
        World world = new World(injector);
       
        Collections.sort(systemsClasses, new Comparator<Pair<Integer, ?>>() {

            @Override
            public int compare(Pair<Integer, ?> o1, Pair<Integer, ?> o2) {
                return -o1.getKey().compareTo(o2.getKey());
            }
        });
        
        for (Pair<?, Class<? extends BaseSystem>> pair : systemsClasses) {
            world.setSystem(injector.getInstance(pair.getValue()));
        }
        
        world.initialize();

        return world;
    }
}
