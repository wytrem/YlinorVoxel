package com.ylinor.server.systems;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.ylinor.library.api.ecs.systems.TickingSystem;
import com.ylinor.library.api.network.NetworkConnection;
import com.ylinor.library.api.protocol.PacketSender;
import com.ylinor.library.api.protocol.Protocol;
import com.ylinor.library.api.protocol.packets.Packet;
import com.ylinor.library.api.protocol.packets.PacketDespawnEntity;
import com.ylinor.library.util.ecs.entity.Entity;

import gnu.trove.iterator.TIntObjectIterator;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;


@Singleton
public class ServerNetworkSystem extends TickingSystem {

    private static final Logger logger = LoggerFactory.getLogger(ServerNetworkSystem.class);

    private Server server;
    private TIntObjectMap<PacketSender> playerConnections;

    @Inject
    NetworkHandlerSystem networkHandlerSystem;

    @Override
    public void initialize() {
        logger.info("Starting up network system.");
        logger.info("Protocol version: {}.", Protocol.PROTOCOL_VERSION);
        this.server = new Server();

        playerConnections = new TIntObjectHashMap<>();

        server.addListener(new Listener() {
            @Override
            public void connected(Connection connection) {
                logger.info("New connection from {}, id: {}", connection.getRemoteAddressTCP()
                                                                        .toString(), connection.getID());
                NetworkConnection playerConnection = new NetworkConnection(connection);
                playerConnections.put(connection.getID(), new PacketSender(playerConnection));

                // TODO : read player position/rotation in database and synchronise
                // TODO : send near chunks
            }

            @Override
            public void disconnected(Connection connection) {
                // TODO @wytrem : cleanup repeating code
                PacketSender sender = playerConnections.get(connection.getID());

                sender.getConnection().close();
                //                sendToAllExcept(playerConnection.getConnectionId(), new PacketDespawnEntity(playersByConnectionId.get(playerConnection.getConnectionId())
                //                                                                                                                 .getEntityId()));
                playerConnections.remove(sender.getConnectionId());
                //                playersByConnectionId.get(playerConnection.getConnectionId())
                //                                     .delete();
                //                playersByConnectionId.remove(playerConnection.getConnectionId());
            }
        });

        server.addListener(networkHandlerSystem.getConnectionListener());
        server.start();

        Packet.registerToKryo(server.getKryo());

        try {
            server.bind(new InetSocketAddress(Protocol.SERVER_PORT), null);
        }
        catch (IOException e) {
            logger.error("Failed to bind server network :", e);
        }

        logger.info("Network system successfully started!");
    }

    @Override
    protected void tick() {
        TIntObjectIterator<PacketSender> iterator = playerConnections.iterator();

        while (iterator.hasNext()) {
            iterator.advance();

            PacketSender sender = iterator.value();
            NetworkConnection connection = sender.getConnection();

            if (connection.shouldDisconnect()) {
                connection.close();

                if (sender.hasEntity()) {
                    Entity entity = sender.getEntity();

                    // TODO : move into EntityTracker
                    sendToAllExcept(connection, new PacketDespawnEntity(entity.getEntityId()));
                    playerConnections.remove(connection.getConnectionId());
                    entity.delete();
                }

                iterator.remove();
            }
        }
    }

    public Collection<Entity> players() {
        return playerConnections.valueCollection()
                                .stream()
                                .filter(PacketSender::hasEntity)
                                .map(PacketSender::getEntity)
                                .collect(Collectors.toList());
    }

    @Nullable
    public Entity getPlayerByConnectionId(int id) {
        return playerConnections.get(id).getEntity();
    }

    public PacketSender getPacketSender(int connectionId) {
        return playerConnections.get(connectionId);
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

    public void sendToAllExcept(NetworkConnection connection, Packet object) {
        sendToAllExcept(connection.getConnectionId(), object);
    }

    public void sendTo(NetworkConnection connection, Packet object) {
        sendTo(connection.getConnectionId(), object);
    }

    public void sendToAllExcept(PacketSender sender, Packet object) {
        sendToAllExcept(sender.getConnectionId(), object);
    }

    public void sendTo(PacketSender sender, Packet object) {
        sendTo(sender.getConnectionId(), object);
    }

    @Override
    public void dispose() {
        logger.info("Closing connections");

        TIntObjectIterator<PacketSender> iterator = playerConnections.iterator();

        while (iterator.hasNext()) {
            iterator.advance();
            iterator.value().getConnection().kick("Server is shutting down.");
            iterator.value().getConnection().close();
        }

        server.stop();
    }
}
