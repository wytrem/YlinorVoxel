package com.ylinor.server;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.ylinor.library.api.YlinorApplication;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.packets.NetworkableObjects;

import net.mostlyoriginal.api.network.marshal.common.MarshalObserver;
import net.mostlyoriginal.api.network.marshal.common.MarshalState;
import net.mostlyoriginal.api.network.marshal.kryonet.KryonetServerMarshalStrategy;
import net.mostlyoriginal.api.network.system.MarshalSystem;


public class YlinorServer extends YlinorApplication {
    private static YlinorServer server;
    private static final Logger logger = LoggerFactory.getLogger(YlinorServer.class);

    private ServerConfiguration configuration;
    private DatabaseManager databaseManager;
    private World world;
    private Terrain terrain;
    private KryonetServerMarshalStrategy kryonetServerMarshalStrategy;
    private MarshalSystem marshalSystem;

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

        kryonetServerMarshalStrategy = new KryonetServerMarshalStrategy("localhost", 32321);
        marshalSystem = new MarshalSystem(NetworkableObjects.MARSHAL_DICTIONARY, kryonetServerMarshalStrategy);
        configuration.setSystem(marshalSystem);
        marshalSystem.getMarshal().setListener(new MarshalObserver() {
            @Override
            public void received(int connectionId, Object object) {
                logger.info("Received (from {}) : {}", connectionId, object);
            }

            @Override
            public void disconnected(int connectionId) {
                logger.info("Disconnected {}", connectionId);
            }

            @Override
            public void connected(int connectionId) {
                logger.info("Connected {}", connectionId);
            }
        });
    }

    private void start() {
        logger.info("Starting Ylinor Server version {}.", getVersion());
        logger.info("Loading terrain.");
        terrain = new ServerTerrain(new File("."));

        logger.info("Creating world object.");
        world = buildWorld();

        logger.info("Starting network system.");
        marshalSystem.start();

        if (marshalSystem.getState() != MarshalState.STARTED) {
            logger.error("Network start failed, aborting.");
            System.exit(-1);
        }

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

    public static void main(String[] args) {
        instance = server = new YlinorServer();
        server.start();
    }
}
