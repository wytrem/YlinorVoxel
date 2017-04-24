package com.ylinor.server;

import com.esotericsoftware.kryonet.Connection;
import com.ylinor.library.util.ecs.component.Component;
import com.ylinor.packets.Packet;
import com.ylinor.packets.PacketDisconnect;


public final class PlayerConnection extends Component {
    private final Connection connection;
    private boolean shouldDisconnect;
    private boolean loggedIn;

    public PlayerConnection(Connection connection) {
        this.connection = connection;
    }

    public void kick(String reason) {
        sendPacket(new PacketDisconnect(reason));
        disconnect();
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

    public boolean shouldDisconnect() {
        return shouldDisconnect;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void disconnect() {
        this.shouldDisconnect = true;
    }

    public void setLoggedIn(boolean flag) {
        this.loggedIn = flag;
    }
}
