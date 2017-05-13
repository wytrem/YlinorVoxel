package com.ylinor.client.network;

import java.io.IOException;
import java.net.InetAddress;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.JOptionPane;

import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.ylinor.client.YlinorClient;
import com.ylinor.client.physics.systems.PhySystem;
import com.ylinor.client.render.EyeHeight;
import com.ylinor.client.render.RenderViewEntity;
import com.ylinor.client.render.ScreenSystem;
import com.ylinor.client.screen.pregame.MainMenuScreen;
import com.ylinor.client.terrain.ClientTerrain;
import com.ylinor.library.api.ecs.components.AABB;
import com.ylinor.library.api.ecs.components.CollisionState;
import com.ylinor.library.api.ecs.components.Heading;
import com.ylinor.library.api.ecs.components.InputControlledEntity;
import com.ylinor.library.api.ecs.components.Physics;
import com.ylinor.library.api.ecs.components.Position;
import com.ylinor.library.api.ecs.components.Rotation;
import com.ylinor.library.api.ecs.components.Size;
import com.ylinor.library.api.ecs.components.Velocity;
import com.ylinor.library.api.protocol.PacketSender;
import com.ylinor.library.api.protocol.Protocol;
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
import com.ylinor.library.api.terrain.Chunk;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.util.ecs.system.NonProcessingSystem;


@Singleton
public class ClientNetworkSystem extends NonProcessingSystem
                implements PacketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientNetworkSystem.class);

    private Client kryoClient;
    public boolean loggedIn;

    @Inject
    PhySystem phySystem;

    @Inject
    ScreenSystem screenSystem;

    @Inject
    @Named("serverIp")
    String serverIp;
    
    @Inject
    ClientTerrain terrain;
    
    @Inject
    YlinorClient client;
    
    public boolean hasReceivedChunkPacket;

    public ClientNetworkSystem() {
    }

    @Override
    public void initialize() {
        this.kryoClient = new Client();
        kryoClient.start();

        Packet.registerToKryo(kryoClient.getKryo());

        loggedIn = false;
        hasReceivedChunkPacket = false;
    }

    public void connectToServer() throws IOException {
        logger.info("Protocol version: {}.", Protocol.PROTOCOL_VERSION);
        logger.info("Connecting to {}:{}.", serverIp, Protocol.SERVER_PORT);
        kryoClient.connect(10000, InetAddress.getByName(serverIp), Protocol.SERVER_PORT);

        kryoClient.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Packet) {
                    Gdx.app.postRunnable(() -> {
                        ((Packet) object).handle(null, ClientNetworkSystem.this);
                    });
                }
            }
        });
    }

    public int send(Packet packet) {
        return kryoClient.sendTCP(packet);
    }

    @Override
    public void handleLogin(PacketSender sender, PacketLogin login) {
        loggedIn = true;
        logger.info("Successfully logged in.");
    }

    @Override
    public void handleMapChunk(PacketSender sender, PacketMapChunk packetMapChunk) {
        logger.info("Received a chunk!");
        Chunk chunk = terrain.getChunk(packetMapChunk.getX(), packetMapChunk.getZ());
        packetMapChunk.populateChunk(chunk);
        
        if (!hasReceivedChunkPacket) {
            hasReceivedChunkPacket = true;
        }
    }

    @Override
    public void handleSpawnEntity(PacketSender sender, PacketSpawnEntity spawnEntity) {
        Entity entity = world.create(spawnEntity.getEntityId())
                             .set(Position.class)
                             .set(Heading.class)
                             .set(Rotation.class);

        entity.get(Position.class).position.set(spawnEntity.getInitialX(), spawnEntity.getInitialY(), spawnEntity.getInitialZ());
        entity.get(Rotation.class).rotationPitch = spawnEntity.getInitialPitch();
        entity.get(Rotation.class).rotationYaw = spawnEntity.getInitialYaw();

        logger.info("Spawned entity {}", spawnEntity.getEntityId());
    }

    @Override
    public void handleDespawnEntity(PacketSender sender, PacketDespawnEntity despawnEntity) {
        world.delete(despawnEntity.getEntityId());
    }

    @Override
    public void handlePositionUpdate(PacketSender sender, PacketPositionAndRotationUpdate positionUpdate) {
        Entity entity = world.get(positionUpdate.getEntityId());

        if (entity == null) {
            logger.warn("Client received a position packet for a null entity!");
            return;
        }

        Vector3f position = entity.get(Position.class).position;
        Rotation rotation = entity.get(Rotation.class);

        position.set(positionUpdate.getX(), positionUpdate.getY(), positionUpdate.getZ());
        rotation.rotationPitch = positionUpdate.getPitch();
        rotation.rotationYaw = positionUpdate.getYaw();
    }

    @Override
    public void handleDisconnect(PacketSender sender, PacketDisconnect disconnect) {
        // TODO create a proper gui alert dialog
        screenSystem.setScreen(MainMenuScreen.class);
        JOptionPane.showMessageDialog(null, "You have been disconnected from the server, reason : " + disconnect.getReason(), "Connection lost", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void handleSpawnClientPlayer(PacketSender sender, PacketSpawnClientPlayer spawnClientPlayer) {
        client.isInGame = true;
        logger.info("Spawning client player.");
        Entity player = world.create(spawnClientPlayer.getEntityId());
        player.set(RenderViewEntity.class)
              .set(InputControlledEntity.class)
              .set(Heading.class)
              .set(Position.class)
              .set(Velocity.class)
              .set(EyeHeight.class)
              .set(Size.class)
              .set(AABB.class)
              .set(CollisionState.class)
              .set(Physics.class)
              .set(Rotation.class)
              .set(PositionSyncComponent.class);
        player.get(EyeHeight.class).eyePadding.y = 1.65f;
        player.get(Size.class).setSize(0.6f, 1.8f);

        phySystem.setPosition(player, 1818, 126, 6710);

        screenSystem.setNoScreen();
    }

    @Override
    public void handleCharacterSelection(PacketSender sender, PacketCharacterSelection packetCharacterSelection) {
        // Invalid.
    }

    @Override
    public void handleCharacterList(PacketSender sender, PacketCharactersList packetCharactersList) {
        // TODO : display character list, and when a choice is done :
        send(new PacketCharacterSelection());
    }
}
