package com.ylinor.server;

import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.packets.PacketDisconnect;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

public final class Player {
    private final PlayerConnection playerConnection;
    private final Entity entity;
    protected String username;
    private Vector3f position;
    private float pitch, yaw;

    // TODO rename this to nearbyEntities
    private List<Player> nearbyPlayers;

    public Player(PlayerConnection playerConnection, Entity entity) {
        this.entity = entity;
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

    public Entity getEntity() {
        return entity;
    }

    public int getEntityID() {
        return entity.getEntityId();
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

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }
}
