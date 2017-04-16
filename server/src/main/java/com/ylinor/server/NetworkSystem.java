package com.ylinor.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.util.ecs.system.BaseSystem;
import com.ylinor.packets.Packet;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;


@Singleton
public final class NetworkSystem extends BaseSystem {
    private static final Logger logger = LoggerFactory.getLogger(NetworkSystem.class);

    private final Server server;

    @Inject
    private YlinorServer ylinorServer;

    @Inject
    private InetSocketAddress serverAdress;
    TIntObjectMap<PlayerConnection> onlineConnections = new TIntObjectHashMap<>();

    public NetworkSystem() {
        this.server = new Server();
        server.start();

        Packet.registerToKryo(server.getKryo());

        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Packet) {
                    Packet packet = (Packet) object;
                    packet.handle(onlineConnections.get(connection.getID()));
                }
            }

            @Override
            public void connected(Connection connection) {
                PlayerConnection playerConnection = new PlayerConnection(connection);

                Entity newPlayer = world.create();
                onlineConnections.put(playerConnection.getConnectionId(), playerConnection);
                newPlayer.set(new Player(playerConnection));
            }

            @Override
            public void disconnected(Connection connection) {
                TIntObjectIterator<PlayerConnection> iterator = onlineConnections.iterator();

                while (iterator.hasNext()) {
                    iterator.advance();
                    if (iterator.value().isSameConnection(connection)) {
                        iterator.value().disconnect();
                    }
                }
            }
        });
    }

    private void bind(InetSocketAddress address) throws IOException {
        server.bind(address, null);
    }

    @Override
    public void initialize() {
        logger.info("Loading server network system.");
        try {
            bind(serverAdress);
        }
        catch (IOException e) {
            logger.error("Failed to load network system :", e);
        }
    }

    @Override
    protected void processSystem() {
        TIntObjectIterator<PlayerConnection> iterator = onlineConnections.iterator();

        while (iterator.hasNext()) {
            iterator.advance();
            if (iterator.value().shouldDisconnect()) {
                iterator.remove();
                logger.info("Player disconnected :'(");
            }
        }
    }
}
