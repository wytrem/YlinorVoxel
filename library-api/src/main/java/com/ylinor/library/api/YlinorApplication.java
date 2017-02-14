package com.ylinor.library.api;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ylinor.library.api.ecs.systems.SystemsPriorities;
import com.ylinor.library.api.ecs.systems.Timer;
import com.ylinor.library.api.ecs.systems.TimerUpdateSystem;
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
        configurationBuilder.dependsOn(SystemsPriorities.Update.TIMER_UPDATE, TimerUpdateSystem.class);

        configurationBuilder.dependsOn(SystemsPriorities.Update.WORLD_UPDATE, WorldEventsDispatcherSystem.class);
        configurationBuilder.dependsOn(SystemsPriorities.Update.EVENT_DISPATCH, EventSystem.class);
    }
    
    protected void configure(WorldConfiguration configuration) {
        configuration.register(new Timer(20.0f));
    }

    public static YlinorApplication getYlinorApplication() {
        return instance;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
