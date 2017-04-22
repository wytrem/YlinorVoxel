package com.ylinor.library.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.AbstractModule;
import com.ylinor.library.api.ecs.systems.SystemsPriorities;
import com.ylinor.library.api.ecs.systems.Timer;
import com.ylinor.library.api.ecs.systems.TimerUpdateSystem;
import com.ylinor.library.api.ecs.systems.WorldEventsDispatcherSystem;
import com.ylinor.library.util.ecs.World;
import com.ylinor.library.util.ecs.WorldConfiguration;
import com.ylinor.library.util.ecs.system.EventSystem;


public abstract class YlinorApplication {
    private ObjectMapper mapper = new ObjectMapper();

    public final World buildWorld() {
        
        WorldConfiguration configuration = new WorldConfiguration();
        configure(configuration);
        return configuration.build();
    }
    
    public abstract String getVersion();

    protected void configure(WorldConfiguration configuration) {
        
        configuration.with(SystemsPriorities.Update.TIMER_UPDATE, TimerUpdateSystem.class);

        configuration.with(SystemsPriorities.Update.WORLD_UPDATE, WorldEventsDispatcherSystem.class);
        configuration.with(SystemsPriorities.Update.EVENT_DISPATCH, EventSystem.class);
        configuration.with(new AbstractModule() {
            
            @Override
            protected void configure() {
                bind(Timer.class).toInstance(new Timer(20.0f));
                bind(YlinorApplication.class).toInstance(YlinorApplication.this);
                bind(ObjectMapper.class).toInstance(mapper);
            }
        });
    }
}
