package com.ylinor.server;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.ylinor.library.api.YlinorApplication;
import com.ylinor.library.api.terrain.Terrain;


public class YlinorServer extends YlinorApplication {
    private static YlinorServer server;

    private World world;
    private Terrain terrain;

    public YlinorServer() {
        instance = this;
        server = this;
    }

    public static void main(String[] args) {
        new YlinorServer().start();
    }

    @Override
    protected void preConfigure(WorldConfigurationBuilder configurationBuilder) {
        super.preConfigure(configurationBuilder);
        //        configurationBuilder.dependsOn(SystemsPriorities.Update.UPDATE_PRIORITY, PhySystem.class);
    }

    @Override
    protected void configure(WorldConfiguration configuration) {
        super.configure(configuration);
        // We want to inject any Terrain field with this
        configuration.register(Terrain.class.getName(), terrain);
        configuration.register(terrain);
        configuration.register(this);
    }

    private void start() {

        world = buildWorld();

        lastRun = System.currentTimeMillis();
        run();
    }

    long lastRun;

    private void run() {
        long deltaMillis = System.currentTimeMillis() - lastRun;

        float delta = deltaMillis / 1000.f;
        world.setDelta(delta);
        world.process();
    }

    public static YlinorServer server() {
        return server;
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }
}
