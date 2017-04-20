package com.ylinor.client.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import javax.inject.Singleton;
import javax.swing.JOptionPane;

import org.joml.Vector3f;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.ylinor.client.physics.components.Heading;
import com.ylinor.client.physics.components.Position;
import com.ylinor.client.physics.components.Rotation;
import com.ylinor.library.util.ecs.entity.Entity;
import com.ylinor.library.util.ecs.system.BaseSystem;
import com.ylinor.packets.Packet;
import com.ylinor.packets.PacketDisconnect;
import com.ylinor.packets.PacketHandler;
import com.ylinor.packets.PacketLogin;
import com.ylinor.packets.PacketMapChunk;
import com.ylinor.packets.PacketPositionAndRotationUpdate;
import com.ylinor.packets.PacketSpawnEntity;

@Singleton
public final class ClientNetworkSystem extends BaseSystem implements PacketHandler {
    private final Client client;
    private final Deque<Packet> packetQueue;
    private List<Entity> nearbyEntities;

    /*
    TODO maybe create a new implementation of PacketHandler for a cleaner code ?
     */

    public ClientNetworkSystem() {
        this.client = new Client();
        client.start();

        Packet.registerToKryo(client.getKryo());

        this.packetQueue = new ArrayDeque<>();
        this.nearbyEntities = new ArrayList<>();
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

    public void init(InetAddress address, int port) throws IOException {
        client.connect(10000, address, port);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof Packet) {
                    ((Packet) object).handle(ClientNetworkSystem.this);
                }
            }
        });
    }

    public void enqueuePacket(Packet packet) {
        packetQueue.addLast(packet);
    }

    @Override
    public void handleLogin(PacketLogin login) {

    }

    @Override
    public void handleMapChunk(PacketMapChunk packetMapChunk) {

    }

    @Override
    public void handleSpawnEntity(PacketSpawnEntity spawnEntity) {
        Entity entity = world.create()
                .set(Position.class)
                .set(Heading.class).set(Rotation.class)
                .set(NetworkIdentifierComponent.class);

        entity.get(NetworkIdentifierComponent.class).setIdentifier(spawnEntity.getEntityID());
        entity.get(Position.class).position.set(spawnEntity.getInitialX(), spawnEntity.getInitialY(), spawnEntity.getInitialZ());
        entity.get(Heading.class).heading.set(spawnEntity.getInitialPitch(), spawnEntity.getInitialYaw(), 0.0f);

        // TODO pitch and yaw

        nearbyEntities.add(entity);
    }

    @Override
    public void handlePositionUpdate(PacketPositionAndRotationUpdate positionUpdate) {
        for (Entity entity : nearbyEntities) {
            if (entity.get(NetworkIdentifierComponent.class).getIdentifier() == positionUpdate.getEntityID()) {
                Vector3f position = entity.get(Position.class).position;
                Rotation rotation = entity.get(Rotation.class);

                position.set(positionUpdate.getX(), positionUpdate.getY(), positionUpdate.getZ());
                rotation.rotationPitch = positionUpdate.getPitch();
                rotation.rotationYaw = positionUpdate.getYaw();
            }
        }
    }

    @Override
    public void handleDisconnect(PacketDisconnect disconnect) {
        // TODO create a proper gui alert dialog

        JOptionPane.showMessageDialog(null, "You have been disconnected from the server, reason : " + disconnect.getReason(), "Connection lost", JOptionPane.ERROR_MESSAGE);
        System.exit(1);
    }

    public List<Entity> getNearbyEntities() {
        return nearbyEntities;
    }
}
