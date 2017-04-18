package com.ylinor.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.ylinor.packets.Packet;

public final class ConnectionListener extends Listener {
    private final PlayerConnection playerConnection;

    public ConnectionListener(PlayerConnection playerConnection) {
        this.playerConnection = playerConnection;
    }

    @Override
    public void disconnected(Connection connection) {
        if (playerConnection.getConnection() == connection) {
            playerConnection.disconnect();
        }
    }

    @Override
    public void received(Connection connection, Object object) {
        if (playerConnection.getConnection() == connection && object instanceof Packet) {
            ((Packet) object).handle(playerConnection);
        }
    }
}
