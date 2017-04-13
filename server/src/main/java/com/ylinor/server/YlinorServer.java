package com.ylinor.server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import com.ylinor.library.api.ecs.systems.SystemsPriorities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.ylinor.library.api.YlinorApplication;
import com.ylinor.library.api.terrain.Terrain;

public class YlinorServer extends YlinorApplication {
    private static YlinorServer server;
    private static final Logger logger = LoggerFactory.getLogger(YlinorServer.class);

    private ServerConfiguration configuration;
    private DatabaseManager databaseManager;
    private World world;
    private Terrain terrain;
    private List<Player> playersList;

    public YlinorServer() {
        logger.info("Loading Ylinor server version Epsilon 0.1");

        this.playersList = new ArrayList<Player>();

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

//      configurationBuilder.dependsOn(SystemsPriorities.Update.UPDATE_PRIORITY, PhySystem.class);
        configurationBuilder.dependsOn(SystemsPriorities.Update.UPDATE_PRIORITY, NetworkSystem.class);
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
        logger.info("Starting Ylinor Server version {}.", getVersion());
        logger.info("Loading terrain.");
        terrain = new ServerTerrain(new File("."));

        logger.info("Creating world object.");
        world = buildWorld();

        logger.info("Starting network system.");

        try {
            world.getSystem(NetworkSystem.class).init(this, new InetSocketAddress("localhost", 25565));
        } catch (IOException e) {
            e.printStackTrace();
        }

        run();
    }

    private void run() {
        long lastFrameTime = System.currentTimeMillis();

        while (true) {
            long time = System.currentTimeMillis();
            long deltaTime = time - lastFrameTime;
            lastFrameTime = time;

            for (Player player : new ArrayList<>(playersList)) {
                if (player.getPlayerConnection().shouldDisconnect()) {
                    playersList.remove(player);

                    System.out.println("Player disconnected :(");
                }
            }

            world.setDelta(deltaTime / 1000.0f);
            world.process();

            if (deltaTime < (1000 / 50)) {
                try {
                    Thread.sleep((1000 / 50) - deltaTime);
                } catch (InterruptedException e) {
                    ;
                }
            }
        }
    }

    protected void newConnection(PlayerConnection playerConnection) {
        playersList.add(new Player(playerConnection));
    }

    public List<Player> getPlayersList() {
        return playersList;
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
