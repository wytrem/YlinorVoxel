package com.ylinor.server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.AbstractModule;
import com.ylinor.library.api.YlinorApplication;
import com.ylinor.library.api.ecs.systems.SystemsPriorities;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.library.util.ecs.World;
import com.ylinor.library.util.ecs.WorldConfiguration;

import gnu.trove.map.TIntObjectMap;

public class YlinorServer extends YlinorApplication {
    private static YlinorServer server;
    private static final Logger logger = LoggerFactory.getLogger(YlinorServer.class);

    private ServerConfiguration configuration;
    private DatabaseManager databaseManager;
    private World world;
    private Terrain terrain;

    public YlinorServer() {
        logger.info("Loading Ylinor server version Epsilon 0.1");

        initConfiguration();
//        initDatabase();
    }

    private void initConfiguration() {
        this.configuration = new ServerConfiguration();
        File configFile = new File("server.properties");

        logger.info("Loading configuration...");

        try {
            configuration.read(configFile);

            logger.info("Done");
        }
        catch (IOException e) {
            logger.warn("Can't read " + configFile.getName(), e);
        }

        configuration.loadDefaults(false);

        try {
            configuration.write(configFile);
        }
        catch (IOException e) {
            ;
        }
    }

    private void initDatabase() {
        String host = configuration.getProperty("database.host");
        String port = configuration.getProperty("database.port");
        String user = configuration.getProperty("database.user");
        String password = configuration.getProperty("database.password");
        String dbName = configuration.getProperty("database.dbname");

        this.databaseManager = new DatabaseManager(host, Integer.valueOf(port), user, password, dbName);
    }

    @Override
    protected void configure(WorldConfiguration configuration) {
        super.configure(configuration);
        
//      configurationBuilder.dependsOn(SystemsPriorities.Update.UPDATE_PRIORITY, PhySystem.class);
        configuration.with(SystemsPriorities.Update.UPDATE_PRIORITY, NetworkSystem.class);
        configuration.with(new AbstractModule() {
            @Override
            protected void configure() {
                bind(Terrain.class).toInstance(terrain);
                bind(YlinorApplication.class).toInstance(YlinorServer.this);
                bind(YlinorServer.class).toInstance(YlinorServer.this);
                bind(InetSocketAddress.class).toInstance(new InetSocketAddress("localhost", 25565));
            }
        });
        
    }

    private void start() {
        logger.info("Starting Ylinor Server version {}.", getVersion());
        logger.info("Loading terrain.");
        terrain = new ServerTerrain(new File("."));

        logger.info("Creating world object.");
        world = buildWorld();

        run();
    }

    private void run() {
        long lastFrameTime = System.currentTimeMillis();

        while (true) {
            long time = System.currentTimeMillis();
            long deltaTime = time - lastFrameTime;
            lastFrameTime = time;

            world.delta = (deltaTime / 1000.0f);
            world.tick();

            if (deltaTime < (1000 / 50)) {
                try {
                    Thread.sleep((1000 / 50) - deltaTime);
                } catch (InterruptedException e) {
                    ;
                }
            }
        }
    }

    public static YlinorServer server() {
        return server;
    }

    @Override
    public String getVersion() {
        return "0.0.1";
    }

    public static void main(String[] args) {
        instance = server = new YlinorServer();
        server.start();
    }
}
