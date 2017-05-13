package com.ylinor.server.systems;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.velocypack.module.jdk8.VPackJdk8Module;
import com.ylinor.library.util.ecs.system.NonProcessingSystem;


@Singleton
public final class DatabaseSystem extends NonProcessingSystem {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSystem.class);

    private ArangoDB arangoDB;
    private ArangoDatabase database;
    private ArangoCollection playersCollection;

    @Inject
    ServerConfigurationSystem configurationSystem;

    @Override
    public void initialize() {
        logger.info("Connecting to database at {}:{}", configurationSystem.get("database.host"), configurationSystem.get("database.port"));

        this.arangoDB = new ArangoDB.Builder().host(configurationSystem.get("database.host"), configurationSystem.getInt("database.port"))
                                              .user(configurationSystem.get("database.user"))
                                              .password(configurationSystem.get("database.password"))
                                              .registerModule(new VPackJdk8Module())
                                              .build();

        this.database = arangoDB.db(configurationSystem.get("database.dbname"));
        this.playersCollection = database.collection("players");
    }

    public ArangoDatabase getDatabase() {
        return database;
    }

    public ArangoCollection getPlayersCollection() {
        return playersCollection;
    }
}
