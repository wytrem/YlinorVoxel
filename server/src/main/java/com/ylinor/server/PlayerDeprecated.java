package com.ylinor.server;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;

import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.packets.PacketDisconnect;

public final class PlayerDeprecated {
    private final PlayerConnection playerConnection;
    private final Entity entity;
    protected String username;
    private Vector3f position;
    private float pitch, yaw;

    // TODO rename this to nearbyEntities
    private List<PlayerDeprecated> nearbyPlayers;

    public PlayerDeprecated(PlayerConnection playerConnection, Entity entity) {
        this.entity = entity;
        this.playerConnection = playerConnection;
        this.position = new Vector3f();
        this.nearbyPlayers = new ArrayList<>();
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public List<PlayerDeprecated> getNearbyPlayers() {
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
