package com.ylinor.server;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.ylinor.library.api.ecs.components.Player;
import com.ylinor.library.api.ecs.components.Position;
import com.ylinor.library.api.ecs.components.Rotation;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.util.ecs.system.NonProcessingSystem;
import com.ylinor.packets.Packet;
import com.ylinor.packets.PacketDespawnEntity;
import com.ylinor.packets.PacketDisconnect;
import com.ylinor.packets.PacketHandler;
import com.ylinor.packets.PacketLogin;
import com.ylinor.packets.PacketMapChunk;
import com.ylinor.packets.PacketPositionAndRotationUpdate;
import com.ylinor.packets.PacketSpawnClientPlayer;
import com.ylinor.packets.PacketSpawnEntity;


@Singleton
public class NetworkHandlerSystem extends NonProcessingSystem
                implements PacketHandler {

    private static final Logger logger = LoggerFactory.getLogger(NetworkHandlerSystem.class);

    @Inject
    ServerNetworkSystem networkSystem;

    @Inject
    private ConnectionListener connectionListener;

    @Override
    public void initialize() {
    }

    public ConnectionListener getConnectionListener() {
        return connectionListener;
    }

    @Override
    public void handleLogin(Entity sender, PacketLogin login) {
        PlayerConnection connection = sender.get(PlayerConnection.class);

        if (!connection.isLoggedIn()) {
            logger.info("Player {} logged in.", login.getAuthToken());
            sender.set(new Player(login.getAuthToken().toString()));
            sender.set(Position.class);
            sender.set(Rotation.class);
            connection.setLoggedIn(true);
            connection.sendPacket(login);
        }
        else {
            connection.kick("Logged twice.");
        }
    }

    @Override
    public void handleMapChunk(Entity sender, PacketMapChunk packetMapChunk) {

    }

    @Override
    public void handleSpawnEntity(Entity sender, PacketSpawnEntity spawnEntity) {

    }

    @Override
    public void handleDespawnEntity(Entity sender, PacketDespawnEntity despawnEntity) {

    }

    @Override
    public void handlePositionUpdate(Entity sender, PacketPositionAndRotationUpdate positionUpdate) {
        PlayerConnection connection = sender.get(PlayerConnection.class);

        if (!connection.isLoggedIn()) {
            return;
        }

        sender.get(Position.class).position.set(positionUpdate.getX(), positionUpdate.getY(), positionUpdate.getZ());

        Rotation rotation = sender.get(Rotation.class);
        rotation.rotationPitch = positionUpdate.getPitch();
        rotation.rotationYaw = positionUpdate.getYaw();
    }

    @Override
    public void handleDisconnect(Entity sender, PacketDisconnect disconnect) {

    }

    public static final class ConnectionListener extends Listener {

        @Inject
        NetworkHandlerSystem netHandler;

        @Inject
        ServerNetworkSystem netSystem;

        @Override
        public void disconnected(Connection connection) {
        }

        @Override
        public void received(Connection connection, Object object) {
            Entity player = netSystem.getPlayerByConnectionId(connection.getID());

            if (player != null) {
                if (object instanceof Packet) {
                    ((Packet) object).handle(player, netHandler);
                }
                else {
                    logger.error("Player {} sent a non-packet object.", player);
                }
            }
            else {
                logger.error("Received a packet from a void player.");
            }
        }

        @Override
        public void connected(Connection connection) {
            netHandler.connected(connection);
        }
    }

    @Override
    public void handleSpawnClientPlayer(Entity sender, PacketSpawnClientPlayer spawnClientPlayer) {
        
    }

    public void connected(Connection connection) {
         
    }
}
