package com.ylinor.server;

import com.arangodb.ArangoDB;
import com.arangodb.ArangoDatabase;
import com.arangodb.velocypack.module.jdk8.VPackJdk8Module;

public final class DatabaseManager {
    private final ArangoDB arangoDB;
    private final ArangoDatabase database;

    public DatabaseManager(String host, int port, String user, String password, String dbName) {
        this.arangoDB = new ArangoDB.Builder()
                .host(host, port)
                .user(user)
                .password(password)
                .registerModule(new VPackJdk8Module())
                .build();

        this.database = arangoDB.db(dbName);

        System.out.println("Collections: " + database.getCollections());
    }

    public ArangoDatabase getDatabase() {
        return database;
    }
}
