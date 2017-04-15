package com.ylinor.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.inject.Singleton;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.ylinor.library.util.ecs.BaseSystem;
import com.ylinor.packets.Packet;
@Singleton
public final class NetworkSystem extends BaseSystem {
    private final Server server;
    private YlinorServer ylinorServer;

    public NetworkSystem() {
        this.server = new Server();
        server.start();

        Packet.registerToKryo(server.getKryo());

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Packet) {
                    Packet packet = (Packet) object;

                    for (Player player : ylinorServer.getPlayersList()) {
                        if (player.getPlayerConnection().isSameConnection(connection)) {
                            packet.handle(player.getPlayerConnection());

                            break;
                        }
                    }
                }
            }

            @Override
            public void connected(Connection connection) {
                PlayerConnection playerConnection = new PlayerConnection(connection);

                ylinorServer.newConnection(playerConnection);
            }

            @Override
            public void disconnected(Connection connection) {
                ylinorServer.getPlayersList().forEach(player -> {
                    if (player.getPlayerConnection().isSameConnection(connection)) {
                        player.getPlayerConnection().disconnect();
                    }
                });
            }
        });
    }

    private void bind(InetSocketAddress address) throws IOException {
        server.bind(address, null);
    }

    public void init(YlinorServer server, InetSocketAddress address) throws IOException {
        this.ylinorServer = server;

        bind(address);
    }

    @Override
    protected void processSystem() {

    }
}
