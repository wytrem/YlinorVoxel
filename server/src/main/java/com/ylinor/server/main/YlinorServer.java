package com.ylinor.server.main;

import com.google.inject.AbstractModule;
import com.ylinor.library.api.YlinorApplication;
import com.ylinor.library.util.ecs.World;
import com.ylinor.library.util.ecs.WorldConfiguration;
import com.ylinor.server.CommandLineSystem;
import com.ylinor.server.FramerateLimitSystem;
import com.ylinor.server.ServerNetworkSystem;


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
        configuration.with(Integer.MIN_VALUE, FramerateLimitSystem.class);

        configuration.with(new AbstractModule() {

            @Override
            protected void configure() {
                bind(YlinorServer.class).toInstance(YlinorServer.this);
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
