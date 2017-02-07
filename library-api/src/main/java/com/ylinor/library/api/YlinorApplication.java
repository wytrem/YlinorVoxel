package com.ylinor.library.api;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.mostlyoriginal.api.event.common.EventSystem;


public class YlinorApplication {
    private ObjectMapper mapper = new ObjectMapper();
    protected static YlinorApplication instance;

    public final World buildWorld() {
        WorldConfiguration configuration = new WorldConfiguration();
        configure(configuration);
        return new World(configuration);
    }

    protected void configure(WorldConfiguration configuration) {
        configuration.setSystem(EventSystem.class);
    }

    public static YlinorApplication getYlinorApplication() {
        return instance;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
