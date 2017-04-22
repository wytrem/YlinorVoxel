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
import com.ylinor.library.api.ecs.systems.TickingSystem;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.packets.Packet;
import com.ylinor.packets.PacketDespawnEntity;
import com.ylinor.packets.PacketSpawnClientPlayer;
import com.ylinor.packets.PacketSpawnEntity;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;


@Singleton
public class ServerNetworkSystem extends TickingSystem {

    private static final Logger logger = LoggerFactory.getLogger(ServerNetworkSystem.class);

    private Server server;
    private TIntObjectMap<Entity> playersByConnectionId;
    private TIntObjectMap<PlayerConnection> playerConnections;

    @Inject
    NetworkHandlerSystem networkHandlerSystem;

    @Override
    public void initialize() {
        logger.info("Starting up network system.");
        this.server = new Server();

        playersByConnectionId = new TIntObjectHashMap<>();
        playerConnections = new TIntObjectHashMap<>();

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                logger.info("New connection from {}, id: {}", connection.getRemoteAddressTCP()
                                                                        .toString(), connection.getID());
                Entity player = world.create();
                playersByConnectionId.put(connection.getID(), player);

                PlayerConnection playerConnection = new PlayerConnection(connection);
                player.set(playerConnection);
                playerConnections.put(connection.getID(), playerConnection);

                sendToAllExcept(connection.getID(), new PacketSpawnEntity(player.getEntityId()));
                playerConnection.sendPacket(new PacketSpawnClientPlayer(player.getEntityId()));

                for (Entity other : playersByConnectionId.valueCollection()) {
                    if (other.getEntityId() != player.getEntityId()) {
                        playerConnection.sendPacket(new PacketSpawnEntity(other.getEntityId()));
                    }
                }
            }

            @Override
            public void disconnected(Connection connection) {
                
                // TODO @wytrem : cleanup repeating code
                PlayerConnection playerConnection = playerConnections.get(connection.getID());

                playerConnection.close();
                sendToAllExcept(playerConnection.getConnectionId(), new PacketDespawnEntity(playersByConnectionId.get(playerConnection.getConnectionId())
                                                                                                                 .getEntityId()));
                playerConnections.remove(playerConnection.getConnectionId());
                playersByConnectionId.get(playerConnection.getConnectionId())
                                     .delete();
                playersByConnectionId.remove(playerConnection.getConnectionId());
            }
        });

        server.addListener(networkHandlerSystem.getConnectionListener());
        server.start();

        Packet.registerToKryo(server.getKryo());

        try {
            server.bind(new InetSocketAddress(25565), null);
        }
        catch (IOException e) {
            logger.error("Failed to bind server network :", e);
        }

        logger.info("Network system successfully started!");
    }

    @Override
    protected void tick() {

        TIntObjectIterator<Entity> iterator = playersByConnectionId.iterator();

        while (iterator.hasNext()) {
            iterator.advance();

            PlayerConnection connection = iterator.value()
                                                  .get(PlayerConnection.class);

            if (connection.shouldDisconnect()) {
                connection.close();
                sendToAllExcept(connection.getConnectionId(), new PacketDespawnEntity(iterator.value()
                                                                                              .getEntityId()));
                playerConnections.remove(connection.getConnectionId());
                iterator.value().delete();
                iterator.remove();
            }
        }
    }

    public Entity getPlayerByConnectionId(int id) {
        return playersByConnectionId.get(id);
    }

    public Connection getConnection(int id) {
        for (Connection connection : server.getConnections()) {
            if (connection.getID() == id) {
                return connection;
            }
        }

        return null;
    }

    public void sendToAll(Packet object) {
        server.sendToAllTCP(object);
    }

    public void sendToAllExcept(int connectionID, Packet object) {
        server.sendToAllExceptTCP(connectionID, object);
    }

    public void sendTo(int connectionID, Packet object) {
        server.sendToTCP(connectionID, object);
    }

    @Override
    public void dispose() {
        logger.info("Closing connections");

        TIntObjectIterator<PlayerConnection> iterator = playerConnections.iterator();

        while (iterator.hasNext()) {
            iterator.advance();
            iterator.value().kick("Server is shutting down.");
            iterator.value().close();
        }

        server.stop();
    }
}
