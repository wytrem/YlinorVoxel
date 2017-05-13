package com.ylinor.server.main;

import java.io.File;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.ylinor.library.api.YlinorApplication;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.util.ecs.World;
import com.ylinor.library.util.ecs.WorldConfiguration;
import com.ylinor.server.ServerTerrain;
import com.ylinor.server.systems.CommandLineSystem;
import com.ylinor.server.systems.DatabaseSystem;
import com.ylinor.server.systems.EntityTrackerSystem;
import com.ylinor.server.systems.FramerateLimitSystem;
import com.ylinor.server.systems.NetworkHandlerSystem;
import com.ylinor.server.systems.ServerConfigurationSystem;
import com.ylinor.server.systems.ServerNetworkSystem;


public final class YlinorServer extends YlinorApplication {
    private final World world;
    private volatile boolean running;

    private YlinorServer() {
        this.world = buildWorld();

        this.running = true;
    }

    @Override
    protected void configure(WorldConfiguration configuration) {
        super.configure(configuration);

        configuration.with(ServerNetworkSystem.class);
        configuration.with(CommandLineSystem.class);
        configuration.with(DatabaseSystem.class);
        configuration.with(NetworkHandlerSystem.class);
        configuration.with(EntityTrackerSystem.class);
        configuration.with(Integer.MAX_VALUE, ServerConfigurationSystem.class);
        configuration.with(Integer.MIN_VALUE, FramerateLimitSystem.class);

        configuration.with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(YlinorServer.class).toInstance(YlinorServer.this);
                bind(File.class).annotatedWith(Names.named("configFile"))
                                .toInstance(new File("server.properties"));
                bind(File.class).annotatedWith(Names.named("mapFolder"))
                                .toInstance(new File(".", "map/"));
                bind(Terrain.class).to(ServerTerrain.class);
            }
        });
    }

    private void run() {
        long lastPass = System.currentTimeMillis();
        long now;
        float delta = 0.0f;
        while (running) {
            now = System.currentTimeMillis();
            delta = (now - lastPass) / 1000.0f;
            world.delta = delta;
            world.process();
            lastPass = now;
        }
    }

    public void stop() {
        running = false;
    }

    public static void main(String[] args) {
        new YlinorServer().run();
    }

    @Override
    public String getVersion() {
        return "0.1";
    }
}
