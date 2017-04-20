package com.ylinor.server;

import com.arangodb.ArangoCollection;
import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.velocypack.module.jdk8.VPackJdk8Module;

public final class DatabaseManager {
    private final ArangoDB arangoDB;
    private final ArangoDatabase database;
    private final ArangoCollection playersCollection;

    public DatabaseManager(String host, int port, String user, String password, String dbName) {
        this.arangoDB = new ArangoDB.Builder()
                .host(host, port)
                .user(user)
                .password(password)
                .registerModule(new VPackJdk8Module())
                .build();

        this.database = arangoDB.db(dbName);
        this.playersCollection = database.collection("players");
    }

    public ArangoDatabase getDatabase() {
        return database;
    }

    public ArangoCollection getPlayersCollection() {
        return playersCollection;
    }
}
