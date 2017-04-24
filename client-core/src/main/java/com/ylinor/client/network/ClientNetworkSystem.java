package com.ylinor.client.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.swing.JOptionPane;

import org.joml.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.ylinor.client.physics.systems.PhySystem;
import com.ylinor.client.render.EyeHeight;
import com.ylinor.client.render.RenderViewEntity;
import com.ylinor.client.render.ScreenSystem;
import com.ylinor.library.api.ecs.components.AABB;
import com.ylinor.library.api.ecs.components.CollisionState;
import com.ylinor.library.api.ecs.components.Heading;
import com.ylinor.library.api.ecs.components.InputControlledEntity;
import com.ylinor.library.api.ecs.components.Physics;
import com.ylinor.library.api.ecs.components.Position;
import com.ylinor.library.api.ecs.components.Rotation;
import com.ylinor.library.api.ecs.components.Size;
import com.ylinor.library.api.ecs.components.Velocity;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.util.ecs.system.BaseSystem;
import com.ylinor.packets.Packet;
import com.ylinor.packets.PacketDespawnEntity;
import com.ylinor.packets.PacketDisconnect;
import com.ylinor.packets.PacketHandler;
import com.ylinor.packets.PacketLogin;
import com.ylinor.packets.PacketMapChunk;
import com.ylinor.packets.PacketPositionAndRotationUpdate;
import com.ylinor.packets.PacketSpawnClientPlayer;
import com.ylinor.packets.PacketSpawnEntity;
import com.ylinor.packets.Protocol;


@Singleton
public final class ClientNetworkSystem extends BaseSystem
                implements PacketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientNetworkSystem.class);

    private Client client;
    private Deque<Packet> packetQueue;
    private List<Entity> nearbyEntities;
    public boolean loggedIn;

    /*
     * TODO maybe create a new implementation of PacketHandler for a cleaner
     * code ? wytrem : approved.
     */

    @Inject
    PhySystem phySystem;

    @Inject
    ScreenSystem screenSystem;
    
    @Inject
    @Named("serverIp")
    String serverIp;

    public ClientNetworkSystem() {
        this.nearbyEntities = new ArrayList<>();
    }

    @Override
    public void initialize() {
        this.client = new Client();
        client.start();

        Packet.registerToKryo(client.getKryo());

        this.packetQueue = new ArrayDeque<>();

        loggedIn = false;
    }

    @Override
    protected void processSystem() {
        Packet packet;

        if (client.isConnected()) {
            while ((packet = packetQueue.poll()) != null) {
                client.sendTCP(packet);
            }
        }
    }

    public void init() throws IOException {
        logger.info("Protocol version: {}.", Protocol.PROTOCOL_VERSION);
        logger.info("Connecting to {}:{}.", serverIp, Protocol.SERVER_PORT);
        client.connect(10000, InetAddress.getByName(serverIp), Protocol.SERVER_PORT);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Packet) {
                    ((Packet) object).handle(null, ClientNetworkSystem.this);
                }
            }
        });
    }

    public void enqueuePacket(Packet packet) {
        packetQueue.addLast(packet);
    }

    @Override
    public void handleLogin(Entity sender, PacketLogin login) {
        loggedIn = true;
        logger.info("Successfully logged in.");
    }

    @Override
    public void handleMapChunk(Entity sender, PacketMapChunk packetMapChunk) {

    }

    @Override
    public void handleSpawnEntity(Entity sender, PacketSpawnEntity spawnEntity) {
        Entity entity = world.create(spawnEntity.getEntityID())
                             .set(Position.class)
                             .set(Heading.class)
                             .set(Rotation.class)
                             .set(NetworkIdentifierComponent.class);

        entity.get(NetworkIdentifierComponent.class)
              .setIdentifier(spawnEntity.getEntityID());
        entity.get(Position.class).position.set(spawnEntity.getInitialX(), spawnEntity.getInitialY(), spawnEntity.getInitialZ());
        entity.get(Rotation.class).rotationPitch = spawnEntity.getInitialPitch();
        entity.get(Rotation.class).rotationYaw = spawnEntity.getInitialYaw();

        nearbyEntities.add(entity);
        logger.info("Spawned entity {}", spawnEntity.getEntityID());
    }

    @Override
    public void handleDespawnEntity(Entity sender, PacketDespawnEntity despawnEntity) {
        Iterator<Entity> it = nearbyEntities.iterator();

        while (it.hasNext()) {
            Entity entity = it.next();

            if (entity.get(NetworkIdentifierComponent.class)
                      .getIdentifier() == despawnEntity.getEntityID()) {
                entity.delete();
                it.remove();

                break;
            }
        }
    }

    @Override
    public void handlePositionUpdate(Entity sender, PacketPositionAndRotationUpdate positionUpdate) {
        for (Entity entity : nearbyEntities) {
            if (entity.get(NetworkIdentifierComponent.class)
                      .getIdentifier() == positionUpdate.getEntityID()) {
                Vector3f position = entity.get(Position.class).position;
                Rotation rotation = entity.get(Rotation.class);

                position.set(positionUpdate.getX(), positionUpdate.getY(), positionUpdate.getZ());
                rotation.rotationPitch = positionUpdate.getPitch();
                rotation.rotationYaw = positionUpdate.getYaw();
            }
        }
    }

    @Override
    public void handleDisconnect(Entity sender, PacketDisconnect disconnect) {
        // TODO create a proper gui alert dialog

        JOptionPane.showMessageDialog(null, "You have been disconnected from the server, reason : " + disconnect.getReason(), "Connection lost", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    public List<Entity> getNearbyEntities() {
        return nearbyEntities;
    }

    @Override
    public void handleSpawnClientPlayer(Entity sender, PacketSpawnClientPlayer spawnClientPlayer) {
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
}
