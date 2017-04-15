package com.ylinor.client.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayDeque;
import java.util.Deque;

import javax.inject.Singleton;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.ylinor.library.util.ecs.system.BaseSystem;
import com.ylinor.packets.Packet;

@Singleton
public final class ClientNetworkSystem extends BaseSystem {
    private final Client client;
    private final Deque<Packet> packetQueue;

    public ClientNetworkSystem() {
        this.client = new Client();
        client.start();

        Packet.registerToKryo(client.getKryo());

        this.packetQueue = new ArrayDeque<>();
    }

    @Override
    protected void processSystem() {
        Packet packet;

        while ((packet = packetQueue.poll()) != null) {
            client.sendTCP(packet);
        }
    }

    public void init(InetAddress address, int port) throws IOException {
        client.connect(10000, address, port);

        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                System.out.println("received " + object);
            }
        });
    }

    public void enqueuePacket(Packet packet) {
        packetQueue.addLast(packet);
    }
}
