package com.ylinor.library.api.protocol;

import org.jetbrains.annotations.Nullable;

import com.ylinor.auth.client.YlinorUser;
import com.ylinor.library.api.network.NetworkConnection;
import com.ylinor.library.api.protocol.packets.Packet;
import com.ylinor.library.util.ecs.entity.Entity;


public class PacketSender {

    private final NetworkConnection connection;

    @Nullable
    private YlinorUser user;

    @Nullable
    private Entity entity;

    public PacketSender(NetworkConnection connection) {
        this.connection = connection;
    }

    public NetworkConnection getConnection() {
        return connection;
    }

    public YlinorUser getUser() {
        return user;
    }

    public void setUser(YlinorUser user) {
        this.user = user;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }
    
    public boolean hasEntity() {
        return entity != null;
    }
    
    public int getConnectionId() {
        return connection.getConnectionId();
    }

    public void kick(String reason) {
        connection.kick(reason);
    }

    public void sendPacket(Packet packet) {
        connection.sendPacket(packet);
    }

    public void setConnectionState(ConnectionState connectionState) {
        connection.setConnectionState(connectionState);
    }
}
