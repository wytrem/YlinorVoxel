package com.ylinor.server;

public class Player {
    private final PlayerConnection playerConnection;

    public Player(PlayerConnection playerConnection) {
        this.playerConnection = playerConnection;
    }

    public PlayerConnection getPlayerConnection() {
        return playerConnection;
    }
}
