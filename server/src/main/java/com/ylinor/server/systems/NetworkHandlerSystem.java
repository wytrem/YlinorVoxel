package com.ylinor.server.systems;

import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.ylinor.auth.client.YlinorUser;
import com.ylinor.auth.client.model.AuthException;
import com.ylinor.library.api.ecs.components.Player;
import com.ylinor.library.api.ecs.components.Position;
import com.ylinor.library.api.ecs.components.Rotation;
import com.ylinor.library.api.protocol.ConnectionState;
import com.ylinor.library.api.protocol.PacketSender;
import com.ylinor.library.api.protocol.packets.Packet;
import com.ylinor.library.api.protocol.packets.PacketCharacterSelection;
import com.ylinor.library.api.protocol.packets.PacketCharactersList;
import com.ylinor.library.api.protocol.packets.PacketDespawnEntity;
import com.ylinor.library.api.protocol.packets.PacketDisconnect;
import com.ylinor.library.api.protocol.packets.PacketHandler;
import com.ylinor.library.api.protocol.packets.PacketLogin;
import com.ylinor.library.api.protocol.packets.PacketMapChunk;
import com.ylinor.library.api.protocol.packets.PacketPositionAndRotationUpdate;
import com.ylinor.library.api.protocol.packets.PacketSpawnClientPlayer;
import com.ylinor.library.api.protocol.packets.PacketSpawnEntity;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.util.ecs.system.NonProcessingSystem;


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
    public void handleLogin(PacketSender sender, PacketLogin login) {

        if (!sender.getConnection().isLoggedIn()) {
            YlinorUser user = new YlinorUser();

            try {
                user.fetch(login.getAuthToken());
            }
            catch (AuthException | IOException e) {
                logger.warn("Login failed from address {}.", sender.getConnection()
                                                                   .getConnection()
                                                                   .getRemoteAddressTCP()
                                                                   .toString());
                sender.kick("Login failed.");
                return;
            }

            logger.info("Player {} successfully logged in.", user.user()
                                                                 .getUsername());
            sender.setConnectionState(ConnectionState.SELECTING_CHARACTER);
            sender.sendPacket(login);
            sender.sendPacket(new PacketCharactersList());
        }
        else {
            sender.kick("Logged twice.");
        }
    }

    @Override
    public void handleMapChunk(PacketSender sender, PacketMapChunk packetMapChunk) {

    }

    @Override
    public void handleSpawnEntity(PacketSender sender, PacketSpawnEntity spawnEntity) {

    }

    @Override
    public void handleDespawnEntity(PacketSender sender, PacketDespawnEntity despawnEntity) {

    }

    @Override
    public void handlePositionUpdate(PacketSender sender, PacketPositionAndRotationUpdate positionUpdate) {
        if (!sender.getConnection().isLoggedIn()) {
            return;
        }

        Entity entity = sender.getEntity();

        entity.get(Position.class).position.set(positionUpdate.getX(), positionUpdate.getY(), positionUpdate.getZ());

        Rotation rotation = entity.get(Rotation.class);
        rotation.rotationPitch = positionUpdate.getPitch();
        rotation.rotationYaw = positionUpdate.getYaw();
    }

    @Override
    public void handleDisconnect(PacketSender sender, PacketDisconnect disconnect) {

    }

    @Override
    public void handleSpawnClientPlayer(PacketSender sender, PacketSpawnClientPlayer spawnClientPlayer) {
        sender.kick("Invalid packet received (PacketSpawnClientPlayer).");
    }

    @Override
    public void handleCharacterSelection(PacketSender sender, PacketCharacterSelection packetCharacterSelection) {
        // TODO : spawn player in world
        Entity player = world.create();
        player.set(new Player(sender));
        sender.setEntity(player);
        Position position = new Position();
        position.position.set(1818, 126, 6710);
        player.set(position);
        player.set(new Rotation());
        
        // TODO : move into EntityTracker
        networkSystem.sendToAllExcept(sender, new PacketSpawnEntity(player.getEntityId()));
        sender.sendPacket(new PacketSpawnClientPlayer(player.getEntityId(), position.position.x, position.position.y, position.position.z));

        for (Entity other : networkSystem.players()) {
            if (other.getEntityId() != player.getEntityId()) {
                sender.sendPacket(new PacketSpawnEntity(other.getEntityId()));
            }
        }
        // -- end todo
    }

    @Override
    public void handleCharacterList(PacketSender sender, PacketCharactersList packetCharactersList) {
        sender.kick("Invalid packet received (PacketCharactersList).");
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
            PacketSender sender = netSystem.getPacketSender(connection.getID());

            if (sender != null) {
                if (object instanceof Packet) {
                    ((Packet) object).handle(sender, netHandler);
                }
                else {
//                    logger.error("Player {} sent a non-packet object.", sender);
                }
            }
            else {
                logger.error("Received an object from a null player (that hurts).");
            }
        }

        @Override
        public void connected(Connection connection) {
        }
    }
}
