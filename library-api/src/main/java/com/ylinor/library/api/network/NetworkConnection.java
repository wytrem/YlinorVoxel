package com.ylinor.library.api.network;

import com.esotericsoftware.kryonet.Connection;
import com.ylinor.library.api.protocol.ConnectionState;
import com.ylinor.library.api.protocol.packets.Packet;
import com.ylinor.library.api.protocol.packets.PacketDisconnect;


public final class NetworkConnection {
    private final Connection connection;
    
    private ConnectionState connectionState;

    public NetworkConnection(Connection connection) {
        this.connection = connection;
        this.connectionState = ConnectionState.HANDSHAKE;
    }

    public void kick(String reason) {
        sendPacket(new PacketDisconnect(reason));
        setConnectionState(ConnectionState.SHOULD_DISCONNECT);
    }

    public int getConnectionId() {
        return connection.getID();
    }

    public void sendPacket(Packet packet) {
        connection.sendTCP(packet);
    }

    public void close() {
        connection.close();
    }

    public Connection getConnection() {
        return connection;
    }
    
    public void setConnectionState(ConnectionState connectionState) {
        this.connectionState = connectionState;
    }

    public boolean shouldDisconnect() {
        return connectionState.ordinal() > ConnectionState.SHOULD_DISCONNECT.ordinal();
    }

    public boolean isLoggedIn() {
        return connectionState.ordinal() > ConnectionState.HANDSHAKE.ordinal();
    }
}
