package com.ylinor.server;

import com.esotericsoftware.kryonet.Connection;
import com.ylinor.packets.PacketHandler;
import com.ylinor.packets.PacketLogin;
import com.ylinor.packets.PacketMapChunk;

public final class PlayerConnection implements PacketHandler {
    private final Connection connection;
    private boolean shouldDisconnect;

    public PlayerConnection(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void handleLogin(PacketLogin login) {
        System.out.println("New user loged in: " + login.getAuthToken());
    }
    
    @Override
    public void handleMapChunk(PacketMapChunk packetMapChunk) {
        
    }

    public void disconnect() {
        this.shouldDisconnect = true;
    }

    public boolean shouldDisconnect() {
        return shouldDisconnect;
    }

    public boolean isSameConnection(Connection connection) {
        return this.connection.getID() == connection.getID();
    }
}
