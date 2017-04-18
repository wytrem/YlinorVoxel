package com.ylinor.server;

import com.ylinor.packets.PacketDisconnect;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public final class Player {
    private final PlayerConnection playerConnection;
    private long entityID;
    protected String username;
    private Vector3f position;

    // TODO rename this to nearbyEntites
    private List<Player> nearbyPlayers;

    public Player(PlayerConnection playerConnection, long entityID) {
        this.entityID = entityID;
        this.playerConnection = playerConnection;
        this.position = new Vector3f();
        this.nearbyPlayers = new ArrayList<>();

        playerConnection.setPlayer(this);
    }

    public void kick(String reason) {
        playerConnection.sendPacket(new PacketDisconnect(reason));
        playerConnection.disconnect();
    }

    public PlayerConnection getPlayerConnection() {
        return playerConnection;
    }

    public long getEntityID() {
        return entityID;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public List<Player> getNearbyPlayers() {
        return nearbyPlayers;
    }
}
