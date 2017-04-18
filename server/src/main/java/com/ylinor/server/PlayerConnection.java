package com.ylinor.server;

import com.esotericsoftware.kryonet.Connection;
import com.ylinor.packets.*;
import org.joml.Vector3f;

public final class PlayerConnection implements PacketHandler {
    private final Connection connection;
    private final ConnectionListener connectionListener;
    private Player player;
    private boolean shouldDisconnect;
    private boolean logged;

    public PlayerConnection(Connection connection) {
        this.connection = connection;

        this.connectionListener = new ConnectionListener(this);
    }


    @Override
    public void handleLogin(PacketLogin login) {
        if (!logged) {
            player.username = login.getAuthToken().toString();

            System.out.println("[PlayerConnection] user logged: " + player.username);

            this.logged = true;
        } else {
            player.kick("Protocol error : logged twice");
        }
    }

    @Override
    public void handleMapChunk(PacketMapChunk packetMapChunk) {

    }

    @Override
    public void handleSpawnEntity(PacketSpawnEntity spawnEntity) {

    }

    @Override
    public void handlePositionUpdate(PacketPositionUpdate positionUpdate) {
        player.setPosition(new Vector3f(positionUpdate.getX(), positionUpdate.getY(), positionUpdate.getZ()));
    }

    @Override
    public void handleDisconnect(PacketDisconnect disconnect) {

    }

    public void sendPacket(Packet packet) {
        connection.sendTCP(packet);
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void close() {
        connection.close();
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean shouldDisconnect() {
        return shouldDisconnect;
    }

    public boolean isLogged() {
        return logged;
    }

    public void disconnect() {
        this.shouldDisconnect = true;
    }

    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }
}
