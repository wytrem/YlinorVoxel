package com.ylinor.server;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import com.ylinor.library.api.YlinorApplication;
import com.ylinor.library.api.terrain.Terrain;
import com.ylinor.packets.Dictionnary;

import net.mostlyoriginal.api.network.marshal.kryonet.KryonetServerMarshalStrategy;
import net.mostlyoriginal.api.network.system.MarshalSystem;

import java.io.File;
import java.io.IOException;

public class YlinorServer extends YlinorApplication {
    private static YlinorServer server;

    private ServerConfiguration configuration;
    private DatabaseManager databaseManager;
    private World world;
    private Terrain terrain;
    private KryonetServerMarshalStrategy kryonetServerMarshalStrategy;
    private MarshalSystem marshalSystem;

    public YlinorServer() {
        this.configuration = new ServerConfiguration();
        loadConfiguration(new File("server.properties"));

        initDatabase();
    }

    private void initDatabase() {
        String host = configuration.getProperty("database.host");
        String port = configuration.getProperty("database.port");
        String user = configuration.getProperty("database.user");
        String password = configuration.getProperty("database.password");
        String dbName = configuration.getProperty("database.dbname");

        this.databaseManager = new DatabaseManager(host, Integer.valueOf(port), user, password, dbName);
    }

    private void loadConfiguration(File configFile) {
        if (configFile.exists()) {
            try {
                configuration.read(configFile);
                configuration.removeUnknownKeys();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        configuration.loadDefaults(false);
        saveConfiguration(configFile);
    }

    private void saveConfiguration(File configFile) {
        try {
            configuration.write(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        marshalSystem = new MarshalSystem(Dictionnary.MARSHAL_DICTIONARY, kryonetServerMarshalStrategy);
        configuration.setSystem(marshalSystem);
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

    public static void main(String[] args) {
        instance = server = new YlinorServer();
        server.start();
    }
}
