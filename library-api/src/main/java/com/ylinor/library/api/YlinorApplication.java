package com.ylinor.library.api;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylinor.library.api.ecs.systems.SystemsPriorities;
import com.ylinor.library.api.ecs.systems.WorldEventsDispatcherSystem;

import net.mostlyoriginal.api.event.common.EventSystem;


public class YlinorApplication {
    private ObjectMapper mapper = new ObjectMapper();
    protected static YlinorApplication instance;

    public final World buildWorld() {
        
        WorldConfigurationBuilder configurationBuilder = new WorldConfigurationBuilder();
        preConfigure(configurationBuilder);
        WorldConfiguration configuration = configurationBuilder.build();
        configure(configuration);
        return new World(configuration);
    }

    protected void preConfigure(WorldConfigurationBuilder configurationBuilder) {
        configurationBuilder.dependsOn(SystemsPriorities.UPDATE_PRIORITY, EventSystem.class, WorldEventsDispatcherSystem.class);
    }
    
    protected void configure(WorldConfiguration configuration) {
        
    }

    public static YlinorApplication getYlinorApplication() {
        return instance;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
